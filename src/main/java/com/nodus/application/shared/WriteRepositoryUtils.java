package com.nodus.application.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class WriteRepositoryUtils {
    private static final String FORMAT_VERSION_KEY = "formatVersion";
    private static final String SUPPORTED_FORMAT_VERSION = "1";

    private WriteRepositoryUtils() {
    }

    public static void generateRepositoryMetadata(Path nodusPath) throws IOException {
        System.out.println("[nodus:repo] Generating metadata in " + nodusPath);
        Path repository = nodusPath.resolve("repository");
        Properties properties = new Properties();

        properties.setProperty(FORMAT_VERSION_KEY, SUPPORTED_FORMAT_VERSION);

        try (var writer = Files.newBufferedWriter(repository)) {
            properties.store(writer, null);
        }
        System.out.println("[nodus:repo] Metadata written to " + repository);
    }

    public static void ensureDirectory(Path path) throws IOException {
        if (Files.exists(path) && !Files.isDirectory(path)) {
            throw new IOException("Path exists but is not a directory: " + path);
        }

        Files.createDirectories(path);
        System.out.println("[nodus:repo] Directory is ready: " + path);
    }
}
