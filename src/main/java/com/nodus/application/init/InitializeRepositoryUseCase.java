package com.nodus.application.init;

import com.nodus.application.port.RepositoryMetadataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectory;

@Component
@RequiredArgsConstructor
public class InitializeRepositoryUseCase {
    private final RepositoryMetadataPort repositoryMetadata;

    public InitializeRepositoryResult execute(Path currentDirectory) throws IOException {
        Path targetNodusPath = currentDirectory.resolve(".nodus");
        PathType pathType = PathType.getPathType(targetNodusPath);

        return switch (pathType) {
            case NOT_FOUND -> {
                createDirectory(targetNodusPath);
                repositoryMetadata.write(targetNodusPath);
                yield InitializeRepositoryResult.INITIALIZED;
            }
            case DIRECTORY -> repositoryMetadata.status(targetNodusPath);
            case REGULAR_FILE, OTHER -> InitializeRepositoryResult.PATH_CONFLICT;
        };
    }

}
