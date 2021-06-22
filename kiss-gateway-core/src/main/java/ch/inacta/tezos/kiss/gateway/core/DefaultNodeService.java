package ch.inacta.tezos.kiss.gateway.core;

import ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver.*;
import ch.inacta.tezos.kiss.gateway.core.configuration.PropertyService;
import ch.inacta.tezos.kiss.gateway.core.remote.RemoteServiceBuilder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


/**
 * class
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
public class DefaultNodeService {
    private final RemoteServiceBuilder remoteServiceBuilder;
    private PropertyService propertyService;

    @Inject
    DefaultNodeService(final PropertyService propertyService, RemoteServiceBuilder remoteServiceBuilder) {
        this.remoteServiceBuilder = remoteServiceBuilder;
        this.propertyService = propertyService;
    }

    /**
     * ping
     *
     * @return response
     */
    public Response ping() {
        return this.remoteServiceBuilder.nodeEnpoint().ping();
    }

    /**
     * Check if address is a valid tezos address
     *
     * @param address input address
     * @return true or false
     */
    public CheckAddressResponse checkAddress(String address) {
        return this.remoteServiceBuilder.nodeEnpoint().checkAddress(address);
    }

    /**
     * get token balance
     *
     * @param contractAddress contract address
     * @param address input address
     * @param tokenId optional token id
     * @return true or false
     */
    public GetBalanceResponse getBalance(String contractAddress, String address, String tokenId) {
        return this.remoteServiceBuilder.nodeEnpoint().getBalance(contractAddress, address, tokenId);
    }

    /**
     * transfer tokens
     *
     * @param request tokenTransferRequest
     * @return TransactionResponse
     */
    public TransactionResponse transfer(TokenTransferRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().transfer(request);
    }

    /**
     * get whitelist entry
     *
     * @param request GetWhitelistRequest
     * @return WhitelistResponse
     */
    public WhitelistResponse getWhitelist(GetWhitelistRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().getWhitelist(request);
    }

    /**
     * modify whitelist entry
     *
     * @param request ModifyWhitelistRequest
     * @param add     add or remove from whitelist
     * @return TransactionResponse
     */
    public TransactionResponse modifyWhitelist(ModifyWhitelistRequest request, boolean add) {
        if (add) {
            return this.remoteServiceBuilder.nodeEnpoint().addToWhitelist(request);
        } else {
            return this.remoteServiceBuilder.nodeEnpoint().removeFromWhitelist(request);
        }
    }

    /**
     * getAdmin
     *
     * @param contractAddress contractAddress
     * @return GetAdminResponse
     */
    public GetAdminResponse getAdmin(String contractAddress) {
        return this.remoteServiceBuilder.nodeEnpoint().getAdmin(contractAddress);
    }

    /**
     * getAllowedActivities
     *
     * @param contractAddress contractAddress
     * @return GetAllowedActivitiesResponse
     */
    public GetAllowedActivitiesResponse getAllowedActivities(@QueryParam("contractAddress") String contractAddress) {
        return this.remoteServiceBuilder.nodeEnpoint().getAllowedActivities(contractAddress);
    }

    /**
     * getSuspendedActivities
     *
     * @param contractAddress contractAddress
     * @return GetSuspendedActivitiesResponse
     */
    public GetSuspendedActivitiesResponse getSuspendedActivities(@QueryParam("contractAddress") String contractAddress) {
        return this.remoteServiceBuilder.nodeEnpoint().getSuspendedActivities(contractAddress);
    }

    /**
     * getNonce
     *
     * @param contractAddress contractAddress
     * @param address         address
     * @return GetNonceResponse
     */
    public GetNonceResponse getNonce(@QueryParam("contractAddress") String contractAddress, @QueryParam("address") String address) {
        return this.remoteServiceBuilder.nodeEnpoint().getNonce(contractAddress, address);
    }

    /**
     * getActivityLogAddress
     *
     * @param contractAddress contractAddress
     * @return GetActivityLogAddressResponse
     */
    public GetActivityLogAddressResponse getActivityLogAddress(@QueryParam("contractAddress") String contractAddress) {
        return this.remoteServiceBuilder.nodeEnpoint().getActivityLogAddress(contractAddress);
    }

    /**
     * calculateUserSignature
     *
     * @param request CalculateUserSignatureRequest
     * @return CalculateUserSignatureResponse
     */
    public CalculateUserSignatureResponse calculateUserSignature(CalculateUserSignatureRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().calculateUserSignature(request);
    }

    /**
     * generateKeyPair
     *
     * @return KeyPair
     */
    public KeyPair generateKeyPair() {
        return this.remoteServiceBuilder.nodeEnpoint().generateKeyPair();
    }

    /**
     * callSuspendAllowedActivity
     *
     * @param request CallSuspendAllowedActivityRequest
     * @return TransactionResponse
     */
    public TransactionResponse callSuspendAllowedActivity(CallSuspendAllowedActivityRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().callSuspendAllowedActivity(request);
    }

    /**
     * callAddAllowedActivity
     *
     * @param request CallAddAllowedActivityRequest
     * @return TransactionResponse
     */
    public TransactionResponse callAddAllowedActivity(CallAddAllowedActivityRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().callAddAllowedActivity(request);
    }

    /**
     * changeAdminThis
     *
     * @param request ChangeAdminThisRequest
     * @return TransactionResponse
     */
    public TransactionResponse changeAdminThis(ChangeAdminThisRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().changeAdminThis(request);
    }

    /**
     * changeActivityLog
     *
     * @param request ChangeActivityLogRequest
     * @return TransactionResponse
     */
    public TransactionResponse changeActivityLog(ChangeActivityLogRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().changeActivityLog(request);
    }

    /**
     * registerTandemClaims
     *
     * @param request RegisterTandemClaimsRequest
     * @return TransactionResponse
     */
    public TransactionResponse registerTandemClaims(RegisterTandemClaimsRequest request) {
        return this.remoteServiceBuilder.nodeEnpoint().registerTandemClaims(request);
    }
}
