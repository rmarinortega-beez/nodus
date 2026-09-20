package com.nodus.infrastructure.adapters.out.filesystem.index;

import com.nodus.application.index.IndexCodec;
import com.nodus.application.index.port.out.IndexStorePort;
import com.nodus.domain.index.Index;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class FileSystemIndexStore implements IndexStorePort {
    private static final String INDEX_FILE = "index";

    private final IndexCodec indexCodec;

    @Override
    public Index load(Path nodusPath) throws IOException {
        Path indexPath = indexPath(nodusPath);
        if (!Files.exists(indexPath)) {
            return Index.empty();
        }

        return indexCodec.decode(Files.readAllBytes(indexPath));
    }

    @Override
    public void save(Path nodusPath, Index index) throws IOException {
        Files.write(indexPath(nodusPath), indexCodec.encode(index));
    }

    private Path indexPath(Path nodusPath) {
        return nodusPath.resolve(INDEX_FILE);
    }
}
