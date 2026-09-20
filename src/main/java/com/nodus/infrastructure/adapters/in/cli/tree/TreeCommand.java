package com.nodus.infrastructure.adapters.in.cli.tree;

import com.nodus.infrastructure.adapters.in.cli.tree.build.BuildTreeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

@Command(
        name = "tree",
        subcommands = {
                BuildTreeCommand.class
        }
)
@Component
@RequiredArgsConstructor
public class TreeCommand implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    @Override
    public Integer call() {
        System.out.println("[nodus:tree] Command group received");
        spec.commandLine().usage(System.out);
        return 0;
    }
}
