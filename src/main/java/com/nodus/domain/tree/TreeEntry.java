package com.nodus.domain.tree;

import com.nodus.domain.enums.ObjectType;
import com.nodus.domain.object.ObjectId;

public record TreeEntry(ObjectType type, String name, ObjectId objectId) {
}
