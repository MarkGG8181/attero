package io.github.client.tracker.impl;

import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;
import io.github.client.command.impl.*;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.interfaces.IMinecraft;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

/**
 * @author markuss
 * @since 13/05/2025
 */
public class CommandTracker extends AbstractTracker<AbstractCommand> implements IMinecraft {
    public static final String COMMAND_PREFIX = ".";

    public static final CommandTracker INSTANCE = new CommandTracker();

    @Override
    public void initialize() {
        super.initialize();
        list.add(new HelpCommand());
        list.add(new ConfigCommand());
        list.add(new BindCommand());
        list.add(new FriendCommand());
    }

    public AbstractCommand getByName(String name) {
        return list.stream()
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
    private void onPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof ChatMessageC2SPacket packet) {
            String message = packet.chatMessage();

            if (message.startsWith(COMMAND_PREFIX)) {
                event.cancelled = true;

                String[] parts = message.substring(COMMAND_PREFIX.length()).split(" ");
                String commandName = parts[0];
                AbstractCommand command = getByName(commandName);

                if (command != null) {
                    command.execute(parts);
                } else {
                    if (commandName.isBlank()) {
                        error("Missing arguments!");
                    } else {
                        error("Command §e{}§r not found!", commandName);
                    }
                }
            }
        }
    }
}