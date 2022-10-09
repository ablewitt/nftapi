package app.nftguy.nftapi.helper;

import com.bloxbean.cardano.client.crypto.KeyGenUtil;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;

public class KeyHelper {

  private SecretKey sKey;
  private VerificationKey vKey;
  private Keys keys;

  public KeyHelper() throws CborSerializationException {
    // create keys
    this.keys = KeyGenUtil.generateKey();
    this.vKey = keys.getVkey();
    this.sKey = keys.getSkey();
  }

  public SecretKey getSKey() {
    return sKey;
  }

  public VerificationKey getVKey() {
    return vKey;
  }
}
