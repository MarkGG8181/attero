package io.github.client.imgui;

import imgui.ImGuiIO;

import java.awt.*;

public interface IImWrapper {
    ImGuiIO getIO();
    void drawRect(float x, float y, float width, float height, Color color);
    void drawSelectiveRect(float x, float y, float width, float height, float radius, boolean cornerUprLeft, boolean cornerlUprRight, boolean cornerlBotRight, boolean cornerlBotLeft, Color color);
    void drawSelectiveRect(float x, float y, float width, float height, boolean cornerUprLeft, boolean cornerlUprRight, boolean cornerlBotRight, boolean cornerlBotLeft, Color color);
    void drawRoundedRect(float x, float y, float width, float height, float radius, Color color);
    void drawGradientRect(float x, float y, float width, float height, Color... colors);
    void drawString(String text, float x, float y, Color color);
}