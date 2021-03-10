package com.proofx.gateway.core.endpoint.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proofx.gateway.api.v1.HumanityResource;
import com.proofx.gateway.api.v1.model.ErrorResponseMessage;
import com.proofx.gateway.api.v1.model.ServiceRuntimeException;
import com.proofx.gateway.api.v1.model.humanity.WebhookRequest;
import com.proofx.gateway.core.DefaultHumanityService;
import com.proofx.gateway.core.configuration.PropertyService;
import io.vertx.ext.web.RoutingContext;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
                this.implementationService.generateVoucher(webhookRequest);
            }

        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException | NullPointerException ex) {
            throw new ServiceRuntimeException(ErrorResponseMessage.INVALID_REQUEST);
        }
    }

}
