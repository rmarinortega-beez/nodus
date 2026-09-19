package com.nodus.application.shared;

import com.nodus.domain.enums.InitializeRepositoryResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class RepositoryUtils {
    private static final String FORMAT_VERSION_KEY = "formatVersion";
    private static final String SUPPORTED_FORMAT_VERSION = "1";

    private RepositoryUtils() {
    }

    public static InitializeRepositoryResult getRepositoryStatus(Path nodusPath) throws IOException {
        Path repositoryFile = nodusPath.resolve("repository");
        if (!Files.exists(repositoryFile)) {
            return InitializeRepositoryResult.INVALID_REPOSITORY;
        }

        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(repositoryFile)) {
            properties.load(reader);
        }

        if (SUPPORTED_FORMAT_VERSION.equals(properties.getProperty(FORMAT_VERSION_KEY))) {
            return InitializeRepositoryResult.ALREADY_INITIALIZED;
        }

        return InitializeRepositoryResult.INVALID_REPOSITORY;
    }

    public static void generateRepositoryMetadata(Path nodusPath) throws IOException {
        Path repository = nodusPath.resolve("repository");
        Properties properties = new Properties();

        properties.setProperty(FORMAT_VERSION_KEY, SUPPORTED_FORMAT_VERSION);

        try (var writer = Files.newBufferedWriter(repository)) {
            properties.store(writer, null);
        }
    }

    public static void ensureDirectory(Path path) throws IOException {
        if (Files.exists(path) && !Files.isDirectory(path)) {
            throw new IOException("Path exists but is not a directory: " + path);
        }

        Files.createDirectories(path);
    }
}
