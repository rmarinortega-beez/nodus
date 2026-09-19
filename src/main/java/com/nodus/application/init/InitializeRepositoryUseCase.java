package com.nodus.application.init;

import com.nodus.application.shared.ReadRepositoryUtils;
import com.nodus.application.shared.WriteRepositoryUtils;
import com.nodus.domain.enums.InitializeRepositoryResult;
import com.nodus.domain.enums.PathType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectory;

@Component
public class InitializeRepositoryUseCase {
    public InitializeRepositoryResult execute(Path currentDirectory) throws IOException {
        System.out.println("[nodus:init] Resolving repository in " + currentDirectory);
        Path targetNodusPath = currentDirectory.resolve(".nodus");
        PathType pathType = PathType.getPathType(targetNodusPath);
        System.out.println("[nodus:init] Target path " + targetNodusPath + " is " + pathType);

        return switch (pathType) {
            case NOT_FOUND -> {
                System.out.println("[nodus:init] Creating repository directory");
                createDirectory(targetNodusPath);
                System.out.println("[nodus:init] Writing repository metadata");
                WriteRepositoryUtils.generateRepositoryMetadata(targetNodusPath);
                yield InitializeRepositoryResult.INITIALIZED;
            }
            case DIRECTORY -> {
                System.out.println("[nodus:init] Existing .nodus directory found; validating metadata");
                yield ReadRepositoryUtils.getRepositoryStatus(targetNodusPath);
            }
            case REGULAR_FILE, OTHER -> InitializeRepositoryResult.PATH_CONFLICT;
        };
    }

}
