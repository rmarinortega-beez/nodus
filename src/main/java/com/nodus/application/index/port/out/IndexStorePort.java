package com.nodus.application.index.port.out;

import com.nodus.domain.index.Index;

import java.io.IOException;
import java.nio.file.Path;

public interface IndexStorePort {
    Index load(Path nodusPath) throws IOException;

    void save(Path nodusPath, Index index) throws IOException;
}
