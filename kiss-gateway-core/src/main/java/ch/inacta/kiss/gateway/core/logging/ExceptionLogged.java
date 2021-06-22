package ch.inacta.kiss.gateway.core.logging;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation allows to enable an all exception-logging in the debug or error level
 *
 * @author ProofX
 * @since 1.0.0
 */
@Documented
@InterceptorBinding
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface ExceptionLogged {

    /**
     * Reroutes the exception-log into the error level
     *
     * @return false = log-level is debug, true = log-level is error
     */
    @Nonbinding
    boolean value() default false;
}
