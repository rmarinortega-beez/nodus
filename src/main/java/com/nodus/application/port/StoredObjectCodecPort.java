package com.nodus.application.port;

import com.nodus.domain.object.ObjectType;
import com.nodus.domain.object.StoredRecord;

public interface StoredObjectCodecPort {
    byte[] encode(ObjectType type, byte[] content);

    StoredRecord decode(byte[] storedObject);
}
