package io.github.client.module.impl.render;

import io.github.client.Attero;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.render.Render2DEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.ColorSetting;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.screen.DropdownClickScreen;
import io.github.client.screen.JelloClickScreen;
import io.github.client.screen.FrameClickScreen;
import io.github.client.screen.data.ImGuiFontManager;
import io.github.client.screen.data.ImGuiImpl;
import io.github.client.util.java.math.ColorUtil;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

@ModuleInfo(name = "Watermark", category = ModuleCategory.RENDER, description = "Draws a watermark")
public class WatermarkModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "ImGui", "ImGui", "Minecraft", "Image");
    private final StringSetting font = new StringSetting("Font", "Inter", "Inter", "Arial", "Comfortaa", "Sansation").hide(() -> !mode.getValue().equals("ImGui"));
    private final NumberSetting fontSize = new NumberSetting("Font size", 21, 21, 30).hide(() -> !mode.is("ImGui"));
    private final BooleanSetting hideName = new BooleanSetting("Hide name", false);
    private final ColorSetting color = new ColorSetting("Color", new Color(0x26A07D));

    private ImFont currentFont = ImGuiImpl.inter17;

    private int clientLogo = -1;

    public WatermarkModule() {
        font.onChange(set -> {
            var font = ImGuiFontManager.getFont(set, fontSize.toInt());
            if (font != null) currentFont = font;
        });

        fontSize.onChange(set -> {
            var font = ImGuiFontManager.getFont(this.font.getValue(), set.intValue());
            if (font != null) currentFont = font;
        });
    }

    @Subscribe
    private void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof DropdownClickScreen || mc.currentScreen instanceof JelloClickScreen || mc.currentScreen instanceof FrameClickScreen) {
            return;
        }

        if (currentFont == null) {
            currentFont = ImGuiFontManager.getFont(font.getValue(), fontSize.toInt());
            if (currentFont == null) currentFont = ImGuiImpl.inter17; // fallback
        }

        if (clientLogo == -1) {
            try {
                clientLogo = ImGuiImpl.fromBufferedImage(ImageIO.read(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Attero.MOD_ID + "/icon.png"))));
            } catch (IOException e) {
                sendError("Failed to load client logo: " +  e.getMessage());
                Attero.LOGGER.error("Failed to load client watermark png", e);
            }
        }

        switch (mode.getValue()) {
            case "ImGui" -> ImGuiImpl.draw(io -> {
                ImGui.pushFont(currentFont);
                var drawList = ImGui.getForegroundDrawList();

                var username = hideName.getValue() ? "Player" : mc.getSession().getUsername();
                var watermarkText = Attero.MOD_ID;
                var userInfoText = "| " + username + " | " + mc.getCurrentFps() + " fps";

                var watermarkTextSize = ImGui.calcTextSize(watermarkText);
                var userInfoTextSize = ImGui.calcTextSize(userInfoText);

                var x = 10;
                var y = 10;

                var width = watermarkTextSize.x + userInfoTextSize.x + 25;
                var height = 30;

                drawList.addRectFilled(x, y, x + width, y + height, ColorUtil.toImGuiColor(new Color(0, 0, 0, 150)), 6.0f);

                var textY = y + (height - ImGui.calcTextSize("A").y) / 2.0f;

                drawList.addText(x + 10, textY, color.toImGuiColor(), watermarkText.substring(0, 3));
                drawList.addText(x + 10 + ImGui.calcTextSize(watermarkText.substring(0, 3)).x, textY, ColorUtil.toImGuiColor(Color.WHITE), watermarkText.substring(3));
                drawList.addText(x + 5 + watermarkTextSize.x + 10, textY, ColorUtil.toImGuiColor(Color.WHITE), userInfoText);

                ImGui.popFont();
            });

            case "Minecraft" -> {
                event.context.drawText(mc.textRenderer, "att§fero", 5, 5, color.getValue().getRGB(), false);
            }

            case "Image" -> ImGuiImpl.draw(io -> {
                    var drawList = ImGui.getForegroundDrawList();
                ImVec2 size = new ImVec2(256, 256);
                ImVec2 uv0= new ImVec2(0, 0); // Top-left UV
                ImVec2 uv= new ImVec2(1, 1); // Bottom-right UV
                drawList.addImage(clientLogo, size, uv0, uv);
            });
        }
    }

    public void onInit() {
        setEnabled(true);
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}