package com.nodus.domain.object;

import java.util.Arrays;
import java.util.Objects;

public record StoredRecord(ObjectType type, byte[] content) {
    public StoredRecord {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(content, "content");
        content = Arrays.copyOf(content, content.length);
    }

    @Override
    public byte[] content() {
        return Arrays.copyOf(content, content.length);
    }
}
