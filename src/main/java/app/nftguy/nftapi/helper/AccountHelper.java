package app.nftguy.nftapi.helper;

import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.common.model.Networks;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AccountHelper {

  private final Account account;

  public AccountHelper(Environment environment) {
    String mnemonic = environment.getProperty("account.mnemonic");
    String network = environment.getProperty("cardano.network");

    Network cardanoNetwork = null;
    switch (network) {
      case "preprod":
        cardanoNetwork = Networks.preprod();
        break;
      case "preview":
        cardanoNetwork = Networks.preview();
        break;
      case "testnet":
        cardanoNetwork = Networks.testnet();
        break;
      default:
        cardanoNetwork = Networks.mainnet();
        break;
    }
    this.account = new Account(cardanoNetwork, mnemonic);
  }

  public Account getAccount() {
    return account;
  }
}
