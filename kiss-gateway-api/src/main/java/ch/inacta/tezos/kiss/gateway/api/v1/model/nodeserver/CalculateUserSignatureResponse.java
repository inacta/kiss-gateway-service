package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class CalculateUserSignatureResponse {
    private String address;
    private Integer nonce;
    private String publicKey;
    private String signature;

    /**
     * Gets the value of the address property.
     *
     * @return possible object is {@link String}
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property
     *
     * @param address allowed object is {@link String }
     * @return the {@link CalculateUserSignatureResponse}
     */
    public CalculateUserSignatureResponse setAddress(String address) {
        this.address = address;
        return this;
    }

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
     * @return the {@link CalculateUserSignatureResponse}
     */
    public CalculateUserSignatureResponse setNonce(Integer nonce) {
        this.nonce = nonce;
        return this;
    }

    /**
     * Gets the value of the publicKey property.
     *
     * @return possible object is {@link String}
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the value of the publicKey property
     *
     * @param publicKey allowed object is {@link String }
     * @return the {@link CalculateUserSignatureResponse}
     */
    public CalculateUserSignatureResponse setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    /**
     * Gets the value of the signature property.
     *
     * @return possible object is {@link String}
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property
     *
     * @param signature allowed object is {@link String }
     * @return the {@link CalculateUserSignatureResponse}
     */
    public CalculateUserSignatureResponse setSignature(String signature) {
        this.signature = signature;
        return this;
    }
}
