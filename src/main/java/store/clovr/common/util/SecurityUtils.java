package store.clovr.common.util;

import org.bouncycastle.crypto.agreement.X25519Agreement;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.generators.X25519KeyPairGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.SecureRandom;
import java.security.Security;

public class SecurityUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final int KEY_SIZE_BITS = 256;
    public static final int PUBLIC_KEY_SIZE = 32;

    public static X25519PrivateKeyParameters generateKeyPair() {
        X25519KeyPairGenerator generator = new X25519KeyPairGenerator();
        generator.init(new X25519KeyGenerationParameters(new SecureRandom()));
        return (X25519PrivateKeyParameters) generator.generateKeyPair().getPrivate();
    }

    public static byte[] getSharedSecret(X25519PrivateKeyParameters privateKey, X25519PublicKeyParameters publicKey) {
        X25519Agreement agreement = new X25519Agreement();
        agreement.init(privateKey);
        byte[] secret = new byte[agreement.getAgreementSize()];
        agreement.calculateAgreement(publicKey, secret, 0);
        return secret;
    }

    public static byte[] deriveKey(byte[] sharedSecret, String context) {
        HKDFParameters params = new HKDFParameters(sharedSecret, null, context.getBytes());
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(SHA256Digest.newInstance());
        hkdf.init(params);
        byte[] derivedKey = new byte[KEY_SIZE_BITS / 8];
        hkdf.generateBytes(derivedKey, 0, derivedKey.length);
        return derivedKey;
    }
}