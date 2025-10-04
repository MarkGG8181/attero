package io.github.client.imgui;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImDrawFlags;
import io.github.client.util.java.math.ColorUtil;

import java.awt.*;

/**
 * @author markuss
 * @since 26th Sept 2025
 */
public class ImWrapper implements IImWrapper {
    private ImDrawList drawList = ImGui.getForegroundDrawList();
    private ImGuiIO io = ImGui.getIO();

    @Override
    public ImGuiIO getIO() {
        return io;
    }

    @Override
    public void drawRect(float x, float y, float width, float height, Color color) {
        drawList.addRectFilled(x, y, x + width, y + height, conv(color), 0.0f);
    }

    @Override
    public void drawSelectiveRect(float x, float y, float width, float height, float radius,
                                  boolean cornerUprLeft, boolean cornerUprRight,
                                  boolean cornerBotRight, boolean cornerBotLeft, Color color) {

        int flags = 0;

        if (cornerUprLeft) flags |= ImDrawFlags.RoundCornersTopLeft;
        if (cornerUprRight) flags |= ImDrawFlags.RoundCornersTopRight;
        if (cornerBotRight) flags |= ImDrawFlags.RoundCornersBottomRight;
        if (cornerBotLeft) flags |= ImDrawFlags.RoundCornersBottomLeft;

        drawList.addRectFilled(x, y, x + width, y + height, conv(color), radius, flags);
    }

    @Override
    public void drawSelectiveRect(float x, float y, float width, float height,
                                  boolean cornerUprLeft, boolean cornerUprRight,
                                  boolean cornerBotRight, boolean cornerBotLeft, Color color) {
        drawSelectiveRect(x, y, width, height, 0f,
                cornerUprLeft, cornerUprRight, cornerBotRight, cornerBotLeft, color);
    }

    @Override
    public void drawGradientRect(float x, float y, float width, float height, Color... color) {
        var colorLeft = conv(color[0]);
        var colorRight = conv(color[1]);

        drawList.addRectFilledMultiColor(x, y, x + width, y + height, colorLeft, colorRight, colorRight, colorLeft);
    }

    @Override
    public void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        drawList.addRectFilled(x, y, x + width, y + height, conv(color), radius);
    }

    @Override
    public void drawString(String text, float x, float y, Color color) {
        drawList.addText(x, y, conv(color), text);
    }

    private int conv(Color color) {
        return ColorUtil.toImGuiColor(color);
    }
}