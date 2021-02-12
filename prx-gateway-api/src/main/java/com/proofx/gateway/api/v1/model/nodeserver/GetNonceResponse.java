package com.proofx.gateway.api.v1.model.nodeserver;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class GetNonceResponse {
    private Integer nonce;

    /**
     * Gets the value of the nonce property.
     *
     * @return possible object is {@link Integer}
     */
    public Integer getNonce() {
        return nonce;
    }

    /**
     * Sets the value of the nonce property
     *
     * @param nonce allowed object is {@link Integer }
     * @return the {@link GetNonceResponse}
     */
    public GetNonceResponse setNonce(Integer nonce) {
        this.nonce = nonce;
        return this;
    }
}
