package fag.ware.client.command.impl;

import fag.ware.client.Fagware;
import fag.ware.client.command.AbstractCommand;
import fag.ware.client.command.data.CommandInfo;
import fag.ware.client.tracker.impl.CommandTracker;

import java.util.Arrays;

@CommandInfo(name = "Bind", description = "Binds a module to a key", aliases = {"bind", "bnd"})
public class BindCommand extends AbstractCommand {
    @Override
    public void execute(String[] args) {
        Fagware.INSTANCE.playerTracker.send(Arrays.toString(args));
    }
}
