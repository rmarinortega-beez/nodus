package com.nodus.application.index.port.in;

import com.nodus.domain.index.Index;

import java.io.IOException;
import java.nio.file.Path;

public interface ShowIndexPort {
    Index show(Path repositoryPath) throws IOException;
}
