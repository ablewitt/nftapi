package app.nftguy.nftapi.helper;


import com.bloxbean.cardano.client.transaction.spec.Asset;
import com.bloxbean.cardano.client.transaction.spec.MultiAsset;

import java.math.BigInteger;


public class AssetHelper {

    MultiAsset multiAsset = new MultiAsset();

    public AssetHelper(String assetName, BigInteger amount, String policyId){
        setPolicyId(policyId);
        addAsset(assetName, amount);
    }

    public void addAsset(Asset asset){
        multiAsset.getAssets().add(asset);
    }

    public void addAsset(String assetName, BigInteger amount){
        multiAsset.getAssets().add(new Asset(assetName, amount));
    }

    public void setPolicyId(String policyId){
        multiAsset.setPolicyId(policyId);
    }

    public MultiAsset getMultiAsset(){
        return multiAsset;
    }
}
