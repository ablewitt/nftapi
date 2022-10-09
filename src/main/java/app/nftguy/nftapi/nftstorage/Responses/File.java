package app.nftguy.nftapi.nftstorage.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "type"})
@Generated("jsonschema2pojo")
public class File {

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  /** No args constructor for use in serialization */
  public File() {}

  /**
   * @param name
   * @param type
   */
  public File(String name, String type) {
    super();
    this.name = name;
    this.type = type;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }
}
