package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.Render2DEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.ColorSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.screen.ClickScreen;
import fag.ware.client.screen.JelloClickScreen;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.util.math.ColorUtil;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;

import java.awt.*;

@ModuleInfo(name = "Watermark", category = ModuleCategory.RENDER, description = "Draws a watermark")
public class WatermarkModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "Rounded", "Rounded", "Minecraft");
    private final ColorSetting color = new ColorSetting("Color", new Color(0x26A07D));

    @Subscribe
    public void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof ClickScreen || mc.currentScreen instanceof JelloClickScreen) {
            return;
        }

        switch (mode.getValue()) {
            case "Rounded" -> {
                ImGuiImpl.draw(io -> {
                    ImGui.pushFont(ImGuiImpl.INTER_REGULAR_17);
                    ImDrawList drawList = ImGui.getForegroundDrawList();

                    String watermarkText = "Fagware";
                    String userInfoText = "| " + mc.getSession().getUsername() + " | " + mc.getCurrentFps() + " fps";

                    ImVec2 watermarkTextSize = ImGui.calcTextSize(watermarkText);
                    ImVec2 userInfoTextSize = ImGui.calcTextSize(userInfoText);

                    float x = 10;
                    float y = 10;

                    float width = watermarkTextSize.x + userInfoTextSize.x + 25;
                    float height = 30;

                    drawList.addRectFilled(x, y, x + width, y + height, ColorUtil.toImGuiColor(new Color(0, 0, 0, 150)), 6.0f);

                    drawList.addText(x + 10, y + 4, color.toImGuiColor(), watermarkText.substring(0, 3));
                    drawList.addText(x + 10 + ImGui.calcTextSize(watermarkText.substring(0, 3)).x, y + 4, ColorUtil.toImGuiColor(Color.WHITE), watermarkText.substring(3));

                    drawList.addText(x + 5 + watermarkTextSize.x + 10, y + 4, ColorUtil.toImGuiColor(Color.WHITE), userInfoText);

                    ImGui.popFont();
                });
            }
            case "Minecraft" -> {
                event.getDrawContext().drawText(mc.textRenderer, "fag", 5, 5, color.getValue().getRGB(), false);
                event.getDrawContext().drawText(mc.textRenderer, "ware", 5 + mc.textRenderer.getWidth("fag"), 5, -1, false);
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