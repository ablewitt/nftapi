package app.nftguy.nftapi;

import app.nftguy.nftapi.model.NftTransactionConfig;
import app.nftguy.nftapi.repository.ConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class Config {

    Environment environment;
    ConfigurationRepository configurationRepository;

    Logger logger = LoggerFactory.getLogger(Config.class);

    public Config(Environment environment, ConfigurationRepository configurationRepository){
        this.configurationRepository = configurationRepository;
        this.environment = environment;
        getConfig();
    }

    public void setConfig(){
        NftTransactionConfig config = new NftTransactionConfig(
                0,
                Long.parseLong(environment.getProperty("create.ttlOffset")),
                new BigInteger(environment.getProperty("create.fee")));
        configurationRepository.save(config);
    }

    public NftTransactionConfig getConfig(){

        if (configurationRepository.findItemById(0) == null){
            logger.info(String.format("Config not found in database"));
            setConfig();
        }

        return configurationRepository.findItemById(0);
    }
}
