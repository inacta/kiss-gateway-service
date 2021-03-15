package com.proofx.gateway.core.endpoint.v1;

import com.proofx.gateway.api.v1.TagResource;
import com.proofx.gateway.api.v1.model.ServiceRuntimeException;
import com.proofx.gateway.api.v1.model.StatusResponse;
import com.proofx.gateway.api.v1.model.tag.Tag;
import com.proofx.gateway.core.DefaultTagService;
import com.proofx.gateway.core.configuration.PropertyService;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * Tag verification
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
@Path("/tag/v1")
public class DefaultTagResource implements TagResource {

    private PropertyService propertyService;
    private DefaultTagService implementationService;

    @Context
    @Inject
    RoutingContext routingContext;

    @Inject
    DefaultTagResource(final PropertyService propertyService, final DefaultTagService implementationService) {
        this.propertyService = propertyService;
        this.implementationService = implementationService;
        this.propertyService.getClass();
    }

    @Override
    public StatusResponse read(String uuid, String counter, String mac) {
        return this.implementationService.read(uuid, counter, mac);
    }

    @Override
    public void write(Tag tag) {
        String authHeader = routingContext.request().getHeader("Authorization");
        if (authHeader == null || !authHeader.equals("e9361336-cb7e-43bc-b89c-3e066c365dc0")) {
            throw new ServiceRuntimeException(UNAUTHORIZED);
        }
        this.implementationService.write(tag);
    }
}
