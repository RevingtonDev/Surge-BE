package dev.revington.security;

import com.google.common.hash.Hashing;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Crypto {

    public static final String AES_GCM_NoPADDING = "AES/GCM/NoPadding";
    public static final int AES_KEY_LENGTH = 32;

    public static String getMD5(String normal) {
        return Hashing.hmacMd5(normal.getBytes(StandardCharsets.UTF_8)).newHasher().hash().toString();
    }

    public static byte[] generateKey() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong().generateSeed(AES_KEY_LENGTH);
    }

    public static byte[] encrypt(byte[] key, byte[] text, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_GCM_NoPADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
        return cipher.doFinal(text);
    }

    public static String decrypt(byte[] key, byte[] text, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_GCM_NoPADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
        return new String(cipher.doFinal(text), StandardCharsets.UTF_8);
    }

}
