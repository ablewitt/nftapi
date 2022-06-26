package app.nftguy.nftapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Data
@Document("config")
public class NftTransactionConfig {

    @Id
    Integer id;

    Long ttl;

    BigInteger createFee;

    public NftTransactionConfig(Integer id, Long ttl, BigInteger createFee) {
        this.id = id;
        this.ttl = ttl;
        this.createFee = createFee;
    }
}
