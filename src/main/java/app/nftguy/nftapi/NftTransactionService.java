package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.*;
import app.nftguy.nftapi.model.NftTransaction;
import app.nftguy.nftapi.model.NftTransactionDraft;
import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.nftstorage.NftStorageService;
import app.nftguy.nftapi.repository.TransactionRepository;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class NftTransactionService {

    AddressHelper addressHelper;
    TransactionRepository transactionRepository;
    NftTransaction nftTransaction;
    BlockFrostHelper blockFrostHelper;
    TransactionCheckService transactionCheckService;

    Logger logger = LoggerFactory.getLogger(NftTransactionService.class);

    NftTransactionService(AddressHelper addressHelper,
                          TransactionRepository transactionRepository,
                          BlockFrostHelper blockFrostHelper,
                          TransactionCheckService transactionCheckService){
        this.addressHelper = addressHelper;
        this.transactionRepository = transactionRepository;
        this.blockFrostHelper = blockFrostHelper;
        this.transactionCheckService = transactionCheckService;
    }

    public NftTransactionDraft checkStatus(String id) throws ApiException {
        this.nftTransaction = this.transactionRepository.findItemById(id);
        if (!this.nftTransaction.getPaymentState().equals(PaymentState.PENDING)){
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

    private Result<String> submit()  {
        try {
            blockFrostHelper.getTxnService().evaluateTx(hexStringToByteArray(nftTransaction.getTransactionCBORBytes()));
            Result<String> result =  blockFrostHelper
                    .getTxnService()
                    .submitTransaction(hexStringToByteArray(nftTransaction.getTransactionCBORBytes()));
            if(result.isSuccessful()){
                logger.info(String.format("Successfully processed transaction %s, cardano transaction %s",
                        nftTransaction.getId(), result.getValue()));
                nftTransaction.setPaymentState(PaymentState.COMPLETED);
                nftTransaction.setTransactionId(result.getValue());
                transactionRepository.save(nftTransaction);
                return result;
            }else {
                logger.warn(String.format("Failed processing transaction %s, %s",
                        nftTransaction.getId(), result.toString()));
                throw new RuntimeException(result.toString());
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    /* s must be an even-length string. */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }



}
