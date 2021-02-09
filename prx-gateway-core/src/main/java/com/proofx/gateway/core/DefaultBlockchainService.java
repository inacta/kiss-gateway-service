package com.proofx.gateway.core;

import com.proofx.gateway.api.v1.model.blockchain.TokenAmountResponse;
import com.proofx.gateway.api.v1.model.nodeserver.Status;
import com.proofx.gateway.core.remote.RemoteServiceBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tezosj.TezosJ;
import org.tezosj.contracts.FA1_2Contract;
import org.tezosj.exceptions.InvalidAddressException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;

/**
 * The default functional implementation of the REST endpoint for the Blockchain service
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
public class DefaultBlockchainService {

    @ConfigProperty(name="provider")
    String provider;

    @ConfigProperty(name="mnemonic")
    String mnemonic;

    @ConfigProperty(name="passPhrase")
    String passPhrase;

    private final RemoteServiceBuilder remoteServiceBuilder;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBlockchainService.class);
    private final TezosJ tezosJ;

    @Inject
    DefaultBlockchainService(final RemoteServiceBuilder remoteServiceBuilder) {
        this.remoteServiceBuilder = remoteServiceBuilder;
        this.tezosJ = new TezosJ("https://testnet-tezos.giganode.io/");
        this.tezosJ.accounts.importWallet("dog nuclear mistake document manage fox grow claim champion online unusual ivory guide know season", "myPassphrase");
    }

    public TokenAmountResponse getTokenAmount(String contractAddress, String address) {
        FA1_2Contract contract;
        try {
            contract = this.tezosJ.contract.at(contractAddress, FA1_2Contract.class);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            return new TokenAmountResponse(Status.ERROR, "Internal Server Error");
        } catch (InvocationTargetException e) {
            return new TokenAmountResponse(Status.ERROR, "invalid contract address");
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
