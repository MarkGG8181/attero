package fag.ware.client.screen;

import fag.ware.client.Fagware;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.setting.Setting;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.ColorSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.imgui.ImGuiImpl;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
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
        super(Text.of("ClickScreen"));
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
            ImGuiImpl.applyTheme();

            for (ModuleCategory category : ModuleCategory.values()) {
                ImVec2 position = positions.get(category);

                ImGui.pushFont(ImGuiImpl.defaultFont);
                ImGui.setNextWindowPos(position, ImGuiCond.Once);
                ImGui.setNextWindowSize(size);

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
                                if (setting instanceof NumberSetting nS) {
                                    float[] value = {nS.toFloat()};

                                    if (ImGui.sliderFloat(nS.getName(), value, nS.getMin().floatValue(), nS.getMax().floatValue())) {
                                        nS.setValue(value[0]);
                                    }
                                } else if (setting instanceof StringSetting sS) {
                                    ImInt index = new ImInt(sS.getIndex());

                                    if (ImGui.combo(sS.getName(), index, sS.getValues())) {
                                        sS.setIndex(index.get());
                                    }
                                } else if (setting instanceof BooleanSetting bS) {
                                    ImBoolean value = new ImBoolean(bS.getValue());

                                    if (ImGui.checkbox(setting.getName(), value)) {
                                        bS.setValue(value.get());
                                    }
                                } else if (setting instanceof ColorSetting cS) {
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

                                    ImGui.text(cS.getName());
                                    ImGui.sameLine();
                                    if (ImGui.colorPicker4("##" + cS.getName(), value, flags)) {
                                        int r = clamp(Math.round(value[0] * 255), 0, 255);
                                        int g = clamp(Math.round(value[1] * 255), 0, 255);
                                        int b = clamp(Math.round(value[2] * 255), 0, 255);
                                        int a = clamp(Math.round(value[3] * 255), 0, 255);
                                        cS.setValue(new Color(r, g, b, a));
                                    }
                                }
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

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
