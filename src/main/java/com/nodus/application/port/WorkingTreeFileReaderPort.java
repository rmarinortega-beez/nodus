package com.nodus.application.port;

import java.io.IOException;
import java.nio.file.Path;

public interface WorkingTreeFileReaderPort {
    byte[] read(Path path) throws IOException;
}
