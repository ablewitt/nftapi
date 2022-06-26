
package app.nftguy.nftapi.nftstorage.Responses;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "batchRootCid",
    "lastChange",
    "miner",
    "network",
    "pieceCid",
    "status",
    "statusText",
    "chainDealID",
    "dealActivation",
    "dealExpiration"
})
@Generated("jsonschema2pojo")
public class Deal {

    @JsonProperty("batchRootCid")
    private String batchRootCid;
    @JsonProperty("lastChange")
    private String lastChange;
    @JsonProperty("miner")
    private String miner;
    @JsonProperty("network")
    private String network;
    @JsonProperty("pieceCid")
    private String pieceCid;
    @JsonProperty("status")
    private String status;
    @JsonProperty("statusText")
    private String statusText;
    @JsonProperty("chainDealID")
    private Integer chainDealID;
    @JsonProperty("dealActivation")
    private String dealActivation;
    @JsonProperty("dealExpiration")
    private String dealExpiration;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Deal() {
    }

    /**
     * 
     * @param statusText
     * @param lastChange
     * @param dealActivation
     * @param chainDealID
     * @param pieceCid
     * @param dealExpiration
     * @param batchRootCid
     * @param miner
     * @param network
     * @param status
     */
    public Deal(String batchRootCid, String lastChange, String miner, String network, String pieceCid, String status, String statusText, Integer chainDealID, String dealActivation, String dealExpiration) {
        super();
        this.batchRootCid = batchRootCid;
        this.lastChange = lastChange;
        this.miner = miner;
        this.network = network;
        this.pieceCid = pieceCid;
        this.status = status;
        this.statusText = statusText;
        this.chainDealID = chainDealID;
        this.dealActivation = dealActivation;
        this.dealExpiration = dealExpiration;
    }

    @JsonProperty("batchRootCid")
    public String getBatchRootCid() {
        return batchRootCid;
    }

    @JsonProperty("batchRootCid")
    public void setBatchRootCid(String batchRootCid) {
        this.batchRootCid = batchRootCid;
    }

    @JsonProperty("lastChange")
    public String getLastChange() {
        return lastChange;
    }

    @JsonProperty("lastChange")
    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }

    @JsonProperty("miner")
    public String getMiner() {
        return miner;
    }

    @JsonProperty("miner")
    public void setMiner(String miner) {
        this.miner = miner;
    }

    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    @JsonProperty("network")
    public void setNetwork(String network) {
        this.network = network;
    }

    @JsonProperty("pieceCid")
    public String getPieceCid() {
        return pieceCid;
    }

    @JsonProperty("pieceCid")
    public void setPieceCid(String pieceCid) {
        this.pieceCid = pieceCid;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("statusText")
    public String getStatusText() {
        return statusText;
    }

    @JsonProperty("statusText")
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @JsonProperty("chainDealID")
    public Integer getChainDealID() {
        return chainDealID;
    }

    @JsonProperty("chainDealID")
    public void setChainDealID(Integer chainDealID) {
        this.chainDealID = chainDealID;
    }

    @JsonProperty("dealActivation")
    public String getDealActivation() {
        return dealActivation;
    }

    @JsonProperty("dealActivation")
    public void setDealActivation(String dealActivation) {
        this.dealActivation = dealActivation;
    }

    @JsonProperty("dealExpiration")
    public String getDealExpiration() {
        return dealExpiration;
    }

    @JsonProperty("dealExpiration")
    public void setDealExpiration(String dealExpiration) {
        this.dealExpiration = dealExpiration;
    }

}
