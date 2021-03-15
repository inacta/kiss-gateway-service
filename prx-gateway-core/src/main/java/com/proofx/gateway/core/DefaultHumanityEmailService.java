package com.proofx.gateway.core;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TrackOpens;
import com.mailjet.client.transactional.TransactionalEmail;
import com.proofx.gateway.core.configuration.PropertyService;
import com.proofx.gateway.core.jpa.VoucherEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Humanity email service
 *
 * @author ProofX
 * @since 1.0.0
 */
@SuppressWarnings({"java:S3252"})
@ApplicationScoped
public class DefaultHumanityEmailService {

    @Inject
    PropertyService propertyService;

    @Inject
    UserTransaction utx;

    MailjetClient mailjetClient;

    @PostConstruct
    void init() {
        ClientOptions options = ClientOptions.builder()
                .apiKey(this.propertyService.getMjApikeyPublic())
                .apiSecretKey(this.propertyService.getMjApikeyPrivate())
                .build();

        this.mailjetClient = new MailjetClient(options);
    }

    /**
     * Sends all emails that have not been sent yet
     */
    public void sendEmails() {
        List<VoucherEntity> entities = VoucherEntity.find("email_sent=:sent AND created <= :created", Map.of("sent", false, "created", LocalDateTime.now().minusMinutes(10))).list();
        for (VoucherEntity entity: entities) {
            try {
                utx.begin();
                this.sendEmail(entity.getVoucher());
                utx.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send email for specific voucher code
     *
     * @param voucherCode voucher
     */
    public void sendEmail(String voucherCode) {
        if (!this.propertyService.isProduction()) {
            return;
        }
        VoucherEntity entity = VoucherEntity.find("voucher", voucherCode).firstResult();

        try {
            TransactionalEmail message = TransactionalEmail
                    .builder()
                    .to(new SendContact(entity.getEmail()))
                    .from(new SendContact(propertyService.getSenderEmail(), "Humanity Inspired"))
                    .htmlPart("<h1>This is your voucher code</h1><br><p>" + voucherCode + "</p><p>Download your voucher <a href=\"" + this.propertyService.getVoucherUri() + entity.getVoucher().replace(" ", "%20") + "\">here</a>!</p>")
                    .subject("Your Experience Token Voucher")
                    .trackOpens(TrackOpens.ENABLED)
                    .build();

            SendEmailsRequest request = SendEmailsRequest.builder().message(message).build();

            request.sendWith(mailjetClient);
            VoucherEntity.update("update VoucherEntity set EMAIL_SENT=true where VOUCHER=?1", entity.getVoucher());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
