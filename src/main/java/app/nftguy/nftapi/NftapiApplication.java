package app.nftguy.nftapi;

import app.nftguy.nftapi.model.NftTransactionConfig;
import app.nftguy.nftapi.repository.ConfigurationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigInteger;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class NftapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftapiApplication.class, args);
    }
}
