package com.nodus.application.init.port.out;

import com.nodus.application.shared.InitializeRepositoryResult;

import java.io.IOException;
import java.nio.file.Path;

public interface RepositoryMetadataPort {
    InitializeRepositoryResult status(Path nodusPath) throws IOException;

    void write(Path nodusPath) throws IOException;
}
