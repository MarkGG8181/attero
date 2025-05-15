package fag.ware.client.module.impl.render;

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
import fag.ware.client.tracker.impl.ModuleTracker;
import fag.ware.client.util.player.EntityUtil;
import imgui.ImColor;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@ModuleInfo(name = "ModuleList", category = ModuleCategory.RENDER, description = "Draws a list of enabled modules")
public class ModuleListModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "Simple", "Simple", "Minecraft");
    private final ColorSetting textColor = new ColorSetting("Text color", new Color(0x26A07D));
    private final BooleanSetting background = new BooleanSetting("Background", true);
    private final BooleanSetting fontShadow = new BooleanSetting("Font shadow", false);
    private final NumberSetting xOffset = new NumberSetting("X offset", 5, 0, 15);
    private final NumberSetting yOffset = new NumberSetting("Y offset", 5, 0, 15);

    @Override
    public void onInit() {
        this.setEnabled(true);
    }

    @Subscribe
    public void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof ClickScreen || mc.currentScreen instanceof JelloClickScreen) {
            return;
        }

        final List<AbstractModule> modules = new LinkedList<>();
        for (final AbstractModule module : ModuleTracker.getInstance().toList()) {
            if (module.isEnabled()) {
                modules.add(module);
            }
        }

        switch (mode.getValue()) {
            case "Simple" -> ImGuiImpl.draw(io -> {
                ImGui.pushFont(ImGuiImpl.INTER_REGULAR_17);
                ImDrawList drawList = ImGui.getForegroundDrawList();

                modules.sort(Comparator.comparingDouble(module -> -ImGui.calcTextSize(module.getInfo().name()).x));

                float y = yOffset.toFloat() + (!EntityUtil.hasVisiblePotionEffects(mc.player) ? 0 : 54);
                for (AbstractModule module : modules) {
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
            case "Minecraft" -> {
                modules.sort(Comparator.comparingDouble(module -> -mc.textRenderer.getWidth(module.getInfo().name())));

                float y = yOffset.toFloat() + (!EntityUtil.hasVisiblePotionEffects(mc.player) ? 0 : 54);
                for (AbstractModule module : modules) {
                    String name = module.toString();
                    float textWidth = mc.textRenderer.getWidth(name);
                    float x = event.getDrawContext().getScaledWindowWidth() - textWidth - xOffset.toInt();

                    if (background.getValue()) {
                        event.getDrawContext().fill(
                                (int) (x - 2),
                                (int) (y - 1),
                                (int) (x + textWidth + 1),
                                (int) (y + mc.textRenderer.fontHeight),
                                new Color(0, 0, 0, 150).getRGB()
                        );
                    }
                    event.getDrawContext().drawText(mc.textRenderer, name, (int) x, (int) y, textColor.toInt(), fontShadow.getValue());
                    y += mc.textRenderer.fontHeight + 1;
                }
            }
        }
    }
}