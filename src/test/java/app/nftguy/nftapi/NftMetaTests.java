package app.nftguy.nftapi;

import app.nftguy.nftapi.cip25.MetaData;
import app.nftguy.nftapi.cip25.MetaDataFile;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NftMetaTests {

    String ipfsLink = "ipfs://Qme7ss3ARVgxv6rXqVPiikMJ8u2NLgmgszg13pYrDKEoiu";
    String nftName = "myNftName";
    String assetName = "myNft";
    String fileName = "image.jpg";
    String fileType = "image/jpeg";
    String policyId = "416a21343a2f4363d91e0e325b5f237399ae109f49c225589ea881b3";


    @Test
    void create(){
        JSONObject attributes = new JSONObject();
        attributes.put("image", ipfsLink);
        attributes.put("name", nftName);
        MetaData meta = new MetaData(assetName, attributes, policyId);
        String metaToString = String.format(
                "{\"721\":{\"\":{\"%s\":{\"image\":\"%s\",\"name\":\"%s\"}},\"version\":\"1.0\"}}",
                assetName, ipfsLink, nftName
        );
        assertEquals(meta.toString(), metaToString);
    }

    @Test
    void noNameFail(){
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            JSONObject attributes = new JSONObject();
            attributes.put("image", ipfsLink);
            MetaData meta = new MetaData(assetName, attributes, policyId);
        });
    }

    @Test
    void noImageLinkFail(){
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            JSONObject attributes = new JSONObject();
            attributes.put("name", nftName);
            MetaData meta = new MetaData(assetName, attributes, policyId);
        });
    }

    @Test
    void addGetFile(){
        JSONObject attributes = new JSONObject();
        attributes.put("name", nftName);
        attributes.put("image", ipfsLink);
        MetaData meta = new MetaData(assetName, attributes, policyId);
        MetaDataFile file = new MetaDataFile(fileName, fileType, ipfsLink);
        meta.addFile(file);
        assertEquals(1, meta.getFiles().length());
    }

    @Test
    void setPolicyId(){
        JSONObject attributes = new JSONObject();
        attributes.put("name", nftName);
        attributes.put("image", ipfsLink);
        MetaData meta = new MetaData(assetName, attributes, policyId);
        meta.setPolicyId(policyId);
        assertTrue(meta.toString().contains(new StringBuilder(policyId)));
    }
}
