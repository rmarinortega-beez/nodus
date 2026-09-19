package com.nodus.infrastructure.worktree;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class WorkingTreeFileReader {
    public byte[] read(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
