
package app.nftguy.nftapi.nftstorage.Responses;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cid",
    "size",
    "created",
    "type",
    "scope",
    "pin",
    "files",
    "deals"
})
@Generated("jsonschema2pojo")
public class Value {

    @JsonProperty("cid")
    private String cid;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("created")
    private String created;
    @JsonProperty("type")
    private String type;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("pin")
    private Pin pin;
    @JsonProperty("files")
    private List<File> files = null;
    @JsonProperty("deals")
    private List<Deal> deals = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Value() {
    }

    /**
     * 
     * @param size
     * @param pin
     * @param created
     * @param scope
     * @param deals
     * @param files
     * @param type
     * @param cid
     */
    public Value(String cid, Integer size, String created, String type, String scope, Pin pin, List<File> files, List<Deal> deals) {
        super();
        this.cid = cid;
        this.size = size;
        this.created = created;
        this.type = type;
        this.scope = scope;
        this.pin = pin;
        this.files = files;
        this.deals = deals;
    }

    @JsonProperty("cid")
    public String getCid() {
        return cid;
    }

    @JsonProperty("cid")
    public void setCid(String cid) {
        this.cid = cid;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    @JsonProperty("scope")
    public void setScope(String scope) {
        this.scope = scope;
    }

    @JsonProperty("pin")
    public Pin getPin() {
        return pin;
    }

    @JsonProperty("pin")
    public void setPin(Pin pin) {
        this.pin = pin;
    }

    @JsonProperty("files")
    public List<File> getFiles() {
        return files;
    }

    @JsonProperty("files")
    public void setFiles(List<File> files) {
        this.files = files;
    }

    @JsonProperty("deals")
    public List<Deal> getDeals() {
        return deals;
    }

    @JsonProperty("deals")
    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

}
