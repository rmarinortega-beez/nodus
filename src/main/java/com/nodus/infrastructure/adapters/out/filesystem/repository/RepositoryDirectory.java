package com.nodus.infrastructure.adapters.out.filesystem.repository;

import com.nodus.application.init.port.out.RepositoryDirectoryPort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class RepositoryDirectory implements RepositoryDirectoryPort {
    @Override
    public void create(Path nodusPath) throws IOException {
        Files.createDirectory(nodusPath);
    }
}
