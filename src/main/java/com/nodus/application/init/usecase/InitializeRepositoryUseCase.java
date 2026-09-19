package com.nodus.application.init.usecase;

import com.nodus.application.init.port.in.InitializeRepositoryPort;
import com.nodus.application.init.port.out.RepositoryDirectoryPort;
import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.application.shared.PathType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class InitializeRepositoryUseCase implements InitializeRepositoryPort {
    private final RepositoryDirectoryPort repositoryDirectory;
    private final RepositoryMetadataPort repositoryMetadata;

    @Override
    public InitializeRepositoryResult execute(Path currentDirectory) throws IOException {
        Path targetNodusPath = currentDirectory.resolve(".nodus");
        PathType pathType = PathType.getPathType(targetNodusPath);

        return switch (pathType) {
            case NOT_FOUND -> {
                repositoryDirectory.create(targetNodusPath);
                repositoryMetadata.write(targetNodusPath);
                yield InitializeRepositoryResult.INITIALIZED;
            }
            case DIRECTORY -> repositoryMetadata.status(targetNodusPath);
            case REGULAR_FILE, OTHER -> InitializeRepositoryResult.PATH_CONFLICT;
        };
    }

}
