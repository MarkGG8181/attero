package io.github.client.tracker.impl;

import io.github.client.Attero;
import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;
import io.github.client.command.impl.*;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.interfaces.IMinecraft;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class CommandTracker extends AbstractTracker<AbstractCommand> implements IMinecraft {
    public static final String COMMAND_PREFIX = ".";

    private static final CommandTracker tracker = new CommandTracker();

    public static CommandTracker getInstance() {
        return tracker;
    }

    @Override
    public void initialize() {
        Attero.BUS.register(this);
        getSet().add(new HelpCommand());
        getSet().add(new ConfigCommand());
        getSet().add(new BindCommand());
        getSet().add(new GarbageCollectorCommand());
        getSet().add(new FriendCommand());
    }

    public AbstractCommand getByName(String name) {
        return getSet().stream()
                .filter(cmd -> {
                    CommandInfo info = cmd.getInfo();
                    if (info.name().equalsIgnoreCase(name)) return true;
                    for (String alias : info.aliases()) {
                        if (alias.equalsIgnoreCase(name)) return true;
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    @Subscribe
    public void onPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof ChatMessageC2SPacket packet) {
            String message = packet.chatMessage();

            if (message.startsWith(COMMAND_PREFIX)) {
                event.setCancelled(true);

                String[] parts = message.substring(COMMAND_PREFIX.length()).split(" ");
                String commandName = parts[0];
                AbstractCommand command = getByName(commandName);

                if (command != null) {
                    command.execute(parts);
                } else {
                    if (commandName.isBlank()) {
                        sendError("Missing arguments!");
                    } else {
                        sendError(String.format("Command §e%s§r not found!", commandName));
                    }
                }
            }
        }
    }
}