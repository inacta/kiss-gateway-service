package com.proofx.gateway.api.v1.model.nodeserver;

public class CallAddAllowedActivityRequest {
    private String contractAddress;
    private Integer newActivity;
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
     * @return the {@link CallAddAllowedActivityRequest}
     */
    public CallAddAllowedActivityRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the newActivity property.
     *
     * @return possible object is {@link Integer}
     */
    public Integer getNewActivity() {
        return newActivity;
    }

    /**
     * Sets the value of the newActivity property
     *
     * @param newActivity allowed object is {@link Integer }
     * @return the {@link CallAddAllowedActivityRequest}
     */
    public CallAddAllowedActivityRequest setNewActivity(Integer newActivity) {
        this.newActivity = newActivity;
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
     * @return the {@link CallAddAllowedActivityRequest}
     */
    public CallAddAllowedActivityRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
