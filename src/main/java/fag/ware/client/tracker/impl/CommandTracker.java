package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.command.Command;
import fag.ware.client.command.data.CommandInfo;
import fag.ware.client.command.impl.HelpCommand;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.SendPacketEvent;
import fag.ware.client.tracker.Tracker;
import fag.ware.client.util.IMinecraft;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class CommandTracker extends Tracker<Command> implements IMinecraft {
    public static final String COMMAND_PREFIX = ".";

    @Override
    public void initialize() {
        Fagware.BUS.register(this);
        getSet().add(new HelpCommand());
    }

    public Command getByName(String name) {
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
                Command command = getByName(commandName);

                if (command != null) {
                    command.execute(parts);
                } else {
                    send(String.format("Command %s not found!", commandName));
                }
            }
        }
    }

}
