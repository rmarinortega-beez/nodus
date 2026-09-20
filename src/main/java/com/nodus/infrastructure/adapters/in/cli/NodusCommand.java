package com.nodus.infrastructure.adapters.in.cli;

import com.nodus.infrastructure.adapters.in.cli.add.AddCommand;
import com.nodus.infrastructure.adapters.in.cli.init.InitCommand;
import com.nodus.infrastructure.adapters.in.cli.object.ObjectCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(
        name = "nodus",
        description = "A small distributed version control system",
        mixinStandardHelpOptions = true,
        subcommands = {
                AddCommand.class,
                InitCommand.class,
                ObjectCommand.class
        }
)
@Component
public class NodusCommand implements Runnable {

    @Spec
    private CommandSpec spec;

    @Override
    public void run() {
        System.out.println("[nodus] Root command received");
        spec.commandLine().usage(System.out);
    }
}
