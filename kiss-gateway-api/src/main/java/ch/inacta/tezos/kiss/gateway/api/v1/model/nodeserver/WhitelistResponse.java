package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

/**
 * WhitelistResponse
 *
 * @author ProofX
 * @since 1.0.0
 */
public class WhitelistResponse {
    private Status status;
    private String message;
    private boolean whitelisted;

    /**
     * Gets the value of the status property.
     *
     * @return possible object is {@link Status}
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property
     *
     * @param status allowed object is {@link Status }
     * @return the {@link WhitelistResponse}
     */
    public WhitelistResponse setStatus(Status status) {
        this.status = status;
        return this;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object is {@link String}
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property
     *
     * @param message allowed object is {@link String }
     * @return the {@link WhitelistResponse}
     */
    public WhitelistResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Gets the value of the whitelisted property.
     *
     * @return possible object is boolean
     */
    public boolean isWhitelisted() {
        return whitelisted;
    }

    /**
     * Sets the value of the whitelisted property
     *
     * @param whitelisted allowed object is boolean
     * @return the {@link WhitelistResponse}
     */
    public WhitelistResponse setWhitelisted(boolean whitelisted) {
        this.whitelisted = whitelisted;
        return this;
    }
}
