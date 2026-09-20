package com.nodus.application.index.port.in;

import com.nodus.domain.object.ObjectId;

import java.io.IOException;
import java.nio.file.Path;

public interface BuildTreeFromIndexPort {
    ObjectId build(Path repositoryPath) throws IOException;
}
