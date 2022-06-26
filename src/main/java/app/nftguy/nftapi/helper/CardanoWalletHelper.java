package app.nftguy.nftapi.helper;

import akka.actor.ActorSystem;
import iog.psg.cardano.CardanoApiCodec;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import iog.psg.cardano.jpi.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CardanoWalletHelper {

    String walletId;
    CardanoApi api;

    CardanoApiCodec.Wallet wallet;

    List<CardanoApiCodec.WalletAddressId> availableAddresses = new ArrayList<>();

    public CardanoWalletHelper(Environment environment) throws CardanoApiException {
        String baseURL = String.format("%s:%s/v2/",
                environment.getProperty("wallet.url"), environment.getProperty("wallet.port"));
        this.walletId = environment.getProperty("wallet.walletId");

        ActorSystem as = ActorSystem.create();
        ExecutorService es = Executors.newFixedThreadPool(10);

        CardanoApiBuilder builder =
                CardanoApiBuilder.create(baseURL)
                        .withActorSystem(as) // <- ActorSystem optional
                        .withExecutorService(es); // <- ExecutorService optional
        api = builder.build();
        refreshAddresses();
        getWallet();
    }

    private void refreshAddresses() throws CardanoApiException {
        api.listAddresses(this.walletId).whenComplete((addresses, throwable) ->{
            this.availableAddresses = new ArrayList<>();
            for (CardanoApiCodec.WalletAddressId address:
                    addresses) {
                if(address.state().toString().equals("Some(unused)")){
                    this.availableAddresses.add(address);
                }
            }
        });
    }

    private void getWallet() throws CardanoApiException {
        api.getWallet(walletId).whenComplete((wallet, throwable) -> {
            this.wallet = wallet;
        });
    }

     public CompletionStage<CardanoApiCodec.NetworkInfo> getNetworkInfo() throws CardanoApiException {
        return api.networkInfo();
    }

    public List<CardanoApiCodec.WalletAddressId> getUnusedAddresses() {
        try {
            refreshAddresses();
        } catch (CardanoApiException e) {
            throw new RuntimeException(e);
        }
        return availableAddresses;
    }

}

