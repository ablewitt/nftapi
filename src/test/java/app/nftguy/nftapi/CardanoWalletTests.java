package app.nftguy.nftapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.cardanowalletclient.ApiClient;
import org.openapitools.cardanowalletclient.ApiException;
import org.openapitools.cardanowalletclient.api.AddressesApi;
import org.openapitools.cardanowalletclient.api.WalletsApi;
import org.openapitools.cardanowalletclient.model.InlineResponse201;
import org.openapitools.cardanowalletclient.model.WalletsBalance;


public class CardanoWalletTests {

    String baseUrl = "http://docker.lan:8090/v2";
    String walletId = "9351a93228bf46ed351450a02986e0339dca8c92";
    ApiClient apiClient;

    @BeforeEach
    void setup(){
        this.apiClient= new ApiClient();
        apiClient.setBasePath(baseUrl);
    }

    @Test
    void wallet() throws ApiException {
        WalletsApi walletsApi = new WalletsApi(this.apiClient);
        WalletsBalance walletsBalance = new WalletsBalance();

        try {
            InlineResponse201 result = walletsApi.getWallet(walletId).balance(walletsBalance);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AddressesApi#inspectAddress");
            e.printStackTrace();
        }
    }

    @Test
    void address() throws ApiException{
        AddressesApi addressesApi = new AddressesApi(this.apiClient);
        addressesApi.listAddresses(this.walletId, "unused").forEach(data ->{
            System.out.println(data.getId());
        });
    }
}
