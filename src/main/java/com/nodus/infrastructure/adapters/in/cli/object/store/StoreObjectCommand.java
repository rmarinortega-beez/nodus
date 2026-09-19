package com.nodus.infrastructure.adapters.in.cli.object.store;

import com.nodus.application.storage.port.in.StoreObjectPort;
import com.nodus.domain.object.ObjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "store")
@Component
@RequiredArgsConstructor
public class StoreObjectCommand implements Callable<Integer> {

    @Parameters(index = "0")
    private Path file;

    private final StoreObjectPort objectStoreUseCase;
    private final Path currentDirectory = Path.of("").toAbsolutePath().normalize();

    @Override
    public Integer call() throws Exception {
        System.out.println("[nodus:store] Command received");

        ObjectId object = objectStoreUseCase.store(file, currentDirectory);
        System.out.println("Stored object with ID: " + object.value());

        return 0;
    }
}
