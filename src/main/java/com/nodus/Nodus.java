package com.nodus;

import com.nodus.cli.NodusCommand;
import com.nodus.infrastructure.config.NodusConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import picocli.CommandLine;

public class Nodus {
    public static void main(String[] args) {
        int exitCode;
        System.out.println("[nodus] Starting CLI");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(NodusConfiguration.class)) {
            System.out.println("[nodus] Loading application context");

            NodusCommand rootCommand = context.getBean(NodusCommand.class);

            CommandLine commandLine = new CommandLine(
                    rootCommand,
                    context::getBean
            );

            exitCode = commandLine.execute(args);
        }

        System.out.println("[nodus] Finished with exit code " + exitCode);
        System.exit(exitCode);
    }
}
