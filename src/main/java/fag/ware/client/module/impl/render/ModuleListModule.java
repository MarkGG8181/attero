package fag.ware.client.module.impl.render;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.Render2DEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.ColorSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.screen.ClickScreen;
import fag.ware.client.screen.JelloClickScreen;
import fag.ware.client.screen.data.ImGuiImpl;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;

import java.awt.*;
import java.util.List;

@ModuleInfo(name = "ModuleList", category = ModuleCategory.RENDER, description = "Draws a list of enabled modules")
public class ModuleListModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "Simple", "Simple", "Minecraft");
    private final ColorSetting textColor = new ColorSetting("Text color", new Color(0x26A07D));
    private final BooleanSetting background = new BooleanSetting("Background", true);
    private final BooleanSetting fontShadow = new BooleanSetting("Font shadow", false);
    private final NumberSetting xOffset = new NumberSetting("X offset", 5, 0, 15);
    private final NumberSetting yOffset = new NumberSetting("Y offset", 5, 0, 15);

    @Subscribe
    public void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof ClickScreen || mc.currentScreen instanceof JelloClickScreen) {
            return;
        }

        List<AbstractModule> sortedModules = Fagware.INSTANCE.moduleTracker.getSet().stream()
                .filter(AbstractModule::isEnabled)
                .sorted((a, b) -> {
                    float aWidth = ImGui.calcTextSize(a.toString()).x;
                    float bWidth = ImGui.calcTextSize(b.toString()).x;
                    return Float.compare(bWidth, aWidth);
                })
                .toList();

        switch (mode.getValue()) {
            case "Simple" -> {
                ImGuiImpl.draw(io -> {
                    ImGui.pushFont(ImGuiImpl.INTER_REGULAR_17);

                    ImDrawList drawList = ImGui.getForegroundDrawList();
                    float y = yOffset.toFloat();

                    for (AbstractModule module : sortedModules) {
                        String name = module.toString();
                        if (name == null) continue;

                        ImVec2 size = ImGui.calcTextSize(name);
                        float textWidth = size.x;
                        float x = io.getDisplaySizeX() - textWidth - xOffset.toFloat();

                        if (background.getValue()) {
                            drawList.addRectFilled(x - 4, y - 1, x + textWidth + 2, y + ImGui.getFontSize() + 1, ImColor.rgba(0, 0, 0, 150));
                        }

                        if (fontShadow.getValue()) {
                            drawList.addText(x + 1, y + 1, ImColor.rgba(0, 0, 0, 160), name);
                        }

                        drawList.addText(x, y, textColor.toImGuiColor(), name);
                        y += ImGui.getFontSize() + 2;
                    }

                    ImGui.popFont();
                });
            }
            case "Minecraft" -> {
                int y = yOffset.toInt();
                for (AbstractModule module : sortedModules) {
                    String name = module.toString();
                    int textWidth = mc.textRenderer.getWidth(name);
                    int x = event.getDrawContext().getScaledWindowWidth() - textWidth - xOffset.toInt();

                    if (background.getValue()) {
                        event.getDrawContext().fill(
                                x - 2,
                                y - 1,
                                x + textWidth + 1,
                                y + mc.textRenderer.fontHeight,
                                new Color(0, 0, 0, 150).getRGB()
                        );
                    }

                    event.getDrawContext().drawText(mc.textRenderer, name, x, y, textColor.toInt(), fontShadow.getValue());

                    y += mc.textRenderer.fontHeight + 1;
                }
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onInit() {
        setEnabled(true);
    }
}