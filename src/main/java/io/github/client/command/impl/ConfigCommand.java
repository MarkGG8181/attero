package io.github.client.command.impl;

import io.github.client.Attero;
import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;
import io.github.client.file.impl.ModulesFile;
import io.github.client.tracker.impl.AuthTracker;
import io.github.client.util.java.FileUtil;
import io.github.client.util.client.ConfigEntry;
import store.clovr.common.protocol.client.C2SRequestConfigDownloadPacket;
import store.clovr.common.protocol.client.C2SRequestConfigListPacket;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@CommandInfo(name = "Config", description = "Manage your configs", aliases = {"config", "cfg", "conf"})
public class ConfigCommand extends AbstractCommand {
    @Override
    public List<String> getHelp() {
        List<String> lines = new ArrayList<>();
        lines.add("Command §e.config§r usage:");
        lines.add(".config §7list§r - shows all available configs");
        lines.add(".config §7folder§r - opens config folder");
        lines.add(".config §e<cfg>§r - loads a config");
        lines.add(".config §7load §e<cfg>§r - loads a config");
        lines.add(".config §7save §e<cfg>§r - saves a config");
        return lines;
    }

    @Override
    public void execute(String[] args) { // [config, load, test]
        try {
            new Thread(() -> {
                try {
                    AuthTracker.INSTANCE.client.sendPacket(new C2SRequestConfigListPacket());
                } catch (Exception e) {
                    Attero.LOGGER.error("Failed to fetch configs", e);
                }
            }).start();


            // ASS CODE ALERT
            switch (args[1].toLowerCase()) {
                case "load" -> {
                    String input = args[2];
                    String configName = input.toLowerCase() + (input.endsWith(".json") ? "" : ".json");

                    boolean isId;
                    long id = -1;
                    try {
                        id = Long.parseLong(input);
                        isId = true;
                    } catch (NumberFormatException e) {
                        isId = false;
                    }

                    if (isId) {
                        long finalId = id;
                        Optional<ConfigEntry> cloudById = AuthTracker.INSTANCE.cloudConfigs.stream()
                                .filter(c -> c.id() == finalId)
                                .findFirst();

                        if (cloudById.isPresent()) {
                            AuthTracker.INSTANCE.client.sendPacket(new C2SRequestConfigDownloadPacket(cloudById.get().id()));
                        } else {
                            error("No cloud config found with ID: {}", id);
                        }
                    } else {
                        List<ConfigEntry> local = FileUtil.listFiles("configs", ".json").stream().toList();
                        boolean foundLocal = local.stream().anyMatch(c -> c.name().equalsIgnoreCase(configName));

                        if (foundLocal) {
                            new ModulesFile(configName).load();
                        } else {
                            Optional<ConfigEntry> cloudByName = AuthTracker.INSTANCE.cloudConfigs.stream()
                                    .filter(c -> c.name().equalsIgnoreCase(configName.replace(".json", "")))
                                    .findFirst();

                            if (cloudByName.isPresent()) {
                                AuthTracker.INSTANCE.client.sendPacket(new C2SRequestConfigDownloadPacket(cloudByName.get().id()));
                            } else {
                                error("Config {} not found locally or in cloud.", configName);
                            }
                        }
                    }
                }

                case "save" -> new ModulesFile(args[2].toLowerCase()).save();

                case "list" -> {
                    send("Available configs:");

                    List<ConfigEntry> local = FileUtil.listFiles("configs", ".json").stream().toList();
                    for (ConfigEntry cfg : local) {
                        send(false, "> {}", cfg.name().replace(".json", ""));
                    }

                    for (ConfigEntry cfg : AuthTracker.INSTANCE.cloudConfigs) {
                        send(false, "> {} ({})", cfg.name().replace(".json", ""), cfg.id());
                    }
                }

                case "folder" -> {
                    File folder = FileUtil.CLIENT_DIR.resolve("configs").toFile();
                    if (!folder.exists()) folder.mkdirs();

                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        Runtime.getRuntime().exec("explorer " + folder.getAbsolutePath().replace("/", "\\"));
                    } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        Runtime.getRuntime().exec("open " + folder.getAbsolutePath());
                    } else {
                        Runtime.getRuntime().exec("xdg-open " + folder.getAbsolutePath());
                    }
                }

                default -> new ModulesFile(args[1].toLowerCase()).load();
            }
        } catch (Throwable t) {
            error(t.getMessage());
        }
    }
}