package com.proofx.gateway.tezosj.contracts;

import com.proofx.gateway.tezosj.TezosJ;
import com.proofx.gateway.tezosj.exceptions.InvalidAddressException;
import com.proofx.gateway.tezosj.exceptions.NoWalletSetException;
import org.json.JSONObject;

import java.math.BigDecimal;

@SuppressWarnings({"java:S107", "java:S1176"})
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
    public JSONObject callEntryPoint(BigDecimal amount, BigDecimal fee, String gasLimit, String storageLimit, String entrypoint, String[] parameters, Boolean rawParameter, String smartContractType) throws NoWalletSetException {
        return this.tezosJ.gateway.callContractEntryPoint(this.tezosJ.accounts.getPublicKeyHash(), contractAddress, amount, fee, gasLimit, storageLimit, entrypoint, parameters, rawParameter, smartContractType);
    }
}