package com.proofx.gateway.core;

import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Email scheduler
 *
 * @author ProofX
 * @since 1.0.0
 */
@ApplicationScoped
public class HumanityEmailScheduler {

    @Inject
    DefaultHumanityEmailService emailService;

    @Scheduled(every="10m")
    void sendEmails() {
        this.emailService.sendEmails();
    }

}
