package io.github.client.file.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.client.Attero;
import io.github.client.file.AbstractFile;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.setting.AbstractSetting;
import io.github.client.module.data.setting.impl.*;
import io.github.client.tracker.impl.ModuleTracker;
import io.github.client.util.client.ConfigEntry;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.io.File;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author markuss
 * @since 22/05/2025
 */
public class ModulesFile extends AbstractFile {
    public ModulesFile(String configName) {
        super("configs" + File.separator + configName);
    }

    @Override
    public void save() {
        JsonObject jsonObject = new JsonObject();

        for (AbstractModule mod : ModuleTracker.INSTANCE.list) {
            JsonObject modObject = new JsonObject();
            modObject.addProperty("Enabled", mod.isEnabled());
            modObject.addProperty("Expanded", mod.isExpanded());

            JsonArray keybinds = new JsonArray();
            mod.getKeybinds().forEach(keybinds::add);
            modObject.add("Keybinds", keybinds);

            if (!mod.getSettings().isEmpty()) {
                JsonObject settingObject = new JsonObject();

                for (AbstractSetting<?> sett : mod.getSettings()) {
                    switch (sett) {
                        case GroupSetting gS -> settingObject.addProperty(gS.getName(), gS.getValue());
                        case BooleanSetting bS -> settingObject.addProperty(bS.getName(), bS.getValue());
                        case NumberSetting nS -> settingObject.addProperty(nS.getName(), nS.getValue());
                        case StringSetting sS -> settingObject.addProperty(sS.getName(), sS.getValue());
                        case ColorSetting cS -> settingObject.addProperty(cS.getName(), cS.toInt());

                        case RangeNumberSetting rNS -> {
                            JsonArray values = new JsonArray();
                            for (Number val : rNS.getValue()) {
                                values.add(val);
                            }
                            settingObject.add(rNS.getName(), values);
                        }

                        case MultiStringSetting mSS -> {
                            JsonArray values = new JsonArray();
                            for (String val : mSS.getValue()) {
                                values.add(val);
                            }
                            settingObject.add(mSS.getName(), values);
                        }

                        default -> settingObject.addProperty(sett.getName(), "Unknown setting type");
                    }
                }

                modObject.add("Settings", settingObject);
            }

            jsonObject.add(mod.getInfo().name(), modObject);
        }

        if (MinecraftClient.getInstance().player == null)
            Attero.LOGGER.info("Saved {} successfully", getFile().getAbsolutePath());
        else
            ModuleTracker.INSTANCE.send("Saved {} config successfully", getFile().getName().replaceAll(".json", ""));

        super.saveJsonObject(jsonObject);
    }

    @Override
    public void load() {
        JsonObject json = loadJsonObject();

        if (json == null || json.isEmpty()) {
            if (MinecraftClient.getInstance().player == null)
                Attero.LOGGER.info("Config {} not found!", getFile().getAbsolutePath());
            else
                ModuleTracker.INSTANCE.error("Config {} not found", getFile().getName().replaceAll(".json", ""));

            return;
        }

        for (AbstractModule mod : ModuleTracker.INSTANCE.list) {
            JsonObject modObject = json.getAsJsonObject(mod.getInfo().name());
            if (modObject == null) continue;

            if (mod.isEnabled())
                mod.setEnabled(false);

            if (modObject.has("Expanded")) mod.setExpanded(modObject.get("Expanded").getAsBoolean());

            if (modObject.has("Keybinds")) {
                JsonArray keybindArray = modObject.getAsJsonArray("Keybinds");
                List<Integer> keybinds = new ArrayList<>();
                for (JsonElement e : keybindArray) keybinds.add(e.getAsInt());
                mod.setKeybinds(keybinds);
            }

            if (modObject.has("Settings")) {
                JsonObject settingObject = modObject.getAsJsonObject("Settings");

                for (AbstractSetting<?> sett : mod.getSettings()) {
                    if (!settingObject.has(sett.getName())) continue;

                    JsonElement element = settingObject.get(sett.getName());

                    switch (sett) {
                        case GroupSetting gS -> gS.setValue(element.getAsBoolean());
                        case BooleanSetting bS -> bS.setValue(element.getAsBoolean());
                        case NumberSetting nS -> nS.setValue(element.getAsDouble());
                        case StringSetting sS -> sS.setValue(element.getAsString());
                        case ColorSetting cS -> cS.setValue(new Color(element.getAsInt(), true));

                        case RangeNumberSetting rNS -> {
                            JsonArray array = element.getAsJsonArray();
                            rNS.setValue(new Number[]{
                                    array.get(0).getAsDouble(),
                                    array.get(1).getAsDouble()
                            });
                        }

                        case MultiStringSetting mSS -> {
                            JsonArray array = element.getAsJsonArray();
                            String[] values = new String[array.size()];
                            for (int i = 0; i < array.size(); i++) {
                                values[i] = array.get(i).getAsString();
                            }
                            mSS.setValue(values);
                        }

                        default -> Attero.LOGGER.warn("Unknown setting type: {}", sett.getName());
                    }
                }
            }

            if (modObject.has("Enabled")) mod.setEnabled(modObject.get("Enabled").getAsBoolean());
        }

        if (MinecraftClient.getInstance().player == null) {
            ModuleTracker.INSTANCE.currentConfig = new ConfigEntry(getFileName(), FileTime.fromMillis(System.currentTimeMillis()));
            Attero.LOGGER.info("Loaded {} successfully", getFile().getAbsolutePath());
        } else
            ModuleTracker.INSTANCE.send("Loaded {} config successfully", getFile().getName().replaceAll(".json", ""));
    }
}