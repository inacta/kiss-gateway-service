package com.proofx.gateway.core.logging;

import static java.lang.String.format;

/**
 * Enums that holds all logging-messages of the service
 *
 * @author ProofX
 * @since 1.0.0
 *
 */
public enum LoggingMessage {

    CALL_WITH_USERNAME("INCBLOSERVICE-LOG-0001", "Service requested by method '%s' and path '%s' from user '%s'"),
    CALL_WITH_JWT("INCBLOSERVICE-LOG-0002", "Service requested with jwt '%s'"),
    CALL_WITH_JWT_BODY("INCBLOSERVICE-LOG-0003", "Service requested with jwt-body '%s'"),
    CALL_WITHOUT_AUTHORIZED_TOKEN("INCBLOSERVICE-LOG-0004", "Service requested without an authorized token.");

    private final String id;
    private final String message;

    LoggingMessage(final String id, final String message) {

        this.id = id;
        this.message = message;
    }

    /**
     * Method for getting the per service unique message-identifier
     *
     * @return the message-identifier of the instance
     */
    public String getId() {

        return this.id;
    }

    /**
     * Method for getting the formatted and enhanced message
     *
     * @param messageParameters
     *            some parameters to enhance the message if need
     * @return the message (e.g. for using in log)
     */
    public String getMessage(final String... messageParameters) {

        return format(this.message, messageParameters);
    }
}
