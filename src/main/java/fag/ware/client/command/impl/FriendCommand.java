package fag.ware.client.command.impl;

import fag.ware.client.command.AbstractCommand;
import fag.ware.client.command.data.CommandInfo;
import fag.ware.client.tracker.impl.FriendTracker;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "Friend", description = "Manage your friends", aliases = {"friend", "friends"})
public class FriendCommand extends AbstractCommand {
    @Override
    public List<String> getHelp() {
        List<String> lines = new ArrayList<>();
        lines.add("Command §e.friend§r usage:");
        lines.add(".friend §7list - shows all of your friends");
        lines.add(".friend §7add §e<name>§r - adds a friend");
        lines.add(".friend §7remove §e<name>§r - removes a friend");
        lines.add(".friend §e<name>§r - adds a friend");
        return lines;
    }

    @Override
    public void execute(String[] args) {
        try {
            switch (args[1].toLowerCase()) {
                case "add" -> {
                    String friendName = args[2];
                    FriendTracker.getInstance().getSet().add(friendName);
                    send(String.format("Added §e%s§r to friends", friendName));
                }
                case "remove" -> {
                    String friendName = args[2];
                    FriendTracker.getInstance().getSet().remove(friendName);
                    send(String.format("Removed §e%s§r from friends", friendName));
                }

                case "list" -> {
                    if (FriendTracker.getInstance().getSet().isEmpty())
                        send("You have no friends :(");
                    else {
                        send("Your friends: ");
                        send("> " + String.join(", ", FriendTracker.getInstance().getSet()), false);
                    }
                }

                default -> {
                    String friendName = args[1];
                    FriendTracker.getInstance().getSet().add(friendName);
                    send(String.format("Added §e%s§r to friends", friendName));
                }
            }
        } catch (Throwable t) {
            sendError(t.getMessage());
        }
    }
}