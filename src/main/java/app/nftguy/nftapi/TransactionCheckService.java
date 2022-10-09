package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.BlockFrostHelper;
import app.nftguy.nftapi.model.NftTransaction;
import app.nftguy.nftapi.model.PaymentState;
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
    for (NftTransaction nftTransaction : transactionRepository.findAll()) {
      checkTtl(nftTransaction);
    }
  }

  public void checkTtl(NftTransaction nftTransaction) throws ApiException {
    if (nftTransaction.getPaymentState().equals(PaymentState.PENDING)) {
      this.currentBlock = blockFrostHelper.getBlockService().getLatestBlock().getValue().getSlot();
      if (nftTransaction.getTtl().compareTo(currentBlock) < 0) {
        logger.info(String.format("Transaction %s expired", nftTransaction.getId()));
        nftTransaction.setPaymentState(PaymentState.EXPIRED);
        transactionRepository.save(nftTransaction);
      }
    }
  }
}
