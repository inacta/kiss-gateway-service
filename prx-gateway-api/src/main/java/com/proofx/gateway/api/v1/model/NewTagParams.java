package com.proofx.gateway.api.v1.model;

/**
 * Parameters when adding a new tag
 *
 * @author ProofX
 * @since 1.0.0
 */
public class NewTagParams {
    String uuid;
    String secret;

    /**
     * Gets the value of the uuid property.
     *
     * @return possible object is {@link String}
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property
     *
     * @param uuid allowed object is {@link String }
     * @return the {@link NewTagParams}
     */
    public NewTagParams setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /**
     * Gets the value of the secret property.
     *
     * @return possible object is {@link String}
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets the value of the secret property
     *
     * @param secret allowed object is {@link String }
     * @return the {@link NewTagParams}
     */
    public NewTagParams setSecret(String secret) {
        this.secret = secret;
        return this;
    }
}
