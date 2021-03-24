package com.proofx.gateway.core.endpoint.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proofx.gateway.api.v1.HumanityResource;
import com.proofx.gateway.api.v1.model.ErrorResponseMessage;
import com.proofx.gateway.api.v1.model.ServiceRuntimeException;
import com.proofx.gateway.api.v1.model.humanity.WebhookRequest;
import com.proofx.gateway.core.DefaultHumanityEmailService;
import com.proofx.gateway.core.DefaultHumanityService;
import com.proofx.gateway.core.configuration.PropertyService;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * Tag verification
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
@Path("/humanity/v1")
public class DefaultHumanityResource implements HumanityResource {

    private PropertyService propertyService;
    private DefaultHumanityService implementationService;

    @Context
    @Inject
    RoutingContext routingContext;

    @Inject
    ManagedExecutor executor;

    @Inject
    DefaultHumanityEmailService emailService;

    @Inject
    UserTransaction utx;

    String shopifyKey;

    @Inject
    DefaultHumanityResource(final PropertyService propertyService, final DefaultHumanityService implementationService) {
        this.propertyService = propertyService;
        this.propertyService.getClass();
        this.implementationService = implementationService;
        this.shopifyKey = propertyService.getShopifyKey();
    }

    @Override
    public void webhook(String body) {
        try {
            WebhookRequest webhookRequest = new ObjectMapper().readValue(body, WebhookRequest.class);
            String shopifyHmacHeader = routingContext.request().getHeader("X-Shopify-Hmac-Sha256");

            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(this.shopifyKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKey);

            String calculatedHash = new String(Base64.getEncoder().encode(hmacSHA256.doFinal(body.getBytes())));
            if (shopifyHmacHeader.equals(calculatedHash)) {
                List<String> vouchers = this.implementationService.generateVoucher(webhookRequest);
                this.executor.execute(() -> {
                    try {
                        if (vouchers.size() >= 10) {
                            utx.begin();
                            this.emailService.sendEmailBulk(vouchers);
                            utx.commit();
                        } else {
                            for (String voucher: vouchers) {
                                    utx.begin();
                                    this.emailService.sendEmail(voucher);
                                    utx.commit();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException | NullPointerException ex) {
            throw new ServiceRuntimeException(ErrorResponseMessage.INVALID_REQUEST);
        }
    }

    @Override
    public String voucherCoupon(String voucherCode) {
        return "<h1>" + voucherCode + "<h1>";
    }

}
