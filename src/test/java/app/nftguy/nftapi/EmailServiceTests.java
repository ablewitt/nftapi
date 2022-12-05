package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.EmailService;
import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.model.Transaction;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

  @Autowired EmailService emailService;

  @Test
  void contextLoads() {}

  @Test
  void sendMimeEmail() {
    Transaction nft =
        new Transaction(
            "addr1thisisanaddress",
            "asdfasdfasdf",
            "addr1thisisanotheraddress",
            new BigInteger("1"),
            new BigInteger("1"),
            999l);
    nft.setPaymentState(PaymentState.COMPLETED);
    nft.setTransactionId("notarealtxid");
    nft.setEmail("adamblewitt@gmail.com");
    emailService.sendMimeMessage(nft);
  }
}
