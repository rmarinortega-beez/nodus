package com.nodus.application.storage;

import com.nodus.domain.enums.InitializeRepositoryResult;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import com.nodus.domain.object.StoredRecord;
import com.nodus.infrastructure.object.ObjectIdCalculator;
import com.nodus.infrastructure.object.ObjectRecordCodec;
import com.nodus.infrastructure.object.ObjectStore;
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
    private final ObjectRecordCodec objectRecordCodec;
    private final ObjectIdCalculator objectIdCalculator;
    private final ObjectStore objectStore;
    private final WorkingTreeFileReader workingTreeFileReader;

    public ObjectId store(Path object, Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.resolve(".nodus");
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        StoredRecord record = new StoredRecord(ObjectType.BLOB, workingTreeFileReader.read(object));
        byte[] canonicalObject = objectRecordCodec.encode(record);
        ObjectId objectId = objectIdCalculator.calculate(canonicalObject);
        objectStore.writeIfAbsent(nodusPath, objectId, canonicalObject);

        return objectId;
    }
}
