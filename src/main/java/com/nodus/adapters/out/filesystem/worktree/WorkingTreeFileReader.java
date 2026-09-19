package com.nodus.adapters.out.filesystem.worktree;

import com.nodus.application.port.WorkingTreeFileReaderPort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class WorkingTreeFileReader implements WorkingTreeFileReaderPort {
    @Override
    public byte[] read(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
