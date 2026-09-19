package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.domain.enums.InitializeRepositoryResult;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import com.nodus.infrastructure.object.ObjectStore;
import com.nodus.infrastructure.object.StoredObjectCodec;
import com.nodus.infrastructure.repository.RepositoryMetadata;
import com.nodus.infrastructure.worktree.WorkingTreeFileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ObjectStoreUseCase {
    private final RepositoryMetadata repositoryMetadata;
    private final StoredObjectCodec storedObjectCodec;
    private final ObjectStore objectStore;
    private final WorkingTreeFileReader workingTreeFileReader;

    public ObjectId store(Path object, Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.resolve(".nodus");
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        byte[] content = workingTreeFileReader.read(object);
        byte[] canonicalObject = storedObjectCodec.encode(ObjectType.BLOB, content);
        ObjectId objectId = HashUtils.calculateHash(canonicalObject);
        objectStore.writeIfAbsent(nodusPath, objectId, canonicalObject);

        return objectId;
    }
}
