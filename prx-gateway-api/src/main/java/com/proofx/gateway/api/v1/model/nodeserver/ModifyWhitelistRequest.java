package com.proofx.gateway.api.v1.model.nodeserver;

/**
 * modify whitelist request
 *
 * @author ProofX
 * @since 1.0.0
 */
public class ModifyWhitelistRequest extends  GetWhitelistRequest {
    private String secretKey;

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
     * @return the {@link ModifyWhitelistRequest}
     */
    public ModifyWhitelistRequest setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
