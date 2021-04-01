package com.proofx.gateway.core;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.transactional.*;
import com.proofx.gateway.api.v1.model.ServiceRuntimeException;
import com.proofx.gateway.core.configuration.PropertyService;
import com.proofx.gateway.core.jpa.VoucherEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
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

    static final String CSV_SEPARATOR = ";";

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

        String html;
        try {
            html = new String(this.getClass().getResourceAsStream("/templates/email_single.html").readAllBytes(), StandardCharsets.UTF_8).replace("@@@_VOUCHERCODE_@@@", entity.getVoucher()).replace("@@@_VOUCHERPRICE_@@@", entity.getPrice()).replace("@@@_VOUCHERURL_@@@", this.propertyService.getVoucherUri() + entity.getVoucher().replace(" ", "%20"));
        } catch (IOException e) {
            throw new ServiceRuntimeException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        try {
            TransactionalEmail message = TransactionalEmail
                    .builder()
                    .to(new SendContact(entity.getEmail()))
                    .from(new SendContact(propertyService.getSenderEmail(), "Humanity Inspired"))
                    .htmlPart(html)
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

    /**
     * Send one email for multiple vouchers
     *
     * @param vouchers list of vouchers
     */
    public void sendEmailBulk(List<String> vouchers) {
        if (!this.propertyService.isProduction()) {
            return;
        }

        StringBuilder csv = new StringBuilder();
        csv.append("Voucher");
        csv.append(CSV_SEPARATOR);
        csv.append("Price");
        csv.append(CSV_SEPARATOR);
        csv.append("Url");
        csv.append(CSV_SEPARATOR);
        csv.append(System.lineSeparator());

        String email = null;
        StringBuilder htmlItems = new StringBuilder();

        for (String voucher: vouchers) {
            VoucherEntity entity = VoucherEntity.find("voucher", voucher).firstResult();
            if (email == null) {
                email = entity.getEmail();
            } else {
                // make sure only the same email is in bulk
                if (!email.equals(entity.getEmail())) {
                    throw new ServiceRuntimeException(Response.Status.INTERNAL_SERVER_ERROR);
                }
            }
            csv.append(entity.getVoucher());
            csv.append(CSV_SEPARATOR);
            csv.append(entity.getPrice());
            csv.append(CSV_SEPARATOR);
            csv.append(this.propertyService.getVoucherUri() + entity.getVoucher());
            csv.append(CSV_SEPARATOR);
            csv.append(System.lineSeparator());
            try {
                htmlItems.append(new String(this.getClass().getResourceAsStream("/templates/email_multiple_item.html").readAllBytes(), StandardCharsets.UTF_8).replace("@@@_VOUCHERCODE_@@@", entity.getVoucher()).replace("@@@_VOUCHERPRICE_@@@", entity.getPrice()).replace("@@@_VOUCHERURL_@@@", this.propertyService.getVoucherUri() + entity.getVoucher().replace(" ", "%20")));
            } catch (IOException e) {
                throw new ServiceRuntimeException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        String html;

        try {
            html = new String(this.getClass().getResourceAsStream("/templates/email_multiple.html").readAllBytes(), StandardCharsets.UTF_8).replace("@@@_VOUCHERS_@@@", htmlItems.toString());
        } catch (IOException e) {
            throw new ServiceRuntimeException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        try {
            Attachment attachment = Attachment.builder().filename("vouchers.csv").contentType("text/csv").base64Content(new String(Base64.getEncoder().encode(csv.toString().getBytes()))).build();

            TransactionalEmail message = TransactionalEmail
                    .builder()
                    .to(new SendContact(email))
                    .from(new SendContact(propertyService.getSenderEmail(), "Humanity Inspired"))
                    .htmlPart(html)
                    .subject("Your Experience Token Vouchers")
                    .attachment(attachment)
                    .trackOpens(TrackOpens.ENABLED)
                    .build();

            SendEmailsRequest request = SendEmailsRequest.builder().message(message).build();

            request.sendWith(mailjetClient);

            for (String voucher: vouchers) {
                VoucherEntity.update("update VoucherEntity set EMAIL_SENT=true where VOUCHER=?1", voucher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
