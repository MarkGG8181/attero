package io.github.client.command.impl;

import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;
import io.github.client.tracker.impl.FriendTracker;

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
                    FriendTracker.INSTANCE.list.add(friendName);
                    send("Added {} to friends", friendName);
                }
                case "remove" -> {
                    String friendName = args[2];
                    FriendTracker.INSTANCE.list.remove(friendName);
                    send("Removed {} from friends", friendName);
                }

                case "list" -> {
                    if (FriendTracker.INSTANCE.list.isEmpty())
                        send("You have no friends :(");
                    else {
                        send("Your friends: ");
                        send(false, "> {}", String.join(", ", FriendTracker.INSTANCE.list));
                    }
                }

                default -> {
                    String friendName = args[1];
                    FriendTracker.INSTANCE.list.add(friendName);
                    send("Added {} to friends", friendName);
                }
            }
        } catch (Throwable t) {
            error(t.getMessage());
        }
    }
}