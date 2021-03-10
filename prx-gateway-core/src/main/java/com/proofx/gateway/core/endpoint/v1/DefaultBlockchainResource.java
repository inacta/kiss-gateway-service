package com.proofx.gateway.core.endpoint.v1;

import com.proofx.gateway.api.v1.BlockchainResource;
import com.proofx.gateway.api.v1.model.ServiceRuntimeException;
import com.proofx.gateway.api.v1.model.blockchain.TokenAmountResponse;
import com.proofx.gateway.core.DefaultBlockchainService;
import com.proofx.gateway.core.logging.ExceptionLogged;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * The default implementation of the REST endpoint for the Blockchain service
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
@ExceptionLogged
@Path("/blockchain/v1")
public class DefaultBlockchainResource implements BlockchainResource {

    final DefaultBlockchainService implementationService;

    @Context
    @Inject
    RoutingContext routingContext;

    @Inject
    DefaultBlockchainResource(final DefaultBlockchainService implementationService) {
        this.implementationService = implementationService;
    }

    @Override
    public TokenAmountResponse getTokenAmount(String contractAddress, String address) {
        String authHeader = routingContext.request().getHeader("Authorization");
        if (authHeader == null || !authHeader.equals("810887b3-29dc-4cad-85ab-e7b1ae765ff8")) {
            throw new ServiceRuntimeException(UNAUTHORIZED);
        }
        return implementationService.getTokenAmount(contractAddress, address);
    }
}
