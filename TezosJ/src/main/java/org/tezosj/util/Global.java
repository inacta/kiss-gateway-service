package org.tezosj.util;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;

public class Global {

    private Global() {}

    public static final String TEZOS_SYMBOL = "\uA729";
    public static final Integer UTEZ = 1000000;
    public static final String TZJ_KEY_ALIAS = "tzj";
    public static final String OPERATION_KIND_TRANSACTION = "transaction";
    public static final String OPERATION_KIND_DELEGATION = "delegation";
    public static final String OPERATION_KIND_ORIGINATION = "origination";
    public static KeyStore myKeyStore = null;
    public static String proxyHost = "";
    public static String proxyPort = "";
    public static String defaultProvider = "https://mainnet.tezrpc.me";
    public static OkHttpClient myOkhttpClient = null;
    public static Builder myOkhttpBuilder = null;
    public static String ledgerDerivationPath = "";
    public static String ledgerTezosFolderPath = "";
    public static String ledgerTezosFilePath = "";
    public static String KT_TO_TZ_GAS_LIMIT = "26283";
    public static String KT_TO_TZ_STORAGE_LIMIT = "0";
    public static String KT_TO_TZ_FEE = "0.005";
    public static String KT_TO_KT_GAS_LIMIT = "44725";
    public static String KT_TO_KT_STORAGE_LIMIT = "0";
    public static String KT_TO_KT_FEE = "0.005";
    public static String NAT_STORAGE_ADDRESS = "KT1NhtHwHD5cqabfSdwg1Fowud5f175eShwx";
    public static String NAT_STORAGE_TESTNET_ADDRESS = "KT1JSo98VAy8HThip93mhMxdS3t3P7t5RSeV";
    public static String FA12_STANDARD = "FA12";
    public static String FA2_STANDARD = "F2";
    public static String NYX_STANDARD = "NYX";
    public static String GENERIC_STANDARD = "generic";
    public static String FA12_TRANSFER = "transfer";
    public static String FA12_APPROVE = "approve";
    public static String FA12_GET_ALLOWANCE = "getAllowance";
    public static String FA12_GET_BALANCE = "getBalance";
    public static String FA12_GET_TOTAL_SUPPLY = "getTotalSupply";
    public static String CONFIRM_WITH_LEDGER_MESSAGE = "Waiting for transaction confirmation on Ledger hardware device...";
    public static int RAND_SEED = new SecureRandom().nextInt(1000000) + 1;

    public static void initKeyStore() throws KeyStoreException {
        if (myKeyStore == null) {
            myKeyStore = KeyStore.getInstance("JCEKS");
        }
    }

    public static void initOkhttp() {
        if (myOkhttpClient == null) {
            myOkhttpClient = new OkHttpClient();

            if (myOkhttpBuilder == null) {
                myOkhttpBuilder = myOkhttpClient.newBuilder();
            }
        }
    }
}
