package com.nodus.application.storage.port.in;

import com.nodus.domain.object.ObjectId;

import java.io.IOException;
import java.nio.file.Path;

public interface StoreObjectPort {
    ObjectId store(Path object, Path repositoryPath) throws IOException;
}
