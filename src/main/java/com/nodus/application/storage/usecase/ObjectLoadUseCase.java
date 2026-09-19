package com.nodus.application.storage.usecase;

import com.nodus.application.shared.HashUtils;
import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.storage.port.in.LoadObjectPort;
import com.nodus.application.storage.port.out.ObjectStorePort;
import com.nodus.application.storage.port.out.StoredObjectCodecPort;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.StoredRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ObjectLoadUseCase implements LoadObjectPort {
    private final RepositoryMetadataPort repositoryMetadata;
    private final StoredObjectCodecPort storedObjectCodec;
    private final ObjectStorePort objectStore;

    @Override
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
