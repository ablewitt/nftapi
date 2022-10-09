package app.nftguy.nftapi.repository;

import app.nftguy.nftapi.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ConfigurationRepository extends MongoRepository<NftTransactionConfig, Integer> {

  @Query()
  NftTransactionConfig findItemById(Integer id);

  public long count();
}
