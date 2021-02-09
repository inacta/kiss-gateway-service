package com.proofx.gateway.api.v1.model;

public class StatusResponse {
    String status;

    public StatusResponse(String status) {
        this.status = status;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is {@link String}
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property
     *
     * @param status allowed object is {@link String }
     * @return the {@link StatusResponse}
     */
    public StatusResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}
