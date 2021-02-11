package org.tezosj;

import org.tezosj.core.Accounts;
import org.tezosj.core.ContractFactory;
import org.tezosj.core.Gateway;
import org.tezosj.core.Util;
import org.tezosj.util.Global;

public class TezosJ {

    public final Accounts accounts;
    public final ContractFactory contract;
    public final Util util;
    public final Gateway gateway;

    public TezosJ(String provider) {
        this.accounts = new Accounts();
        this.contract = new ContractFactory(this);
        this.util = new Util();
        this.gateway = new Gateway(this);
        Global.defaultProvider = provider;
    }

}
