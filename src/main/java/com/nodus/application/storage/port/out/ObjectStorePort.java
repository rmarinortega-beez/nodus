package com.nodus.application.storage.port.out;

import com.nodus.domain.object.ObjectId;

import java.io.IOException;
import java.nio.file.Path;

public interface ObjectStorePort {
    void writeIfAbsent(Path nodusPath, ObjectId objectId, byte[] canonicalObject) throws IOException;

    byte[] read(Path nodusPath, ObjectId objectId) throws IOException;
}
