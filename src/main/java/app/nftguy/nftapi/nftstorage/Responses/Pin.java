
package app.nftguy.nftapi.nftstorage.Responses;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cid",
    "name",
    "meta",
    "status",
    "created",
    "size"
})
@Generated("jsonschema2pojo")
public class Pin {

    @JsonProperty("cid")
    private String cid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("status")
    private String status;
    @JsonProperty("created")
    private String created;
    @JsonProperty("size")
    private Integer size;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Pin() {
    }

    /**
     * 
     * @param size
     * @param meta
     * @param created
     * @param name
     * @param cid
     * @param status
     */
    public Pin(String cid, String name, Meta meta, String status, String created, Integer size) {
        super();
        this.cid = cid;
        this.name = name;
        this.meta = meta;
        this.status = status;
        this.created = created;
        this.size = size;
    }

    @JsonProperty("cid")
    public String getCid() {
        return cid;
    }

    @JsonProperty("cid")
    public void setCid(String cid) {
        this.cid = cid;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("meta")
    public Meta getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

}
