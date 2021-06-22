package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

/**
 * get whitelist request
 *
 * @author ProofX
 * @since 1.0.0
 */
public class GetWhitelistRequest {
    private WhitelistVersion version;
    private String contractAddress;
    private String walletAddress;
    private String tokenId;

    /**
     * Gets the value of the version property.
     *
     * @return possible object is {@link WhitelistVersion}
     */
    public WhitelistVersion getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property
     *
     * @param version allowed object is {@link WhitelistVersion }
     * @return the {@link GetWhitelistRequest}
     */
    public GetWhitelistRequest setVersion(WhitelistVersion version) {
        this.version = version;
        return this;
    }

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
     * @return the {@link GetWhitelistRequest}
     */
    public GetWhitelistRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the walletAddress property.
     *
     * @return possible object is {@link String}
     */
    public String getWalletAddress() {
        return walletAddress;
    }

    /**
     * Sets the value of the walletAddress property
     *
     * @param walletAddress allowed object is {@link String }
     * @return the {@link GetWhitelistRequest}
     */
    public GetWhitelistRequest setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
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
     * @return the {@link GetWhitelistRequest}
     */
    public GetWhitelistRequest setTokenId(String tokenId) {
        this.tokenId = tokenId;
        return this;
    }
}
