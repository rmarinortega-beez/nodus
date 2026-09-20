package com.nodus.infrastructure.adapters.in.cli.add;

import com.nodus.application.index.port.in.AddToIndexPort;
import com.nodus.domain.object.ObjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
        name = "add",
        description = "Add a file to the Nodus index"
)
@Component
@RequiredArgsConstructor
public class AddCommand implements Callable<Integer> {
    @Parameters(index = "0")
    private Path file;

    private final AddToIndexPort addToIndexPort;
    private final Path currentDirectory = Path.of("").toAbsolutePath().normalize();

    @Override
    public Integer call() throws Exception {
        ObjectId objectId = addToIndexPort.add(file, currentDirectory);
        System.out.println("Added file to index: " + file + " -> " + objectId.value());
        return 0;
    }
}
