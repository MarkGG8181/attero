package fag.ware.client.screen;

import fag.ware.client.Fagware;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.setting.Setting;
import fag.ware.client.module.data.setting.impl.*;
import fag.ware.client.util.imgui.ImGuiImpl;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ClickScreen extends Screen {
    private final Map<ModuleCategory, ImVec2> positions = new HashMap<>();
    private final ImVec2 size = new ImVec2(230, 0);

    public ClickScreen() {
        super(Text.of("Classic Dropdown"));
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
            ImGuiImpl.applyMarineTheme();

            for (ModuleCategory category : ModuleCategory.values()) {
                ImVec2 position = positions.get(category);

                ImGui.pushFont(ImGuiImpl.INTER_REGULAR_17);
                ImGui.setNextWindowPos(position, ImGuiCond.Once);
                ImGui.setNextWindowSize(size); //once not here to prevent from resizing the windows

                if (ImGui.begin(category.getName(), ImGuiWindowFlags.NoDocking)) {
                    ImVec2 newPosition = ImGui.getWindowPos();
                    position.set(newPosition);

                    for (Module module : Fagware.INSTANCE.moduleTracker.getByCategory(category)) {

                        ImGui.pushID(module.toString());
                        ImBoolean enabledMod = new ImBoolean(module.isEnabled());

                        if (ImGui.checkbox("##Enabled", enabledMod)) {
                            module.setEnabled(enabledMod.get());
                        }

                        ImGui.sameLine();
                        boolean open = ImGui.collapsingHeader(module.toString());

                        if (open) {
                            for (Setting<?> setting : module.getSettings()) {
                                if (setting.getHidden().getAsBoolean()) {
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
        private static void render(Setting<?> setting) {
            switch (setting) {
                case NumberSetting nS -> {
                    float[] value = {nS.toFloat()};
                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(nS.getName());

                    float fullWidth = ImGui.getContentRegionAvailX();
                    ImGui.setNextItemWidth(fullWidth);
                    if (ImGui.sliderFloat("##" + nS.getName(), value, nS.getMin().floatValue(), nS.getMax().floatValue())) {
                        nS.setValue(value[0]);
                    }
                    ImGui.setWindowFontScale(1f);
                }
                case RangeNumberSetting rns -> {
                    float[] minVal = {rns.getMinAsFloat()};
                    float[] maxVal = {rns.getMaxAsFloat()};

                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(rns.getName() + " (MIN-MAX)");

                    float fullWidth = ImGui.getContentRegionAvailX() - 8;

                    float absMin = rns.getAbsoluteMin().floatValue();
                    float absMax = rns.getAbsoluteMax().floatValue();

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
                    if (ImGui.combo("##" + sS.getName(), index, sS.getValues())) {
                        sS.setIndex(index.get());
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
                    int flags = ImGuiColorEditFlags.PickerHueBar
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
                    if (ImGui.treeNode(gS.getName())) {
                        for (Setting<?> child : gS.getChildren()) {
                            render(child);
                        }
                        ImGui.treePop();
                    }
                    ImGui.setWindowFontScale(1f);
                }
                default -> {}
            }
        }

        private static int clampColor(int value) {
            return Math.max(0, Math.min(255, value));
        }
    }
}