package io.github.client.screen;

import io.github.client.module.data.setting.impl.*;
import io.ml.packet.impl.client.CLoadConfigPacket;
import io.github.client.Attero;
import io.github.client.file.impl.ModulesFile;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.setting.AbstractSetting;
import io.github.client.screen.data.ImGuiImpl;
import io.github.client.screen.data.ImGuiThemes;
import io.github.client.tracker.impl.AuthTracker;
import io.github.client.tracker.impl.ModuleTracker;
import io.github.client.util.client.ConfigEntry;
import io.github.client.util.java.FileUtil;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.File;
import java.util.*;

public class DropdownClickScreen extends Screen {
    private final Map<ModuleCategory, ImVec2> positions = new HashMap<>();
    private final ImVec2 size = new ImVec2(230, 0);

    public DropdownClickScreen() {
        super(Text.of("Dropdown"));
    }

    @Override
    protected void init() {
        super.init();

        new Thread(() -> {
            try {
                ModuleTracker.INSTANCE.cloudConfigs = AuthTracker.INSTANCE.fetchConfigList();
            } catch (Exception e) {
                Attero.LOGGER.error("Failed to fetch configs", e);
            }

            ModuleTracker.INSTANCE.configs.clear();
            ModuleTracker.INSTANCE.configs.addAll(FileUtil.listFiles(Attero.MOD_ID + File.separator + "configs", ".json"));
        }).start();
    }

    private boolean initialised;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        if (!initialised) {
            positions.clear();
            int x = 20;
            for (ModuleCategory category : ModuleCategory.values()) {
                positions.put(category, new ImVec2(x, 20));
                x += (int) (size.x + 12);
            }
            initialised = true;
        }

        ImGuiImpl.draw(io -> {
            ImGuiThemes.applyTheme();

            for (ModuleCategory category : ModuleCategory.values()) {
                ImVec2 position = positions.get(category);

                ImGui.pushFont(ImGuiImpl.inter17);
                ImGui.setNextWindowPos(position, ImGuiCond.Once);
                ImGui.setNextWindowSize(size); //once not here to prevent from resizing the windows

                if (ImGui.begin(category.getName(), ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoScrollbar)) {
                    ImVec2 newPosition = ImGui.getWindowPos();
                    position.set(newPosition);

                    if (category.equals(ModuleCategory.CONFIGS)) {
                        boolean openLC = ImGui.collapsingHeader("Local configs");
                        if (openLC) {
                            ImGui.setWindowFontScale(0.8f);
                            for (ConfigEntry config : ModuleTracker.INSTANCE.configs) {
                                boolean selected = !ModuleTracker.INSTANCE.activeIsCloud && config.name().equals(ModuleTracker.INSTANCE.activeConfigName);

                                if (ImGui.radioButton(config.name(), selected)) {
                                    ModuleTracker.INSTANCE.activeConfigName = config.name();
                                    ModuleTracker.INSTANCE.activeIsCloud = false;
                                    new ModulesFile(config.name()).load();
                                }
                            }
                            ImGui.setWindowFontScale(1.0f);
                        }

                        boolean openCC = ImGui.collapsingHeader("Cloud configs");
                        if (openCC) {
                            ImGui.setWindowFontScale(0.8f);
                            for (ConfigEntry config : ModuleTracker.INSTANCE.cloudConfigs) {
                                boolean selected = ModuleTracker.INSTANCE.activeIsCloud && config.name().equals(ModuleTracker.INSTANCE.activeConfigName);

                                if (ImGui.radioButton(config.name(), selected)) {
                                    ModuleTracker.INSTANCE.activeConfigName = config.name();
                                    ModuleTracker.INSTANCE.activeIsCloud = true;
                                    AuthTracker.INSTANCE.send(new CLoadConfigPacket(config.name()));
                                }
                            }
                            ImGui.setWindowFontScale(1.0f);
                        }
                    }

                    for (AbstractModule module : ModuleTracker.INSTANCE.getByCategory(category)) {
                        ImGui.pushID(module.toString());
                        ImBoolean enabledMod = new ImBoolean(module.isEnabled());

                        if (ImGui.checkbox("##Enabled", enabledMod)) {
                            module.setEnabled(enabledMod.get());
                        }

                        ImGui.sameLine();
                        boolean open = ImGui.collapsingHeader(module.toString());

                        if (ImGui.isItemHovered()) {
                            ImGui.beginTooltip();

                            if (module.getKeybinds().isEmpty()) {
                                ImGui.setTooltip(module.getInfo().description());
                            } else {
                                StringBuilder sb = new StringBuilder();

                                for (Integer keybind : module.getKeybinds()) {
                                    sb.append((char) (int) keybind).append(", ");
                                }

                                sb.delete(sb.length() - 2, sb.length());
                                ImGui.setTooltip(String.format("%s%nBinds: %s", module.getInfo().description(), sb));
                            }

                            ImGui.endTooltip();
                        }

                        if (open) {
                            for (AbstractSetting<?> setting : module.getSettings()) {
                                if (setting.getHidden().getAsBoolean() || (setting.getParentSetting() != null && !setting.getParentSetting().getValue())) {
                                    continue;
                                }

                                SettingRenderer.render(setting);
                            }
                        }
                        ImGui.popID();
                    }
                }

                ImGui.end();
                ImGui.popFont();
            }
        });
    }

    private static class SettingRenderer {
        private static void render(AbstractSetting<?> setting) {
            switch (setting) {
                case NumberSetting nS -> {
                    float[] value = {nS.toFloat()};
                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(nS.getName());

                    float fullWidth = ImGui.getContentRegionAvailX();
                    ImGui.setNextItemWidth(fullWidth);
                    if (ImGui.sliderFloat("##" + nS.getName(), value, nS.min.floatValue(), nS.max.floatValue())) {
                        nS.setValue(value[0]);
                    }
                    ImGui.setWindowFontScale(1f);
                }
                case RangeNumberSetting rns -> {
                    float[] minVal = {rns.getMinAsFloat()};
                    float[] maxVal = {rns.getMaxAsFloat()};

                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(rns.getName() + " (Min-Max)");

                    float fullWidth = ImGui.getContentRegionAvailX() - 8;

                    float absMin = rns.absoluteMin.floatValue();
                    float absMax = rns.absoluteMax.floatValue();

                    ImGui.setNextItemWidth(fullWidth / 2f);
                    boolean minChanged = ImGui.sliderFloat("##" + rns.getName() + "_min", minVal, absMin, absMax);

                    ImGui.sameLine();

                    ImGui.setNextItemWidth(fullWidth / 2f);
                    boolean maxChanged = ImGui.sliderFloat("##" + rns.getName() + "_max", maxVal, absMin, absMax);

                    if (minChanged || maxChanged) {
                        rns.setRange(minVal[0], maxVal[0]);
                    }

                    ImGui.setWindowFontScale(1f);
                }
                case BooleanSetting bS -> {
                    ImBoolean value = new ImBoolean(bS.getValue());

                    ImGui.setWindowFontScale(0.8f);
                    if (ImGui.checkbox(setting.getName(), value)) {
                        bS.setValue(value.get());
                    }
                    ImGui.setWindowFontScale(1f);
                }
                case StringSetting sS -> {
                    ImInt index = new ImInt(sS.getIndex());

                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(sS.getName());

                    float fullWidth = ImGui.getContentRegionAvailX();
                    ImGui.setNextItemWidth(fullWidth);
                    if (ImGui.combo("##" + sS.getName(), index, sS.values)) {
                        sS.setIndex(index.get());
                    }
                    ImGui.setWindowFontScale(1f);
                }
                case MultiStringSetting mSS -> {
                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(mSS.getName());

                    float fullWidth = ImGui.getContentRegionAvailX();
                    ImGui.setNextItemWidth(fullWidth);

                    String popupId = "##popup_" + mSS.getName();
                    if (ImGui.button(mSS + "##" + mSS.getName() + "_" + mSS.hashCode())) {
                        ImGui.openPopup(popupId);
                    }

                    if (ImGui.beginPopup(popupId)) {
                        for (String value : mSS.getAll()) {
                            boolean selected = Arrays.asList(mSS.getValue()).contains(value);

                            if (ImGui.selectable(value, selected, ImGuiSelectableFlags.DontClosePopups)) {
                                if (selected) {
                                    mSS.setValue(Arrays.stream(mSS.getValue())
                                            .filter(v -> !v.equals(value))
                                            .toArray(String[]::new));
                                } else {
                                    String[] newEnabled = Arrays.copyOf(mSS.getValue(), mSS.getValue().length + 1);
                                    newEnabled[mSS.getValue().length] = value;
                                    mSS.setValue(newEnabled);
                                }
                            }
                        }
                        ImGui.endPopup();
                    }

                    ImGui.setWindowFontScale(1f);
                }
                case ColorSetting cS -> {
                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(cS.getName());

                    float[] value = {
                            cS.getRed() / 255f,
                            cS.getGreen() / 255f,
                            cS.getBlue() / 255f,
                            cS.getAlpha() / 255f
                    };

                    int flags = ImGuiColorEditFlags.PickerHueWheel
                            | ImGuiColorEditFlags.AlphaBar
                            | ImGuiColorEditFlags.NoSidePreview
                            | ImGuiColorEditFlags.Float
                            | ImGuiColorEditFlags.NoInputs;

                    float fullWidth = ImGui.getContentRegionAvailX();
                    ImGui.setNextItemWidth(fullWidth);

                    if (ImGui.colorPicker4("##" + cS.getName(), value, flags)) {
                        int r = clampColor(Math.round(value[0] * 255));
                        int g = clampColor(Math.round(value[1] * 255));
                        int b = clampColor(Math.round(value[2] * 255));
                        int a = clampColor(Math.round(value[3] * 255));
                        cS.setValue(new Color(r, g, b, a));
                    }
                    ImGui.setWindowFontScale(1f);
                }
                case GroupSetting gS -> {
                    ImGui.setWindowFontScale(0.8f);
                    boolean expanded = ImGui.treeNode(gS.getName());
                    gS.setValue(expanded);
                    if (expanded) {
                        ImGui.treePop();
                    }
                    ImGui.setWindowFontScale(1f);
                }
                default -> {
                }
            }
        }

        private static int clampColor(int value) {
            return Math.max(0, Math.min(255, value));
        }
    }
}