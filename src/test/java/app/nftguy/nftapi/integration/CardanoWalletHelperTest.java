package app.nftguy.nftapi.integration;

import app.nftguy.nftapi.helper.CardanoWalletHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.cardanowalletclient.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

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
    void getNetworkInfo() throws ApiException {
        cardanoWalletHelper.getNetworkInfo();
    }

    @Test
    void getAddresses() {
        for (String address:
                cardanoWalletHelper.getUnusedAddresses()) {
            Assertions.assertTrue(address.contains("addr"));
        }
    }

}
