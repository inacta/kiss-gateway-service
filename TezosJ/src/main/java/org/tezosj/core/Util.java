package org.tezosj.core;

import org.tezosj.exceptions.InvalidAddressException;
import org.tezosj.util.Base58Check;

public class Util {

    public void checkAddress(String address) throws InvalidAddressException {
        try {
            Base58Check.decode(address);
        } catch (Exception e) {
            throw new InvalidAddressException(address);
        }
    }
}
