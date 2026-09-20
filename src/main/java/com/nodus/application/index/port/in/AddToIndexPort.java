package com.nodus.application.index.port.in;

import com.nodus.domain.object.ObjectId;

import java.io.IOException;
import java.nio.file.Path;

public interface AddToIndexPort {
    ObjectId add(Path file, Path repositoryPath) throws IOException;
}
