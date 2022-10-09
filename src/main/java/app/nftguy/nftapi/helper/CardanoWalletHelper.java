package app.nftguy.nftapi.helper;

import java.util.ArrayList;
import java.util.List;
import org.openapitools.cardanowalletclient.ApiClient;
import org.openapitools.cardanowalletclient.ApiException;
import org.openapitools.cardanowalletclient.Configuration;
import org.openapitools.cardanowalletclient.api.AddressesApi;
import org.openapitools.cardanowalletclient.api.NetworkApi;
import org.openapitools.cardanowalletclient.api.TransactionsApi;
import org.openapitools.cardanowalletclient.api.TransactionsNewApi;
import org.openapitools.cardanowalletclient.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CardanoWalletHelper {

  String walletId;
  AddressesApi addressesApi;
  NetworkApi networkApi;
  TransactionsNewApi transactionsNewApi;

  TransactionsApi transactionsApi;
  List<String> availableAddresses = new ArrayList<>();

  Logger logger = LoggerFactory.getLogger(CardanoWalletHelper.class);

  public CardanoWalletHelper(Environment environment) throws ApiException {
    String baseURL =
        String.format(
            "%s:%s/v2",
            environment.getProperty("wallet.url"), environment.getProperty("wallet.port"));
    this.walletId = environment.getProperty("wallet.walletId");

    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(baseURL);
    Configuration.setDefaultApiClient(apiClient);

    this.addressesApi = new AddressesApi();
    this.networkApi = new NetworkApi();
    this.transactionsNewApi = new TransactionsNewApi();
    this.transactionsApi = new TransactionsApi();
    refreshAddresses();
  }

  private void refreshAddresses() throws ApiException {
    this.availableAddresses.clear();
    this.addressesApi
        .listAddresses(this.walletId, "unused")
        .forEach(
            data -> {
              this.availableAddresses.add(data.getId());
            });
  }

  public ApiNetworkInformationSyncProgress getNetworkInfo() throws ApiException {
    return networkApi.getNetworkInformation().getSyncProgress();
  }

  public List<String> getUnusedAddresses() {
    try {
      refreshAddresses();
    } catch (ApiException e) {
      throw new RuntimeException(e);
    }
    return availableAddresses;
  }

  public InlineResponse2025 SubmitTransaction(String cborBytes) {
    try {
      WalletIdTransactionssubmitBody transactionssubmitBody = new WalletIdTransactionssubmitBody();
      transactionssubmitBody.setTransaction(cborBytes);
      InlineResponse2025 response =
          transactionsNewApi.submitTransaction(transactionssubmitBody, this.walletId);
      logger.info(String.format("Success submitting transaction. %s", response.toString()));
      return response;
    } catch (ApiException e) {
      logger.warn(String.format("Error submitting transaction. %s", e.toString()));
      throw new RuntimeException(e);
    }
  }
}
