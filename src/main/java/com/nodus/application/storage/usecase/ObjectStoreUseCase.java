package com.nodus.application.storage.usecase;

import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.storage.ObjectStorageService;
import com.nodus.application.storage.port.in.StoreObjectPort;
import com.nodus.application.storage.port.out.WorkingTreeFileReaderPort;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ObjectStoreUseCase implements StoreObjectPort {
    private final RepositoryMetadataPort repositoryMetadata;
    private final ObjectStorageService objectStorageService;
    private final WorkingTreeFileReaderPort workingTreeFileReader;

    @Override
    public ObjectId store(Path object, Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.resolve(".nodus");
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        byte[] content = workingTreeFileReader.read(object);
        return objectStorageService.store(nodusPath, ObjectType.BLOB, content);
    }
}
