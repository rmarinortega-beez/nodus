package com.nodus.application.index;

import com.nodus.domain.index.Index;
import com.nodus.domain.object.ObjectId;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class IndexCodec {
    private static final byte FIELD_SEPARATOR = 0;

    public byte[] encode(Index index) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        index.entries().entrySet().stream()
                .sorted(Comparator.comparing(entry -> serializedPath(entry.getKey())))
                .forEach(entry -> {
                    writeField(output, serializedPath(entry.getKey()));
                    writeField(output, entry.getValue().value());
                });

        return output.toByteArray();
    }

    public Index decode(byte[] content) {
        Map<Path, ObjectId> entries = new LinkedHashMap<>();
        int offset = 0;

        while (offset < content.length) {
            Field path = readField(content, offset, "path");
            Field objectId = readField(content, path.nextOffset(), "objectId");
            Path indexPath = Path.of(path.value());

            if (entries.put(indexPath, new ObjectId(objectId.value())) != null) {
                throw new IllegalArgumentException("Invalid index: duplicate path " + path.value());
            }

            offset = objectId.nextOffset();
        }

        return new Index(entries);
    }

    private void writeField(ByteArrayOutputStream output, String value) {
        output.writeBytes(value.getBytes(StandardCharsets.UTF_8));
        output.write(FIELD_SEPARATOR);
    }

    private Field readField(byte[] content, int offset, String fieldName) {
        if (offset >= content.length) {
            throw new IllegalArgumentException("Invalid index: missing " + fieldName);
        }

        int separatorIndex = findSeparator(content, offset);
        if (separatorIndex < 0) {
            throw new IllegalArgumentException("Invalid index: missing separator after " + fieldName);
        }

        String value = new String(content, offset, separatorIndex - offset, StandardCharsets.UTF_8);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Invalid index: empty " + fieldName);
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

    private String serializedPath(Path path) {
        return path.toString();
    }

    private record Field(String value, int nextOffset) {
    }
}
