package dev.revington.security;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Crypto {

    public static String getMD5(String normal) {
        return Hashing.hmacMd5(normal.getBytes(StandardCharsets.UTF_8)).newHasher().hash().toString();
    }

}
