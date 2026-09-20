package com.nodus.application.storage.usecase;

import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.application.storage.ObjectStorageService;
import com.nodus.application.storage.TreeCodec;
import com.nodus.application.storage.port.in.StoreTreePort;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import com.nodus.domain.tree.Tree;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class StoreTreeUseCase implements StoreTreePort {
    private final RepositoryMetadataPort repositoryMetadata;
    private final TreeCodec treeCodec;
    private final ObjectStorageService objectStorageService;

    @Override
    public ObjectId store(Tree tree, Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.resolve(".nodus");
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        byte[] treeContent = treeCodec.encode(tree);
        return objectStorageService.store(nodusPath, ObjectType.TREE, treeContent);
    }
}
