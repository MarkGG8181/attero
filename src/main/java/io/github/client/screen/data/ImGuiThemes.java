package io.github.client.screen.data;

import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;

public class ImGuiThemes {
    public static void applyWhiteTheme() {
        ImGuiStyle style = ImGui.getStyle();

        style.setWindowBorderSize(0.0f);

        style.setWindowRounding(7.0f);
        style.setChildRounding(7.0f);
        style.setFrameRounding(7.0f);
        style.setScrollbarRounding(16.0f);
        style.setTabRounding(4.0f);
        style.setPopupRounding(7.0f);

        style.setColor(ImGuiCol.Text, 0.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.TextDisabled, 0.60f, 0.60f, 0.60f, 1.00f);
        style.setColor(ImGuiCol.WindowBg, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.ChildBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.PopupBg, 1.00f, 1.00f, 1.00f, 0.98f);
        style.setColor(ImGuiCol.Border, 0.70f, 0.70f, 0.70f, 1.00f);
        style.setColor(ImGuiCol.BorderShadow, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.FrameBg, 0.90f, 0.90f, 0.90f, 1.00f);
        style.setColor(ImGuiCol.FrameBgHovered, 0.85f, 0.85f, 0.85f, 1.00f);
        style.setColor(ImGuiCol.FrameBgActive, 0.26f, 0.59f, 0.98f, 0.67f);
        style.setColor(ImGuiCol.TitleBg, 0.90f, 0.90f, 0.90f, 1.00f);
        style.setColor(ImGuiCol.TitleBgActive, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.TitleBgCollapsed, 0.90f, 0.90f, 0.90f, 1.00f);
        style.setColor(ImGuiCol.MenuBarBg, 0.86f, 0.86f, 0.86f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarBg, 0.98f, 0.98f, 0.98f, 0.53f);
        style.setColor(ImGuiCol.ScrollbarGrab, 0.69f, 0.69f, 0.69f, 0.80f);
        style.setColor(ImGuiCol.ScrollbarGrabHovered, 0.49f, 0.49f, 0.49f, 0.80f);
        style.setColor(ImGuiCol.ScrollbarGrabActive, 0.49f, 0.49f, 0.49f, 1.00f);
        style.setColor(ImGuiCol.CheckMark, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(ImGuiCol.SliderGrab, 0.26f, 0.59f, 0.98f, 0.78f);
        style.setColor(ImGuiCol.SliderGrabActive, 0.46f, 0.54f, 0.80f, 0.60f);
        style.setColor(ImGuiCol.Button, 0.26f, 0.59f, 0.98f, 0.40f);
        style.setColor(ImGuiCol.ButtonHovered, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(ImGuiCol.ButtonActive, 0.06f, 0.53f, 0.98f, 1.00f);
        style.setColor(ImGuiCol.Header, 0.26f, 0.59f, 0.98f, 0.31f);
        style.setColor(ImGuiCol.HeaderHovered, 0.26f, 0.59f, 0.98f, 0.80f);
        style.setColor(ImGuiCol.HeaderActive, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(ImGuiCol.Separator, 0.39f, 0.39f, 0.39f, 0.62f);
        style.setColor(ImGuiCol.SeparatorHovered, 0.14f, 0.44f, 0.80f, 0.78f);
        style.setColor(ImGuiCol.SeparatorActive, 0.14f, 0.44f, 0.80f, 1.00f);
        style.setColor(ImGuiCol.ResizeGrip, 0.35f, 0.35f, 0.35f, 0.17f);
        style.setColor(ImGuiCol.ResizeGripHovered, 0.26f, 0.59f, 0.98f, 0.67f);
        style.setColor(ImGuiCol.ResizeGripActive, 0.26f, 0.59f, 0.98f, 0.95f);
        style.setColor(ImGuiCol.Tab, 0.76f, 0.80f, 0.84f, 0.93f);
        style.setColor(ImGuiCol.TabHovered, 0.26f, 0.59f, 0.98f, 0.80f);
        style.setColor(ImGuiCol.TabActive, 0.60f, 0.73f, 0.88f, 1.00f);
        style.setColor(ImGuiCol.TabUnfocused, 0.92f, 0.93f, 0.94f, 0.99f);
        style.setColor(ImGuiCol.TabUnfocusedActive, 0.74f, 0.82f, 0.91f, 1.00f);
        style.setColor(ImGuiCol.DockingPreview, 0.26f, 0.59f, 0.98f, 0.22f);
        style.setColor(ImGuiCol.DockingEmptyBg, 0.20f, 0.20f, 0.20f, 1.00f);
        style.setColor(ImGuiCol.PlotLines, 0.39f, 0.39f, 0.39f, 1.00f);
        style.setColor(ImGuiCol.PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogramHovered, 1.00f, 0.45f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.TableHeaderBg, 0.78f, 0.87f, 0.98f, 1.00f);
        style.setColor(ImGuiCol.TableBorderStrong, 0.57f, 0.57f, 0.64f, 1.00f);
        style.setColor(ImGuiCol.TableBorderLight, 0.68f, 0.68f, 0.74f, 1.00f);
        style.setColor(ImGuiCol.TableRowBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.TableRowBgAlt, 0.30f, 0.30f, 0.30f, 0.09f);
        style.setColor(ImGuiCol.TextSelectedBg, 0.26f, 0.59f, 0.98f, 0.35f);
        style.setColor(ImGuiCol.DragDropTarget, 0.26f, 0.59f, 0.98f, 0.95f);
        style.setColor(ImGuiCol.NavHighlight, 0.26f, 0.59f, 0.98f, 0.80f);
        style.setColor(ImGuiCol.NavWindowingHighlight, 0.70f, 0.70f, 0.70f, 0.70f);
        style.setColor(ImGuiCol.NavWindowingDimBg, 0.20f, 0.20f, 0.20f, 0.20f);
        style.setColor(ImGuiCol.ModalWindowDimBg, 0.20f, 0.20f, 0.20f, 0.35f);
    }

    public static void applyMarineTheme() {
        ImGuiStyle style = ImGui.getStyle();

        style.setWindowBorderSize(0.0f);

        style.setWindowRounding(6.0f);
        style.setChildRounding(6.0f);
        style.setFrameRounding(6.0f);
        style.setScrollbarRounding(16.0f);
        style.setTabRounding(6.0f);
        style.setPopupRounding(6.0f);
        style.setGrabRounding(4.0f);

        ImVec4[] colors = style.getColors();
        colors[ImGuiCol.Text] = new ImVec4(1.00f, 1.00f, 1.00f, 1.00f);
        colors[ImGuiCol.TextDisabled] = new ImVec4(0.50f, 0.50f, 0.50f, 1.00f);
        colors[ImGuiCol.WindowBg] = new ImVec4(0.06f, 0.06f, 0.06f, 0.94f);
        colors[ImGuiCol.ChildBg] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[ImGuiCol.PopupBg] = new ImVec4(0.08f, 0.08f, 0.08f, 0.94f);
        colors[ImGuiCol.Border] = new ImVec4(0.43f, 0.43f, 0.50f, 0.50f);
        colors[ImGuiCol.BorderShadow] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[ImGuiCol.FrameBg] = new ImVec4(0.10f, 0.45f, 0.35f, 1.00f);
        colors[ImGuiCol.FrameBgHovered] = new ImVec4(0.15f, 0.63f, 0.49f, 1.00f);
        colors[ImGuiCol.FrameBgActive] = new ImVec4(0.15f, 0.63f, 0.49f, 1.00f);
        colors[ImGuiCol.TitleBg] = new ImVec4(0.15f, 0.63f, 0.49f, 1.00f);
        colors[ImGuiCol.TitleBgActive] = new ImVec4(0.11f, 0.53f, 0.41f, 1.00f);
        colors[ImGuiCol.TitleBgCollapsed] = new ImVec4(0.00f, 0.00f, 0.00f, 0.51f);
        colors[ImGuiCol.MenuBarBg] = new ImVec4(0.14f, 0.14f, 0.14f, 1.00f);
        colors[ImGuiCol.ScrollbarBg] = new ImVec4(0.02f, 0.02f, 0.02f, 0.53f);
        colors[ImGuiCol.ScrollbarGrab] = new ImVec4(0.31f, 0.31f, 0.31f, 1.00f);
        colors[ImGuiCol.ScrollbarGrabHovered] = new ImVec4(0.41f, 0.41f, 0.41f, 1.00f);
        colors[ImGuiCol.ScrollbarGrabActive] = new ImVec4(0.51f, 0.51f, 0.51f, 1.00f);
        colors[ImGuiCol.CheckMark] = new ImVec4(0.05f, 0.88f, 0.64f, 1.00f);
        colors[ImGuiCol.SliderGrab] = new ImVec4(0.18f, 0.82f, 0.64f, 1.00f);
        colors[ImGuiCol.SliderGrabActive] = new ImVec4(0.19f, 0.93f, 0.72f, 1.00f);
        colors[ImGuiCol.Button] = new ImVec4(0.14f, 0.58f, 0.46f, 1.00f);
        colors[ImGuiCol.ButtonHovered] = new ImVec4(0.10f, 0.43f, 0.34f, 1.00f);
        colors[ImGuiCol.ButtonActive] = new ImVec4(0.15f, 0.63f, 0.49f, 1.00f);
        colors[ImGuiCol.Header] = new ImVec4(0.12f, 0.53f, 0.42f, 1.00f);
        colors[ImGuiCol.HeaderHovered] = new ImVec4(0.13f, 0.61f, 0.47f, 1.00f);
        colors[ImGuiCol.HeaderActive] = new ImVec4(0.14f, 0.70f, 0.54f, 1.00f);
        colors[ImGuiCol.Separator] = new ImVec4(0.49f, 0.49f, 0.49f, 0.50f);
        colors[ImGuiCol.SeparatorHovered] = new ImVec4(0.12f, 0.53f, 0.42f, 1.00f);
        colors[ImGuiCol.SeparatorActive] = new ImVec4(0.13f, 0.61f, 0.47f, 1.00f);
        colors[ImGuiCol.ResizeGrip] = new ImVec4(0.14f, 0.70f, 0.54f, 1.00f);
        colors[ImGuiCol.ResizeGripHovered] = new ImVec4(0.16f, 0.78f, 0.60f, 1.00f);
        colors[ImGuiCol.ResizeGripActive] = new ImVec4(0.17f, 0.87f, 0.67f, 1.00f);
        colors[ImGuiCol.Tab] = new ImVec4(0.14f, 0.63f, 0.49f, 1.00f);
        colors[ImGuiCol.TabHovered] = new ImVec4(0.12f, 0.57f, 0.44f, 1.00f);
        colors[ImGuiCol.TabActive] = new ImVec4(0.17f, 0.84f, 0.65f, 1.00f);
        colors[ImGuiCol.TabUnfocused] = new ImVec4(0.03f, 0.11f, 0.09f, 1.00f);
        colors[ImGuiCol.TabUnfocusedActive] = new ImVec4(0.12f, 0.57f, 0.45f, 1.00f);
        colors[ImGuiCol.DockingPreview] = new ImVec4(0.14f, 0.63f, 0.49f, 1.00f);
        colors[ImGuiCol.DockingEmptyBg] = new ImVec4(0.20f, 0.20f, 0.20f, 1.00f);
        colors[ImGuiCol.PlotLines] = new ImVec4(0.61f, 0.61f, 0.61f, 1.00f);
        colors[ImGuiCol.PlotLinesHovered] = new ImVec4(1.00f, 0.43f, 0.35f, 1.00f);
        colors[ImGuiCol.PlotHistogram] = new ImVec4(0.90f, 0.70f, 0.00f, 1.00f);
        colors[ImGuiCol.PlotHistogramHovered] = new ImVec4(1.00f, 0.60f, 0.00f, 1.00f);
        colors[ImGuiCol.TableHeaderBg] = new ImVec4(0.19f, 0.19f, 0.20f, 1.00f);
        colors[ImGuiCol.TableBorderStrong] = new ImVec4(0.31f, 0.31f, 0.35f, 1.00f);
        colors[ImGuiCol.TableBorderLight] = new ImVec4(0.23f, 0.23f, 0.25f, 1.00f);
        colors[ImGuiCol.TableRowBg] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[ImGuiCol.TableRowBgAlt] = new ImVec4(1.00f, 1.00f, 1.00f, 0.06f);
        colors[ImGuiCol.TextSelectedBg] = new ImVec4(0.13f, 0.61f, 0.47f, 1.00f);
        colors[ImGuiCol.DragDropTarget] = new ImVec4(1.00f, 1.00f, 0.00f, 0.90f);
        colors[ImGuiCol.NavHighlight] = new ImVec4(0.13f, 0.61f, 0.47f, 1.00f);
        colors[ImGuiCol.NavWindowingHighlight] = new ImVec4(1.00f, 1.00f, 1.00f, 0.70f);
        colors[ImGuiCol.NavWindowingDimBg] = new ImVec4(0.80f, 0.80f, 0.80f, 0.20f);
        colors[ImGuiCol.ModalWindowDimBg] = new ImVec4(0.80f, 0.80f, 0.80f, 0.35f);
        style.setColors(colors);
    }

    public static void applyDarkTheme() {
        ImGuiStyle style = ImGui.getStyle();

        style.setWindowBorderSize(0.0f);

        style.setWindowRounding(6.0f);
        style.setChildRounding(6.0f);
        style.setFrameRounding(6.0f);
        style.setScrollbarRounding(16.0f);
        style.setTabRounding(6.0f);
        style.setPopupRounding(6.0f);
        style.setGrabRounding(4.0f);

        ImVec4[] colors = style.getColors();

        colors[ImGuiCol.Text] = new ImVec4(1.00f, 1.00f, 1.00f, 1.00f);
        colors[ImGuiCol.TextDisabled] = new ImVec4(0.50f, 0.50f, 0.50f, 1.00f);
        colors[ImGuiCol.WindowBg] = new ImVec4(0.06f, 0.06f, 0.06f, 0.94f);
        colors[ImGuiCol.ChildBg] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[ImGuiCol.PopupBg] = new ImVec4(0.08f, 0.08f, 0.08f, 0.94f);
        colors[ImGuiCol.Border] = new ImVec4(0.43f, 0.43f, 0.50f, 0.50f);
        colors[ImGuiCol.BorderShadow] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[ImGuiCol.FrameBg] = new ImVec4(0.16f, 0.29f, 0.48f, 0.54f);
        colors[ImGuiCol.FrameBgHovered] = new ImVec4(0.26f, 0.59f, 0.98f, 0.40f);
        colors[ImGuiCol.FrameBgActive] = new ImVec4(0.26f, 0.59f, 0.98f, 0.67f);
        colors[ImGuiCol.TitleBg] = new ImVec4(0.04f, 0.04f, 0.04f, 1.00f);
        colors[ImGuiCol.TitleBgActive] = new ImVec4(0.16f, 0.29f, 0.48f, 1.00f);
        colors[ImGuiCol.TitleBgCollapsed] = new ImVec4(0.00f, 0.00f, 0.00f, 0.51f);
        colors[ImGuiCol.MenuBarBg] = new ImVec4(0.14f, 0.14f, 0.14f, 1.00f);
        colors[ImGuiCol.ScrollbarBg] = new ImVec4(0.02f, 0.02f, 0.02f, 0.53f);
        colors[ImGuiCol.ScrollbarGrab] = new ImVec4(0.31f, 0.31f, 0.31f, 1.00f);
        colors[ImGuiCol.ScrollbarGrabHovered] = new ImVec4(0.41f, 0.41f, 0.41f, 1.00f);
        colors[ImGuiCol.ScrollbarGrabActive] = new ImVec4(0.51f, 0.51f, 0.51f, 1.00f);
        colors[ImGuiCol.CheckMark] = new ImVec4(0.26f, 0.59f, 0.98f, 1.00f);
        colors[ImGuiCol.SliderGrab] = new ImVec4(0.24f, 0.52f, 0.88f, 1.00f);
        colors[ImGuiCol.SliderGrabActive] = new ImVec4(0.26f, 0.59f, 0.98f, 1.00f);
        colors[ImGuiCol.Button] = new ImVec4(0.26f, 0.59f, 0.98f, 0.40f);
        colors[ImGuiCol.ButtonHovered] = new ImVec4(0.26f, 0.59f, 0.98f, 1.00f);
        colors[ImGuiCol.ButtonActive] = new ImVec4(0.06f, 0.53f, 0.98f, 1.00f);
        colors[ImGuiCol.Header] = new ImVec4(0.26f, 0.59f, 0.98f, 0.31f);
        colors[ImGuiCol.HeaderHovered] = new ImVec4(0.26f, 0.59f, 0.98f, 0.80f);
        colors[ImGuiCol.HeaderActive] = new ImVec4(0.26f, 0.59f, 0.98f, 1.00f);
        colors[ImGuiCol.Separator] = new ImVec4(0.43f, 0.43f, 0.50f, 0.50f);
        colors[ImGuiCol.SeparatorHovered] = new ImVec4(0.10f, 0.40f, 0.75f, 0.78f);
        colors[ImGuiCol.SeparatorActive] = new ImVec4(0.10f, 0.40f, 0.75f, 1.00f);
        colors[ImGuiCol.ResizeGrip] = new ImVec4(0.26f, 0.59f, 0.98f, 0.20f);
        colors[ImGuiCol.ResizeGripHovered] = new ImVec4(0.26f, 0.59f, 0.98f, 0.67f);
        colors[ImGuiCol.ResizeGripActive] = new ImVec4(0.26f, 0.59f, 0.98f, 0.95f);
        colors[ImGuiCol.Tab] = new ImVec4(0.18f, 0.35f, 0.58f, 0.86f);
        colors[ImGuiCol.TabHovered] = new ImVec4(0.26f, 0.59f, 0.98f, 0.80f);
        colors[ImGuiCol.TabActive] = new ImVec4(0.20f, 0.41f, 0.68f, 1.00f);
        colors[ImGuiCol.TabUnfocused] = new ImVec4(0.07f, 0.10f, 0.15f, 0.97f);
        colors[ImGuiCol.TabUnfocusedActive] = new ImVec4(0.14f, 0.26f, 0.42f, 1.00f);
        colors[ImGuiCol.DockingPreview] = new ImVec4(0.26f, 0.59f, 0.98f, 0.70f);
        colors[ImGuiCol.DockingEmptyBg] = new ImVec4(0.20f, 0.20f, 0.20f, 1.00f);
        colors[ImGuiCol.PlotLines] = new ImVec4(0.61f, 0.61f, 0.61f, 1.00f);
        colors[ImGuiCol.PlotLinesHovered] = new ImVec4(1.00f, 0.43f, 0.35f, 1.00f);
        colors[ImGuiCol.PlotHistogram] = new ImVec4(0.90f, 0.70f, 0.00f, 1.00f);
        colors[ImGuiCol.PlotHistogramHovered] = new ImVec4(1.00f, 0.60f, 0.00f, 1.00f);
        colors[ImGuiCol.TableHeaderBg] = new ImVec4(0.19f, 0.19f, 0.20f, 1.00f);
        colors[ImGuiCol.TableBorderStrong] = new ImVec4(0.31f, 0.31f, 0.35f, 1.00f);
        colors[ImGuiCol.TableBorderLight] = new ImVec4(0.23f, 0.23f, 0.25f, 1.00f);
        colors[ImGuiCol.TableRowBg] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[ImGuiCol.TableRowBgAlt] = new ImVec4(1.00f, 1.00f, 1.00f, 0.06f);
        colors[ImGuiCol.TextSelectedBg] = new ImVec4(0.26f, 0.59f, 0.98f, 0.35f);
        colors[ImGuiCol.DragDropTarget] = new ImVec4(1.00f, 1.00f, 0.00f, 0.90f);
        colors[ImGuiCol.NavHighlight] = new ImVec4(0.26f, 0.59f, 0.98f, 1.00f);
        colors[ImGuiCol.NavWindowingHighlight] = new ImVec4(1.00f, 1.00f, 1.00f, 0.70f);
        colors[ImGuiCol.NavWindowingDimBg] = new ImVec4(0.80f, 0.80f, 0.80f, 0.20f);
        colors[ImGuiCol.ModalWindowDimBg] = new ImVec4(0.80f, 0.80f, 0.80f, 0.35f);
        style.setColors(colors);
    }
}
