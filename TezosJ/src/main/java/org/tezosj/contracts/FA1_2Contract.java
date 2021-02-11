package org.tezosj.contracts;

import org.json.JSONObject;
import org.tezosj.TezosJ;
import org.tezosj.exceptions.InvalidAddressException;
import org.tezosj.util.Global;

import java.math.BigDecimal;

import static org.tezosj.util.Helpers.extractBacktrackedResult;

public class FA1_2Contract extends Contract {
    public FA1_2Contract(TezosJ tezosj, String contractAddress) throws InvalidAddressException {
        super(tezosj, contractAddress);
    }

    public JSONObject getTokenBalance(String owner) throws Exception {
        JSONObject jsonObject = this.callEntryPoint(new BigDecimal("0"), new BigDecimal("0.1"), "", "", "getBalance", new String[]{owner, Global.NAT_STORAGE_TESTNET_ADDRESS}, false, Global.FA12_STANDARD);
        JSONObject result = new JSONObject();
        result.put("result", extractBacktrackedResult(jsonObject.getJSONObject("result")));
        return result;
    }

}
