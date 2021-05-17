package com.proofx.gateway.api.v1;

import com.proofx.gateway.api.v1.model.nodeserver.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A REST endpoint for the Node service (API)
 *
 * @author ProofX
 * @since 1.0.0
 */
@RegisterRestClient
@Path("/xtzsmartcontract/v1")
public interface NodeResource {

    /**
     * ping
     *
     * @return response
     */
    @GET
    @Path("/ping")
    Response ping();

    /**
     * Check if address is a valid tezos address
     *
     * @param address input address
     * @return true or false
     */
    @GET
    @Path("/checkAddress")
    @QueryParam("address")
    @Produces(MediaType.APPLICATION_JSON)
    CheckAddressResponse checkAddress(@QueryParam("address") String address);

    /**
     * get token balance
     *
     * @param contractAddress contract address
     * @param address input address
     * @param tokenId optional token id
     * @return true or false
     */
    @GET
    @Path("/getBalance")
    @QueryParam("address")
    @Produces(MediaType.APPLICATION_JSON)
    GetBalanceResponse getBalance(@QueryParam("contractAddress") String contractAddress, @QueryParam("address") String address, @QueryParam("tokenId") String tokenId);

    /**
     * transfer tokens
     *
     * @param request tokenTransferRequest
     * @return TransactionResponse
     */
    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse transfer(TokenTransferRequest request);

    /**
     * get whitelist entry
     *
     * @param request GetWhitelistRequest
     * @return WhitelistResponse
     */
    @POST
    @Path("/getWhitelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    WhitelistResponse getWhitelist(GetWhitelistRequest request);

    /**
     * add whitelist entry
     *
     * @param request ModifyWhitelistRequest
     * @return TransactionResponse
     */
    @POST
    @Path("/addToWhitelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse addToWhitelist(ModifyWhitelistRequest request);

    /**
     * remove whitelist entry
     *
     * @param request ModifyWhitelistRequest
     * @return TransactionResponse
     */
    @DELETE
    @Path("/removeFromWhitelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse removeFromWhitelist(ModifyWhitelistRequest request);

    /**
     * getAdmin
     *
     * @param contractAddress contractAddress
     * @return GetAdminResponse
     */
    @GET
    @Path("/getAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    GetAdminResponse getAdmin(@QueryParam("contractAddress") String contractAddress);

    /**
     * getAllowedActivities
     *
     * @param contractAddress contractAddress
     * @return GetAllowedActivitiesResponse
     */
    @GET
    @Path("/getAllowedActivities")
    @Produces(MediaType.APPLICATION_JSON)
    GetAllowedActivitiesResponse getAllowedActivities(@QueryParam("contractAddress") String contractAddress);

    /**
     * getSuspendedActivities
     *
     * @param contractAddress contractAddress
     * @return GetSuspendedActivitiesResponse
     */
    @GET
    @Path("/getSuspendedActivities")
    @Produces(MediaType.APPLICATION_JSON)
    GetSuspendedActivitiesResponse getSuspendedActivities(@QueryParam("contractAddress") String contractAddress);

    /**
     * getNonce
     *
     * @param contractAddress contractAddress
     * @param address address
     * @return GetNonceResponse
     */
    @GET
    @Path("/getNonce")
    @Produces(MediaType.APPLICATION_JSON)
    GetNonceResponse getNonce(@QueryParam("contractAddress") String contractAddress, @QueryParam("address") String address);

    /**
     * getActivityLogAddress
     *
     * @param contractAddress contractAddress
     * @return GetActivityLogAddressResponse
     */
    @GET
    @Path("/getActivityLogAddress")
    @Produces(MediaType.APPLICATION_JSON)
    GetActivityLogAddressResponse getActivityLogAddress(@QueryParam("contractAddress") String contractAddress);

    /**
     * generateKeyPair
     *
     * @return KeyPair
     */
    @GET
    @Path("/generateKeyPair")
    @Produces(MediaType.APPLICATION_JSON)
    KeyPair generateKeyPair();

    /**
     * calculateUserSignature
     *
     * @param request CalculateUserSignatureRequest
     * @return CalculateUserSignatureResponse
     */
    @POST
    @Path("/calculateUserSignature")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CalculateUserSignatureResponse calculateUserSignature(CalculateUserSignatureRequest request);

    /**
     * callSuspendAllowedActivity
     *
     * @param request CallSuspendAllowedActivityRequest
     * @return TransactionResponse
     */
    @POST
    @Path("/callSuspendAllowedActivity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse callSuspendAllowedActivity(CallSuspendAllowedActivityRequest request);

    /**
     * callAddAllowedActivity
     *
     * @param request CallAddAllowedActivityRequest
     * @return TransactionResponse
     */
    @POST
    @Path("/callAddAllowedActivity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse callAddAllowedActivity(CallAddAllowedActivityRequest request);

    /**
     * changeAdminThis
     *
     * @param request ChangeAdminThisRequest
     * @return TransactionResponse
     */
    @POST
    @Path("/changeAdminThis")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse changeAdminThis(ChangeAdminThisRequest request);

    /**
     * changeActivityLog
     *
     * @param request ChangeActivityLogRequest
     * @return TransactionResponse
     */
    @POST
    @Path("/changeActivityLog")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse changeActivityLog(ChangeActivityLogRequest request);

    /**
     * registerTandemClaims
     *
     * @param request RegisterTandemClaimsRequest
     * @return TransactionResponse
     */
    @POST
    @Path("/registerTandemClaims")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    TransactionResponse registerTandemClaims(RegisterTandemClaimsRequest request);

}
