package com.proofx.gateway.api.v1;

import io.quarkus.vertx.web.Body;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * A REST endpoint for the Tag service (API)
 *
 * @author ProofX
 * @since 1.0.0
 */
@RegisterRestClient
@Path("/humanity/v1")
public interface HumanityResource {

    /**
     * Receive webhook from shopify
     *
     * @param body webhook request
     */
    @POST
    @Transactional
    @Path("/webhook")
    @Consumes(MediaType.APPLICATION_JSON)
    void webhook(@Body String body);

    /**
     * Get coupon for voucher
     *
     * @param voucherCode voucher code
     * @return generated html
     */
    @GET
    @Transactional
    @Path("/voucher/{code}")
    @Produces("text/html")
    String voucherCoupon(@PathParam("code") String voucherCode);
}
