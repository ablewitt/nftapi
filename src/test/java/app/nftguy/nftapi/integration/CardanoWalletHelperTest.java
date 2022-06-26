package app.nftguy.nftapi.integration;

import app.nftguy.nftapi.helper.CardanoWalletHelper;
import iog.psg.cardano.CardanoApiCodec;
import iog.psg.cardano.jpi.CardanoApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@SpringBootTest
public class CardanoWalletHelperTest {


    @Autowired
    private Environment environment;

    @Autowired
    CardanoWalletHelper cardanoWalletHelper;


    @Test
    void create(){
        Assertions.assertEquals(cardanoWalletHelper.getClass(), CardanoWalletHelper.class);
    }

    @Test
    void getNetworkInfo() throws CardanoApiException {
        cardanoWalletHelper.getNetworkInfo();
    }

    @Test
    void openWalletById() throws CardanoApiException, ExecutionException, InterruptedException {
   //     cardanoWalletHelper.openWalletById(environment.getProperty("wallet.walletId"));
    }

    @Test
    void getAddresses() throws CardanoApiException, ExecutionException, InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        cardanoWalletHelper.getUnusedAddresses();
        for (CardanoApiCodec.WalletAddressId addressId:
                cardanoWalletHelper.getUnusedAddresses()) {
            Assertions.assertEquals("Some(unused)", addressId.state().toString());
        };
    }

}
