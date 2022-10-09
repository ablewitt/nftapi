package app.nftguy.nftapi.repository;

import app.nftguy.nftapi.model.NftTransaction;
import app.nftguy.nftapi.model.NftTransactionAddress;
import app.nftguy.nftapi.model.NftTransactionDraft;
import app.nftguy.nftapi.model.PaymentState;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TransactionRepository extends MongoRepository<NftTransaction, String> {

  @Query("{_id:'?0'}")
  NftTransaction findItemById(String id);

  @Query("{address:'?0'}")
  NftTransaction findItemByAddress(String address);

  @Query(
      value = "{paymentState:'?0'}",
      fields = "{'_id' : 1, 'nftPayAddress' : 1, 'paymentState':  1}")
  List<NftTransactionAddress> findAddressByPaymentState(PaymentState state);

  @Query(
      value = "{_id:'?0'}",
      fields =
          "{'_id' : 1, 'transactionId': 1, 'nftPayAddress' : 1,'nftRxAddress':  1,'networkFee': 1,'createFee': 1, 'paymentState': 1 }")
  NftTransactionDraft findItemByIdRestricted(String id);

  public long count();
}
