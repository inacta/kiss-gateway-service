package org.tezosj.contracts;

import org.tezosj.TezosJ;
import org.tezosj.exceptions.InvalidAddressException;

public class FA2Contract extends Contract {
    public FA2Contract(TezosJ tezosj, String contractAddress) throws InvalidAddressException {
        super(tezosj, contractAddress);
    }
}
