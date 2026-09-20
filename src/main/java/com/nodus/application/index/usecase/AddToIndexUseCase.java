package com.nodus.application.index.usecase;

import com.nodus.application.index.port.in.AddToIndexPort;
import com.nodus.application.index.port.out.IndexStorePort;
import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.application.shared.PathType;
import com.nodus.application.storage.ObjectStorageService;
import com.nodus.application.storage.port.out.WorkingTreeFileReaderPort;
import com.nodus.application.storage.port.out.WorkingTreePathInspectorPort;
import com.nodus.domain.index.Index;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AddToIndexUseCase implements AddToIndexPort {
    private static final String NODUS_DIRECTORY = ".nodus";

    private final RepositoryMetadataPort repositoryMetadata;
    private final WorkingTreePathInspectorPort workingTreePathInspector;
    private final WorkingTreeFileReaderPort workingTreeFileReader;
    private final ObjectStorageService objectStorageService;
    private final IndexStorePort indexStore;

    @Override
    public ObjectId add(Path file, Path repositoryPath) throws IOException {
        Path repositoryRoot = repositoryPath.toAbsolutePath().normalize();
        Path nodusPath = repositoryRoot.resolve(NODUS_DIRECTORY);

        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        Path filePath = resolveFilePath(repositoryRoot, file);
        Path relativePath = repositoryRoot.relativize(filePath);
        validateStagedPath(relativePath);

        if (workingTreePathInspector.typeOf(filePath) != PathType.REGULAR_FILE) {
            throw new IOException("Only regular files can be added to the index: " + file);
        }

        byte[] content = workingTreeFileReader.read(filePath);
        ObjectId objectId = objectStorageService.store(nodusPath, ObjectType.BLOB, content);

        Index currentIndex = indexStore.load(nodusPath);
        Map<Path, ObjectId> entries = new HashMap<>(currentIndex.entries());
        entries.put(relativePath, objectId);
        indexStore.save(nodusPath, new Index(entries));

        return objectId;
    }

    private Path resolveFilePath(Path repositoryRoot, Path file) throws IOException {
        Path filePath = file.isAbsolute()
                ? file.normalize()
                : repositoryRoot.resolve(file).normalize();

        if (!filePath.startsWith(repositoryRoot)) {
            throw new IOException("Cannot add a file outside the repository: " + file);
        }

        return filePath;
    }

    private void validateStagedPath(Path relativePath) throws IOException {
        if (relativePath.getNameCount() == 0 || relativePath.startsWith(NODUS_DIRECTORY)) {
            throw new IOException("Cannot add Nodus internal files to the index: " + relativePath);
        }
    }
}
