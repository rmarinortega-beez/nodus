package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.application.shared.ReadRepositoryUtils;
import com.nodus.application.shared.WriteRepositoryUtils;
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
public class ObjectStoreUseCase {
    public ObjectId store(Path object, Path repositoryPath) throws IOException, NoSuchAlgorithmException {
        System.out.println("[nodus:store] Storing object from " + object);
        System.out.println("[nodus:store] Using working directory " + repositoryPath);

        Path nodusPath = repositoryPath.resolve(".nodus");
        if (ReadRepositoryUtils.getRepositoryStatus(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }
        System.out.println("[nodus:store] Repository metadata is valid");

        ObjectId objectId = new ObjectId(HashUtils.calculateHash(object, ObjectType.BLOB));
        System.out.println("[nodus:store] Calculated object id " + objectId.value());

        Path objectsPath = nodusPath.resolve("objects");
        WriteRepositoryUtils.ensureDirectory(objectsPath);

        Path persistedObjectPath = objectsPath.resolve(objectId.value());
        if (!Files.exists(persistedObjectPath)) {
            System.out.println("[nodus:store] Persisting object at " + persistedObjectPath);
            Files.copy(object, persistedObjectPath);
        } else {
            System.out.println("[nodus:store] Object already exists at " + persistedObjectPath);
        }

        return objectId;
    }
}
