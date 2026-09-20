package com.nodus.infrastructure.adapters.in.cli.tree.build;

import com.nodus.application.index.port.in.BuildTreeFromIndexPort;
import com.nodus.domain.object.ObjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
        name = "build",
        description = "Build the root tree object from the Nodus index"
)
@Component
@RequiredArgsConstructor
public class BuildTreeCommand implements Callable<Integer> {

    private final BuildTreeFromIndexPort buildTreeFromIndexPort;
    private final Path currentDirectory = Path.of("").toAbsolutePath().normalize();

    @Override
    public Integer call() throws Exception {
        System.out.println("[nodus:tree:build] Command received");

        ObjectId rootTreeId = buildTreeFromIndexPort.build(currentDirectory);
        System.out.println("Root tree: " + rootTreeId.value());

        return 0;
    }
}
