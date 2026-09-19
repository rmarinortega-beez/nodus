package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.domain.enums.InitializeRepositoryResult;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.StoredRecord;
import com.nodus.infrastructure.object.ObjectStore;
import com.nodus.infrastructure.object.StoredObjectCodec;
import com.nodus.infrastructure.repository.RepositoryMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ObjectLoadUseCase {
    private final RepositoryMetadata repositoryMetadata;
    private final StoredObjectCodec storedObjectCodec;
    private final ObjectStore objectStore;

    public StoredRecord load(ObjectId objectId, Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.resolve(".nodus");
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        byte[] canonicalObject = objectStore.read(nodusPath, objectId);

        if (!HashUtils.calculateHash(canonicalObject).equals(objectId)) {
            throw new IOException("Object with ID " + objectId.value() + " is corrupted or does not exist.");
        }

        try {
            return storedObjectCodec.decode(canonicalObject);
        } catch (IllegalArgumentException e) {
            throw new IOException("Object with ID " + objectId.value() + " has an invalid stored format.", e);
        }
    }
}
