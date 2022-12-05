package app.nftguy.nftapi.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("nft_transaction")
public class Transaction {

  @Id private String id;

  private String transactionId;

  private String nftPayAddress;

  private String transactionCBORBytes;

  private LocalDateTime dateAdded;

  private PaymentState paymentState;

  private String nftRxAddress;

  private BigInteger networkFee;

  private BigInteger createFee;

  private String email;

  private Long ttl;

  public Transaction(
      String nftPayAddress,
      String transactionCBORBytes,
      String nftRxAddress,
      BigInteger networkFee,
      BigInteger createFee,
      Long ttl) {

    this.nftPayAddress = nftPayAddress;
    this.transactionCBORBytes = transactionCBORBytes;
    this.nftRxAddress = nftRxAddress;
    this.networkFee = networkFee;
    this.createFee = createFee;
    this.dateAdded = LocalDateTime.now();
    this.paymentState = PaymentState.UPLOADING;
    this.ttl = ttl;
  }

  public void setTransactionCBORBytes(String transactionCBORBytes) {
    this.transactionCBORBytes = transactionCBORBytes;
  }

  public void setNetworkFee(BigInteger networkFee) {
    this.networkFee = networkFee;
  }

  public void setTtl(Long ttl) {
    this.ttl = ttl;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getId() {
    return id;
  }

  public String getNftPayAddress() {
    return nftPayAddress;
  }

  public String getTransactionCBORBytes() {
    return transactionCBORBytes;
  }

  public LocalDateTime getDateAdded() {
    return dateAdded;
  }

  public PaymentState getPaymentState() {
    return paymentState;
  }

  public String getNftRxAddress() {
    return nftRxAddress;
  }

  public BigInteger getNetworkFee() {
    return networkFee;
  }

  public BigInteger getCreateFee() {
    return createFee;
  }

  public Long getTtl() {
    return ttl;
  }

  public void setPaymentState(PaymentState paymentState) {
    this.paymentState = paymentState;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
