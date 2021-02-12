package com.proofx.gateway.tezosj.contracts;

import com.proofx.gateway.tezosj.TezosJ;
import com.proofx.gateway.tezosj.exceptions.InvalidAddressException;
import com.proofx.gateway.tezosj.exceptions.NoWalletSetException;
import com.proofx.gateway.tezosj.util.Global;
import com.proofx.gateway.tezosj.util.Helpers;
import org.json.JSONObject;

import java.math.BigDecimal;

@SuppressWarnings({"java:S1176", "java:S101"})
public class FA1_2Contract extends Contract {
    public FA1_2Contract(TezosJ tezosj, String contractAddress) throws InvalidAddressException {
        super(tezosj, contractAddress);
    }

    public JSONObject getTokenBalance(String owner) throws NoWalletSetException {
        JSONObject jsonObject = this.callEntryPoint(new BigDecimal("0"), new BigDecimal("0.1"), "", "", "getBalance", new String[]{owner, Global.NAT_STORAGE_TESTNET_ADDRESS}, false, Global.FA12_STANDARD);
        JSONObject result = new JSONObject();
        result.put("result", Helpers.extractBacktrackedResult(jsonObject.getJSONObject("result")));
        return result;
    }

}
