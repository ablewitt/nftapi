package app.nftguy.nftapi;

import org.junit.jupiter.api.Test;
import org.openapitools.cardanowalletclient.ApiClient;
import org.openapitools.cardanowalletclient.ApiException;
import org.openapitools.cardanowalletclient.api.WalletsApi;
import org.openapitools.cardanowalletclient.model.InlineResponse201;
import org.openapitools.cardanowalletclient.model.WalletsBalance;


public class CardanoWalletTests {

    String baseUrl = "http://docker.lan:8090/v2";

    @Test
    void setup() throws ApiException {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(baseUrl);

        WalletsApi walletsApi = new WalletsApi(apiClient);

        WalletsBalance walletsBalance = new WalletsBalance();

        String addressId = "9351a93228bf46ed351450a02986e0339dca8c92"; // String |
        try {
            InlineResponse201 result = walletsApi.getWallet(addressId).balance(walletsBalance);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AddressesApi#inspectAddress");
            e.printStackTrace();
        }
    }
}
