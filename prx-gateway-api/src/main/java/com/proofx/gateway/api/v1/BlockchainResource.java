package com.proofx.gateway.api.v1;

import com.proofx.gateway.api.v1.model.blockchain.TokenAmountResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A REST endpoint for the Blockchain service (API)
 *
 * @author ProofX
 * @since 1.0.0
 */
@RegisterRestClient
@Path("/blockchain/v1")
public interface BlockchainResource {

    /**
     * A method for getting an endpoint property-key that should be used for configuration on platform
     *
     * @return property-key
     */
    static String getEndpointPropertyKey() {
        return "BLOCKCHAIN_SERVICE_ENDPOINT";
    }

    /**
     * gets the token amount for the Humanity experience token
     *
     * @param contractAddress contractAddress
     * @param address address
     * @return amount of tokens
     */
    @GET
    @Path("/tokenBalance/{contractAddress}/{address}")
    @Produces(MediaType.APPLICATION_JSON)
    TokenAmountResponse getTokenAmount(@PathParam("contractAddress") String contractAddress, @PathParam("address") String address);
}
