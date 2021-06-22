package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

/**
 * Token transfer request
 *
 * @author ProofX
 * @since 1.0.0
 */
public class TokenTransferRequest {
    private String contractAddress;
    private String to;
    private String amount;
    private String tokenId;
    private String secretKey;

    /**
     * Gets the value of the contractAddress property.
     *
     * @return possible object is {@link String}
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Sets the value of the contractAddress property
     *
     * @param contractAddress allowed object is {@link String }
     * @return the {@link TokenTransferRequest}
     */
    public TokenTransferRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the to property.
     *
     * @return possible object is {@link String}
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the value of the to property
     *
     * @param to allowed object is {@link String }
     * @return the {@link TokenTransferRequest}
     */
    public TokenTransferRequest setTo(String to) {
        this.to = to;
        return this;
    }

    /**
     * Gets the value of the amount property.
     *
     * @return possible object is {@link String}
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property
     *
     * @param amount allowed object is {@link String }
     * @return the {@link TokenTransferRequest}
     */
    public TokenTransferRequest setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Gets the value of the tokenId property.
     *
     * @return possible object is {@link String}
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * Sets the value of the tokenId property
     *
     * @param tokenId allowed object is {@link String }
     * @return the {@link TokenTransferRequest}
     */
    public TokenTransferRequest setTokenId(String tokenId) {
        this.tokenId = tokenId;
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
     * @return the {@link TokenTransferRequest}
     */
    public TokenTransferRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
