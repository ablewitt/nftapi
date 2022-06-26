package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.PolicyHelper;
import com.bloxbean.cardano.client.crypto.KeyGenUtil;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.transaction.spec.Policy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class PolicyHelperTest {

    @Test
    void create() throws CborSerializationException {
        Keys keys = KeyGenUtil.generateKey();
        VerificationKey vKey = keys.getVkey();
        SecretKey sKey = keys.getSkey();
        PolicyHelper ph = new PolicyHelper(vKey, sKey);
    }

    @Test
    void getPolicyIdTest() throws CborSerializationException {
        Keys keys = KeyGenUtil.generateKey();
        VerificationKey vKey = keys.getVkey();
        SecretKey sKey = keys.getSkey();
        PolicyHelper ph = new PolicyHelper(vKey, sKey);
        final String regex = "[0-9a-f]{56}";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Assertions.assertTrue(pattern.matcher(ph.getPolicyId()).matches());
    }

    @Test
    void getPolicyTest() throws CborSerializationException {
        Keys keys = KeyGenUtil.generateKey();
        VerificationKey vKey = keys.getVkey();
        SecretKey sKey = keys.getSkey();
        PolicyHelper ph = new PolicyHelper(vKey, sKey);
        Assertions.assertEquals(ph.getPolicy().getClass(), Policy.class);
    }
}
