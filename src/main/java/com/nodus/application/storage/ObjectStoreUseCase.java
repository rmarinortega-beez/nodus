package com.nodus.application.storage;

import com.nodus.application.shared.HashUtils;
import com.nodus.application.shared.RepositiryUtils;
import com.nodus.domain.enums.ObjectType;
import com.nodus.domain.object.ObjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
public class ObjectStoreUseCase {
    public ObjectId store(Path object, Path repositoryPath) throws IOException, NoSuchAlgorithmException {
        RepositiryUtils.isValidRepository(repositoryPath);

        ObjectId objectId = new ObjectId(HashUtils.calculateHash(object, ObjectType.BLOB_00));

        RepositiryUtils.checkOrCreateObjectDirectory(repositoryPath.resolve(".nodus").resolve("objects"));
        Path persistedObjectPath = repositoryPath.resolve(".nodus").resolve("objects").resolve(objectId.value());
        if(!Files.exists(persistedObjectPath)) {
            Files.copy(object, persistedObjectPath);
        }

        return objectId;
    }
}
