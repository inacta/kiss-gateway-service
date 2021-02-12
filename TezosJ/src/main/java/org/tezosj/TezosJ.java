package org.tezosj;

import org.tezosj.core.*;
import org.tezosj.util.Global;

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
