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

    public static String getMD5(String normal) {
        return Hashing.hmacMd5(normal.getBytes(StandardCharsets.UTF_8)).newHasher().hash().toString();
    }

}
