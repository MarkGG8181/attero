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
import fag.ware.client.screen.PanelClickScreen;
import fag.ware.client.screen.data.ImGuiFontManager;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.util.math.ColorUtil;
import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec2;

import java.awt.*;

@ModuleInfo(name = "Watermark", category = ModuleCategory.RENDER, description = "Draws a watermark")
public class WatermarkModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "ImGui", "ImGui", "Minecraft");
    private final StringSetting font = (StringSetting) new StringSetting("Font", "Inter","Inter", "Arial", "Comfortaa").hide(() -> !mode.getValue().equals("ImGui"));
    private final NumberSetting fontSize = (NumberSetting) new NumberSetting("Font size", 21, 21, 30).hide(() -> !mode.is("ImGui"));
    private final BooleanSetting hideName = new BooleanSetting("Hide name", false);
    private final ColorSetting color = new ColorSetting("Color", new Color(0x26A07D));

    private ImFont currentFont = ImGuiImpl.inter17;

    public WatermarkModule() {
        font.onChange(set -> {
            ImFont font = ImGuiFontManager.getFont(set, fontSize.toInt());
            if (font != null) currentFont = font;
        });

        fontSize.onChange(set -> {
            ImFont font = ImGuiFontManager.getFont(this.font.getValue(), set.intValue());
            if (font != null) currentFont = font;
        });
    }

    @Subscribe
    public void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof ClickScreen || mc.currentScreen instanceof JelloClickScreen || mc.currentScreen instanceof PanelClickScreen) {
            return;
        }

        switch (mode.getValue()) {
            case "ImGui" -> ImGuiImpl.draw(io -> {
                if (currentFont == null) {
                    currentFont = ImGuiFontManager.getFont(font.getValue(), fontSize.toInt());
                    if (currentFont == null) currentFont = ImGuiImpl.inter17; // fallback
                }
                ImGui.pushFont(currentFont);
                ImDrawList drawList = ImGui.getForegroundDrawList();
                String username = hideName.getValue() ? "Player" : mc.getSession().getUsername();
                String watermarkText = "Fagware";
                String userInfoText = "| " + username + " | " + mc.getCurrentFps() + " fps";

                ImVec2 watermarkTextSize = ImGui.calcTextSize(watermarkText);
                ImVec2 userInfoTextSize = ImGui.calcTextSize(userInfoText);

                float x = 10;
                float y = 10;

                float width = watermarkTextSize.x + userInfoTextSize.x + 25;
                float height = 30;

                drawList.addRectFilled(x, y, x + width, y + height, ColorUtil.toImGuiColor(new Color(0, 0, 0, 150)), 6.0f);

                float textY = y + (height - ImGui.calcTextSize("A").y) / 2.0f;

                drawList.addText(x + 10, textY, color.toImGuiColor(), watermarkText.substring(0, 3));
                drawList.addText(x + 10 + ImGui.calcTextSize(watermarkText.substring(0, 3)).x, textY, ColorUtil.toImGuiColor(Color.WHITE), watermarkText.substring(3));
                drawList.addText(x + 5 + watermarkTextSize.x + 10, textY, ColorUtil.toImGuiColor(Color.WHITE), userInfoText);

                ImGui.popFont();
            });
            case "Minecraft" -> {
                event.getDrawContext().drawText(mc.textRenderer, "fag", 5, 5, color.getValue().getRGB(), false);
                event.getDrawContext().drawText(mc.textRenderer, "ware", 5 + mc.textRenderer.getWidth("fag"), 5, -1, false);
            }
        }
    }

    @Override
    public void onInit() {
        setEnabled(true);
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}