package com.proofx.gateway.tezosj;

import com.proofx.gateway.tezosj.core.*;
import com.proofx.gateway.tezosj.util.Global;

@SuppressWarnings("java:S1176")
public class TezosJ {

    public final Accounts accounts;
    public final ContractFactory contract;
    public final Util util;
    public final Gateway gateway;
    public final Crypto crypto;

    public TezosJ(String provider) {
        this.accounts = new Accounts(this);
        this.contract = new ContractFactory(this);
        this.util = new Util();
        this.gateway = new Gateway(this);
        this.crypto = new Crypto(this);
        Global.defaultProvider = provider;
    }

}
