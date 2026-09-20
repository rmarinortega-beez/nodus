package com.nodus.application.storage;

import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import com.nodus.domain.tree.Tree;
import com.nodus.domain.tree.TreeEntry;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class TreeCodec {
    private static final byte FIELD_SEPARATOR = 0;

    public byte[] encode(Tree tree) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        tree.entries().stream()
                .sorted(Comparator.comparing(TreeEntry::name))
                .forEach(entry -> writeEntry(output, entry));

        return output.toByteArray();
    }

    public Tree decode(byte[] content) {
        List<TreeEntry> entries = new ArrayList<>();
        int offset = 0;

        while (offset < content.length) {
            Field type = readField(content, offset, "type");
            Field objectId = readField(content, type.nextOffset(), "objectId");
            Field name = readField(content, objectId.nextOffset(), "name");

            entries.add(new TreeEntry(
                    ObjectType.fromSerializedName(type.value()),
                    name.value(),
                    new ObjectId(objectId.value())
            ));
            offset = name.nextOffset();
        }

        return new Tree(entries);
    }

    private void writeEntry(ByteArrayOutputStream output, TreeEntry entry) {
        writeField(output, entry.type().serializedName());
        writeField(output, entry.objectId().value());
        writeField(output, entry.name());
    }

    private void writeField(ByteArrayOutputStream output, String value) {
        output.writeBytes(value.getBytes(StandardCharsets.UTF_8));
        output.write(FIELD_SEPARATOR);
    }

    private Field readField(byte[] content, int offset, String fieldName) {
        if (offset >= content.length) {
            throw new IllegalArgumentException("Invalid tree object: missing " + fieldName);
        }

        int separatorIndex = findSeparator(content, offset);
        if (separatorIndex < 0) {
            throw new IllegalArgumentException("Invalid tree object: missing separator after " + fieldName);
        }

        String value = new String(content, offset, separatorIndex - offset, StandardCharsets.UTF_8);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Invalid tree object: empty " + fieldName);
        }

        return new Field(value, separatorIndex + 1);
    }

    private int findSeparator(byte[] content, int offset) {
        for (int i = offset; i < content.length; i++) {
            if (content[i] == FIELD_SEPARATOR) {
                return i;
            }
        }

        return -1;
    }

    private record Field(String value, int nextOffset) {
    }
}
