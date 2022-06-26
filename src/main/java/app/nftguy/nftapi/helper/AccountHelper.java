package app.nftguy.nftapi.helper;

import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.common.model.Networks;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AccountHelper {

    private final Account account;

    public AccountHelper(Environment environment){
        String mnemonic = environment.getProperty("account.mnemonic");
        String network = environment.getProperty("cardano.network");
        Network cardanoNetwork = Networks.testnet();
        if(network.equalsIgnoreCase("mainnet")){
            cardanoNetwork = Networks.mainnet();
        }
        this.account = new Account(cardanoNetwork, mnemonic);
    }

    public Account getAccount() {
        return account;
    }

}
