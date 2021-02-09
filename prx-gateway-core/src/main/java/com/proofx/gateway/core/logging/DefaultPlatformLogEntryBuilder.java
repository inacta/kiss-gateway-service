package com.proofx.gateway.core.logging;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;

//import org.eclipse.microprofile.jwt.Claim;
import org.slf4j.Logger;
import org.slf4j.MDC;

import io.vertx.ext.web.RoutingContext;

/**
 * The default log-entry builder for this platform-service
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
public class DefaultPlatformLogEntryBuilder implements PlatformLogEntryBuilder {

    private final String tenant;

    private String logEntry;

//    @Claim("preferred_username")
//    String preferredUsername;

    DefaultPlatformLogEntryBuilder(@Context final RoutingContext routingContext) {

        this.tenant = routingContext.request().headers().contains("tenant") ? routingContext.request().getHeader("tenant") : "dev";
    }

    @Override
    public PlatformLogEntryBuilder withMessage(final LoggingMessage message, final String... messageParameters) {

        MDC.put("logCallerInfo", getLogCallerInfo());
        MDC.put("service-id", "incat-blockchain-service");
        MDC.put("tenant-id", this.tenant);
//        MDC.put("user-id", this.preferredUsername);
        MDC.put("message-id", message.getId());

        this.logEntry = message.getMessage(messageParameters);

        return this;
    }

    @Override
    public void info(final Logger targetLogger) {

        targetLogger.info(this.logEntry);
    }

    @Override
    public void debug(final Logger targetLogger) {

        targetLogger.debug(this.logEntry);
    }

    @Override
    public void debug(final Logger targetLogger, final Throwable t) {

        targetLogger.debug(this.logEntry, t);
    }

    @Override
    public void trace(final Logger targetLogger) {

        targetLogger.trace(this.logEntry);
    }

    @Override
    public void error(final Logger targetLogger) {

        targetLogger.error(this.logEntry);
    }

    @Override
    public void error(final Logger targetLogger, final Throwable t) {

        targetLogger.error(this.logEntry, t);
    }

    private String getLogCallerInfo() {
        
        final String callersClassName = Thread.currentThread().getStackTrace()[4].getClassName();
        final String callersMethodName = Thread.currentThread().getStackTrace()[4].getMethodName();
        final int callersLineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber();

        return callersClassName
                + "."
                + callersMethodName
                + "("
                + callersClassName.substring(callersClassName.lastIndexOf('.') + 1)
                + ":"
                + callersLineNumber
                + ")";
    }
}