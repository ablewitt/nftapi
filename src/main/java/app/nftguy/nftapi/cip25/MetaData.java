package app.nftguy.nftapi.cip25;

import com.bloxbean.cardano.client.metadata.cbor.CBORMetadata;
import com.bloxbean.cardano.client.metadata.helper.JsonNoSchemaToMetadataConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;

public class MetaData {
    private String metaVersion = "1.0";
    private String metaRootId = "721";
    private String policyId = "";
    private String assetName; // max 24 chars
    private JSONObject attributes; // must contain "name": "<asset_name>", "image": "<ipfs_link>"
    private static int assetNameMaxLength = 24;

    JSONObject rootTag = new JSONObject();
    JSONObject policyTag = new JSONObject();
    JSONObject assetNameTag = new JSONObject();


    public MetaData(String assetName, JSONObject attributes, String policyId){
        if(assetName.length() > assetNameMaxLength){
            throw new IllegalArgumentException(
                    String.format("asset_name max length (%s) exceeded: length %s", assetNameMaxLength, assetName.length()));
        }
        if(!attributes.has("name") || !attributes.has("image")){
            throw new IllegalArgumentException(
                    "attributes must contain \"name\" & \"image\" keys");
        }

        this.assetName = assetName;
        this.attributes = attributes;
        this.policyId = policyId;
        intiStructure();
    }

    private void intiStructure(){
        this.assetNameTag.put(this.assetName, this.attributes);
        this.policyTag.put(this.policyId, this.assetNameTag);
        this.policyTag.put("version", this.metaVersion);
        this.rootTag.put(this.metaRootId, this.policyTag);
    }

    /**
     * Add file to "files" : [] of Nft meta data.
     * This method is required to be run at least once before getFiles() can be used.
     */
    public void addFile(MetaDataFile file){
        if(!attributes.has("files")){
            JSONArray files = new JSONArray();
            attributes.put("files", files);
        }
        attributes.getJSONArray("files").put(file.get());
    }

    /**
     * Returns "files" : [] of Nft meta data, format is JSONArray of MetaDataFile JSONObjects.
     * Will only return once a file has been added via addFile method.
     * Otherwise 'org.json.JSONException' exception is thrown.
     * @return files JSONArray of MetaDataFile JSONObjects
     */
    public JSONArray getFiles(){
        return attributes.getJSONArray("files");
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;

        for (String key : this.policyTag.keySet()) {
            if (!key.equals("version")) {
                this.policyTag.remove(key);
                this.policyTag.put(this.policyId, this.assetNameTag);
                break;
            }
        }

    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public JSONObject getAttributes() {
        return attributes;
    }

    public void setAttributes(JSONObject attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString(){
        return rootTag.toString();
    }

    public CBORMetadata getMetaData() throws JsonProcessingException {
        return (CBORMetadata) JsonNoSchemaToMetadataConverter.jsonToCborMetadata(this.toString());
    }
}
