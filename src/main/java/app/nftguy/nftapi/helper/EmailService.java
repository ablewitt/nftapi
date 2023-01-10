package app.nftguy.nftapi.helper;

import app.nftguy.nftapi.model.Transaction;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

  Logger logger = LoggerFactory.getLogger(EmailService.class);
  private final JavaMailSender emailSender;
  private final Environment environment;

  EmailService(JavaMailSender emailSender, Environment environment) {
    this.emailSender = emailSender;
    this.environment = environment;
  }

  public void sendMimeMessage(Transaction nft) {
    final Document doc;
    try {
      doc =
          Jsoup.parse(new File(Objects.requireNonNull(environment.getProperty("email.template"))));
    } catch (IOException e) {
      logger.warn(
          String.format(
              "Email template files error %s, template location %s",
              e, environment.getProperty("email.template")));
      return;
    }
    MimeMessagePreparator preparator =
        new MimeMessagePreparator() {
          @Override
          public void prepare(MimeMessage mimeMessage) throws Exception {
            doc.select("#NFTname").append("");
            doc.select("#NFTstatus").append(nft.getPaymentState().toString());
            doc.select("#TransactionLink").attr("href", ExplorerLink(nft.getTransactionId()));
            doc.select("#rxAddress").append(nft.getNftRxAddress());
            mimeMessage.setContent(doc.toString(), "text/html; charset=utf-8");
            mimeMessage.setRecipients(Message.RecipientType.TO, nft.getEmail());
            mimeMessage.setFrom(environment.getProperty("email.fromaddress"));
            mimeMessage.setSubject("cunft.app details");
          }
        };
    try {
      this.emailSender.send(preparator);
    } catch (MailException e) {
      logger.warn(String.format("Email send error %s", e));
    }
  }

  public String ExplorerLink(String txId) {
    if (Objects.equals(environment.getProperty("cardano.network"), "testnet")) {
      return "https://explorer.cardano-testnet.iohkdev.io/en/transaction?id=" + txId;
    } else {
      return "https://explorer.cardano.org/en/transaction?id=" + txId;
    }
  }
}
