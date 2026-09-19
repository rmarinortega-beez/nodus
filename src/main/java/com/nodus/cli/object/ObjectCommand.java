package com.nodus.cli.object;

import com.nodus.cli.object.store.LoadObjectCommand;
import com.nodus.cli.object.store.StoreObjectCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

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

    @Override
    public Integer call() throws Exception {
        System.out.println("Object command is running");
        return 0;
    }
}
