package com.nodus.cli;

import com.nodus.cli.init.InitCommand;
import com.nodus.cli.object.ObjectCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Command(
        name = "nodus",
        description = "A small distributed version control system",
        mixinStandardHelpOptions = true,
        subcommands = {
                InitCommand.class,
                ObjectCommand.class
        }
)
@Component
public class NodusCommand implements Runnable {

    @Override
    public void run() {
    }
}