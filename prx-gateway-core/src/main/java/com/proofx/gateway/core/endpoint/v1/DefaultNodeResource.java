package com.proofx.gateway.core.endpoint.v1;

import com.proofx.gateway.api.v1.NodeResource;
import com.proofx.gateway.api.v1.model.nodeserver.*;
import com.proofx.gateway.core.DefaultNodeService;
import com.proofx.gateway.core.configuration.PropertyService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class DefaultNodeResource implements NodeResource {

    private PropertyService propertyService;
    private DefaultNodeService implementationService;

    @Inject
    DefaultNodeResource(final PropertyService propertyService, final DefaultNodeService implementationService) {

        this.propertyService = propertyService;
        this.implementationService = implementationService;
        this.propertyService.getClass();
    }

    @Override
    public Response ping() {
        return this.implementationService.ping();
    }

    @Override
    public CheckAddressResponse checkAddress(String address) {
        return this.implementationService.checkAddress(address);
    }

    @Override
    public TransactionResponse transfer(TokenTransferRequest request) {
        return this.implementationService.transfer(request);
    }

    @Override
    public WhitelistResponse getWhitelist(GetWhitelistRequest request) {
        return this.implementationService.getWhitelist(request);
    }

    @Override
    public TransactionResponse addToWhitelist(ModifyWhitelistRequest request) {
        return this.implementationService.modifyWhitelist(request, true);
    }

    @Override
    public TransactionResponse removeFromWhitelist(ModifyWhitelistRequest request) {
        return this.implementationService.modifyWhitelist(request, false);
    }

    @Override
    public GetAdminResponse getAdmin(String contractAddress) {
        return this.implementationService.getAdmin(contractAddress);
    }

    @Override
    public GetAllowedActivitiesResponse getAllowedActivities(String contractAddress) {
        return this.implementationService.getAllowedActivities(contractAddress);
    }

    @Override
    public GetSuspendedActivitiesResponse getSuspendedActivities(String contractAddress) {
        return this.implementationService.getSuspendedActivities(contractAddress);
    }

    @Override
    public GetNonceResponse getNonce(String contractAddress, String address) {
        return this.implementationService.getNonce(contractAddress, address);
    }

    @Override
    public GetActivityLogAddressResponse getActivityLogAddress(String contractAddress) {
        return this.implementationService.getActivityLogAddress(contractAddress);
    }

    @Override
    public CalculateUserSignatureResponse calculateUserSignature(CalculateUserSignatureRequest request) {
        return this.implementationService.calculateUserSignature(request);
    }

    @Override
    public TransactionResponse callSuspendAllowedActivity(CallSuspendAllowedActivityRequest request) {
        return this.implementationService.callSuspendAllowedActivity(request);
    }

    @Override
    public TransactionResponse callAddAllowedActivity(CallAddAllowedActivityRequest request) {
        return this.implementationService.callAddAllowedActivity(request);
    }

    @Override
    public TransactionResponse changeAdminThis(ChangeAdminThisRequest request) {
        return this.implementationService.changeAdminThis(request);
    }

    @Override
    public TransactionResponse changeActivityLog(ChangeActivityLogRequest request) {
        return this.implementationService.changeActivityLog(request);
    }

    @Override
    public TransactionResponse registerTandemClaims(RegisterTandemClaimsRequest request) {
        return this.implementationService.registerTandemClaims(request);
    }
}
