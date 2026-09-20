package com.nodus.application.storage.usecase;

import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.application.storage.ObjectStorageService;
import com.nodus.application.storage.TreeCodec;
import com.nodus.application.storage.port.in.LoadTreePort;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import com.nodus.domain.object.StoredRecord;
import com.nodus.domain.tree.Tree;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class LoadTreeUseCase implements LoadTreePort {
    private final RepositoryMetadataPort repositoryMetadata;
    private final TreeCodec treeCodec;
    private final ObjectStorageService objectStorageService;

    @Override
    public Tree load(ObjectId objectId, Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.resolve(".nodus");
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        StoredRecord storedRecord = objectStorageService.load(nodusPath, objectId);
        if (storedRecord.type() != ObjectType.TREE) {
            throw new IOException("Object with ID " + objectId.value() + " is not a tree.");
        }

        try {
            return treeCodec.decode(storedRecord.content());
        } catch (IllegalArgumentException e) {
            throw new IOException("Tree object with ID " + objectId.value() + " has an invalid format.", e);
        }
    }
}
