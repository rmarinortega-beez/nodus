package com.nodus;

import com.nodus.cli.NodusCommand;
import com.nodus.infrastructure.config.NodusConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import picocli.CommandLine;

public class Nodus {
    public static void main(String[] args) {
        int exitCode;

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(NodusConfiguration.class)) {

            NodusCommand rootCommand = context.getBean(NodusCommand.class);

            CommandLine commandLine = new CommandLine(
                    rootCommand,
                    context::getBean
            );

            exitCode = commandLine.execute(args);
        }

        System.exit(exitCode);
    }
}
