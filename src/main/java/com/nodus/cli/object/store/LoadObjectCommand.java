package com.nodus.cli.object.store;

import com.nodus.application.storage.ObjectLoadUseCase;
import com.nodus.domain.object.ObjectId;
import com.nodus.domain.object.StoredRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "load")
@Component
@RequiredArgsConstructor
public class LoadObjectCommand implements Callable<Integer> {

    @Parameters(index = "0")
    private String objectId;

    private final ObjectLoadUseCase objectLoadUseCase;
    private final Path currentDirectory = Path.of("").toAbsolutePath().normalize();

    @Override
    public Integer call() throws Exception {
        System.out.println("[nodus:load] Command received");
        ObjectId objectId = new ObjectId(this.objectId);

        StoredRecord object = objectLoadUseCase.load(objectId, currentDirectory);
        System.out.println("Loaded object with ID: " + objectId.value());
        System.out.println("Object content: " + new String(object.content(), StandardCharsets.UTF_8));

        return 0;
    }
}
