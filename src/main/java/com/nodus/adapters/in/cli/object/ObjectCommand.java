package com.nodus.adapters.in.cli.object;

import com.nodus.adapters.in.cli.object.store.LoadObjectCommand;
import com.nodus.adapters.in.cli.object.store.StoreObjectCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

@Command(
        name = "object",
        subcommands = {
                StoreObjectCommand.class,
                LoadObjectCommand.class
        }
)
@Component
@RequiredArgsConstructor
public class ObjectCommand implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        System.out.println("[nodus:object] Command group received");
        spec.commandLine().usage(System.out);
        return 0;
    }
}
