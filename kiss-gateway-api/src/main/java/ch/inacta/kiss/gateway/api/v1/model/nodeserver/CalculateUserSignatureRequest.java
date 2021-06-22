package ch.inacta.kiss.gateway.api.v1.model.nodeserver;

import java.util.List;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class CalculateUserSignatureRequest {
    private List<Integer> activities;
    private String contractAddress;
    private List<String> helpers;
    private Integer minutes;
    private String secretKey;

    /**
     * Gets the value of the activities property.
     *
     * @return possible object is {@link List< Integer>}
     */
    public List<Integer> getActivities() {
        return activities;
    }

    /**
     * Sets the value of the activities property
     *
     * @param activities allowed object is {@link List< Integer> }
     * @return the {@link CalculateUserSignatureRequest}
     */
    public CalculateUserSignatureRequest setActivities(List<Integer> activities) {
        this.activities = activities;
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
     * @return the {@link CalculateUserSignatureRequest}
     */
    public CalculateUserSignatureRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the helpers property.
     *
     * @return possible object is {@link List< String>}
     */
    public List<String> getHelpers() {
        return helpers;
    }

    /**
     * Sets the value of the helpers property
     *
     * @param helpers allowed object is {@link List< String> }
     * @return the {@link CalculateUserSignatureRequest}
     */
    public CalculateUserSignatureRequest setHelpers(List<String> helpers) {
        this.helpers = helpers;
        return this;
    }

    /**
     * Gets the value of the minutes property.
     *
     * @return possible object is {@link Integer}
     */
    public Integer getMinutes() {
        return minutes;
    }

    /**
     * Sets the value of the minutes property
     *
     * @param minutes allowed object is {@link Integer }
     * @return the {@link CalculateUserSignatureRequest}
     */
    public CalculateUserSignatureRequest setMinutes(Integer minutes) {
        this.minutes = minutes;
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
     * @return the {@link CalculateUserSignatureRequest}
     */
    public CalculateUserSignatureRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
