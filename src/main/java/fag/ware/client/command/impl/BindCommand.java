package fag.ware.client.command.impl;

import fag.ware.client.command.AbstractCommand;
import fag.ware.client.command.data.CommandInfo;

import java.util.Arrays;

@CommandInfo(name = "Bind", description = "Binds a module to a key", aliases = {"bind", "bnd"})
public class BindCommand extends AbstractCommand {
    @Override
    public void execute(String[] args) {
        send(Arrays.toString(args));
    }
}
