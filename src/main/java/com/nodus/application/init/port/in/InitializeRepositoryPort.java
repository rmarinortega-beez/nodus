package com.nodus.application.init.port.in;

import com.nodus.application.shared.InitializeRepositoryResult;

import java.io.IOException;
import java.nio.file.Path;

public interface InitializeRepositoryPort {
    InitializeRepositoryResult execute(Path currentDirectory) throws IOException;
}
