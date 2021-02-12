package org.tezosj.exceptions;

public class TezosJRuntimeException extends RuntimeException {
    public TezosJRuntimeException(String s) {
        super(s);
    }

    public TezosJRuntimeException(String s, Throwable c) {
        super(s, c);
    }
}
