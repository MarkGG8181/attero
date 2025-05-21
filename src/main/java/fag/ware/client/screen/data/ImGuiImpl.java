/*
 * This file is part of fabric-imgui-example-mod - https://github.com/FlorianMichael/fabric-imgui-example-mod
 * by FlorianMichael/EnZaXD and contributors
 */
package fag.ware.client.screen.data;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import fag.ware.client.Fagware;
import imgui.*;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImGuiImpl {
    private final static ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private final static ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();

    // Fonts
    public static ImFont inter17;
    public static ImFont inter30;
    public static ImFont sansation17;
    public static ImFont comfortaa17;
    public static ImFont arial17;

    public static void create(final long handle) throws IOException {
        ImGui.createContext();
        ImPlot.createContext();

        final ImGuiIO data = ImGui.getIO();
        data.setIniFilename(Fagware.MOD_ID + File.separator + Fagware.MOD_ID + ".ini");
        data.setFontGlobalScale(1F);

        final ImFontAtlas fonts = data.getFonts();

        // Shared glyph range (basic Latin + numbers)
        short[] defaultRanges = fonts.getGlyphRangesDefault();

        List<ImFont> interGeneratedFonts = new ArrayList<>();
        for (int i = 21; i < 50; i++) {
            ImFontConfig interConfig = new ImFontConfig();
            interConfig.setGlyphRanges(defaultRanges);
            interConfig.setName("Inter " + i + "px");
            ImFont inter = fonts.addFontFromMemoryTTF(
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Fagware.MOD_ID + "/fonts/Inter28pt_Regular.ttf"))),
                    i,
                    interConfig
            );
            ImGuiFontManager.register("Inter", inter, i);
            interGeneratedFonts.add(inter);
            interConfig.destroy();
        }

        List<ImFont> sansationGeneratedFonts = new ArrayList<>();
        for (int i = 21; i < 50; i++) {
            ImFontConfig sansationCfg = new ImFontConfig();
            sansationCfg.setGlyphRanges(defaultRanges);
            sansationCfg.setName("Sansation " + i + "px");
            ImFont sansation = fonts.addFontFromMemoryTTF(
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Fagware.MOD_ID + "/fonts/Sansation_Regular.ttf"))),
                    i,
                    sansationCfg
            );
            ImGuiFontManager.register("Sansation", sansation, i);
            sansationGeneratedFonts.add(sansation);
            sansationCfg.destroy();
        }

        List<ImFont> comfortaaGeneratedFonts = new ArrayList<>();
        for (int i = 21; i < 50; i++) {
            ImFontConfig comfortaaCfg = new ImFontConfig();
            comfortaaCfg.setGlyphRanges(defaultRanges);
            comfortaaCfg.setName("Comfortaa " + i + "px");
            ImFont comfortaa = fonts.addFontFromMemoryTTF(
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Fagware.MOD_ID + "/fonts/Comfortaa_Regular.ttf"))),
                    i,
                    comfortaaCfg
            );
            ImGuiFontManager.register("Comfortaa", comfortaa, i);
            comfortaaGeneratedFonts.add(comfortaa);
            comfortaaCfg.destroy();
        }

        List<ImFont> arialGeneratedFonts = new ArrayList<>();
        for (int i = 21; i < 50; i++) {
            ImFontConfig arialCfg = new ImFontConfig();
            arialCfg.setGlyphRanges(defaultRanges);
            arialCfg.setName("Arial " + i + "px");
            ImFont arial = fonts.addFontFromMemoryTTF(
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Fagware.MOD_ID + "/fonts/Arial_Regular.ttf"))),
                    i,
                    arialCfg
            );
            ImGuiFontManager.register("Arial", arial, i);
            arialGeneratedFonts.add(arial);
            arialCfg.destroy();
        }

        fonts.build();

        inter17 = interGeneratedFonts.get(0);
        inter30 = interGeneratedFonts.get(9);
        sansation17 = sansationGeneratedFonts.get(0);
        comfortaa17 = comfortaaGeneratedFonts.get(0);
        arial17 = arialGeneratedFonts.get(0);

        data.setConfigFlags(ImGuiConfigFlags.DockingEnable);

        imGuiImplGlfw.init(handle, true);
        imGuiImplGl3.init();
    }

    public static void draw(final RenderInterface runnable) {
        // Minecraft will not bind the framebuffer unless it is needed, so do it manually and hope Vulcan never gets real:tm:
        final Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
        final var previousFramebuffer = ((GlTexture) framebuffer.getColorAttachment()).getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getFramebufferManager(), null);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, previousFramebuffer);
        GL11.glViewport(0, 0, framebuffer.viewportWidth, framebuffer.viewportHeight);

        imGuiImplGl3.newFrame();
        imGuiImplGlfw.newFrame();
        ImGui.newFrame();

        runnable.render(ImGui.getIO());

        // end frame
        ImGui.render();
        imGuiImplGl3.renderDrawData(ImGui.getDrawData());

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, previousFramebuffer);

// Add this code if you have enabled Viewports in the create method
//        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//            final long pointer = GLFW.glfwGetCurrentContext();
//            ImGui.updatePlatformWindows();
//            ImGui.renderPlatformWindowsDefault();
//
//            GLFW.glfwMakeContextCurrent(pointer);
//        }
    }

    public static void dispose() {
        imGuiImplGl3.shutdown();

        ImGui.destroyContext();
        ImPlot.destroyContext();
    }

    public static int convertBufferedImageToTexture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4); // 4 bytes for RGBA
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
            buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
            buffer.put((byte) (pixel & 0xFF));         // Blue
            buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha (fully opaque)
        }
        buffer.flip();

        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        MemoryUtil.memFree(buffer);

        return textureID;
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