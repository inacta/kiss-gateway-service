package ch.inacta.tezos.kiss.gateway.core.logging;

import org.apache.commons.exec.LogOutputStream;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * Class that represents a ExecLogHandler.
 *
 * @author ProofX
 * @since 1.0.0
 */
public class ExecLogHandler extends LogOutputStream {

    private final Logger log;

    /**
     * ExecLogHandler
     *
     * @param log log
     * @param logLevel logLevel
     */
    public ExecLogHandler(Logger log, Level logLevel) {
        super(logLevel.toInt());
        this.log = log;
    }

    @Override
    protected void processLine(String line, int logLevel) {
        switch (logLevel) {
            case 0:
                log.trace(line);
                break;
            case 10:
                log.debug(line);
                break;
            case 20:
                log.info(line);
                break;
            case 30:
                log.warn(line);
                break;
            case 40:
                log.error(line);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + logLevel);
        }
    }
}