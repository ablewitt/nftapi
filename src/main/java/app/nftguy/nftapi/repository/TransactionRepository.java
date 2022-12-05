package app.nftguy.nftapi.repository;

import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.model.Transaction;
import app.nftguy.nftapi.model.TransactionAddress;
import app.nftguy.nftapi.model.TransactionClientModel;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

  @Query("{_id:'?0'}")
  Transaction findTransactionById(String id);

  @Query("{address:'?0'}")
  Transaction findTransactionByAddress(String address);

  @Query(
      value = "{paymentState:'?0'}",
      fields = "{'_id' : 1, 'nftPayAddress' : 1, 'paymentState':  1}")
  List<TransactionAddress> findAddressByPaymentState(PaymentState state);

  @Query(
      value = "{_id:'?0'}",
      fields =
          "{'_id' : 1, 'transactionId': 1, 'nftPayAddress' : 1,'nftRxAddress':  1,'networkFee': 1,'createFee': 1, 'paymentState': 1 }")
  TransactionClientModel createTransactionClientModelById(String id);

  public long count();
}
