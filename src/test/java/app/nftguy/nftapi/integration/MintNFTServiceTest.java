package app.nftguy.nftapi.integration;

import app.nftguy.nftapi.NftBuilder;
import app.nftguy.nftapi.cip25.MetaData;
import app.nftguy.nftapi.helper.*;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.helper.model.TransactionResult;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigInteger;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
public class MintNFTServiceTest {

  String receiveAddress =
      "addr_test1qrf87vnetzcmf4vdzhpyatzr90c036xpzkhsdjp4y5as4c72wk6fknuew0vvy6s7jklr2c92t7uf3plyuqvcw390wywql37usz";
  String ipfsLink = "ipfs://Qme7ss3ARVgxv6rXqVPiikMJ8u2NLgmgszg13pYrDKEoiu";
  String nftName = "My-Cool_NFT";
  String tokenName = "TestAsset-Meta";

  @Autowired AccountHelper accountHelper;

  @Autowired BlockFrostHelper blockFrostHelper;

  @Test
  public void create() throws CborSerializationException, JsonProcessingException, ApiException {
    NftBuilder nftBuilder = new NftBuilder(blockFrostHelper, accountHelper);
    KeyHelper keyHelper = new KeyHelper();
    PolicyHelper policyHelper = new PolicyHelper(keyHelper.getVKey(), keyHelper.getSKey());
    AssetHelper assetHelper =
        new AssetHelper("TestAsset", BigInteger.valueOf(1L), policyHelper.getPolicyId());

    JSONObject attributes = new JSONObject();
    attributes.put("name", nftName);
    attributes.put("image", ipfsLink);
    MetaData metaData = new MetaData(tokenName, attributes, policyHelper.getPolicyId());

    nftBuilder
        .setMultiAsset(assetHelper.getMultiAsset())
        .setPolicy(policyHelper.getPolicy())
        .setMetaData(metaData)
        .setReceiveAddress(receiveAddress)
        .setTTL(1000L)
        .build();
  }

  @Test
  public void createAndSubmit()
      throws CborSerializationException, JsonProcessingException, ApiException {
    NftBuilder nftBuilder = new NftBuilder(blockFrostHelper, accountHelper);
    KeyHelper keyHelper = new KeyHelper();
    PolicyHelper policyHelper = new PolicyHelper(keyHelper.getVKey(), keyHelper.getSKey());
    AssetHelper assetHelper =
        new AssetHelper("TestAsset", BigInteger.valueOf(1L), policyHelper.getPolicyId());

    JSONObject attributes = new JSONObject();
    attributes.put("name", nftName);
    attributes.put("image", ipfsLink);
    MetaData metaData = new MetaData(tokenName, attributes, policyHelper.getPolicyId());

    nftBuilder
        .setMultiAsset(assetHelper.getMultiAsset())
        .setPolicy(policyHelper.getPolicy())
        .setMetaData(metaData)
        .setReceiveAddress(receiveAddress)
        .setTTL(1000L)
        .build();

    Result<TransactionResult> result = nftBuilder.submit();

    Assertions.assertTrue(result.isSuccessful(), result.toString());
  }
}
