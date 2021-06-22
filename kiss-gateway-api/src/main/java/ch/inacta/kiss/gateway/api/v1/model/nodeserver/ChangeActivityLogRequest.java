package ch.inacta.kiss.gateway.api.v1.model.nodeserver;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class ChangeActivityLogRequest {
    private String contractAddress;
    private String newActivityLogAddress;
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
     * @return the {@link ChangeActivityLogRequest}
     */
    public ChangeActivityLogRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the newActivityLogAddress property.
     *
     * @return possible object is {@link String}
     */
    public String getNewActivityLogAddress() {
        return newActivityLogAddress;
    }

    /**
     * Sets the value of the newActivityLogAddress property
     *
     * @param newActivityLogAddress allowed object is {@link String }
     * @return the {@link ChangeActivityLogRequest}
     */
    public ChangeActivityLogRequest setNewActivityLogAddress(String newActivityLogAddress) {
        this.newActivityLogAddress = newActivityLogAddress;
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
     * @return the {@link ChangeActivityLogRequest}
     */
    public ChangeActivityLogRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
