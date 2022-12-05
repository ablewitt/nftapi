package app.nftguy.nftapi.model;

import java.math.BigInteger;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("config")
public class TransactionConfiguration {

  @Id Integer id;

  Long ttl;

  BigInteger createFee;

  public TransactionConfiguration(Integer id, Long ttl, BigInteger createFee) {
    this.id = id;
    this.ttl = ttl;
    this.createFee = createFee;
  }
}
