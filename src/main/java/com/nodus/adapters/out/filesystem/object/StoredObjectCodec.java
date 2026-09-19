package com.nodus.adapters.out.filesystem.object;

import com.nodus.application.port.StoredObjectCodecPort;
import com.nodus.domain.object.ObjectType;
import com.nodus.domain.object.StoredRecord;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class StoredObjectCodec implements StoredObjectCodecPort {
    private static final byte TYPE_SEPARATOR = 0;

    @Override
    public byte[] encode(ObjectType type, byte[] content) {
        byte[] serializedType = type.serializedName().getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream output = new ByteArrayOutputStream(
                serializedType.length + 1 + content.length
        );

        output.writeBytes(serializedType);
        output.write(TYPE_SEPARATOR);
        output.writeBytes(content);

        return output.toByteArray();
    }

    @Override
    public StoredRecord decode(byte[] storedObject) {
        int separatorIndex = findSeparator(storedObject);
        if (separatorIndex < 0) {
            throw new IllegalArgumentException("Invalid stored object: missing type separator");
        }

        String serializedType = new String(storedObject, 0, separatorIndex, StandardCharsets.UTF_8);
        byte[] content = Arrays.copyOfRange(storedObject, separatorIndex + 1, storedObject.length);

        return new StoredRecord(ObjectType.fromSerializedName(serializedType), content);
    }

    private int findSeparator(byte[] storedObject) {
        for (int i = 0; i < storedObject.length; i++) {
            if (storedObject[i] == TYPE_SEPARATOR) {
                return i;
            }
        }

        return -1;
    }
}
