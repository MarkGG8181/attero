package io.github.client.command.impl;

import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;
import io.github.client.tracker.impl.CommandTracker;

@CommandInfo(name = "Help", description = "Returns helpful information", aliases = {"help", "?"})
public class HelpCommand extends AbstractCommand {
    @Override
    public void execute(String[] args) {
        try {
            AbstractCommand command = CommandTracker.INSTANCE.getByName(args[1]);
            if (command != null) {
                if (!command.getHelp().isEmpty()) {
                    command.getHelp().forEach(line -> send(line, line.contains("usage")));
                }
            } else {
                error(String.format("Command §e%s§r not found!", args[1]));
            }
        } catch (Throwable t) {
            CommandTracker.INSTANCE.list.forEach(cmd -> send(String.format("§6%s§e%s§r - %s", CommandTracker.COMMAND_PREFIX, cmd.getInfo().aliases()[0], cmd.getInfo().description())));
        }
    }
}