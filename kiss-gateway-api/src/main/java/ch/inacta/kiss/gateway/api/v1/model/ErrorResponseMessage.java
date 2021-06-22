package ch.inacta.kiss.gateway.api.v1.model;

import static java.lang.String.format;

/**
 * Enums that holds all error-messages of the service
 *
 * @author ProofX
 * @since 1.0.0
 */
public enum ErrorResponseMessage {

    PROPERTY_NOT_FOUND_ERROR("PRXSERCIVE-ERROR-0001", "The system property with key '%s' can not be resolved for tenant '%s'."),
    UNAUTHORIZED_NO_TOKEN_PROVIDED("PRXSERCIVE-ERROR-0002", "Unauthorized. Was a bearer token provided in the authorization header?"),
    CONTRACT_EXECUTION_NOT_ENABLED("PRXSERCIVE-ERROR-0003", "Contract execution is not enabled?"),
    UNAUTHORIZED("PRXSERCIVE-ERROR-0004", "Unauthorized"),
    INVALID_REQUEST("PRXSERCIVE-ERROR-0005", "Invalid Request");

    private final String id;
    private final String message;

    ErrorResponseMessage(final String id, final String message) {

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
