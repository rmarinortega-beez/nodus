package com.nodus.infrastructure.adapters.out.filesystem.object;

import com.nodus.application.storage.port.out.ObjectStorePort;
import com.nodus.domain.object.ObjectId;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ObjectStore implements ObjectStorePort {
    private static final String OBJECTS_DIRECTORY = "objects";

    @Override
    public void writeIfAbsent(Path nodusPath, ObjectId objectId, byte[] canonicalObject) throws IOException {
        Path objectPath = objectPath(nodusPath, objectId);
        Files.createDirectories(objectPath.getParent());

        if (!Files.exists(objectPath)) {
            Files.write(objectPath, canonicalObject);
        }
    }

    @Override
    public byte[] read(Path nodusPath, ObjectId objectId) throws IOException {
        return Files.readAllBytes(objectPath(nodusPath, objectId));
    }

    private Path objectPath(Path nodusPath, ObjectId objectId) {
        return nodusPath.resolve(OBJECTS_DIRECTORY).resolve(objectId.value());
    }
}
