package com.nodus.application.storage.port.out;

import com.nodus.application.shared.PathType;

import java.nio.file.Path;

public interface WorkingTreePathInspectorPort {
    PathType typeOf(Path path);
}
