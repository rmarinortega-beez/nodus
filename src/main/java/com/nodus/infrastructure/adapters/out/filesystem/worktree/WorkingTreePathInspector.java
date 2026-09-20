package com.nodus.infrastructure.adapters.out.filesystem.worktree;

import com.nodus.application.shared.PathType;
import com.nodus.application.storage.port.out.WorkingTreePathInspectorPort;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class WorkingTreePathInspector implements WorkingTreePathInspectorPort {
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
}
