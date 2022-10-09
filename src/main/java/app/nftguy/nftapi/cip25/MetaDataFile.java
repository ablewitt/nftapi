package app.nftguy.nftapi.cip25;

import org.json.JSONObject;

public class MetaDataFile {
  private String name;
  private String mediaType;
  private String src;
  private JSONObject json = new JSONObject();

  public MetaDataFile(String name, String mediaType, String src) {
    this.name = name;
    this.mediaType = mediaType;
    this.src = src;

    json.put("name", this.name);
    json.put("mediaType", this.mediaType);
    json.put("src", this.src);
  }

  public JSONObject get() {
    return this.json;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  public String toString() {
    return json.toString();
  }
}
