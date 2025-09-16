package io.github.client.screen.data;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.client.Attero;
import imgui.*;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class ImGuiImpl {
    private final static ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private final static ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();

    // Fonts
    public static ImFont inter17;
    public static ImFont inter30;
    public static ImFont sansation17;
    public static ImFont comfortaa17;
    public static ImFont arial17;

    public static long correctChecksum = -1L;

    public static void create(long handle) throws IOException {
        ImGui.createContext();
        ImPlot.createContext();

        ImGuiIO data = ImGui.getIO();
        data.setIniFilename(Attero.MOD_ID + File.separator + Attero.MOD_ID + ".ini");
        data.setFontGlobalScale(1F);

        ImFontAtlas fonts = data.getFonts();

        // Shared glyph range (basic Latin + numbers)
        short[] defaultRanges = fonts.getGlyphRangesDefault();

        List<ImFont> interGeneratedFonts = new ArrayList<>();
        for (int i = 21; i < 50; i++) {
            ImFontConfig interConfig = new ImFontConfig();
            interConfig.setGlyphRanges(defaultRanges);
            interConfig.setName("Inter " + i + "px");
            ImFont inter = fonts.addFontFromMemoryTTF(
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Attero.MOD_ID + "/fonts/Inter28pt_Regular.ttf"))),
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
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Attero.MOD_ID + "/fonts/Sansation_Regular.ttf"))),
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
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Attero.MOD_ID + "/fonts/Comfortaa_Regular.ttf"))),
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
                    IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Attero.MOD_ID + "/fonts/Arial_Regular.ttf"))),
                    i,
                    arialCfg
            );
            ImGuiFontManager.register("Arial", arial, i);
            arialGeneratedFonts.add(arial);
            arialCfg.destroy();
        }

        fonts.build();

        inter17 = interGeneratedFonts.getFirst();
        inter30 = interGeneratedFonts.get(9);
        sansation17 = sansationGeneratedFonts.getFirst();
        comfortaa17 = comfortaaGeneratedFonts.getFirst();
        arial17 = arialGeneratedFonts.getFirst();

        data.setConfigFlags(ImGuiConfigFlags.DockingEnable);

        imGuiImplGlfw.init(handle, true);
        imGuiImplGl3.init();
    }

    public static void draw(IRenderInterface runnable) {
        if (MinecraftClient.getInstance().getWindow().isMinimized()) {
            return;
        }

        Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
        var previousFramebuffer = ((GlTexture) framebuffer.getColorAttachment()).getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getBufferManager(), null);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, previousFramebuffer);
        GL11.glViewport(0, 0, framebuffer.viewportWidth, framebuffer.viewportHeight);

        imGuiImplGl3.newFrame();
        imGuiImplGlfw.newFrame();
        ImGui.newFrame();

        runnable.render(ImGui.getIO());

        ImGui.render();
        imGuiImplGl3.renderDrawData(ImGui.getDrawData());

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, previousFramebuffer);
    }

    public static void dispose() {
        imGuiImplGl3.shutdown();

        ImGui.destroyContext();
        ImPlot.destroyContext();
    }

    public static int fromBufferedImage(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        int texture = GlStateManager._genTexture();

        GlStateManager._bindTexture(texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        return texture;
    }
}