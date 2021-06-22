package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class ChangeAdminThisRequest {
    private String contractAddress;
    private String newAdmin;
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
     * @return the {@link ChangeAdminThisRequest}
     */
    public ChangeAdminThisRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the newAdmin property.
     *
     * @return possible object is {@link String}
     */
    public String getNewAdmin() {
        return newAdmin;
    }

    /**
     * Sets the value of the newAdmin property
     *
     * @param newAdmin allowed object is {@link String }
     * @return the {@link ChangeAdminThisRequest}
     */
    public ChangeAdminThisRequest setNewAdmin(String newAdmin) {
        this.newAdmin = newAdmin;
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
     * @return the {@link ChangeAdminThisRequest}
     */
    public ChangeAdminThisRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
