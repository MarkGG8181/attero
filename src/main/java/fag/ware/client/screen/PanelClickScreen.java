package fag.ware.client.screen;

import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.setting.AbstractSetting;
import fag.ware.client.module.data.setting.impl.*;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.tracker.impl.ModuleTracker;
import imgui.ImGui;
import imgui.flag.ImGuiColorEditFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.Arrays;

import static imgui.ImGui.*;

public final class PanelClickScreen extends Screen
{
    public PanelClickScreen()
    {
        super(Text.of("sex"));
    }

    @Override
    public void render(DrawContext context,
                       int mouseX,
                       int mouseY,
                       float deltaTicks)
    {
        super.render(context, mouseX, mouseY, deltaTicks);

        ImGuiImpl.draw(io -> {
            ImGuiImpl.applyDarkTheme();

            pushFont(ImGuiImpl.INTER_REGULAR_17);

            if (begin("Fagware"))
            {
                if (beginTabBar("tabs"))
                {
                    for (ModuleCategory value : ModuleCategory.values())
                    {
                        if (beginTabItem(value.name().toLowerCase()))
                        {
                            for (AbstractModule module : ModuleTracker.getInstance().getByCategory(value))
                            {
                                boolean open = ImGui.collapsingHeader(module.toString());

                                if (ImGui.isItemHovered())
                                {
                                    ImGui.beginTooltip();

                                    if (module.getKeybinds().isEmpty())
                                    {
                                        ImGui.setTooltip(module.getInfo().description());
                                    }
                                    else
                                    {
                                        StringBuilder sb = new StringBuilder();

                                        for (Integer keybind : module.getKeybinds())
                                        {
                                            sb.append((char)(int)keybind).append(", ");
                                        }

                                        sb.delete(sb.length() - 2, sb.length());
                                        ImGui.setTooltip(String.format("%s%nBinds: %s", module.getInfo().description(), sb));
                                    }

                                    ImGui.endTooltip();
                                }

                                if (open) {
                                    ImBoolean enabledMod = new ImBoolean(module.isEnabled());

                                    ImGui.pushID(module.toString());

                                    if (ImGui.checkbox("enabled", enabledMod)) {
                                        module.setEnabled(enabledMod.get());
                                    }

                                    for (AbstractSetting<?> setting : module.getSettings()) {
                                        if (setting.getHidden().getAsBoolean() || (setting.getParentSetting() != null && !setting.getParentSetting().getValue())) {
                                            continue;
                                        }

                                        PanelClickScreen.SettingRenderer.render(setting);
                                    }
                                    
                                    ImGui.popID();
                                }
                            }

                            endTabItem();
                        }
                    }

                    endTabBar();
                }

            }

            end();
            popFont();
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
                case MultiStringSetting mSS -> {
                    ImGui.setWindowFontScale(0.8f);
                    ImGui.text(mSS.getName());

                    float fullWidth = ImGui.getContentRegionAvailX();
                    ImGui.setNextItemWidth(fullWidth);
                    if (ImGui.beginCombo("##" + mSS.getName(), mSS.toString())) {
                        for (String value : mSS.getAll()) {
                            boolean selected = Arrays.asList(mSS.getValue()).contains(value);

                            if (ImGui.selectable(value, selected)) {
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
                        ImGui.endCombo();
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
