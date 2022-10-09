package app.nftguy.nftapi.helper;

import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.transaction.spec.Policy;
import com.bloxbean.cardano.client.transaction.spec.script.ScriptPubkey;

public class PolicyHelper {

  private ScriptPubkey scriptPubkey;
  private VerificationKey vKey;
  private SecretKey sKey;
  private Policy policy;

  public PolicyHelper(VerificationKey vKey, SecretKey sKey) throws CborSerializationException {
    this.vKey = vKey;
    this.sKey = sKey;
    createScriptPubKey();
  }

  public void setVKey(VerificationKey vKey) {
    this.vKey = vKey;
    createScriptPubKey();
  }

  private void createScriptPubKey() {
    this.scriptPubkey = ScriptPubkey.create(this.vKey);
    this.policy = new Policy(this.scriptPubkey);
    this.policy.addKey(this.sKey);
  }

  public VerificationKey getVKey() {
    return this.vKey;
  }

  public String getPolicyId() throws CborSerializationException {
    return scriptPubkey.getPolicyId();
  }

  public Policy getPolicy() {
    return policy;
  }
}
