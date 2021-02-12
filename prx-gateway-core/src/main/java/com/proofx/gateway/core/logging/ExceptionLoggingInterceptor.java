package com.proofx.gateway.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * This is the interceptor is activated with the {@link ExceptionLogged} annotation and does logging all thrown exceptions of all methods in a class or a
 * single method into the debug / trace log-level
 *
 * @author ProofX
 * @since 1.0.0
 */
@Interceptor
@ExceptionLogged
public class ExceptionLoggingInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionLoggingInterceptor.class);

    @Inject
    PlatformLogEntryBuilder platformLogEntryBuilder;

    /**
     * Logs the e
     *
     * @param inovationContext
     *            InvocationContext
     * @return object
     * @throws Exception
     *             ex
     */
    @AroundInvoke
    public Object invoke(final InvocationContext inovationContext) throws Exception {

        try {

            return inovationContext.proceed();

        } catch (final Exception e) {

            if (getAnnotationClass(inovationContext).value()) {
                this.platformLogEntryBuilder.error(LOG, e);
            } else {
                this.platformLogEntryBuilder.debug(LOG, e);
            }

            throw e;
        }
    }

    private ExceptionLogged getAnnotationClass(final InvocationContext inovationContext) {

        ExceptionLogged annotationClass = inovationContext.getMethod().getAnnotation(ExceptionLogged.class);

        if (annotationClass == null) {
            annotationClass = inovationContext.getTarget().getClass().getSuperclass().getAnnotation(ExceptionLogged.class);
        }

        return annotationClass;
    }
}
