package com.nodus.domain.tree;

import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;

public record TreeEntry(ObjectType type, String name, ObjectId objectId) {
}
