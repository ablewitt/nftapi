package app.nftguy.nftapi.integration;

import app.nftguy.nftapi.helper.AccountHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountHelperTest {

  @Autowired AccountHelper accountHelper;

  @Test
  void create() {
    Assertions.assertEquals(accountHelper.getClass(), AccountHelper.class);
  }
}
