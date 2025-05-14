package fag.ware.client.command.impl;

import fag.ware.client.Fagware;
import fag.ware.client.command.AbstractCommand;
import fag.ware.client.command.data.CommandInfo;
import fag.ware.client.tracker.impl.CommandTracker;

@CommandInfo(name = "Help", description = "Returns helpful information", aliases = {"help", "?"})
public class HelpCommand extends AbstractCommand {
    @Override
    public void execute(String[] args) {
        Fagware.INSTANCE.commandTracker.getSet().forEach(cmd -> send(String.format("%s%s - %s", CommandTracker.COMMAND_PREFIX, cmd.getInfo().aliases()[0], cmd.getInfo().description())));
    }
}
