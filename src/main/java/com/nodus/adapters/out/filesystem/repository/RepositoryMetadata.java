package com.nodus.adapters.out.filesystem.repository;

import com.nodus.application.init.InitializeRepositoryResult;
import com.nodus.application.port.RepositoryMetadataPort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Component
public class RepositoryMetadata implements RepositoryMetadataPort {
    private static final String METADATA_FILE = "repository";
    private static final String FORMAT_VERSION_KEY = "formatVersion";
    private static final String SUPPORTED_FORMAT_VERSION = "1";

    @Override
    public InitializeRepositoryResult status(Path nodusPath) throws IOException {
        Path metadataPath = metadataPath(nodusPath);
        if (!Files.exists(metadataPath)) {
            return InitializeRepositoryResult.INVALID_REPOSITORY;
        }

        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(metadataPath)) {
            properties.load(reader);
        }

        if (SUPPORTED_FORMAT_VERSION.equals(properties.getProperty(FORMAT_VERSION_KEY))) {
            return InitializeRepositoryResult.ALREADY_INITIALIZED;
        }

        return InitializeRepositoryResult.INVALID_REPOSITORY;
    }

    @Override
    public void write(Path nodusPath) throws IOException {
        Properties properties = new Properties();
        properties.setProperty(FORMAT_VERSION_KEY, SUPPORTED_FORMAT_VERSION);

        try (var writer = Files.newBufferedWriter(metadataPath(nodusPath))) {
            properties.store(writer, null);
        }
    }

    private Path metadataPath(Path nodusPath) {
        return nodusPath.resolve(METADATA_FILE);
    }
}
