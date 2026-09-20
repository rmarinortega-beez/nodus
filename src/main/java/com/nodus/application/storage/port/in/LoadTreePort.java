package com.nodus.application.storage.port.in;

import com.nodus.domain.object.ObjectId;
import com.nodus.domain.tree.Tree;

import java.io.IOException;
import java.nio.file.Path;

public interface LoadTreePort {
    Tree load(ObjectId objectId, Path repositoryPath) throws IOException;
}
