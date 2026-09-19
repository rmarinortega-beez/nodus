package com.nodus.application.storage;

import com.nodus.application.port.ObjectStorePort;
import com.nodus.application.port.RepositoryMetadataPort;
import com.nodus.application.port.StoredObjectCodecPort;
import com.nodus.application.port.WorkingTreeFileReaderPort;
import com.nodus.application.init.InitializeRepositoryResult;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ObjectStoreUseCase {
    private final RepositoryMetadataPort repositoryMetadata;
    private final StoredObjectCodecPort storedObjectCodec;
    private final ObjectStorePort objectStore;
    private final WorkingTreeFileReaderPort workingTreeFileReader;

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
