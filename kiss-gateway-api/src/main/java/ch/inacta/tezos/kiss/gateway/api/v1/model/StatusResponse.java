package ch.inacta.tezos.kiss.gateway.api.v1.model;


/**
 * Status Response
 *
 * @author ProofX
 * @since 1.0.0
 */
public class StatusResponse {
    String status;


    /**
     * Initialize status response
     *
     * @param status status
     */
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
