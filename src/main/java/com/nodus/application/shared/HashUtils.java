package com.nodus.application.shared;

import com.nodus.domain.object.ObjectId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class HashUtils {
    private HashUtils() {
    }

    public static ObjectId calculateHash(byte[] bytes) {
        return new ObjectId(HexFormat.of().formatHex(sha256().digest(bytes)));
    }

    private static MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
