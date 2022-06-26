
package app.nftguy.nftapi.nftstorage.Responses;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "value"
})
@Generated("jsonschema2pojo")
public class UploadResponse {

    @JsonProperty("ok")
    private Boolean ok;
    @JsonProperty("value")
    private Value value;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UploadResponse() {
    }

    /**
     * 
     * @param ok
     * @param value
     */
    public UploadResponse(Boolean ok, Value value) {
        super();
        this.ok = ok;
        this.value = value;
    }

    @JsonProperty("ok")
    public Boolean getOk() {
        return ok;
    }

    @JsonProperty("ok")
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    @JsonProperty("value")
    public Value getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Value value) {
        this.value = value;
    }

}
