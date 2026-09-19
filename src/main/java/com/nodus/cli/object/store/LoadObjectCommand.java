package com.nodus.cli.object.store;

import com.nodus.application.storage.ObjectLoadUseCase;
import com.nodus.domain.object.ObjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "load")
@Component
@RequiredArgsConstructor
public class LoadObjectCommand implements Callable<Integer> {

    @Parameters(index = "0")
    private String objectId;

    private final ObjectLoadUseCase objectLoadUseCase;

    @Override
    public Integer call() throws Exception {
        ObjectId objectId = new ObjectId(this.objectId);

        byte[] object = objectLoadUseCase.load(objectId);
        System.out.println("Loaded object with ID: " + objectId.value());
        System.out.println("Object content: " + new String(object));

        return 0;
    }
}
