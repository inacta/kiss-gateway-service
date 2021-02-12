package com.proofx.gateway.api.v1;

import com.proofx.gateway.api.v1.model.NewTagParams;
import com.proofx.gateway.api.v1.model.StatusResponse;
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
@Path("/tag/v1")
public interface TagResource {

    /**
     * Verify a tags validity
     *
     * @param uuid uuid of the nfc tag
     * @param counter nfc tag counter
     * @param mac nfc tag verification code
     * @return if nfc tag is valid
     */
    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    StatusResponse read(
            @QueryParam("u") String uuid,
            @QueryParam("n") String counter,
            @QueryParam("e") String mac);

    /**
     * Add a new tag
     *
     * @param params uuid and secret of the nfc tag
     * @return success or failure
     */
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    String write(NewTagParams params);

}
