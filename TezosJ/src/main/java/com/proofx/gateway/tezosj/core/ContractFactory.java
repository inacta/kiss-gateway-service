package com.proofx.gateway.tezosj.core;

import com.proofx.gateway.tezosj.TezosJ;
import com.proofx.gateway.tezosj.contracts.Contract;
import com.proofx.gateway.tezosj.exceptions.InvalidAddressException;

public class ContractFactory {
    private final TezosJ tezosJ;

    public ContractFactory(TezosJ tezosJ) {
        this.tezosJ = tezosJ;
    }

    public Contract at(String contractAddress) throws InvalidAddressException {
        return new Contract(this.tezosJ, contractAddress);
    }

    public <T extends Contract> T at(String contractAddress, Class<T> contractType) throws InvalidAddressException {
        Class[] classArgs = new Class[2];
        classArgs[0] = TezosJ.class;
        classArgs[1] = String.class;
        T contract;
        try {
            contract = contractType.getDeclaredConstructor(classArgs).newInstance(this.tezosJ, contractAddress);
        } catch (Exception e) {
            throw new InvalidAddressException(e.getMessage());
        }
        return contract;
    }
}
