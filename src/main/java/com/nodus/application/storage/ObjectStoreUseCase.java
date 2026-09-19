package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.application.shared.RepositoryUtils;
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
        Path nodusPath = repositoryPath.resolve(".nodus");
        if (RepositoryUtils.getRepositoryStatus(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        ObjectId objectId = new ObjectId(HashUtils.calculateHash(object, ObjectType.BLOB_00));

        Path objectsPath = nodusPath.resolve("objects");
        RepositoryUtils.ensureDirectory(objectsPath);

        Path persistedObjectPath = objectsPath.resolve(objectId.value());
        if (!Files.exists(persistedObjectPath)) {
            Files.copy(object, persistedObjectPath);
        }

        return objectId;
    }
}
