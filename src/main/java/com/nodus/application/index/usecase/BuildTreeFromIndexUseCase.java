package com.nodus.application.index.usecase;

import com.nodus.application.index.port.in.BuildTreeFromIndexPort;
import com.nodus.application.index.port.out.IndexStorePort;
import com.nodus.application.init.port.out.RepositoryMetadataPort;
import com.nodus.application.shared.InitializeRepositoryResult;
import com.nodus.application.storage.ObjectStorageService;
import com.nodus.application.storage.TreeCodec;
import com.nodus.domain.index.Index;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.ObjectType;
import com.nodus.domain.tree.Tree;
import com.nodus.domain.tree.TreeEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class BuildTreeFromIndexUseCase implements BuildTreeFromIndexPort {
    private static final String NODUS_DIRECTORY = ".nodus";

    private final RepositoryMetadataPort repositoryMetadata;
    private final IndexStorePort indexStore;
    private final TreeCodec treeCodec;
    private final ObjectStorageService objectStorageService;

    @Override
    public ObjectId build(Path repositoryPath) throws IOException {
        Path nodusPath = repositoryPath.toAbsolutePath().normalize().resolve(NODUS_DIRECTORY);
        if (repositoryMetadata.status(nodusPath) != InitializeRepositoryResult.ALREADY_INITIALIZED) {
            throw new IOException("Current directory is not a valid Nodus repository.");
        }

        Index index = indexStore.load(nodusPath);
        TreeNode root = buildHierarchy(index);
        return storeTree(nodusPath, root);
    }

    private TreeNode buildHierarchy(Index index) throws IOException {
        TreeNode root = new TreeNode();

        for (Map.Entry<Path, ObjectId> entry : index.entries().entrySet()) {
            addIndexEntry(root, entry.getKey(), entry.getValue());
        }

        return root;
    }

    private void addIndexEntry(TreeNode root, Path path, ObjectId objectId) throws IOException {
        if (path.isAbsolute() || path.getNameCount() == 0 || path.toString().isBlank()) {
            throw new IOException("Invalid index path: " + path);
        }

        TreeNode current = root;
        for (int i = 0; i < path.getNameCount() - 1; i++) {
            String directoryName = path.getName(i).toString();
            if (current.files.containsKey(directoryName)) {
                throw new IOException("Invalid index: path is both file and directory: " + directoryName);
            }

            current = current.directories.computeIfAbsent(directoryName, ignored -> new TreeNode());
        }

        String fileName = path.getFileName().toString();
        if (current.directories.containsKey(fileName)) {
            throw new IOException("Invalid index: path is both file and directory: " + path);
        }

        current.files.put(fileName, objectId);
    }

    private ObjectId storeTree(Path nodusPath, TreeNode node) throws IOException {
        ArrayList<TreeEntry> entries = new ArrayList<>();

        for (Map.Entry<String, ObjectId> file : node.files.entrySet()) {
            entries.add(new TreeEntry(ObjectType.BLOB, file.getKey(), file.getValue()));
        }

        for (Map.Entry<String, TreeNode> directory : node.directories.entrySet()) {
            ObjectId childTreeId = storeTree(nodusPath, directory.getValue());
            entries.add(new TreeEntry(ObjectType.TREE, directory.getKey(), childTreeId));
        }

        byte[] treeContent = treeCodec.encode(new Tree(entries));
        return objectStorageService.store(nodusPath, ObjectType.TREE, treeContent);
    }

    private static class TreeNode {
        private final Map<String, TreeNode> directories = new TreeMap<>();
        private final Map<String, ObjectId> files = new TreeMap<>();
    }
}
