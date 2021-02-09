package com.proofx.gateway.core;

import com.proofx.gateway.core.logging.ExecLogHandler;
import io.quarkus.runtime.Startup;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Starts the node server in a separate thread
 *
 * @author inacta AG
 * @since 1.0.0
 */
@ApplicationScoped
@Startup
public class Boot {

    @ConfigProperty(name="node-executable")
    String nodeExec;

    @ConfigProperty(name="node-location")
    String nodeLocation;

    @ConfigProperty(name="private.key")
    Optional<String> privateKey;

    private static final Logger LOG = LoggerFactory.getLogger(Boot.class);

    @PostConstruct
    void init() {
        final Thread startThread = new Thread(() -> {
            try {
                CommandLine cmdLine = new CommandLine(nodeExec);
                cmdLine.addArgument(nodeLocation);
                DefaultExecutor executor = new DefaultExecutor();
                PumpStreamHandler psh = new PumpStreamHandler(new ExecLogHandler(LOG, Level.INFO), new ExecLogHandler(LOG, Level.ERROR));
                executor.setStreamHandler(psh);

                LOG.info("Start node server");
//                var envs = EnvironmentUtils.getProcEnvironment();
//                privateKey.ifPresent(s -> EnvironmentUtils.addVariableToEnvironment(envs, "PRIVATE_KEY=" + s));
                executor.execute(cmdLine);
            } catch (Exception e) {
                LOG.error("Could not start node server", e);
            }
        });

        startThread.start();
    }
}
