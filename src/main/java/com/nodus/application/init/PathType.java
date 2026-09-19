package com.nodus.application.init;

import java.nio.file.Files;
import java.nio.file.Path;

public enum PathType {
    NOT_FOUND,
    DIRECTORY,
    REGULAR_FILE,
    OTHER;

    public static PathType getPathType(Path path) {
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
