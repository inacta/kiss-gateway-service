import com.proofx.gateway.tezosj.TezosJ;
import com.proofx.gateway.tezosj.contracts.FA1_2Contract;
import org.json.JSONObject;

import java.math.BigDecimal;

@SuppressWarnings({"java:S125","java:S127", "java:S1220", "java:S106", "java:S1176", "java:S1220"})
public class Main {
    public static void main(String[] args) throws Exception {
        String address = "tz1bejpHpyj574Hp3auFMdXFYn15YSedMxBk";
        String contractAddress = "KT1XJufutki3QvPdWF9Jx1ZGCSkyNzFdQacf";
        TezosJ tezosJ = new TezosJ("https://testnet-tezos.giganode.io/");
        // Delphi NetXm8tYqnMWky1
        // Mainnet NetXdQprcVkpaWU
        System.out.println(tezosJ.gateway.getChainId());
        tezosJ.accounts.importWallet("dog nuclear mistake document manage fox grow claim champion online unusual ivory guide know season", "myPassphrase");
        FA1_2Contract contract = tezosJ.contract.at(contractAddress, FA1_2Contract.class);
        JSONObject jsonObject = contract.getTokenBalance(address);
        System.out.println(jsonObject.get("result"));
        jsonObject = tezosJ.gateway.sendTransaction(address, address, new BigDecimal("1"), new BigDecimal("0.001"), "", "");
        System.out.println(jsonObject.get("result"));
    }
}
