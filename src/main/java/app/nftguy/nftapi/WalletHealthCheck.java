package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.CardanoWalletHelper;
import iog.psg.cardano.CardanoApiCodec;
import org.openapitools.cardanowalletclient.model.ApiNetworkInformationSyncProgress;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("cardanoWalletAPI")
public class WalletHealthCheck implements HealthIndicator {

    CardanoWalletHelper cardanoWalletHelper;

    public WalletHealthCheck(CardanoWalletHelper cardanoWalletHelper) {
        this.cardanoWalletHelper = cardanoWalletHelper;
    }

    @Override
    public Health health() {
        HashMap<String, String> status = check();
        //perform some specific health check
        if (status.containsKey("ready")) {
            return Health.up()
                    .withDetails(status).build();
        }
        return Health.down()
                .withDetails(status).build();
    }

    public HashMap<String, String> check() {
        HashMap<String, String> status = new HashMap<>();
        try {
            ApiNetworkInformationSyncProgress response = cardanoWalletHelper.getNetworkInfo();
            status.put(response.getStatus().getValue(),
                        response.getProgress() == null ?
                                "100" : response.getProgress().getQuantity().toString());
        } catch (Exception e) {
            status.put("Error", "Wallet not reachable");
        }
        return status;
    }
}
