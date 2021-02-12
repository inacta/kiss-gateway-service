package com.proofx.gateway.api.v1.model.nodeserver;

/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
public class GetAdminResponse {
    private String admin;

    /**
     * Gets the value of the admin property.
     *
     * @return possible object is {@link String}
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * Sets the value of the admin property
     *
     * @param admin allowed object is {@link String }
     * @return the {@link GetAdminResponse}
     */
    public GetAdminResponse setAdmin(String admin) {
        this.admin = admin;
        return this;
    }
}
