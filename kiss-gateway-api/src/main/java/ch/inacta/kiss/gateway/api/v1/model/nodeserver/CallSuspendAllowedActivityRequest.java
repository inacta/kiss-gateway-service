package ch.inacta.kiss.gateway.api.v1.model.nodeserver;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class CallSuspendAllowedActivityRequest {
    private String contractAddress;
    private Integer activity;
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
     * @return the {@link CallSuspendAllowedActivityRequest}
     */
    public CallSuspendAllowedActivityRequest setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Gets the value of the activity property.
     *
     * @return possible object is {@link Integer}
     */
    public Integer getActivity() {
        return activity;
    }

    /**
     * Sets the value of the activity property
     *
     * @param activity allowed object is {@link Integer }
     * @return the {@link CallSuspendAllowedActivityRequest}
     */
    public CallSuspendAllowedActivityRequest setActivity(Integer activity) {
        this.activity = activity;
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
     * @return the {@link CallSuspendAllowedActivityRequest}
     */
    public CallSuspendAllowedActivityRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
