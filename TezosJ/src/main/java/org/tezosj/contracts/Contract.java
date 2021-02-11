package org.tezosj.contracts;

import org.json.JSONObject;
import org.tezosj.TezosJ;
import org.tezosj.exceptions.InvalidAddressException;

import java.math.BigDecimal;

public class Contract {

    private TezosJ tezosJ;
    private String contractAddress;

    public Contract(TezosJ tezosj, String contractAddress) throws InvalidAddressException {
        this.tezosJ = tezosj;
        tezosj.util.checkAddress(contractAddress);
        this.contractAddress = contractAddress;
    }

    // Calls a smart contract entrypoint passing parameters.
    // Returns to the user the operation results from Tezos node.
    public JSONObject callEntryPoint(BigDecimal amount, BigDecimal fee, String gasLimit, String storageLimit, String entrypoint, String[] parameters, Boolean rawParameter, String smartContractType) throws Exception {
        if ((amount == null) || (entrypoint == null) || (parameters == null)) {
            throw new java.lang.RuntimeException("The fields: Amount, Entrypoint and Parameters are required.");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new java.lang.RuntimeException("Amount must be greater than or equal to zero.");
        }
        if (fee.compareTo(BigDecimal.ZERO) <= 0) {
            throw new java.lang.RuntimeException("Fee must be greater than zero.");
        }
        return this.tezosJ.gateway.callContractEntryPoint(this.tezosJ.accounts.getPublicKeyHash(), contractAddress, amount, fee, gasLimit, storageLimit, entrypoint, parameters, rawParameter, smartContractType);
    }
}