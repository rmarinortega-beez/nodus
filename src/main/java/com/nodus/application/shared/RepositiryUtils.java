package com.nodus.application.shared;

import com.nodus.domain.enums.InitializeRepositoryResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class RepositiryUtils {
    public static InitializeRepositoryResult isValidRepository(Path nodusPath){
        Path repositoryFile = nodusPath.resolve("repository");
        if (Files.exists(repositoryFile)) {
            Properties properties = new Properties();

            try (var reader = Files.newBufferedReader(repositoryFile)) {
                properties.load(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (Integer.valueOf(properties.getProperty("formatVersion")) == 1){
                return InitializeRepositoryResult.ALREADY_INITIALIZED;
            }
        }
        return InitializeRepositoryResult.INVALID_REPOSITORY;
    }

    public static void generateRepositoryMetadata(Path nodusPath) {
        Path repository = nodusPath.resolve("repository");
        Properties properties = new Properties();

        properties.setProperty("formatVersion", "1");

        try (var writer = Files.newBufferedWriter(repository)) {
            properties.store(writer, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkOrCreateObjectDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                throw new IOException("Path exists but is not a directory: " + path);
            }
        } else {
            Files.createDirectories(path);
            return true;
        }
        return true;
    }
}
