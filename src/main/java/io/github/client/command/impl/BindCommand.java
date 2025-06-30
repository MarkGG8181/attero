package io.github.client.command.impl;

import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;
import io.github.client.module.AbstractModule;
import io.github.client.tracker.impl.ModuleTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CommandInfo(name = "Bind", description = "Binds a module to a key", aliases = {"bind", "binds", "bnd"})
public class BindCommand extends AbstractCommand {
    @Override
    public List<String> getHelp() {
        List<String> lines = new ArrayList<>();
        lines.add("Command §e.bind§7 usage:");
        lines.add(".bind §7add §e<mod> <key>§r - adds a bind to a module");
        lines.add(".bind §7remove §3<mod> <key>§r - removes a bind from a module");
        lines.add(".bind §7list §e<key>§r - shows all modules that are bound to a key");
        lines.add(".bind §7list §eall§r - shows all bound modules");
        lines.add(".bind §7clear§r - clears all binds");
        lines.add(".bind §7clear §e<key>§r - clears all bound modules from a key");
        lines.add(".bind §7clear §e<mod>§r - clears all binds from a module");
        return lines;
    }

    @Override
    public void execute(String[] args) { // [bind, add, speed, m]
        try {
            switch (args[1].toLowerCase()) {
                case "add" -> {
                    if (args.length != 4) throw new IllegalArgumentException("Expected 2 args to a \"add\" operation!");
                    AbstractModule byName = ModuleTracker.INSTANCE.getByName(args[2]);
                    int key = getKeyBind(args, 3);

                    if (byName == null)
                        throw new IllegalArgumentException(String.format("Module \"%s\" does not exist!", args[2]));
                    byName.getKeybinds().add(key);

                    send(String.format("Bound %s to %c", byName.getInfo().name(), (char) key));
                }
                case "remove" -> {
                    if (args.length != 4)
                        throw new IllegalArgumentException("Expected 2 args to a \"remove\" operation!");
                    AbstractModule byName = ModuleTracker.INSTANCE.getByName(args[2]);
                    int key = getKeyBind(args, 3);

                    if (byName == null)
                        throw new IllegalArgumentException(String.format("Module \"%s\" does not exist!", args[2]));
                    byName.getKeybinds().remove((Integer) key);

                    send(String.format("Unbound %s from %c", byName.getInfo().name(), (char) key));
                }
                case "list" -> {
                    if (args.length == 2) {
                        ModuleTracker.INSTANCE.list.forEach(m -> {
                            if (m.getKeybinds().isEmpty()) return;

                            StringBuilder sb = new StringBuilder();

                            for (Integer keybind : m.getKeybinds()) {
                                sb.append((char) (int) keybind).append(", ");
                            }

                            sb.delete(sb.length() - 2, sb.length());

                            send(String.format("%s -> %s", m.getInfo().name(), sb));
                        });
                    } else if (args.length == 3) {
                        AbstractModule byName = ModuleTracker.INSTANCE.getByName(args[2]);

                        if (byName != null) {
                            StringBuilder sb = new StringBuilder();

                            for (Integer keybind : byName.getKeybinds()) {
                                sb.append((char) (int) keybind).append(", ");
                            }

                            sb.delete(sb.length() - 2, sb.length());

                            send(String.format("%s -> %s", byName.getInfo().name(), sb));
                        } else {
                            int keyBind = getKeyBind(args, 2);

                            for (AbstractModule abstractModule : ModuleTracker.INSTANCE.list) {
                                if (abstractModule.getKeybinds().contains(keyBind)) {
                                    send(abstractModule.getInfo().name());
                                }
                            }
                        }
                    } else throw new IllegalArgumentException("Expected 1 or 0 args to a \"list\" operation!");
                }
                case "clear" -> {
                    if (args.length == 2) {
                        ModuleTracker.INSTANCE.list.forEach(m -> m.getKeybinds().clear());
                        send("Cleared all binds!");
                    } else if (args.length == 3) {
                        AbstractModule byName = ModuleTracker.INSTANCE.getByName(args[2]);

                        if (byName != null) {
                            byName.getKeybinds().clear();
                            send(String.format("Cleared all for module %s!", byName.getInfo().name()));
                        } else {
                            int keyBind = getKeyBind(args, 2);

                            ModuleTracker.INSTANCE.list.forEach(m -> m.getKeybinds().remove((Integer) keyBind));
                        }
                    } else throw new IllegalArgumentException("Expected 1 or 0 args to a \"clear\" operation!");
                }
                default -> throw new IllegalArgumentException("Invalid operation!");
            }
        } catch (Throwable t) {
            sendError(t.getMessage()); // lol
        }
    }

    /**
     * Extracted to a seperate method to support nigger binds soon
     */
    private static char getKeyBind(String[] args, int idx) {
        return args[idx].toUpperCase(Locale.ROOT).charAt(0);
    }
}