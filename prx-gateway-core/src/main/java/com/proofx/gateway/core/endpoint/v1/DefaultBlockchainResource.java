package com.proofx.gateway.core.endpoint.v1;

import com.proofx.gateway.api.v1.BlockchainResource;
import com.proofx.gateway.api.v1.model.blockchain.TokenAmountResponse;
import com.proofx.gateway.core.DefaultBlockchainService;
import com.proofx.gateway.core.logging.ExceptionLogged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

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

    private static final Logger LOG = LoggerFactory.getLogger(DefaultBlockchainResource.class);

    final DefaultBlockchainService implementationService;

    @Inject
    DefaultBlockchainResource(final DefaultBlockchainService implementationService) {
        this.implementationService = implementationService;
    }

    @Override
    public TokenAmountResponse getTokenAmount(String contractAddress, String address) {
        return implementationService.getTokenAmount(contractAddress, address);
    }
}
