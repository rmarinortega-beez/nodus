package com.nodus.application.init.port.out;

import com.nodus.application.shared.PathType;

import java.io.IOException;
import java.nio.file.Path;

public interface RepositoryDirectoryPort {
    PathType typeOf(Path path);

    void create(Path nodusPath) throws IOException;
}
