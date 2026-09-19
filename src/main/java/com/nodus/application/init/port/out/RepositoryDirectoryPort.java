package com.nodus.application.init.port.out;

import java.io.IOException;
import java.nio.file.Path;

public interface RepositoryDirectoryPort {
    void create(Path nodusPath) throws IOException;
}
