package io.github.client.module.impl.render;

import io.github.client.Attero;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.render.Render2DEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.ColorSetting;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.screen.DropdownClickScreen;
import io.github.client.screen.FrameClickScreen;
import io.github.client.imgui.ImGuiImpl;
import imgui.ImGui;

import java.awt.*;

/**
 * @author markuss
 * @since 04/05/2025
 */
@ModuleInfo(name = "Watermark", category = ModuleCategory.RENDER, description = "Draws a watermark")
public class WatermarkModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "ImGui", "ImGui", "Minecraft");
    private final BooleanSetting hideName = new BooleanSetting("Hide name", false);
    private final ColorSetting color = new ColorSetting("Color", new Color(0x26A07D));

    @Subscribe
    private void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof DropdownClickScreen || mc.currentScreen instanceof FrameClickScreen) {
            return;
        }

        switch (mode.getValue()) {
            case "ImGui" -> {
                ImGuiImpl.render(wrapper -> {
                    var username = hideName.getValue() ? "Player" : mc.getSession().getUsername();
                    var watermarkText = Attero.MOD_ID;
                    var userInfoText = "| " + username + " | " + mc.getCurrentFps() + " fps";

                    var watermarkTextSize = ImGui.calcTextSizeX(watermarkText);
                    var userInfoTextSize = ImGui.calcTextSizeX(userInfoText);

                    var x = 10;
                    var y = 10;

                    var width = watermarkTextSize + userInfoTextSize + 25;
                    var height = 30;

                    var textY = y + (height - ImGui.calcTextSize("A").y) / 2.0f;

                    wrapper.drawRoundedRect(x, y, width, height, 6f, new Color(0, 0, 0, 150));

                    wrapper.drawString(watermarkText.substring(0, 3), x + 10, textY, color.getValue());

                    wrapper.drawString(watermarkText.substring(3), x + 10 + ImGui.calcTextSizeX(watermarkText.substring(0, 3)), textY, Color.WHITE);
                    wrapper.drawString(userInfoText, x + 5 + watermarkTextSize + 10, textY, Color.WHITE);
                });
            }

            case "Minecraft" ->
                    event.context.drawText(mc.textRenderer, "att§fero", 5, 5, color.getValue().getRGB(), false);
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