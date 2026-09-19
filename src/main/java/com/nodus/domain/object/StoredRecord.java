package com.nodus.domain.object;

import com.nodus.domain.enums.ObjectType;

public record StoredRecord(ObjectType type, byte[] content) {
}
