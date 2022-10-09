package app.nftguy.nftapi.integration;

import app.nftguy.nftapi.helper.AddressHelper;
import com.bloxbean.cardano.client.api.exception.ApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddressHelperTest {

  @Autowired AddressHelper addressHelper;

  @Test
  void create() {
    Assertions.assertEquals(addressHelper.getClass(), AddressHelper.class);
  }

  @Test
  void getAddressBalanceTest() throws ApiException {
    Assertions.assertTrue(
        addressHelper
                .getAddressBalance(
                    "addr_test1qzlukggg0kpd6dgs2eumh6nsajx4mtsl0urnsawnekvkfgesl2jjsnfkffuhuuug5vywcyh0704hpyldjrzjacts4qpqjfcz38")
                .longValue()
            >= 0);
  }
}
