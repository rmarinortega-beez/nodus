package com.nodus.application.init;

import com.nodus.application.shared.RepositoryUtils;
import com.nodus.domain.enums.InitializeRepositoryResult;
import com.nodus.domain.enums.PathType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectory;

@Component
public class InitializeRepositoryUseCase {
    public InitializeRepositoryResult execute(Path currentDirectory) throws IOException {
        Path targetNodusPath = currentDirectory.resolve(".nodus");
        PathType pathType = PathType.getPathType(targetNodusPath);
        return switch (pathType) {
            case NOT_FOUND -> {
                createDirectory(targetNodusPath);
                RepositoryUtils.generateRepositoryMetadata(targetNodusPath);
                yield InitializeRepositoryResult.INITIALIZED;
            }
            case DIRECTORY -> RepositoryUtils.getRepositoryStatus(targetNodusPath);
            case REGULAR_FILE, OTHER -> InitializeRepositoryResult.PATH_CONFLICT;
        };
    }

}
