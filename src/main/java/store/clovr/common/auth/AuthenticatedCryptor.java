package store.clovr.common.auth;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.concurrent.atomic.AtomicLong;

public class AuthenticatedCryptor {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final int GCM_NONCE_LENGTH_BYTES = 12;

    private final SecretKeySpec keySpec;
    private final AtomicLong sequenceNumber = new AtomicLong(0);
    private long lastPeerSequence = -1;

    public AuthenticatedCryptor(byte[] key) {
        this.keySpec = new SecretKeySpec(key, "AES");
    }

    public synchronized byte[] encrypt(byte[] plaintext) throws GeneralSecurityException {
        byte[] nonce = new byte[GCM_NONCE_LENGTH_BYTES];
        long currentSeq = sequenceNumber.getAndIncrement();
        ByteBuffer.wrap(nonce).putLong(4, currentSeq);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

        byte[] ciphertext = cipher.doFinal(plaintext);

        ByteBuffer message = ByteBuffer.allocate(nonce.length + ciphertext.length);
        message.put(nonce);
        message.put(ciphertext);
        return message.array();
    }

    public synchronized byte[] decrypt(byte[] encryptedMessage) throws GeneralSecurityException {
        ByteBuffer message = ByteBuffer.wrap(encryptedMessage);

        byte[] nonce = new byte[GCM_NONCE_LENGTH_BYTES];
        message.get(nonce);

        long receivedSeq = ByteBuffer.wrap(nonce).getLong(4);
        if (receivedSeq <= lastPeerSequence) {
            throw new GeneralSecurityException("Anti-replay check failed! Received old sequence number.");
        }
        lastPeerSequence = receivedSeq;

        byte[] ciphertext = new byte[message.remaining()];
        message.get(ciphertext);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        return cipher.doFinal(ciphertext);
    }
}