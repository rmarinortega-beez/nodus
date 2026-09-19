package com.nodus.infrastructure.adapters.in.cli.init;

import com.nodus.application.init.port.in.InitializeRepositoryPort;
import com.nodus.application.shared.InitializeRepositoryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
        name = "init",
        description = "Create a new Nodus repository"
)
@Component
@RequiredArgsConstructor
public class InitCommand implements Callable<Integer> {

    private final InitializeRepositoryPort initializeRepositoryUseCase;
    private final Path currentDirectory = Path.of("").toAbsolutePath().normalize();

    @Override
    public Integer call() throws Exception {
        System.out.println("[nodus:init] Command received");
        return switch (initializeRepositoryUseCase.execute(currentDirectory)) {
            case InitializeRepositoryResult.INITIALIZED -> {
                System.out.println("Initialized empty Nodus repository in "
                        + currentDirectory.resolve(".nodus"));
                yield 0;
            }
            case InitializeRepositoryResult.ALREADY_INITIALIZED -> {
                System.out.println("Nodus repository already initialized.");
                yield 0;
            }
            case InitializeRepositoryResult.PATH_CONFLICT -> {
                System.err.println("Cannot initialize repository: .nodus already exists.");
                yield 1;
            }
            case InitializeRepositoryResult.INVALID_REPOSITORY -> {
                System.err.println("Repository is invalid");
                yield 1;
            }
        };
    }
}
