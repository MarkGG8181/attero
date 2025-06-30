package io.github.client.command.impl;

import io.ml.packet.impl.client.CLoadConfigPacket;
import io.github.client.Attero;
import io.github.client.command.AbstractCommand;
import io.github.client.command.data.CommandInfo;
import io.github.client.file.impl.ModulesFile;
import io.github.client.tracker.impl.AuthTracker;
import io.github.client.util.java.FileUtil;
import io.github.client.util.client.ConfigEntry;

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
    private List<ConfigEntry> cloudConfigs = new ArrayList<>();

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
        new Thread(() -> {
            try {
                cloudConfigs = AuthTracker.INSTANCE.fetchConfigList();
            } catch (Exception e) {
                Attero.LOGGER.error("Failed to fetch configs", e);
            }
        }).start();

        try {
            // ASS CODE ALERT
            switch (args[1].toLowerCase()) {
                case "load" -> {
                    String configName = args[2].toLowerCase() + (args[2].endsWith(".json") ? "" : ".json");
                    List<ConfigEntry> local = FileUtil.listFiles(Attero.MOD_ID + File.separator + "configs", ".json").stream().toList();

                    boolean foundLocal = local.stream().anyMatch(c -> c.name().equalsIgnoreCase(configName));
                    if (foundLocal) {
                        new ModulesFile(configName).load();
                    } else {
                        Optional<ConfigEntry> cloud = cloudConfigs.stream()
                                .filter(c -> c.name().equalsIgnoreCase(configName))
                                .findFirst();

                        if (cloud.isPresent()) {
                            AuthTracker.INSTANCE.send(new CLoadConfigPacket(configName));
                        } else {
                            sendError("Config " + configName + " not found.");
                        }
                    }
                }

                case "save" -> new ModulesFile(args[2].toLowerCase()).save();

                case "list" -> {
                    send("Available configs:");

                    List<ConfigEntry> local = FileUtil.listFiles(Attero.MOD_ID + File.separator + "configs", ".json").stream().toList();
                    for (ConfigEntry cfg : local) {
                        String formattedTime = Instant.ofEpochMilli(cfg.createdTime().toMillis())
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        send("> §e" + cfg.name().replace(".json", "") + " §7(" + formattedTime + ")", false);
                    }

                    for (ConfigEntry cfg : cloudConfigs) {
                        String formattedTime = Instant.ofEpochMilli(cfg.createdTime().toMillis())
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        send("> §b" + cfg.name().replace(".json", "") + " §7(" + formattedTime + ")", false);
                    }
                }

                case "folder" -> {
                    File folder = new File(Attero.MOD_ID + File.separator + "configs");
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
            sendError(t.getMessage());
        }
    }
}