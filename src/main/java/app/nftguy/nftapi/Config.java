package app.nftguy.nftapi;

import app.nftguy.nftapi.model.TransactionConfiguration;
import app.nftguy.nftapi.repository.ConfigurationRepository;
import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Config {

  Environment environment;
  ConfigurationRepository configurationRepository;

  Logger logger = LoggerFactory.getLogger(Config.class);

  public Config(Environment environment, ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
    this.environment = environment;
    getConfig();
  }

  public void setConfig() {
    TransactionConfiguration config =
        new TransactionConfiguration(
            0,
            Long.parseLong(environment.getProperty("create.ttlOffset")),
            new BigInteger(environment.getProperty("create.fee")));
    configurationRepository.save(config);
  }

  public TransactionConfiguration getConfig() {

    if (configurationRepository.findItemById(0) == null) {
      logger.info(String.format("Config not found in database, setting to defaults"));
      setConfig();
    }

    return configurationRepository.findItemById(0);
  }
}
