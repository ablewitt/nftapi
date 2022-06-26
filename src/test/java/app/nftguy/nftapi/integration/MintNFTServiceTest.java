package app.nftguy.nftapi.integration;

import app.nftguy.nftapi.helper.*;
import app.nftguy.nftapi.NftBuilder;
import app.nftguy.nftapi.cip25.MetaData;
import com.bloxbean.cardano.client.api.helper.model.TransactionResult;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

@SpringBootTest
public class MintNFTServiceTest {

    String receiveAddress = "addr_test1qrf87vnetzcmf4vdzhpyatzr90c036xpzkhsdjp4y5as4c72wk6fknuew0vvy6s7jklr2c92t7uf3plyuqvcw390wywql37usz";
    String ipfsLink = "ipfs://Qme7ss3ARVgxv6rXqVPiikMJ8u2NLgmgszg13pYrDKEoiu";
    String nftNmae = "My-Cool_NFT";
    String tokenName = "TestAsset-Meta";

    @Autowired
    NftBuilder NFTBuilder;

    @Test
    public void create() throws CborSerializationException, JsonProcessingException {

        KeyHelper keyHelper = new KeyHelper();

        PolicyHelper policyHelper = new PolicyHelper(keyHelper.getVKey(), keyHelper.getSKey());

        AssetHelper assetHelper = new AssetHelper(
                "TestAsset",
                BigInteger.valueOf(1L),
                policyHelper.getPolicyId());

        JSONObject attributes = new JSONObject();
        attributes.put("name", nftNmae);
        attributes.put("image", ipfsLink);
        MetaData metaData = new MetaData(tokenName, attributes, policyHelper.getPolicyId());

        NFTBuilder.init(
                assetHelper.getMultiAsset(),
                policyHelper.getPolicy(),
                metaData.getMetaData(),
                receiveAddress,
                10L);
    }

    @Test
    public void createSubmit() throws CborSerializationException, JsonProcessingException {

        KeyHelper keyHelper = new KeyHelper();

        PolicyHelper policyHelper = new PolicyHelper(keyHelper.getVKey(), keyHelper.getSKey());

        AssetHelper assetHelper = new AssetHelper(
                "TestAsset",
                BigInteger.valueOf(1),
                policyHelper.getPolicyId());

        JSONObject attributes = new JSONObject();
        attributes.put("name", nftNmae);
        attributes.put("image", ipfsLink);
        MetaData metaData = new MetaData(tokenName, attributes, policyHelper.getPolicyId());

        NFTBuilder.init(
                assetHelper.getMultiAsset(),
                policyHelper.getPolicy(),
                metaData.getMetaData(),
                receiveAddress,
                1000);

        Result<TransactionResult> result =  NFTBuilder.submit();
        Assertions.assertTrue(result.isSuccessful());



    }
}
