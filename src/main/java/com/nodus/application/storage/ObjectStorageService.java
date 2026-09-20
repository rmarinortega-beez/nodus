package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.application.storage.port.out.ObjectStorePort;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import com.nodus.domain.object.StoredRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ObjectStorageService {
    private final StoredObjectCodec storedObjectCodec;
    private final ObjectStorePort objectStore;

    public ObjectId store(Path nodusPath, ObjectType type, byte[] content) throws IOException {
        byte[] canonicalObject = storedObjectCodec.encode(type, content);
        ObjectId objectId = HashUtils.calculateHash(canonicalObject);
        objectStore.writeIfAbsent(nodusPath, objectId, canonicalObject);
        return objectId;
    }

    public StoredRecord load(Path nodusPath, ObjectId objectId) throws IOException {
        byte[] canonicalObject = objectStore.read(nodusPath, objectId);

        if (!HashUtils.calculateHash(canonicalObject).equals(objectId)) {
            throw new IOException("Object with ID " + objectId.value() + " is corrupted or does not exist.");
        }

        try {
            return storedObjectCodec.decode(canonicalObject);
        } catch (IllegalArgumentException e) {
            throw new IOException("Object with ID " + objectId.value() + " has an invalid stored format.", e);
        }
    }
}
