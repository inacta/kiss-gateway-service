package com.proofx.gateway.core;

import com.proofx.gateway.api.v1.model.blockchain.TokenAmountResponse;
import com.proofx.gateway.api.v1.model.nodeserver.Status;
import com.proofx.gateway.tezosj.TezosJ;
import com.proofx.gateway.tezosj.contracts.FA1_2Contract;
import com.proofx.gateway.tezosj.exceptions.InvalidAddressException;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;

/**
 * The default functional implementation of the REST endpoint for the Blockchain service
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
public class DefaultBlockchainService {

    @ConfigProperty(name = "provider")
    String provider;

    @ConfigProperty(name = "mnemonic")
    String mnemonic;

    @ConfigProperty(name = "passPhrase")
    String passPhrase;

    private final TezosJ tezosJ;

    @Context
    @Inject
    RoutingContext routingContext;

    @Inject
    DefaultBlockchainService() {
        this.tezosJ = new TezosJ("https://testnet-tezos.giganode.io/");
        this.tezosJ.accounts.importWallet("dog nuclear mistake document manage fox grow claim champion online unusual ivory guide know season", "myPassphrase");
    }


    /**
     * retrieves token amount
     *
     * @param contractAddress address of token smart contract
     * @param address token holder address
     * @return token amount or error
     */
    public TokenAmountResponse getTokenAmount(String contractAddress, String address) {
        if (!routingContext.request().getHeader("Authorization").equals("810887b3-29dc-4cad-85ab-e7b1ae765ff8")) {
            return new TokenAmountResponse(Status.ERROR, "Unauthorized");
        }
        FA1_2Contract contract;
        try {
            contract = this.tezosJ.contract.at(contractAddress, FA1_2Contract.class);
        } catch (IllegalArgumentException e) {
            return new TokenAmountResponse(Status.ERROR, "invalid contract address");
        } catch (Exception e) {
            return new TokenAmountResponse(Status.ERROR, "Internal Server Error");
        }
        try {
            this.tezosJ.util.checkAddress(address);
        } catch (InvalidAddressException e) {
            return new TokenAmountResponse(Status.ERROR, "invalid address");
        }
        JSONObject result;
        try {
            result = contract.getTokenBalance(address);
        } catch (Exception e) {
            return new TokenAmountResponse(Status.ERROR, "error while fetching token balance");
        }

        return new TokenAmountResponse(Status.SUCCESS, result.get("result").toString());
    }
}
