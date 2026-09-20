package com.nodus.domain.tree;

import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;

import java.util.Objects;

public record TreeEntry(ObjectType type, String name, ObjectId objectId) {
    public TreeEntry {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(objectId, "objectId");
    }
}
