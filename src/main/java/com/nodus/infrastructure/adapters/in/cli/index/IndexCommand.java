package com.nodus.infrastructure.adapters.in.cli.index;

import com.nodus.infrastructure.adapters.in.cli.index.show.ShowIndexCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

@Command(
        name = "index",
        subcommands = {
                ShowIndexCommand.class
        }
)
@Component
@RequiredArgsConstructor
public class IndexCommand implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    @Override
    public Integer call() {
        System.out.println("[nodus:index] Command group received");
        spec.commandLine().usage(System.out);
        return 0;
    }
}
