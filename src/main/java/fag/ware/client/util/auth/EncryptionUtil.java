package fag.ware.client.util.auth;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Manages everything related to encryption, currently includes RSA.
 * TODO: AES to backend->client encryption
 * @author marie
 */
public final class EncryptionUtil
{
    /**
     * Fully initialized RSA PublicKey instance (backend has the private key)
     */
    private static final PublicKey RSA_PUBLIC_KEY;

    static
    {
        PublicKey tempKey;

        try
        {
            // hardcoded allah
            tempKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode("""
                                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7TJjM4m6Kv0dne2ch+izs/Z/3f1UF6R9
                                yVGPilo3pn5VvnSk9qRenhB9twIEP/fXjOqcYVvNZZqsicxzEqbqEVLmZ2lbAUDkICMTWiIeCTAP
                                w61d7TKi/jE4SctGorXYbjEvC0E75b3YSHOPZw+X+zlsw3ocYEIh1qXJSSYP8MlbqlMY8604Fdw7
                                wr83FsUobIaAlEdB7VTW+7Muu7dYKsKiFJl5FraKaSKdj3ZDVNXTw9l48cv39kKzETDWHzHT9gdt
                                nPOQg7G948HBpto31rxVYPHxT6HTL5BpvyJ+l+cFo6WGv/nykuy9xogjp6Fa+mN/USNrUiCQJMdg
                                wLR0tQIDAQAB
                                """.replaceAll("\\s", ""))));
        }
        catch (final Throwable t)
        {
            for (;;) {} // nah, fuck this
        }

        RSA_PUBLIC_KEY = tempKey;
    }

    /**
     * Encrypts data using the public RSA key
     * @param bytes Data to encrypt
     * @return Encrypted data
     */
    public static byte[] rsaEncrypt(final byte[] bytes)
    {
        try
        {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, RSA_PUBLIC_KEY);
            return cipher.doFinal(bytes);
        }
        catch (final Throwable t)
        {
            for (;;) {}
        }
    }
}
