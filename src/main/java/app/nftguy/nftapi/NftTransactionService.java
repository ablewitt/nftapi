package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.AddressHelper;
import app.nftguy.nftapi.helper.CardanoWalletHelper;
import app.nftguy.nftapi.helper.EmailService;
import app.nftguy.nftapi.model.NftTransaction;
import app.nftguy.nftapi.model.NftTransactionDraft;
import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.repository.TransactionRepository;
import com.bloxbean.cardano.client.api.exception.ApiException;
import java.math.BigInteger;
import org.openapitools.cardanowalletclient.model.InlineResponse2025;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NftTransactionService {

  AddressHelper addressHelper;
  TransactionRepository transactionRepository;
  NftTransaction nftTransaction;
  CardanoWalletHelper cardanoWalletHelper;
  TransactionCheckService transactionCheckService;
  EmailService emailService;

  Logger logger = LoggerFactory.getLogger(NftTransactionService.class);

  NftTransactionService(
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

  public NftTransactionDraft checkStatus(String id) throws ApiException {
    this.nftTransaction = this.transactionRepository.findItemById(id);
    if (!this.nftTransaction.getPaymentState().equals(PaymentState.PENDING)) {
      return this.transactionRepository.findItemByIdRestricted(id);
    }
    transactionCheckService.checkTtl(this.nftTransaction);
    NftTransactionDraft nftTransactionDraft = this.transactionRepository.findItemByIdRestricted(id);
    BigInteger balanceOwing = checkBalanceOwing();
    nftTransactionDraft.setNftPayAddressBalance(balanceOwing);
    if (balanceOwing.compareTo(new BigInteger("0")) < 1) {
      submit();
    }
    return nftTransactionDraft;
  }

  private BigInteger checkBalanceOwing() throws ApiException {
    String nftPayAddress = nftTransaction.getNftPayAddress();
    BigInteger addressBalance = addressHelper.getAddressBalance(nftPayAddress);
    BigInteger totalBill = nftTransaction.getCreateFee().add(nftTransaction.getNetworkFee());
    return totalBill.subtract(addressBalance);
  }

  private String submit() {
    InlineResponse2025 result =
        cardanoWalletHelper.SubmitTransaction(nftTransaction.getTransactionCBORBytes());
    if (result.getId().length() > 1) {
      logger.info(
          String.format(
              "Successfully processed transaction %s, cardano transaction %s",
              nftTransaction.getId(), result));
      nftTransaction.setPaymentState(PaymentState.COMPLETED);
      nftTransaction.setTransactionId(result.getId());
      transactionRepository.save(nftTransaction);
      emailService.sendMimeMessage(nftTransaction);
      return result.getId();
    } else {
      logger.warn(String.format("Failed processing transaction %s", nftTransaction.getId()));
      throw new RuntimeException(result.toString());
    }
  }
}
