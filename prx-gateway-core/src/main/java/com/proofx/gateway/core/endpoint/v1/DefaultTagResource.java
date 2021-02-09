package com.proofx.gateway.core.endpoint.v1;

import com.proofx.gateway.api.v1.TagResource;
import com.proofx.gateway.api.v1.model.NewTagParams;
import com.proofx.gateway.api.v1.model.StatusResponse;
import com.proofx.gateway.core.DefaultTagService;
import com.proofx.gateway.core.configuration.PropertyService;

import javax.inject.Inject;

public class DefaultTagResource implements TagResource {

    private PropertyService propertyService;
    private DefaultTagService implementationService;

    @Inject
    DefaultTagResource(final PropertyService propertyService, final DefaultTagService implementationService) {
        this.propertyService = propertyService;
        this.implementationService = implementationService;
        this.propertyService.getClass();
    }

    @Override
    public StatusResponse read(String uuid, String counter, String MAC) {
        return this.implementationService.read(uuid, counter, MAC);
    }

    @Override
    public String write(NewTagParams params) {
        return null;
    }
}
