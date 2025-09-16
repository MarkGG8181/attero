package io.github.client.imgui;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import imgui.*;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import io.github.client.Attero;
import io.github.client.util.java.FileUtil;
import io.github.client.util.interfaces.IRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author <a href="https://github.com/FlorianMichael/fabric-imgui-example-mod">fabric imgui example mod</a>
 */
public class ImGuiImpl {
    private final static ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private final static ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();

    public static ImFont mainFont;

    public static void create(long handle) throws IOException {
        ImGui.createContext();
        ImPlot.createContext();

        final ImGuiIO io = ImGui.getIO();
        final ImFontAtlas atlas = io.getFonts();
        io.setIniFilename(FileUtil.CLIENT_DIR.resolve("imgui.ini").toString());
        io.setFontGlobalScale(1F);

        {
            final ImFontAtlas fonts = io.getFonts();
            final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();

            rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
            rangesBuilder.addRanges(io.getFonts().getGlyphRangesCyrillic());
            rangesBuilder.addRanges(io.getFonts().getGlyphRangesJapanese());

            final short[] glyphRanges = rangesBuilder.buildRanges();

            final ImFontConfig basicConfig = new ImFontConfig();
            basicConfig.setGlyphRanges(io.getFonts().getGlyphRangesCyrillic());

            List<ImFont> generatedFonts = new ArrayList<>();
            for (int i = 5 /* MINIMUM_FONT_SIZE */; i <= 50 /* MAXIMUM_FONT_SIZE */; i++) {
                basicConfig.setName("Inter Regular " + i + "px");
                generatedFonts.add(fonts.addFontFromMemoryTTF(IOUtils.toByteArray(Objects.requireNonNull(ImGuiImpl.class.getResourceAsStream("/assets/" + Attero.MOD_ID + "/fonts/Inter_Regular.ttf"))), i, basicConfig, glyphRanges));
            }
            fonts.build();
            basicConfig.destroy();

            mainFont = generatedFonts.get(17);
            io.setFontDefault(mainFont); //bypasses the mainframe so we can inject into the cpu
        }

        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);

        imGuiImplGlfw.init(handle, true);
        imGuiImplGl3.init();
    }

    public static void draw(final IRender renderInterface) {
        // Minecraft will not bind the framebuffer unless it is needed, so do it manually and hope Vulcan never gets real:tm:
        final Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
        final int previousFramebuffer = ((GlTexture) framebuffer.getColorAttachment()).getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getBufferManager(), null);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, previousFramebuffer);
        GL11.glViewport(0, 0, framebuffer.viewportWidth, framebuffer.viewportHeight);

        // start frame
        imGuiImplGl3.newFrame();
        imGuiImplGlfw.newFrame(); // Handle keyboard and mouse interactions
        ImGui.newFrame();

        // do rendering logic
        renderInterface.render(ImGui.getIO());

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
        imGuiImplGlfw.shutdown();

        ImPlot.destroyContext();
        ImGui.destroyContext();
    }
}