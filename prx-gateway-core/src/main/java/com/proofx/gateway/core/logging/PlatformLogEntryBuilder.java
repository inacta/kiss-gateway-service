package com.proofx.gateway.core.logging;

import org.slf4j.Logger;

/**
 * A log-entry builder for this platform-service
 *
 * @author ProofX
 * @since 1.0.0
 */
public interface PlatformLogEntryBuilder {

    /**
     * A method for preparing the log-entry builder with the message and for the platform need meta-data
     *
     * @param loggingMessage
     *            the message which should logged
     * @param messageParameters
     *            if the message need, there are some parameters for it
     * @return the prepared log-entry builder
     */
    PlatformLogEntryBuilder withMessage(LoggingMessage loggingMessage, String... messageParameters);

    /**
     * Log a message at the INFO level.
     *
     * @param targetLogger
     *            the slf4j logger to which should be logged the entry;
     */
    void info(Logger targetLogger);

    /**
     * Log a message at the DEBUG level.
     *
     * @param targetLogger
     *            the slf4j logger to which should be logged the entry;
     */
    void debug(Logger targetLogger);

    /**
     * Log a message at the DEBUG level with an exception stack-trace (excpected cases).
     *
     * @param targetLogger
     *            the slf4j logger to which should be logged the entry;
     * @param t
     *            the exception (throwable) to log
     */
    void debug(Logger targetLogger, Throwable t);

    /**
     * Log a message at the TRACE level.
     *
     * @param targetLogger
     *            the slf4j logger to which should be logged the entry;
     */
    void trace(Logger targetLogger);

    /**
     * Log a message at the ERROR level.
     *
     * @param targetLogger
     *            the slf4j logger to which should be logged the entry;
     */
    void error(Logger targetLogger);

    /**
     * Log a message at the ERROR level with an exception stack-trace (real error cases).
     *
     * @param targetLogger
     *            the slf4j logger to which should be logged the entry;
     * @param t
     *            the exception (throwable) to log
     */
    void error(Logger targetLogger, Throwable t);
}