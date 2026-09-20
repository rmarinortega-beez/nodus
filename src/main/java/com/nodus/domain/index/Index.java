package com.nodus.domain.index;

import com.nodus.domain.object.ObjectId;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public record Index(Map<Path, ObjectId> entries) {
    public Index {
        Objects.requireNonNull(entries, "entries");
        entries = Map.copyOf(entries);

        for (Map.Entry<Path, ObjectId> entry : entries.entrySet()) {
            Objects.requireNonNull(entry.getKey(), "entry path");
            Objects.requireNonNull(entry.getValue(), "entry objectId");
            if (entry.getKey().isAbsolute()) {
                throw new IllegalArgumentException("Index path must be repository-relative: " + entry.getKey());
            }
        }
    }

    public static Index empty() {
        return new Index(Map.of());
    }
}
