package ch.inacta.kiss.gateway.api.v1.model.nodeserver;

/**
 * key pair
 *
 * @author ProofX
 * @since 1.0.0
 */
public class KeyPair {
    private String address;
    private String publicKey;
    private String secretKey;

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
     * @return the {@link KeyPair}
     */
    public KeyPair setAddress(String address) {
        this.address = address;
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
     * @return the {@link KeyPair}
     */
    public KeyPair setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    /**
     * Gets the value of the secretKey property.
     *
     * @return possible object is {@link String}
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the value of the secretKey property
     *
     * @param secretKey allowed object is {@link String }
     * @return the {@link KeyPair}
     */
    public KeyPair setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
