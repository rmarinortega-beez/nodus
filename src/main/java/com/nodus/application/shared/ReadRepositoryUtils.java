package com.nodus.application.shared;

import com.nodus.domain.enums.InitializeRepositoryResult;
import com.nodus.domain.enums.ObjectType;
import com.nodus.domain.object.StoredRecord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

public final class ReadRepositoryUtils {
    private static final String FORMAT_VERSION_KEY = "formatVersion";
    private static final String SUPPORTED_FORMAT_VERSION = "1";

    private ReadRepositoryUtils() {
    }

    public static InitializeRepositoryResult getRepositoryStatus(Path nodusPath) throws IOException {
        System.out.println("[nodus:repo] Checking repository metadata in " + nodusPath);
        Path repositoryFile = nodusPath.resolve("repository");
        if (!Files.exists(repositoryFile)) {
            System.out.println("[nodus:repo] Metadata file not found: " + repositoryFile);
            return InitializeRepositoryResult.INVALID_REPOSITORY;
        }

        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(repositoryFile)) {
            properties.load(reader);
        }

        if (SUPPORTED_FORMAT_VERSION.equals(properties.getProperty(FORMAT_VERSION_KEY))) {
            System.out.println("[nodus:repo] Repository format version is supported");
            return InitializeRepositoryResult.ALREADY_INITIALIZED;
        }

        System.out.println("[nodus:repo] Unsupported repository format version: "
                + properties.getProperty(FORMAT_VERSION_KEY));
        return InitializeRepositoryResult.INVALID_REPOSITORY;
    }

    public static StoredRecord readStoredRecord(Path recordPath) throws IOException {
        byte[] storedObject = Files.readAllBytes(recordPath);

        int separatorIndex = findSeparator(storedObject);

        if (separatorIndex < 0) {
            throw new IllegalArgumentException(
                    "Invalid stored object: missing type separator"
            );
        }

        String serializedType = new String(
                storedObject,
                0,
                separatorIndex,
                StandardCharsets.UTF_8
        );

        byte[] content = Arrays.copyOfRange(
                storedObject,
                separatorIndex + 1,
                storedObject.length
        );

        return new StoredRecord(
                ObjectType.fromSerializedName(serializedType),
                content
        );
    }

    private static int findSeparator(byte[] storedObject) {
        for (int i = 0; i < storedObject.length; i++) {
            if (storedObject[i] == 0) {
                return i;
            }
        }

        return -1;
    }
}
