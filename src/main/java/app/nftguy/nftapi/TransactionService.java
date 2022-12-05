package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.AddressHelper;
import app.nftguy.nftapi.helper.CardanoWalletHelper;
import app.nftguy.nftapi.helper.EmailService;
import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.model.Transaction;
import app.nftguy.nftapi.model.TransactionClientModel;
import app.nftguy.nftapi.repository.TransactionRepository;
import com.bloxbean.cardano.client.api.exception.ApiException;
import java.math.BigInteger;
import org.openapitools.cardanowalletclient.model.InlineResponse2025;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

  AddressHelper addressHelper;
  TransactionRepository transactionRepository;
  Transaction transaction;
  CardanoWalletHelper cardanoWalletHelper;
  TransactionCheckService transactionCheckService;
  EmailService emailService;

  Logger logger = LoggerFactory.getLogger(TransactionService.class);

  TransactionService(
      AddressHelper addressHelper,
      TransactionRepository transactionRepository,
      CardanoWalletHelper cardanoWalletHelper,
      TransactionCheckService transactionCheckService,
      EmailService emailService) {
    this.addressHelper = addressHelper;
    this.transactionRepository = transactionRepository;
    this.cardanoWalletHelper = cardanoWalletHelper;
    this.transactionCheckService = transactionCheckService;
    this.emailService = emailService;
  }

  public TransactionClientModel checkStatus(String id) throws ApiException {
    this.transaction = this.transactionRepository.findTransactionById(id);
    if (!this.transaction.getPaymentState().equals(PaymentState.PENDING)) {
      return this.transactionRepository.createTransactionClientModelById(id);
    }
    transactionCheckService.checkTtl(this.transaction);
    TransactionClientModel transactionClientModel =
        this.transactionRepository.createTransactionClientModelById(id);
    BigInteger balanceOwing = checkBalanceOwing();
    transactionClientModel.setNftPayAddressBalance(balanceOwing);
    if (balanceOwing.compareTo(new BigInteger("0")) < 1) {
      submit();
    }
    return transactionClientModel;
  }

  private BigInteger checkBalanceOwing() throws ApiException {
    String nftPayAddress = transaction.getNftPayAddress();
    BigInteger addressBalance = addressHelper.getAddressBalance(nftPayAddress);
    BigInteger totalBill = transaction.getCreateFee().add(transaction.getNetworkFee());
    return totalBill.subtract(addressBalance);
  }

  private String submit() {
    InlineResponse2025 result =
        cardanoWalletHelper.SubmitTransaction(transaction.getTransactionCBORBytes());
    if (result.getId().length() > 1) {
      logger.info(
          String.format(
              "Successfully processed transaction %s, cardano transaction %s",
              transaction.getId(), result));
      transaction.setPaymentState(PaymentState.COMPLETED);
      transaction.setTransactionId(result.getId());
      transactionRepository.save(transaction);
      emailService.sendMimeMessage(transaction);
      return result.getId();
    } else {
      logger.warn(String.format("Failed processing transaction %s", transaction.getId()));
      throw new RuntimeException(result.toString());
    }
  }
}
