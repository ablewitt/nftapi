package app.nftguy.nftapi.helper;

import org.openapitools.cardanowalletclient.ApiClient;
import org.openapitools.cardanowalletclient.ApiException;
import org.openapitools.cardanowalletclient.api.AddressesApi;
import org.openapitools.cardanowalletclient.api.NetworkApi;
import org.openapitools.cardanowalletclient.model.ApiNetworkInformationSyncProgress;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardanoWalletHelper {

    String walletId;
    AddressesApi addressesApi;
    NetworkApi networkApi;

    List<String> availableAddresses = new ArrayList<>();

    public CardanoWalletHelper(Environment environment) throws ApiException {
        String baseURL = String.format("%s:%s/v2",
                environment.getProperty("wallet.url"),
                environment.getProperty("wallet.port"));
        this.walletId = environment.getProperty("wallet.walletId");

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(baseURL);
        this.addressesApi = new AddressesApi(apiClient);
        this.networkApi = new NetworkApi(apiClient);
        refreshAddresses();
    }

    private void refreshAddresses() throws ApiException {
        this.availableAddresses.clear();
        this.addressesApi.listAddresses(this.walletId, "unused").forEach(data ->{
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

}

