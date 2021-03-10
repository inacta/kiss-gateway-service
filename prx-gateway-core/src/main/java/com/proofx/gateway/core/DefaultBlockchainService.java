package com.proofx.gateway.core;

import com.proofx.gateway.api.v1.model.blockchain.TokenAmountResponse;
import com.proofx.gateway.api.v1.model.nodeserver.Status;
import com.proofx.gateway.core.configuration.PropertyService;
import com.proofx.gateway.tezosj.TezosJ;
import com.proofx.gateway.tezosj.contracts.FA1_2Contract;
import com.proofx.gateway.tezosj.exceptions.InvalidAddressException;
import io.vertx.ext.web.RoutingContext;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
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

    @Inject
    PropertyService propertyService;

    String provider;
    String mnemonic;
    String passPhrase;

    private TezosJ tezosJ;

    @PostConstruct
    void init() {
        this.provider = propertyService.getTezosProvider();
        this.mnemonic = propertyService.getMnemonic();
        this.passPhrase = propertyService.getPassphrase();
        this.tezosJ = new TezosJ(this.provider);
        this.tezosJ.accounts.importWallet(this.mnemonic, this.passPhrase);
    }


    /**
     * retrieves token amount
     *
     * @param contractAddress address of token smart contract
     * @param address token holder address
     * @return token amount or error
     */
    public TokenAmountResponse getTokenAmount(String contractAddress, String address) {
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
