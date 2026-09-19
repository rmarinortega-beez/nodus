package com.nodus.application.port;

import com.nodus.application.init.InitializeRepositoryResult;

import java.io.IOException;
import java.nio.file.Path;

public interface RepositoryMetadataPort {
    InitializeRepositoryResult status(Path nodusPath) throws IOException;

    void write(Path nodusPath) throws IOException;
}
