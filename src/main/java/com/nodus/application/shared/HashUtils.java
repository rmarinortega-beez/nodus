package com.nodus.application.shared;

import com.nodus.domain.enums.ObjectType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HashUtils {

    public static String calculateHash(Path file, ObjectType type) throws IOException, NoSuchAlgorithmException {
        System.out.println("[nodus:hash] Calculating SHA-256 for " + file + " as " + type.serializedName());
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream inputStream = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            digest.update((type.serializedName() + "\0").getBytes(StandardCharsets.UTF_8));

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        String hash = HexFormat.of().formatHex(digest.digest());
        System.out.println("[nodus:hash] Calculated hash " + hash);
        return hash;
    }

}
