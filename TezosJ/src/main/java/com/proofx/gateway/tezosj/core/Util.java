package com.proofx.gateway.tezosj.core;

import com.proofx.gateway.tezosj.exceptions.InvalidAddressException;
import com.proofx.gateway.tezosj.util.Base58Check;

@SuppressWarnings({"java:S1176"})
public class Util {

    public void checkAddress(String address) throws InvalidAddressException {
        try {
            Base58Check.decode(address);
        } catch (Exception e) {
            throw new InvalidAddressException(address);
        }
    }
}
