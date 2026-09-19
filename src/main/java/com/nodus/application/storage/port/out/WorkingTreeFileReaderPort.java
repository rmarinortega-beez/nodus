package com.nodus.application.storage.port.out;

import java.io.IOException;
import java.nio.file.Path;

public interface WorkingTreeFileReaderPort {
    byte[] read(Path path) throws IOException;
}
