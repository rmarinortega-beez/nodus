package com.nodus.application.storage.port.in;

import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.StoredRecord;

import java.io.IOException;
import java.nio.file.Path;

public interface LoadObjectPort {
    StoredRecord load(ObjectId objectId, Path repositoryPath) throws IOException;
}
