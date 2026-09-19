package com.nodus.infrastructure.object;

import com.nodus.domain.object.ObjectId;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class ObjectIdCalculator {
    public ObjectId calculate(byte[] canonicalObject) {
        return new ObjectId(HexFormat.of().formatHex(sha256().digest(canonicalObject)));
    }

    private MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
