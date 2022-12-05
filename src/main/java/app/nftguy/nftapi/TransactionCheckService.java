package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.BlockFrostHelper;
import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.model.Transaction;
import app.nftguy.nftapi.repository.TransactionRepository;
import com.bloxbean.cardano.client.api.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransactionCheckService {

  BlockFrostHelper blockFrostHelper;
  TransactionRepository transactionRepository;

  Long currentBlock;

  Logger logger = LoggerFactory.getLogger(TransactionCheckService.class);

  TransactionCheckService(
      BlockFrostHelper blockFrostHelper, TransactionRepository transactionRepository) {
    this.blockFrostHelper = blockFrostHelper;
    this.transactionRepository = transactionRepository;
  }

  @Scheduled(fixedDelay = 60000)
  public void scheduleExpirationCheck() throws ApiException {
    logger.debug("Checking for expired transactions");
    for (Transaction transaction : transactionRepository.findAll()) {
      checkTtl(transaction);
    }
  }

  public void checkTtl(Transaction transaction) throws ApiException {
    if (transaction.getPaymentState().equals(PaymentState.PENDING)) {
      this.currentBlock = blockFrostHelper.getBlockService().getLatestBlock().getValue().getSlot();
      if (transaction.getTtl().compareTo(currentBlock) < 0) {
        logger.info(String.format("Transaction %s expired", transaction.getId()));
        transaction.setPaymentState(PaymentState.EXPIRED);
        transactionRepository.save(transaction);
      }
    }
  }
}
