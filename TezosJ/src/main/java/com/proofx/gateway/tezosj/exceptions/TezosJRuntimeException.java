package com.proofx.gateway.tezosj.exceptions;

@SuppressWarnings({"java:S1176"})
public class TezosJRuntimeException extends RuntimeException {
    public TezosJRuntimeException(String s) {
        super(s);
    }

    public TezosJRuntimeException(String s, Throwable c) {
        super(s, c);
    }
}
