package app.nftguy.nftapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.cardanowalletclient.ApiClient;
import org.openapitools.cardanowalletclient.ApiException;
import org.openapitools.cardanowalletclient.api.AddressesApi;
import org.openapitools.cardanowalletclient.api.TransactionsNewApi;
import org.openapitools.cardanowalletclient.api.WalletsApi;
import org.openapitools.cardanowalletclient.model.*;

public class CardanoWalletTests {

  String baseUrl = "http://docker.lan:8090/v2";
  String walletId = "9351a93228bf46ed351450a02986e0339dca8c92";
  ApiClient apiClient;

  @BeforeEach
  void setup() {
    this.apiClient = new ApiClient();
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
  void address() throws ApiException {
    AddressesApi addressesApi = new AddressesApi(this.apiClient);
    addressesApi
        .listAddresses(this.walletId, "unused")
        .forEach(
            data -> {
              System.out.println(data.getId());
            });
  }

  @Test
  void testTransactionSubmit() throws ApiException {
    String txBody =
        "84a60081825820039fc80965fd58b0c201fbff999962367d2f8fa853e321593a79ede6bed3415b010182825839003fa8f2f0b5b179409eada44315763f450f95099193638fa39b210bce30faa5284d364a797e7388a308ec12eff3eb7093ed90c52ee170a802821a0113db52b1581c047c095a864b35616709dddf8f432c87daff7fcadcd865a80ae4e38aa149537065656479426f6902581c091bc3c54257600ef904aabf1739a25269cda8b0714dfa8d5bf7dc2ea1446164616d01581c1000196f824f8d82c1d453878b9bc3dd6de32d909b7f51ec4da6e8a8a148736466677364666702581c32c1dec81d0fbbf6137d26f53b72761f069b77f9a4fc0e07fd380d67a1446164616d01581c416a21343a2f4363d91e0e325b5f237399ae109f49c225589ea881b3a148646667686466676801581c45fdf51e59b526ff11dbb4b1b7cbd80af04f7a17e11051c4cad33c0ba14a6672656e63685f6e667401581c52c17de02d5016304145086974bb60793f005f86007d58a94c8469b9a14a6672656e63685f6e667401581c610fc9c99f4c233ea9d474dbf6e29242912495e198aecb50ef36298aa14974657374746f6b656e01581c7c27b773040ccfd3a1774571d832905d2b5ac275011b67fe6693b64da1447878585801581c88e771feee9eace73df82fa29dd47e3cbc0ea4e235b351aa92d164daa14954657374417373657401581c9748431644a73864c306697cad63894dc9282dd6e7e198e00373baaea14974657374746f6b656e01581ca3b6d4e2cfbbdcb33f04ccae438c84df5290e7237422793e8987b589a1446164616d01581cbeb51ea634ccf38297bcc91738c9ad6c1ef1e47151d36c5fdc4eff33a149537065656479426f6902581cd19a74d9f85197ec83925c33b1ae73260e595e60bd7a6163c7b27642a1446164616d01581cd94b7e1a8ea6290b310b0e0d92ed9db5df86a1a0bfb6e32e368a4687a1446164616d01581cf398b4a213ae0866f69fa0c09e74d56e7753fe2320d7c2736c480644a145616473616402581cfbc75d2d924b77e7ab9bc8213c373b7afafd708c73b7f6300430f50fa1446164616d0182583900d27f327958b1b4d58d15c24eac432bf0f8e8c115af06c835253b0ae3ca75b49b4f9973d8c26a1e95be3560aa5fb89887e4e0198744af711c821a0011b0dea1581c9b773139fcb95ae6e0b92561e14854ec5541875c7e9bfa1d4c098aeca149537065656479426f6901021a00034e61031a041836150758207a82d91ef0246a120173acfdd3a6bef07937a5a41db3d68d80c6338b12834ee609a1581c9b773139fcb95ae6e0b92561e14854ec5541875c7e9bfa1d4c098aeca149537065656479426f6901a200828258207f72094372c7115a032368b227e5a09af64a6f42d0539acc95c5114533bdca1c584052a1264144b53c503f323f277313a646a6a9b9bb567324ff240f6443211667e0ae6b3dd28a9f01bed5031f825448822095eceb1b0afb162d26f9e50d32cdeb028258206b255cbc3c5380c0e40c0fcd300faa03e8127f4bc10d24bfd0d326e0fc8865845840b15e522d289418daf5fabf6ef0338db403090742896bcd9839715da6d7a49890a5e9c387c7c4f61b51d8735d4662794762896fe6bbf7628e10442e643e8b420801818200581cc7c5b05b4ee80730073421b35857fa3446864d03458a82074c4fac0ff5a11902d1a26776657273696f6e63312e3078383962373733313339666362393561653665306239323536316531343835346563353534313837356337653962666131643463303938616563a169537065656479426f69a2646e616d6569537065656479426f6965696d616765783b6261666b72656961346e7273766b75636f79357577743578797067786f7773716e73627068616e68657078357066723372346d776c746976767271";
    TransactionsNewApi transactionsNewApi = new TransactionsNewApi(apiClient);
    WalletIdTransactionssubmitBody walletIdTransactionssubmitBody =
        new WalletIdTransactionssubmitBody();
    walletIdTransactionssubmitBody.setTransaction(txBody);
    transactionsNewApi.submitTransaction(walletIdTransactionssubmitBody, this.walletId);
  }
}
