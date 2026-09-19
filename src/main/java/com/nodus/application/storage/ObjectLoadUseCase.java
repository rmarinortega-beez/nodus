package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.domain.enums.ObjectType;
import com.nodus.domain.object.ObjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class ObjectLoadUseCase {
    public byte[] load(ObjectId objectId) throws IOException, NoSuchAlgorithmException {
        Path repositoryPath = Path.of("").toAbsolutePath().normalize().resolve(".nodus");
        Path objectPath = repositoryPath.resolve("objects").resolve(objectId.value());

        if(!isValid(objectPath, ObjectType.BLOB_00)) {
            throw new IOException("Object with ID " + objectId.value() + " is corrupted or does not exist.");
        }

        return Files.readAllBytes(objectPath);
    }

    public boolean isValid(Path objectPath, ObjectType type) throws IOException, NoSuchAlgorithmException {
        String actualHash = HashUtils.calculateHash(objectPath, type);
        String expectedHash = objectPath.getFileName().toString();
        return expectedHash.equals(actualHash);
    }
}
