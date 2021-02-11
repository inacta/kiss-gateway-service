package org.tezosj.core;

import okhttp3.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tezosj.TezosJ;
import org.tezosj.legacy.CustomSodium;
import org.tezosj.model.SignedOperationGroup;
import org.tezosj.util.Base58Check;
import org.tezosj.util.Encoder;
import org.tezosj.util.EncryptedKeys;
import org.tezosj.util.Global;

import javax.net.ssl.SSLContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.tezosj.util.Helpers.*;

public class Gateway {

    private final TezosJ tezosJ;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType textPlainMT = MediaType.parse("text/plain; charset=utf-8");
    private static final Integer HTTP_TIMEOUT = 20;
    private static final String RESULT = "result";

    public Gateway(TezosJ tezosJ) {
        this.tezosJ = tezosJ;
        Global.initOkhttp();
    }

    public String getChainId() throws Exception {
        return ((JSONObject) query("/chains/main/chain_id", null)).get(RESULT).toString();
    }

    public JSONObject getBalance(String address) throws Exception {
        return (JSONObject) query("/chains/main/blocks/head/context/contracts/" + address + "/balance", null);
    }

    public JSONObject getAccountManagerForBlock(String blockHash, String accountID) throws Exception {
        return (JSONObject) query("/chains/main/blocks/" + blockHash + "/context/contracts/" + accountID + "/manager_key", null);
    }

    public String[] getContractEntryPoints(String contractAddress) throws Exception {
        JSONObject response = (JSONObject) query("/chains/main/blocks/head/context/contracts/" + contractAddress + "/entrypoints", null);

        JSONObject entryPointsJson = (JSONObject) response.get("entrypoints");

        String[] entrypoints = JSONObject.getNames(entryPointsJson);

        // If there is a list of entrypoints, then sort its elements.
        // This is fundamental for the call to work, as the order of the entrypoints
        // matter.
        if (entrypoints != null) {
            Arrays.sort(entrypoints);
        } else {
            // If there are no entrypoints declared in the contract, consider only the
            // "default" entrypoint.
            entrypoints = new String[]{"default"};
        }
        return entrypoints;
    }

    public String[] getContractEntryPointsParameters(String contractAddress, String entrypoint, String namesOrTypes)
            throws Exception {
        ArrayList<String> parameters = new ArrayList<String>();

        // If no desired entrypoint was specified, use the "default" entrypoint.
        if ((entrypoint == null) || (entrypoint.length() == 0)) {
            entrypoint = "default";
        }

        JSONObject response = (JSONObject) query("/chains/main/blocks/head/context/contracts/" + contractAddress + "/entrypoints/" + entrypoint, null);

        JSONArray paramArray = decodeParameters(response, null);

        JSONObject jsonObj;

        parameters = new ArrayList<String>();

        if (namesOrTypes.equals("names")) {
            for (int i = 0; i < paramArray.length(); i++) {
                jsonObj = (JSONObject) paramArray.get(i);
                if (jsonObj.has("annots")) {
                    JSONArray annotsArray = (JSONArray) jsonObj.get("annots");
                    parameters.add(((String) annotsArray.getString(0).replace("%", "")).replace(":", ""));
                }
            }
        } else if (namesOrTypes.equals("types")) {
            for (int i = 0; i < paramArray.length(); i++) {
                jsonObj = (JSONObject) paramArray.get(i);
                if (jsonObj.has("prim")) {
                    if (!(jsonObj.get("prim").toString()).contains("contract")) {
                        parameters.add((String) jsonObj.get("prim"));
                    }
                }
            }
        }

        String[] tempArray = new String[parameters.size()];
        tempArray = parameters.toArray(tempArray);

        return tempArray;
    }

    // Calls a contract passing parameters.
    public JSONObject callContractEntryPoint(String from, String contract, BigDecimal amount, BigDecimal fee,
                                             String gasLimit, String storageLimit, String entrypoint,
                                             String[] parameters, boolean rawParameter, String smartContractType)
            throws Exception {
        JSONObject result = new JSONObject();

        BigDecimal roundedAmount = amount.setScale(6, RoundingMode.HALF_UP);
        BigDecimal roundedFee = fee.setScale(6, RoundingMode.HALF_UP);
        JSONArray operations = new JSONArray();
        JSONObject revealOperation;
        JSONObject transaction = new JSONObject();
        JSONObject head;
        JSONObject account;
        int counter = 0;

        // Check if address has enough funds to do the transfer operation.
        JSONObject balance = getBalance(from);
        if (balance.has(RESULT)) {
            BigDecimal bdAmount = amount.multiply(BigDecimal.valueOf(Global.UTEZ));
            BigDecimal total = new BigDecimal(((balance.getString(RESULT).replace("\n", "")).replace("\"", "").replace("'", "")));
            if (total.compareTo(bdAmount) < 0) // Returns -1 if value is less than amount.
            {
                // Not enough funds to do the transfer.
                JSONObject returned = new JSONObject();
                returned.put(RESULT,
                        "{ \"result\":\"error\", \"kind\":\"TezosJ_SDK_exception\", \"id\": \"Not enough funds\" }");

                return returned;
            }
        }
        if (gasLimit == null) {
            gasLimit = "750000";
        } else {
            if ((gasLimit.length() == 0) || (gasLimit.equals("0"))) {
                gasLimit = "750000";
            }
        }
        if (storageLimit == null) {
            storageLimit = "1000";
        } else {
            if (storageLimit.length() == 0) {
                storageLimit = "1000";
            }
        }

        head = new JSONObject(query("/chains/main/blocks/head/header", null).toString());
        account = getAccountForBlock(head.get("hash").toString(), from);
        counter = Integer.parseInt(account.get("counter").toString());

        // Append Reveal Operation if needed.
        revealOperation = appendRevealOperation(head, from, (counter));

        if (revealOperation != null) {
            operations.put(revealOperation);
            counter = counter + 1;
        }

        transaction.put("destination", contract);
        transaction.put("amount", (String.valueOf(roundedAmount.multiply(BigDecimal.valueOf(Global.UTEZ)).toBigInteger())));
        transaction.put("storage_limit", storageLimit);
        transaction.put("gas_limit", gasLimit);
        transaction.put("counter", String.valueOf(counter + 1));
        transaction.put("fee", (String.valueOf(roundedFee.multiply(BigDecimal.valueOf(Global.UTEZ)).toBigInteger())));
        transaction.put("source", from);
        transaction.put("kind", Global.OPERATION_KIND_TRANSACTION);


        JSONObject myParams = null;
        if (!rawParameter) {
            // Builds a Michelson-compatible set of parameters to pass to the smart
            // contract.

            JSONObject myparamJson;

            String[] contractEntrypoints = getContractEntryPoints(contract);

            if (!Arrays.asList(contractEntrypoints).contains("default")) {
                if (!Arrays.asList(contractEntrypoints).contains(entrypoint)) {
                    throw new Exception("Wrong or missing entrypoint name");
                }
            } else {
                entrypoint = "default";
            }

            // Check if smartContractType parameter is null or empty.
            if (smartContractType == null || smartContractType.isEmpty()) {
                smartContractType = Global.GENERIC_STANDARD;
            }

            // According to each smart contract standard, get the entrypoint parameters.
            String[] contractEntryPointParameters = null;
            String[] contractEntryPointParametersTypes = null;
            if (smartContractType.equals(Global.GENERIC_STANDARD)) {

                contractEntryPointParameters = getContractEntryPointsParameters(contract, entrypoint, "names");
                contractEntryPointParametersTypes = getContractEntryPointsParameters(contract, entrypoint, "types");

            } else if (smartContractType.equals(Global.FA12_STANDARD)) {
                // If the smartContractType standard is FA1.2, then we already know the entrypoint parameters.
                if (entrypoint.equals(Global.FA12_TRANSFER)) {
                    contractEntryPointParameters = new String[]{"from", "to", "value"};
                    contractEntryPointParametersTypes = new String[]{"string", "string", "nat"};
                } else if (entrypoint.equals(Global.FA12_APPROVE)) {
                    contractEntryPointParameters = new String[]{"spender", "value"};
                    contractEntryPointParametersTypes = new String[]{"string", "nat"};
                } else if (entrypoint.equals(Global.FA12_GET_ALLOWANCE)) {
                    contractEntryPointParameters = new String[]{"owner", "spender", "callback"};
                    contractEntryPointParametersTypes = new String[]{"string", "string", "string"};
                } else if (entrypoint.equals(Global.FA12_GET_BALANCE)) {
                    contractEntryPointParameters = new String[]{"owner", "callback"};
                    contractEntryPointParametersTypes = new String[]{"string", "string"};
                } else if (entrypoint.equals(Global.FA12_GET_TOTAL_SUPPLY)) {
                    contractEntryPointParameters = new String[]{"unit", "callback"};
                    contractEntryPointParametersTypes = new String[]{"unit", "string"};
                }

            } else {
                smartContractType = Global.GENERIC_STANDARD;
                contractEntryPointParameters = getContractEntryPointsParameters(contract, entrypoint, "names");
                contractEntryPointParametersTypes = getContractEntryPointsParameters(contract, entrypoint, "types");
            }

            myparamJson = paramValueBuilder(entrypoint, contractEntrypoints, parameters,
                    contractEntryPointParameters,
                    contractEntryPointParametersTypes,
                    smartContractType);

            // Adds the smart contract parameters to the transaction.
            myParams = new JSONObject();
            myParams.put("entrypoint", entrypoint);
            myParams.put("value", myparamJson);

        } else {
            // Adds the smart contract parameters to the transaction.
            myParams = new JSONObject();
            myParams.put("entrypoint", entrypoint);
            JSONArray myJsonArray = new JSONArray(parameters[0]);
            myParams.put("value", myJsonArray);
        }

        transaction.put("parameters", myParams);

        operations.put(transaction);

        if ((smartContractType.equals(Global.FA12_STANDARD)) && (!entrypoint.equals(Global.FA12_TRANSFER)) && (!entrypoint.equals(Global.FA12_APPROVE))) {

            JSONObject jsonObj = callRunOperation(operations);

            result.put(RESULT, jsonObj);
        } else {
            result = sendOperation(operations);
        }

        return result;
    }

    private JSONObject sendOperation(JSONArray operations) throws Exception {
        JSONObject result = new JSONObject();
        JSONObject head = (JSONObject) query("/chains/main/blocks/head/header", null);
        String forgedOperationGroup = forgeOperations(head, operations);

        // Check for errors.
        if (forgedOperationGroup.toLowerCase().contains("failed") || forgedOperationGroup.toLowerCase().contains("unexpected")
                || forgedOperationGroup.toLowerCase().contains("missing") || forgedOperationGroup.toLowerCase().contains("error")) {
            throw new Exception("Error while forging operation : " + forgedOperationGroup);
        }

        SignedOperationGroup signedOpGroup = signOperationGroup(forgedOperationGroup);

        if (signedOpGroup == null) // User cancelled the operation.
        {
            result.put("result", "There were errors: 'User has cancelled the operation'");
            return result;
        } else {

            String operationGroupHash = computeOperationHash(signedOpGroup);
            JSONObject appliedOp = applyOperation(head, operations, operationGroupHash, forgedOperationGroup, signedOpGroup);
            JSONObject opResult = checkAppliedOperationResults(appliedOp);

            if (opResult.get("result").toString().length() == 0) {
                JSONObject injectedOperation = injectOperation(signedOpGroup);
                if (isJSONArray(injectedOperation.toString())) {
                    if (((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).has("error")) {
                        String err = (String) ((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).get("error");
                        String reason = "There were errors: '" + err + "'";

                        result.put("result", reason);
                    } else {
                        result.put("result", "");
                    }
                    if (((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).has("Error")) {
                        String err = (String) ((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).get("Error");
                        String reason = "There were errors: '" + err + "'";

                        result.put("result", reason);
                    } else {
                        result.put("result", "");
                    }

                } else if (isJSONObject(injectedOperation.toString())) {
                    if (injectedOperation.has("result")) {
                        if (isJSONArray(injectedOperation.get("result").toString())) {
                            if (((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).has("error")) {
                                String err = (String) ((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0))
                                        .get("error");
                                String reason = "There were errors: '" + err + "'";

                                result.put("result", reason);
                            } else if (((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).has("kind")) {
                                if (((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).has("msg")) {
                                    String err = (String) ((JSONObject) ((JSONArray) injectedOperation.get("result")).get(0)).get("msg");
                                    String reason = "There were errors: '" + err + "'";

                                    result.put("result", reason);
                                } else {
                                    result.put("result", "");
                                }


                            } else {
                                result.put("result", "");
                            }

                        } else {
                            result.put("result", injectedOperation.get("result"));
                        }
                    } else {
                        result.put("result", "There were errors.");
                    }
                }

            } else {
                result.put("result", opResult.get("result").toString());
            }
        }
        return result;
    }

    private JSONObject injectOperation(SignedOperationGroup signedOpGroup) throws Exception {
        String payload = signedOpGroup.getSbytes();
        return nodeInjectOperation("\"" + payload + "\"");
    }

    private JSONObject nodeInjectOperation(String payload) throws Exception {
        return (JSONObject) query("/injection/operation?chain=main", payload);
    }

    private JSONObject applyOperation(JSONObject head, JSONArray operations, String operationGroupHash,
                                      String forgedOperationGroup, SignedOperationGroup signedOpGroup)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("protocol", head.get("protocol"));
        jsonObject.put("branch", head.get("hash"));
        jsonObject.put("contents", operations);
        jsonObject.put("signature", signedOpGroup.getSignature());

        JSONArray payload = new JSONArray();
        payload.put(jsonObject);

        return nodeApplyOperation(payload);
    }

    private JSONObject nodeApplyOperation(JSONArray payload) throws Exception {
        return (JSONObject) query("/chains/main/blocks/head/helpers/preapply/operations", payload.toString());
    }

    private JSONObject checkAppliedOperationResults(JSONObject appliedOp) throws Exception {
        JSONObject returned = new JSONObject();

        String[] validAppliedKinds = new String[]{"activate_account", "reveal", "transaction", "origination", "delegation"};

        String firstApplied = appliedOp.toString().replaceAll("\\\\n", "").replaceAll("\\\\", "");
        JSONArray result = new JSONArray(new JSONObject(firstApplied).get("result").toString());
        JSONObject firstObject = (JSONObject) result.get(0);

        try {
            JSONObject first = new JSONObject(firstObject.toString());
            if (first.has("kind") && first.has("id")) {
                returned.put(RESULT, "There were errors: kind '" + first.getString("kind") + "' id '" + first.getString("id") + "'");
                return returned;
            }
        } catch (JSONException e) {
        }

        try {
            JSONArray first = new JSONArray(firstObject.toString());
            int elements = ((JSONArray) firstObject.get("contents")).length();
            for (int i = 0; i < elements; i++) {
                JSONObject operationResult = ((JSONObject) ((JSONObject) (((JSONObject) (((JSONArray) firstObject.get("contents")).get(i))).get("metadata"))).get("operation_result"));
                if (operationResult.getString("status").equals("failed") && operationResult.has("errors")) {
                    JSONObject err = (JSONObject) ((JSONArray) operationResult.get("errors")).get(0);
                    returned.put(RESULT, "There were errors: kind '" + err.getString("kind") + "' id '" + err.getString("id") + "'");
                }
            }
        } catch (JSONException e) {
        }

        returned.put(RESULT, "");
        return returned;
    }

    private SignedOperationGroup signOperationGroup(String forgedOperation) throws Exception {
        JSONObject signed = null;

        if ((!Global.ledgerDerivationPath.isEmpty()) && (!Global.ledgerTezosFolderPath.isEmpty())) {
            System.out.println(Global.CONFIRM_WITH_LEDGER_MESSAGE);
            signed = this.tezosJ.accounts.signWithLedger(Encoder.HEX.decode(forgedOperation), "03");
        } else {
            // Traditional signing.
            signed = this.tezosJ.accounts.sign(Encoder.HEX.decode(forgedOperation), "03");
        }

        if (signed == null) // User cancelled the operation.
        {
            return null;
        } else {
            // Prepares the object to be returned.
            byte[] workBytes = ArrayUtils.addAll(Encoder.HEX.decode(forgedOperation), Encoder.HEX.decode((String) signed.get("sig")));
            return new SignedOperationGroup(workBytes, (String) signed.get("edsig"), (String) signed.get("sbytes"));
        }
    }

    private Object query(String endpoint, String data) throws Exception {
        JSONObject result = null;
        boolean methodPost = false;
        Request request = null;
        Proxy proxy = null;
        SSLContext sslcontext = null;

        OkHttpClient client = Global.myOkhttpClient;
        OkHttpClient.Builder myBuilder = Global.myOkhttpBuilder;

        final MediaType MEDIA_PLAIN_TEXT_JSON = MediaType.parse("application/json");
        String DEFAULT_PROVIDER = Global.defaultProvider;
        RequestBody body = RequestBody.create(textPlainMT, DEFAULT_PROVIDER + endpoint);

        if (data != null) {
            methodPost = true;
            body = RequestBody.create(MEDIA_PLAIN_TEXT_JSON, data.getBytes());
        }

        if (!methodPost) {
            request = new Request.Builder().url(DEFAULT_PROVIDER + endpoint).build();
        } else {
            request = new Request.Builder().url(DEFAULT_PROVIDER + endpoint).addHeader("Content-Type", "text/plain")
                    .post(body).build();
        }

        // If user specified a proxy host.
        if ((Global.proxyHost.length() > 0) && (Global.proxyPort.length() > 0)) {
            proxy = new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress(Global.proxyHost, Integer.parseInt(Global.proxyPort)));
            myBuilder.proxy(proxy); // If behind a firewall/proxy.
        }

        // Constructs the builder;
        myBuilder.connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS).writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS).build();

        try {
            Response response = client.newCall(request).execute();
            String strResponse = response.body().string();

            try {
                result = new JSONObject(strResponse);
            } catch (JSONException JSONObjectException) {
                try {
                    JSONArray myJSONArray = new JSONArray(strResponse);
                    result = new JSONObject();
                    result.put(RESULT, myJSONArray);
                } catch (JSONException JSONArrayException) {
                    result = new JSONObject();
                    result.put(RESULT, strResponse);
                }
            }
        } catch (Exception e) {
            // If there is a real error...
            e.printStackTrace();
            result = new JSONObject();
            result.put(RESULT, e.toString());
        }

        return result;
    }

    // Call Tezos RUN_OPERATION.
    private JSONObject callRunOperation(JSONArray operations) throws Exception {
        JSONObject result = new JSONObject();
        JSONObject head = (JSONObject) query("/chains/main/blocks/head/header", null);
        String forgedOperationGroup = forgeOperations(head, operations);

        // Check for errors.
        if (forgedOperationGroup.toLowerCase().contains("failed") || forgedOperationGroup.toLowerCase().contains("unexpected")
                || forgedOperationGroup.toLowerCase().contains("missing") || forgedOperationGroup.toLowerCase().contains("error")) {
            throw new Exception("Error while forging operation : " + forgedOperationGroup);
        }

        SignedOperationGroup signedOpGroup = signOperationGroupSimulation(forgedOperationGroup);

        if (signedOpGroup == null) // User cancelled the operation.
        {
            result.put("result", "There were errors: 'User has cancelled the operation'");
            return result;
        } else {

            String operationGroupHash = computeOperationHash(signedOpGroup);

            // Call RUN_OPERATIONS.
            JSONObject runOp = runOperation(head, operations, operationGroupHash, forgedOperationGroup, signedOpGroup);

            result = runOp;
        }
        return result;
    }

    private String computeOperationHash(SignedOperationGroup signedOpGroup) throws Exception {
        byte[] hash = new byte[32];
        new CustomSodium("").crypto_generichash(hash, hash.length, signedOpGroup.getTheBytes(), signedOpGroup.getTheBytes().length, signedOpGroup.getTheBytes(), 0);
        return Base58Check.encode(hash);
    }

    private JSONObject runOperation(JSONObject head, JSONArray operations, String operationGroupHash,
                                    String forgedOperationGroup, SignedOperationGroup signedOpGroup)
            throws Exception {
        JSONObject jsonObject = new JSONObject();
        String chainId = head.get("chain_id").toString();
        jsonObject.put("branch", head.get("hash"));
        jsonObject.put("contents", operations);
        jsonObject.put("signature", signedOpGroup.getSignature());

        JSONArray payload = new JSONArray();
        payload.put(jsonObject);

        return nodeRunOperation(payload, chainId);
    }

    private JSONObject nodeRunOperation(JSONArray payload, String chainId) throws Exception {
        JSONObject operation = new JSONObject();
        operation.put("operation", payload.get(0));
        operation.put("chain_id", chainId);

        return (JSONObject) query("/chains/main/blocks/head/helpers/scripts/run_operation", operation.toString());
    }

    private SignedOperationGroup signOperationGroupSimulation(String forgedOperation) throws Exception {
        JSONObject signed = new JSONObject();

        byte[] bytes = Encoder.HEX.decode(forgedOperation);
        byte[] sig = new byte[64];

        byte[] edsigPrefix = {9, (byte) 245, (byte) 205, (byte) 134, 18};
        byte[] edsigPrefixedSig = ArrayUtils.addAll(edsigPrefix, sig);
        String edsig = Base58Check.encode(edsigPrefixedSig);
        String sbytes = Encoder.HEX.encode(bytes) + Encoder.HEX.encode(sig);

        signed.put("bytes", Encoder.HEX.encode(bytes));
        signed.put("sig", Encoder.HEX.encode(sig));
        signed.put("edsig", edsig);
        signed.put("sbytes", sbytes);

        // Prepares the object to be returned.
        byte[] workBytes = ArrayUtils.addAll(Encoder.HEX.decode(forgedOperation), Encoder.HEX.decode((String) signed.get("sig")));
        return new SignedOperationGroup(workBytes, (String) signed.get("edsig"), (String) signed.get("sbytes"));
    }


    private String forgeOperations(JSONObject blockHead, JSONArray operations) throws Exception {
        JSONObject result = new JSONObject();
        result.put("branch", blockHead.get("hash"));
        result.put("contents", operations);

        return nodeForgeOperations(result.toString());
    }

    private String nodeForgeOperations(String opGroup) throws Exception {
        JSONObject response = (JSONObject) query("/chains/main/blocks/head/helpers/forge/operations", opGroup);
        String forgedOperation = (String) response.get(RESULT);
        return ((forgedOperation.replace("\n", "")).replace("\"", "").replace("'", ""));
    }

    private JSONObject getAccountForBlock(String blockHash, String accountID) throws Exception {
        return (JSONObject) query("/chains/main/blocks/" + blockHash + "/context/contracts/" + accountID, null);
    }

    private JSONObject appendRevealOperation(JSONObject blockHead, String pkh, Integer counter)
            throws Exception {
        // Create new JSON object for the reveal operation.
        JSONObject revealOp = new JSONObject();
        EncryptedKeys encKeys = this.tezosJ.accounts.getEncKeys();
        // Get public key from encKeys.
        byte[] bytePk = encKeys.getEncPublicKey();
        String publicKey = new String(decryptBytes(bytePk, this.tezosJ.accounts.encryptionKey(encKeys)));

        // If Manager key is not revealed for account...
        if (!isManagerKeyRevealedForAccount(blockHead, pkh)) {
            BigDecimal fee = new BigDecimal("0.002490");
            BigDecimal roundedFee = fee.setScale(6, RoundingMode.HALF_UP);
            revealOp.put("kind", "reveal");
            revealOp.put("source", pkh);
            revealOp.put("fee", (String.valueOf(roundedFee.multiply(BigDecimal.valueOf(Global.UTEZ)).toBigInteger())));
            revealOp.put("counter", String.valueOf(counter + 1));
            revealOp.put("gas_limit", "15400");
            revealOp.put("storage_limit", "300");
            revealOp.put("public_key", publicKey);
        } else {
            revealOp = null;
        }

        return revealOp;
    }

    private boolean isManagerKeyRevealedForAccount(JSONObject blockHead, String pkh) throws Exception {
        String blockHeadHash = blockHead.getString("hash");
        JSONObject accountManager = getAccountManagerForBlock(blockHeadHash, pkh);
        if (accountManager.has(RESULT)) {
            String r = (String) accountManager.get(RESULT);
            // Do some cleaning.
            r = r.replace("\"", "").replace("\n", "").trim();
            return !r.equals("null");
        }
        return false;
    }

    private JSONArray decodeParameters(JSONObject jsonObj, JSONArray builtArray) {

        JSONObject left;
        JSONObject right;

        if ((jsonObj.has("args")) || (jsonObj.has("prim"))) {
            if (builtArray == null) {
                builtArray = new JSONArray();
            }

            if (jsonObj.has("args")) {
                JSONArray myArr = jsonObj.getJSONArray("args");
                left = myArr.getJSONObject(0);
                builtArray = decodeParameters(left, builtArray);

                if (myArr.length() > 1) {
                    right = myArr.getJSONObject(1);
                    builtArray = decodeParameters(right, builtArray);
                }
            } else {

                // Now we can extract a single element.
                builtArray.put(jsonObj);

                return builtArray;
            }
        }
        return builtArray;
    }

    private JSONObject paramValueBuilder(String entrypoint, String[] contractEntrypoints, String[] parameters,
                                         String[] contractEntryPointParameters, String[] datatypes,
                                         String smartContractType) throws Exception {

        if (smartContractType.equals(Global.GENERIC_STANDARD) && parameters.length != datatypes.length) {
            throw new Exception("Wrong number of parameters to contract entrypoint");
        }

        // Creates the JSON object that will be returned by the methd.
        JSONObject myJsonObj = new JSONObject();

        List<String> parametersList = Arrays.asList(parameters);
        List<String> typesList = Arrays.asList(datatypes);

        // Corrects typeList.
        for (int i = 0; i < typesList.size(); i++) {
            switch (typesList.get(i)) {
                case "int":
                    typesList.set(i, "int");
                    break;
                case "string":
                    typesList.set(i, "string");
                    break;
                case "nat":
                    typesList.set(i, "int");
                    break;
                case "mutez":
                    typesList.set(i, "int");
                    break;
                case "tez":
                    typesList.set(i, "int");
                    break;
                case "bytes":
                    typesList.set(i, "int");
                    break;
                case "key_hash":
                    typesList.set(i, "string");
                    break;
                case "bool":
                    typesList.set(i, "prim");
                    break;
                case "unit":
                    typesList.set(i, "unit");
                    break;
                case "address":
                    typesList.set(i, "string");
                    break;
                case "owner":
                    typesList.set(i, "string");
                    break;
                case "spender":
                    typesList.set(i, "string");
                    break;

                default:
                    typesList.set(i, "string");

            }

        }

        Pair<Pair, List> basePair = null;
        Pair pair = buildParameterPairs(myJsonObj, basePair, parametersList, contractEntryPointParameters, false, smartContractType, entrypoint);

        // Create JSON from Pair.
        myJsonObj = (JSONObject) solvePair(pair, typesList, smartContractType);

        return myJsonObj;

    }

    private Pair buildParameterPairs(JSONObject jsonObj, Pair pair, List<String> parameters,
                                     String[] contractEntryPointParameters,
                                     Boolean doSolveLeft, String smartContractType, String entrypoint) throws Exception {

        // Test parameters validity.
        if (parameters.isEmpty()) {
            throw new Exception("Missing parameters to pass to contract entrypoint");
        }

        List<String> left;
        List<String> right;
        Pair newPair = null;

        if (parameters.size() == 1) {
            // If number of parameters is only 1.
            newPair = new MutablePair<>(null, new ArrayList<String>(Arrays.asList(parameters.get(0))));
        } else {

            if (pair == null) {
                if (smartContractType.equals(Global.FA12_STANDARD) && entrypoint.equals(Global.FA12_GET_ALLOWANCE)) {
                    left = parameters.subList(0, 2);
                    right = parameters.subList(2, parameters.size());

                    newPair = new MutablePair<>(left, right);
                } else {
                    Integer half = (Math.abs(parameters.size() / 2));

                    left = parameters.subList(0, half);
                    right = parameters.subList(half, parameters.size());

                    newPair = new MutablePair<>(left, right);
                }
            } else {
                List<String> newList;

                if (doSolveLeft) {
                    newList = ((List<String>) pair.getLeft());
                } else {
                    newList = ((List<String>) pair.getRight());
                }

                Integer half = (Math.abs(newList.size() / 2));

                left = newList.subList(0, half);
                right = newList.subList(half, newList.size());

                newPair = new MutablePair<>(left, right);

            }


            if ((((List) newPair.getRight()).size() > 2) || (((List) newPair.getLeft()).size() > 2)) {

                newPair = new MutablePair<>(buildParameterPairs(jsonObj, newPair, parameters, contractEntryPointParameters, true, smartContractType, entrypoint),
                        buildParameterPairs(jsonObj, newPair, parameters, contractEntryPointParameters, false, smartContractType, entrypoint));

            } else {
                return newPair;
            }

        }

        return newPair;

    }

    private Object solvePair(Object pair, List datatypes, String smartContractType) throws Exception {

        Object result = null;

        // Extract and check contents.
        if (!hasPairs((Pair) pair)) {
            // Here we've got List in both sides. But they might have more than one element.
            Object jsonLeft = ((Pair) pair).getLeft() == null ? null : toJsonFormat((List) ((Pair) pair).getLeft(), datatypes, 0);
            Object jsonRight = ((Pair) pair).getRight() == null ? null : toJsonFormat((List) ((Pair) pair).getRight(), datatypes, ((Pair) pair).getLeft() == null ? 0 : ((List) ((Pair) pair).getLeft()).size());

            // Test if there is only one parameter.
            if (jsonLeft == null) {
                if (jsonRight == null) {
                    throw new Exception("Pair cannot be (null, null)");
                } else {
                    // FA1.2 handling
                    if (smartContractType.equals(Global.FA12_STANDARD)) {
                        if (((JSONObject) jsonRight).has("unit")) {
                            JSONObject tmpObj = new JSONObject(), tmpItem1 = new JSONObject(), tmpItem2 = new JSONObject();
                            tmpObj.put("prim", "Pair");
                            Iterator<?> keys = ((JSONObject) jsonRight).keys();
                            String key = (String) keys.next();
                            tmpItem1.put("prim", "Unit");
                            tmpItem2.put("string", ((JSONObject) jsonRight).get(key));

                            JSONArray arr = new JSONArray();
                            arr.put(tmpItem1);
                            arr.put(tmpItem2);

                            tmpObj.put("args", arr);

                            return tmpObj;
                        } else {
                            return jsonRight;
                        }

                    } else {
                        return jsonRight;
                    }

                }
            } else if (jsonRight == null) {
                return jsonLeft;
            }

            // Build json outter pair.
            JSONObject jsonPair = new JSONObject();
            jsonPair.put("prim", "Pair");

            // Create pair contents array.
            JSONArray pairContents = new JSONArray();
            pairContents.put(jsonLeft);
            pairContents.put(jsonRight);
            jsonPair.put("args", pairContents);

            return jsonPair;
        } else {
            Object jsonLeft = solvePair(((Pair<Pair, List>) pair).getLeft(), datatypes, smartContractType);
            Object jsonRight = solvePair(((Pair<Pair, List>) pair).getRight(), datatypes.subList(countPairElements((Pair) ((Pair) pair).getLeft()), datatypes.size()), smartContractType);

            // Build json outter pair.
            JSONObject jsonPair = new JSONObject();
            jsonPair.put("prim", "Pair");

            // Create pair contents array.
            JSONArray pairContents = new JSONArray();
            pairContents.put(jsonLeft);
            pairContents.put(jsonRight);
            jsonPair.put("args", pairContents);

            return jsonPair;
        }

    }

    private Integer countPairElements(Pair pair) {
        int leftCount;
        int rightCount;

        Object left = pair.getLeft();
        Object right = pair.getRight();

        if (left instanceof Pair) {
            leftCount = countPairElements((Pair) left);
        } else {
            leftCount = ((List) left).size();
        }

        if (right instanceof Pair) {
            rightCount = countPairElements((Pair) right);
        } else {
            rightCount = ((List) right).size();
        }
        return leftCount + rightCount;
    }

    private boolean hasPairs(Pair pair) {
        Object left = pair.getLeft();
        Object right = pair.getRight();
        return ((left instanceof Pair) || (right instanceof Pair));
    }

    private JSONObject toJsonFormat(List list, List datatypes, Integer firstElement) {
        JSONArray result = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            JSONObject element = new JSONObject();
            element.put((String) datatypes.get(firstElement + i), list.get(i));

            // Add element to array.
            result.put(element);

        }

        if (result.length() > 1) {
            // Wrap json result in outter pair.
            JSONObject jsonPair = new JSONObject();
            jsonPair.put("prim", "Pair");
            jsonPair.put("args", result);

            return jsonPair;
        } else {
            return (JSONObject) result.get(0);
        }

    }

}
