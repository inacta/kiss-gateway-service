package org.tezosj.contracts;

import org.json.JSONObject;
import org.tezosj.TezosJ;
import org.tezosj.exceptions.InvalidAddressException;

import java.math.BigDecimal;

@SuppressWarnings({"java:S107"})
public class Contract {

    private final TezosJ tezosJ;
    private final String contractAddress;

    public Contract(TezosJ tezosj, String contractAddress) throws InvalidAddressException {
        this.tezosJ = tezosj;
        tezosj.util.checkAddress(contractAddress);
        this.contractAddress = contractAddress;
    }

    // Calls a smart contract entrypoint passing parameters.
    // Returns to the user the operation results from Tezos node.
    public JSONObject callEntryPoint(BigDecimal amount, BigDecimal fee, String gasLimit, String storageLimit, String entrypoint, String[] parameters, Boolean rawParameter, String smartContractType) throws Exception {
        return this.tezosJ.gateway.callContractEntryPoint(this.tezosJ.accounts.getPublicKeyHash(), contractAddress, amount, fee, gasLimit, storageLimit, entrypoint, parameters, rawParameter, smartContractType);
    }
}