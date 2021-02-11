import org.tezosj.TezosJ;
import org.tezosj.contracts.FA1_2Contract;

public class Main {
    public static void main(String[] args) throws Exception {
        String address = "tz1bejpHpyj574Hp3auFMdXFYn15YSedMxBk";
        String contractAddress = "KT1XJufutki3QvPdWF9Jx1ZGCSkyNzFdQac";
        TezosJ tezosJ = new TezosJ("https://mainnet-tezos.giganode.io/");
        // Delphi NetXm8tYqnMWky1
        // Mainnet NetXdQprcVkpaWU
        System.out.println(tezosJ.gateway.getChainId());
        tezosJ.accounts.importWallet("dog nuclear mistake document manage fox grow claim champion online unusual ivory guide know season", "myPassphrase");
//        FA1_2Contract contract = tezosJ.contract.at(contractAddress, FA1_2Contract.class);
//        JSONObject jsonObject = contract.getTokenBalance(address);
//        System.out.println(jsonObject.get("result"));
    }
}
