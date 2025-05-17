package fag.ware.client.screen;

import fag.ware.client.Fagware;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.setting.AbstractSetting;
import fag.ware.client.module.data.setting.impl.*;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.tracker.impl.ModuleTracker;
import fag.ware.client.tracker.impl.ScreenTracker;
import imgui.*;
import imgui.flag.*;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import lombok.AllArgsConstructor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JelloClickScreen extends Screen {
    private final Map<ModuleCategory, ImVec2> positions = new HashMap<>();
    private final ImVec2 size = new ImVec2(230, 230);

    public JelloClickScreen() {
        super(Text.of("Jello"));
    }

    private boolean initialised;
    private SettingRenderer settingRenderer;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        if (!initialised) {
            positions.clear();
            int columns = 3;
            int spacing = 12;
            int startX = 20;
            int startY = 20;

            int x = startX;
            int y = startY;
            int col = 0;

            for (ModuleCategory category : ModuleCategory.values()) {
                positions.put(category, new ImVec2(x, y));

                col++;
                if (col >= columns) {
                    col = 0;
                    x = startX;
                    y += (int) (size.y + spacing);
                } else {
                    x += (int) (size.x + spacing);
                }
            }

            initialised = true;
        }

        ImGuiImpl.draw(io -> {
            ImGuiImpl.applyDarkTheme();
            for (ModuleCategory category : ModuleCategory.values()) {
                ImVec2 position = positions.get(category);

                ImGui.pushFont(ImGuiImpl.INTER_REGULAR_17);
                ImGui.setNextWindowPos(position, ImGuiCond.Once);
                ImGui.setNextWindowSize(size); //once not here to prevent from resizing the windows

                if (ImGui.begin(category.getName() + "##1", ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse)) {
                    ImVec2 newPosition = ImGui.getWindowPos();
                    position.set(newPosition);

                    for (AbstractModule module : ModuleTracker.getInstance().getByCategory(category)) {
                        ImGui.pushID(module.toString());

                        boolean isToggled = module.isEnabled();
                        if (isToggled) {
                            ImGui.pushStyleColor(ImGuiCol.Button, 0.20f, 0.50f, 0.90f, 1.0f);       // base
                            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.55f, 0.95f, 1.0f); // hover
                            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.45f, 0.85f, 1.0f); // pressed
                        }

                        if (ImGui.button(module.toString(), -1, 0)) {
                            module.toggle();
                        }

                        if (ImGui.isItemHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Right)) {
                            module.setExpanded(true);
                            settingRenderer = new SettingRenderer(module);
                        }

                        if (isToggled) {
                            ImGui.popStyleColor(3);
                        }

                        ImGui.popID();
                    }
                }

                ImGui.end();

                ImGui.popFont();
            }

            if (settingRenderer != null) {
                settingRenderer.render(io);
            }
        });
    }

    @AllArgsConstructor
    private static class SettingRenderer {
        public AbstractModule parent;

        public void render(ImGuiIO io) {
            ImGui.setNextWindowPos(new ImVec2(io.getDisplaySize().x * 0.5f, io.getDisplaySize().y * 0.5f), ImGuiCond.Always, new ImVec2(0.5f, 0.5f));
            ImGui.setNextWindowSize(430, 500);

            if (ImGui.begin("Settings", ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar)) {

                ImGui.pushFont(ImGuiImpl.INTER_REGULAR_30);

                ImDrawList drawList = ImGui.getForegroundDrawList();
                ImVec2 winPos = ImGui.getWindowPos();
                drawList.addText(new ImVec2(winPos.x + 5, winPos.y - ImGui.getTextLineHeight() - 5), ImColor.rgb(255, 255, 255), parent.toString());

                for (AbstractSetting<?> setting : parent.getSettings()) {
                    if (setting.getHidden().getAsBoolean() || (setting.getParentSetting() != null && !setting.getParentSetting().getValue())) {
                        continue;
                    }

                    switch (setting) {
                        case NumberSetting nS -> {
                            ImGui.setWindowFontScale(0.5f);
                            ImGui.text(nS.getName());
                            ImGui.sameLine();
                            float[] value = {nS.toFloat()};
                            float fullWidth = ImGui.getContentRegionAvailX();
                            float sliderWidth = 280; // Adjust width as needed
                            float rightAlignX = fullWidth - sliderWidth;

                            ImGui.setCursorPosX(ImGui.getCursorPosX() + rightAlignX);
                            if (ImGui.sliderFloat("##" + nS.getName(), value, nS.getMin().floatValue(), nS.getMax().floatValue())) {
                                nS.setValue(value[0]);
                            }
                            ImGui.setWindowFontScale(1f);
                        }
                        case RangeNumberSetting rns -> {
                            float[] minVal = {rns.getMinAsFloat()};
                            float[] maxVal = {rns.getMaxAsFloat()};

                            ImGui.setWindowFontScale(0.5f);
                            ImGui.text("Min " + rns.getName());
                            ImGui.sameLine();

                            float absMin = rns.getAbsoluteMin().floatValue();
                            float absMax = rns.getAbsoluteMax().floatValue();

                            float fullWidth = ImGui.getContentRegionAvailX();
                            float sliderWidth = 280;
                            float rightAlignX = fullWidth - sliderWidth;

                            ImGui.setCursorPosX(ImGui.getCursorPosX() + rightAlignX);
                            boolean minChanged = ImGui.sliderFloat("##" + rns.getName() + "_min", minVal, absMin, absMax);


                            ImGui.text("Max " + rns.getName());
                            ImGui.sameLine();
                            float fullWidth2 = ImGui.getContentRegionAvailX();
                            float sliderWidth2 = 280;
                            float rightAlignX2 = fullWidth2 - sliderWidth2;

                            ImGui.setCursorPosX(ImGui.getCursorPosX() + rightAlignX2);
                            boolean maxChanged = ImGui.sliderFloat("##" + rns.getName() + "_max", maxVal, absMin, absMax);

                            if (minChanged || maxChanged) {
                                rns.setRange(minVal[0], maxVal[0]);
                            }

                            ImGui.setWindowFontScale(1f);
                        }
                        case StringSetting sS -> {
                            ImGui.setWindowFontScale(0.5f);
                            ImGui.text(sS.getName());
                            ImGui.sameLine();

                            ImInt index = new ImInt(sS.getIndex());
                            float fullWidth = ImGui.getContentRegionAvailX();
                            float sliderWidth = 280; // Adjust width as needed
                            float rightAlignX = fullWidth - sliderWidth;

                            ImGui.setCursorPosX(ImGui.getCursorPosX() + rightAlignX);
                            if (ImGui.combo("##" + sS.getName(), index, sS.getValues())) {
                                sS.setIndex(index.get());
                            }
                            ImGui.setWindowFontScale(1f);
                        }
                        case BooleanSetting bS -> {
                            ImGui.setWindowFontScale(0.5f);
                            ImGui.text(bS.getName());
                            ImGui.sameLine();

                            ImBoolean value = new ImBoolean(bS.getValue());
                            float fullWidth = ImGui.getContentRegionAvailX();
                            float sliderWidth = 24; // Adjust width as needed
                            float rightAlignX = fullWidth - sliderWidth;

                            ImGui.setCursorPosX(ImGui.getCursorPosX() + rightAlignX);
                            if (ImGui.checkbox("##" + setting.getName(), value)) {
                                bS.setValue(value.get());
                            }
                            ImGui.setWindowFontScale(1f);
                        }

                        case GroupSetting gS -> {
                            ImGui.setWindowFontScale(0.5f);
                            boolean expanded = ImGui.treeNode(gS.getName());
                            gS.setValue(expanded);
                            if (expanded) {
                                ImGui.treePop();
                            }
                            ImGui.setWindowFontScale(1f);
                        }

                        case ColorSetting cS -> {
                            ImGui.setWindowFontScale(0.5f);
                            ImGui.text(cS.getName());
                            ImGui.sameLine();

                            float[] value = {
                                    cS.getRed() / 255f,
                                    cS.getGreen() / 255f,
                                    cS.getBlue() / 255f,
                                    cS.getAlpha() / 255f
                            };
                            int flags = ImGuiColorEditFlags.NoInputs
                                    | ImGuiColorEditFlags.NoLabel
                                    | ImGuiColorEditFlags.NoTooltip
                                    | ImGuiColorEditFlags.AlphaPreview
                                    | ImGuiColorEditFlags.PickerHueBar
                                    | ImGuiColorEditFlags.DisplayRGB;

                            float fullWidth = ImGui.getContentRegionAvailX();
                            float sliderWidth = 24; // Adjust width as needed
                            float rightAlignX = fullWidth - sliderWidth;

                            ImGui.setCursorPosX(ImGui.getCursorPosX() + rightAlignX);
                            ImGui.setNextItemWidth(sliderWidth);

                            if (ImGui.colorEdit4("##" + cS.getName(), value, flags)) {
                                cS.setValue(new Color(
                                        (int)(value[0] * 255),
                                        (int)(value[1] * 255),
                                        (int)(value[2] * 255),
                                        (int)(value[3] * 255))
                                );
                            }
                            ImGui.setWindowFontScale(1f);
                        }
                        case MultiStringSetting mSS -> {
                            ImGui.setWindowFontScale(0.5f);
                            ImGui.text(mSS.getName());
                            ImGui.sameLine();

                            float fullWidth = ImGui.getContentRegionAvailX();
                            float sliderWidth = 280; // Adjust width as needed
                            float rightAlignX = fullWidth - sliderWidth;

                            ImGui.setCursorPosX(ImGui.getCursorPosX() + rightAlignX);
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
                        default -> {
                            ImGui.setWindowFontScale(0.5f);
                            ImGui.text(setting.getName());
                            ImGui.setWindowFontScale(1f);
                        }
                    }
                }

                if (!ImGui.isWindowFocused(ImGuiFocusedFlags.RootAndChildWindows)) {
                    ScreenTracker.getInstance().getByClass(JelloClickScreen.class).settingRenderer = null;
                }

                ImGui.popFont();
            }
            ImGui.end();
        }
    }
}