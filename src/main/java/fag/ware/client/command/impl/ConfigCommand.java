package fag.ware.client.command.impl;

import fag.ware.client.Fagware;
import fag.ware.client.command.AbstractCommand;
import fag.ware.client.command.data.CommandInfo;
import fag.ware.client.file.impl.ModulesFile;
import fag.ware.client.util.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public void execute(String[] args) { // [config, load, configName]
        try {
            switch (args[1].toLowerCase()) {
                case "load" -> new ModulesFile(args[2].toLowerCase()).load();
                case "save" -> new ModulesFile(args[2].toLowerCase()).save();

                case "list" -> {
                    send("Available configs: ");
                    FileUtil.listFiles(Fagware.MOD_ID + File.separator + "configs", ".json").forEach(cfg -> {
                        String formattedTime = Instant.ofEpochMilli(cfg.createdTime().toMillis())
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                        send("> §e" + cfg.name().replaceAll(".json", "") + " §7(" + formattedTime + ")", false);
                    });
                }

                case "folder" -> {
                    File folder = new File(Fagware.MOD_ID + File.separator + "configs");
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
