package com.junho.systemdesign.urlshortener.service;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashGenerator {

    private static final String HASH_ALGORITHM = "SHA-256";

    public String generateHash(String longUrl, int hashLength) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] digest = instance.digest(longUrl.getBytes());
        validateHashLength(hashLength, digest.length);
        return toHexString(digest).substring(0, hashLength); // 7자리로 자르기
    }

    private void validateHashLength(int hashLength, int digestLength) {
        if (hashLength < 1) {
            throw new IllegalArgumentException("해시 길이는 1보다 커야 합니다.");
        }
        if (digestLength <= hashLength) {
            throw new IllegalArgumentException("해당 길이의 해시값을 생성할 수 없습니다.");
        }
    }

    private String toHexString(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
