package com.nodus.infrastructure.adapters.out.filesystem.repository;

import com.nodus.application.init.port.out.RepositoryDirectoryPort;
import com.nodus.application.shared.PathType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class RepositoryDirectory implements RepositoryDirectoryPort {
    @Override
    public PathType typeOf(Path path) {
        if (!Files.exists(path)) {
            return PathType.NOT_FOUND;
        }

        if (Files.isDirectory(path)) {
            return PathType.DIRECTORY;
        }

        if (Files.isRegularFile(path)) {
            return PathType.REGULAR_FILE;
        }

        return PathType.OTHER;
    }

    @Override
    public void create(Path nodusPath) throws IOException {
        Files.createDirectory(nodusPath);
    }
}
