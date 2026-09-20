package com.nodus.infrastructure.adapters.in.cli.index.show;

import com.nodus.application.index.port.in.ShowIndexPort;
import com.nodus.domain.index.Index;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.Callable;

@Command(
        name = "show",
        description = "Show staged files in the Nodus index"
)
@Component
@RequiredArgsConstructor
public class ShowIndexCommand implements Callable<Integer> {

    private final ShowIndexPort showIndexPort;
    private final Path currentDirectory = Path.of("").toAbsolutePath().normalize();

    @Override
    public Integer call() throws Exception {
        System.out.println("[nodus:index:show] Command received");

        Index index = showIndexPort.show(currentDirectory);
        index.entries().entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().toString()))
                .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue().value()));

        return 0;
    }
}
