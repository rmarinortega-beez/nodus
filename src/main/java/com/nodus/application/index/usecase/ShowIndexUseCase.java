package com.nodus.application.index.usecase;

import com.nodus.application.index.port.in.ShowIndexPort;
import com.nodus.application.index.port.out.IndexStorePort;
import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.domain.index.Index;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ShowIndexUseCase implements ShowIndexPort {
    private static final String NODUS_DIRECTORY = ".nodus";

    private final RepositoryMetadataPort repositoryMetadata;
    private final IndexStorePort indexStore;

    @Override
    public Index show(Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.toAbsolutePath().normalize().resolve(NODUS_DIRECTORY);
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        return indexStore.load(nodusPath);
    }
}
