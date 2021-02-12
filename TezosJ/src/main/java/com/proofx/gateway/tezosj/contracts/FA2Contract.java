package com.proofx.gateway.tezosj.contracts;

import com.proofx.gateway.tezosj.TezosJ;
import com.proofx.gateway.tezosj.exceptions.InvalidAddressException;

public class FA2Contract extends Contract {
    public FA2Contract(TezosJ tezosj, String contractAddress) throws InvalidAddressException {
        super(tezosj, contractAddress);
    }
}
