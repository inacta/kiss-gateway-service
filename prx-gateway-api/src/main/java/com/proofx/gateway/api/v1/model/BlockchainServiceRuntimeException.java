package com.proofx.gateway.api.v1.model;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 * The default REST exception of the service
 *
 * @author ProofX
 * @since 1.0.0
 */
public class BlockchainServiceRuntimeException extends WebApplicationException {
    
    private static final String MESSAGE_ID = "messageId";

    /**
     * The constructor for creating an instance with the default HTTP response-status 400 (bad request)
     *
     * @param errorMessage
     *            the concrete error-message this exception should represents
     * @param messageParams
     *            some optional parameters for the {@link ErrorResponseMessage} of the exception (if any need)
     */
    public BlockchainServiceRuntimeException(final ErrorResponseMessage errorMessage, final String... messageParams) {
        
        super(errorMessage.getMessage(messageParams),
                status(BAD_REQUEST).header(MESSAGE_ID, errorMessage.getId()).entity(errorMessage.getMessage(messageParams)).build());
    }

    /**
     * The constructor for creating an instance with a custom HTTP response-status
     *
     * @param errorMessage
     *            the concrete error-message this exception should represents
     * @param responseStatus
     *            the HTTP response-status code which is returned in the response when this exception is thrown by the service
     * @param messageParams
     *            some optional parameters for the {@link ErrorResponseMessage} of the exception (if any need)
     */
    public BlockchainServiceRuntimeException(final ErrorResponseMessage errorMessage, final Status responseStatus, final String... messageParams) {

        super(errorMessage.getMessage(messageParams),
                status(responseStatus).header(MESSAGE_ID, errorMessage.getId()).entity(errorMessage.getMessage(messageParams)).build());
    }

    /**
     * The constructor for creating an instance with the default HTTP response-status 400 (bad request)
     *
     * @param errorMessage
     *            the concrete error-message this exception should represents
     * @param cause
     *            the root cause of the exception
     * @param messageParams
     *            some optional parameters for the {@link ErrorResponseMessage} of the exception (if any need)
     */
    public BlockchainServiceRuntimeException(final ErrorResponseMessage errorMessage, final Throwable cause, final String... messageParams) {

        super(errorMessage.getMessage(messageParams), cause,
                status(BAD_REQUEST).header(MESSAGE_ID, errorMessage.getId()).entity(errorMessage.getMessage(messageParams)).build());
    }

    /**
     * The constructor for creating an instance with a custom HTTP response-status
     *
     * @param errorMessage
     *            the concrete error-message this exception should represents
     * @param cause
     *            the root cause of the exception
     * @param responseStatus
     *            the HTTP response-status code which is returned in the response when this exception is thrown by the service
     * @param messageParams
     *            some optional parameters for the {@link ErrorResponseMessage} of the exception (if any need)
     */
    public BlockchainServiceRuntimeException(final ErrorResponseMessage errorMessage, final Throwable cause, final Status responseStatus,
            final String... messageParams) {

        super(errorMessage.getMessage(messageParams), cause,
                status(responseStatus).header(MESSAGE_ID, errorMessage.getId()).entity(errorMessage.getMessage(messageParams)).build());
    }
}
