package com.proofx.gateway.core.logging;

import com.proofx.gateway.api.v1.model.BlockchainServiceRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerRequest;
import org.jboss.resteasy.core.interception.jaxrs.PostMatchContainerRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.Map;

import static com.proofx.gateway.api.v1.model.ErrorResponseMessage.UNAUTHORIZED_NO_TOKEN_PROVIDED;
import static java.util.Base64.getDecoder;

/**
 * The logging request-filter for logging all access on this service
 *
 * @author ProofX
 * @since 1.0.0
 */
@Provider
public class LoggingRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingRequestFilter.class);

    @Context
    HttpServerRequest request;

    @Inject
    PlatformLogEntryBuilder logEntryBuilder;

    @Override
    public void filter(final ContainerRequestContext context) {

//        final String authorization = this.request.getHeader("AUTHORIZATION");
//
//        if (authorization != null) {
//
//            try {
//
//                final String requestMethod = context.getMethod();
//                final String requestPath = context.getUriInfo().getPath();
//
//                @SuppressWarnings("unchecked")
//                final Map<String, String> bodyEntries = new ObjectMapper().readValue(extractBody(authorization), Map.class);
//
//                this.logEntryBuilder.withMessage(LoggingMessage.CALL_WITH_USERNAME, requestMethod, requestPath, bodyEntries.get("preferred_username")).info(LOG);
//
//            } catch (final JsonProcessingException e) {
//
//                this.logEntryBuilder.withMessage(LoggingMessage.CALL_WITHOUT_AUTHORIZED_TOKEN).debug(LOG);
//
//                throw new BlockchainServiceRuntimeException(UNAUTHORIZED_NO_TOKEN_PROVIDED);
//            }
//        } else if(Arrays.stream(((PostMatchContainerRequestContext) context).getResourceMethod().getMethodAnnotations()).noneMatch(i -> i.annotationType() == PermitAll.class)){
//
//            this.logEntryBuilder.withMessage(LoggingMessage.CALL_WITHOUT_AUTHORIZED_TOKEN).debug(LOG);
//
//            throw new BlockchainServiceRuntimeException(UNAUTHORIZED_NO_TOKEN_PROVIDED);
//        }
    }

    private String extractBody(final String authorization) {

        final String[] authorizationArray = authorization.split(" ");
        final String jwtToken = authorizationArray.length > 1 ? authorizationArray[1] : "";
        this.logEntryBuilder.withMessage(LoggingMessage.CALL_WITH_JWT, jwtToken).debug(LOG);

        final String base64EncodedBody = jwtToken.split("\\.")[1];
        String jwtBody = new String(getDecoder().decode(base64EncodedBody));
        this.logEntryBuilder.withMessage(LoggingMessage.CALL_WITH_JWT_BODY, jwtBody).debug(LOG);

        return jwtBody;
    }
}