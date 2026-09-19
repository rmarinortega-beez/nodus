package com.nodus.domain.object;

public enum ObjectType {
    BLOB("blob"),
    TREE("tree");

    private final String serializedName;

    ObjectType(String serializedName) {
        this.serializedName = serializedName;
    }

    public String serializedName() {
        return serializedName;
    }

    public static ObjectType fromSerializedName(String serializedType) {
        for (ObjectType type : ObjectType.values()) {
            if (type.serializedName().equals(serializedType)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown object type: " + serializedType);
    }
}
