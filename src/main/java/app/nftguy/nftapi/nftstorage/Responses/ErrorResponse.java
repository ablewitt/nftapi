
package app.nftguy.nftapi.nftstorage.Responses;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "error"
})
@Generated("jsonschema2pojo")
public class ErrorResponse {

    @JsonProperty("ok")
    private Boolean ok;
    @JsonProperty("error")
    private Error error;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ErrorResponse() {
    }

    /**
     * 
     * @param ok
     * @param error
     */
    public ErrorResponse(Boolean ok, Error error) {
        super();
        this.ok = ok;
        this.error = error;
    }

    @JsonProperty("ok")
    public Boolean getOk() {
        return ok;
    }

    @JsonProperty("ok")
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    @JsonProperty("error")
    public Error getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Error error) {
        this.error = error;
    }

}
