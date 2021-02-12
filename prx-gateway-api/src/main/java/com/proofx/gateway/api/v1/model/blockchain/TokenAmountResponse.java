package com.proofx.gateway.api.v1.model.blockchain;

import com.proofx.gateway.api.v1.model.nodeserver.Status;


/**
 * Token amount response
 *
 * @author ProofX
 * @since 1.0.0
 */
public class TokenAmountResponse {
    private Status status;
    private String message;


    /**
     * Init token amount response
     *
     * @param status success or failure
     * @param message message
     */
    public TokenAmountResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

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
     * @return the {@link TokenAmountResponse}
     */
    public TokenAmountResponse setStatus(Status status) {
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
     * @return the {@link TokenAmountResponse}
     */
    public TokenAmountResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
