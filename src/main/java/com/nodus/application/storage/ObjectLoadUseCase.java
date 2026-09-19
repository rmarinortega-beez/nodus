package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.application.shared.ReadRepositoryUtils;
import com.nodus.domain.enums.InitializeRepositoryResult;
import com.nodus.domain.enums.ObjectType;
import com.nodus.domain.object.ObjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
public class ObjectLoadUseCase {
    public byte[] load(ObjectId objectId, Path repositoryPath) throws IOException, NoSuchAlgorithmException {
        System.out.println("[nodus:load] Loading object " + objectId.value());
        System.out.println("[nodus:load] Using working directory " + repositoryPath);

        Path nodusPath = repositoryPath.resolve(".nodus");
        if (ReadRepositoryUtils.getRepositoryStatus(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }
        System.out.println("[nodus:load] Repository metadata is valid");

        Path objectPath = nodusPath.resolve("objects").resolve(objectId.value());
        System.out.println("[nodus:load] Resolved object path " + objectPath);

        if (!isValid(objectPath, ObjectType.BLOB)) {
            throw new IOException("Object with ID " + objectId.value() + " is corrupted or does not exist.");
        }
        System.out.println("[nodus:load] Object hash is valid");

        return Files.readAllBytes(objectPath);
    }

    public boolean isValid(Path objectPath, ObjectType type) throws IOException, NoSuchAlgorithmException {
        System.out.println("[nodus:load] Validating object hash as " + type.serializedName());
        String actualHash = HashUtils.calculateHash(objectPath, type);
        String expectedHash = objectPath.getFileName().toString();
        boolean valid = expectedHash.equals(actualHash);
        System.out.println("[nodus:load] Expected " + expectedHash + ", actual " + actualHash);
        return valid;
    }
}
