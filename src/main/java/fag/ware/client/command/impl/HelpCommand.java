package fag.ware.client.command.impl;

import fag.ware.client.command.AbstractCommand;
import fag.ware.client.command.data.CommandInfo;
import fag.ware.client.tracker.impl.CommandTracker;

@CommandInfo(name = "Help", description = "Returns helpful information", aliases = {"help", "?"})
public class HelpCommand extends AbstractCommand {
    @Override
    public void execute(String[] args) {
        try {
            AbstractCommand command = CommandTracker.getInstance().getByName(args[1]);
            if (command != null) {
                if (!command.getHelp().isEmpty()) {
                    command.getHelp().forEach(line -> send(line, line.contains("usage")));
                }
            } else {
                sendError(String.format("Command §e%s§r not found!", args[1]));
            }
        } catch (Throwable t) {
            CommandTracker.getInstance().getSet().forEach(cmd -> send(String.format("§6%s§e%s§r - %s", CommandTracker.COMMAND_PREFIX, cmd.getInfo().aliases()[0], cmd.getInfo().description())));
        }
    }
}