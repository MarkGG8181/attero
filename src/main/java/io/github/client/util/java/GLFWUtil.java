package io.github.client.util.java;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.client.Attero;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45C;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;

public class GLFWUtil {
    private static final Map<String, Integer> textureCache = new HashMap<>();
    private static final Map<Integer, Boolean> lastKeyStates = new HashMap<>();

    /**
     * Util method to check if a key is being held down at this moment
     *
     * @param window The display handle
     * @param key    The glfw key code
     * @return Returns a boolean value if the specified key is held down
     */
    public static boolean isKeyDown(long window, int key) {
        return org.lwjgl.glfw.GLFW.glfwGetKey(window, key) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
    }

    public static boolean isKeyDown(int key) {
        return isKeyDown(MinecraftClient.getInstance().getWindow().getHandle(), key);
    }

    /**
     * Util method to check if a key was pressed down this frame
     *
     * @param window The display handle
     * @param key    The glfw key code
     * @return Returns a boolean value if the specified key was pressed
     */
    public static boolean isKeyPressed(long window, int key) {
        boolean current = isKeyDown(window, key);
        boolean last = lastKeyStates.getOrDefault(key, false);

        lastKeyStates.put(key, current);
        return current && !last;
    }

    public static boolean isKeyPressed(int key) {
        return isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key);
    }

    private static int createTextureFromRawPixels(ByteBuffer pixelBuffer, int width, int height, boolean pixelated) {
        int textureId = GL45C.glGenTextures();
        GL45C.glBindTexture(GL45C.GL_TEXTURE_2D, textureId);

        if (pixelated) {
            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_MIN_FILTER, GL45C.GL_NEAREST_MIPMAP_NEAREST);
            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_MAG_FILTER, GL45C.GL_NEAREST);
        } else {
            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_MIN_FILTER, GL45C.GL_LINEAR_MIPMAP_LINEAR);
            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_MAG_FILTER, GL45C.GL_LINEAR);
        }

        GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_WRAP_S, GL45C.GL_CLAMP_TO_EDGE);
        GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_WRAP_T, GL45C.GL_CLAMP_TO_EDGE);

        GL45C.glTexImage2D(GL45C.GL_TEXTURE_2D, 0, GL45C.GL_RGBA, width, height, 0, GL45C.GL_RGBA, GL45C.GL_UNSIGNED_BYTE, pixelBuffer);
        GL45C.glGenerateMipmap(GL45C.GL_TEXTURE_2D);

        if (!pixelated && GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float maxAnisotropy = GL45C.glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
            if (maxAnisotropy > 1.0f) {
                GL45C.glTexParameterf(GL45C.GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy);
            }
        }

        GL45C.glBindTexture(GL45C.GL_TEXTURE_2D, 0);
        return textureId;
    }

    private static int createTextureFromBuffer(ByteBuffer imageBuffer, MemoryStack stack) {
        IntBuffer width = stack.mallocInt(1);
        IntBuffer height = stack.mallocInt(1);
        IntBuffer channels = stack.mallocInt(1);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, width, height, channels, 4);
        STBImage.stbi_set_flip_vertically_on_load(false);

        if (image == null) {
            Attero.LOGGER.error("Failed to load image: {}", STBImage.stbi_failure_reason());
            return 0;
        }

        int textureId = createTextureFromRawPixels(image, width.get(0), height.get(0), false);

        STBImage.stbi_image_free(image);
        return textureId;
    }

    public static int loadTextureFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return 0;
        }
        if (textureCache.containsKey(imageUrl)) {
            return textureCache.get(imageUrl);
        }

        int textureId = GL45C.glGenTextures();
        textureCache.put(imageUrl, textureId);

        GL45C.glBindTexture(GL45C.GL_TEXTURE_2D, textureId);
        ByteBuffer placeholder = ByteBuffer.allocateDirect(4).putInt(0x00000000);
        placeholder.flip();
        GL45C.glTexImage2D(GL45C.GL_TEXTURE_2D, 0, GL45C.GL_RGBA, 1, 1, 0, GL45C.GL_RGBA, GL45C.GL_UNSIGNED_BYTE, placeholder);
        GL45C.glBindTexture(GL45C.GL_TEXTURE_2D, 0);

        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                ByteBuffer imageData;
                try (InputStream inputStream = url.openStream();
                     ReadableByteChannel channel = Channels.newChannel(inputStream)) {
                    imageData = readChannelToBuffer(channel);
                }

                try (MemoryStack stack = MemoryStack.stackPush()) {
                    IntBuffer width = stack.mallocInt(1);
                    IntBuffer height = stack.mallocInt(1);
                    IntBuffer channels = stack.mallocInt(1);

                    STBImage.stbi_set_flip_vertically_on_load(true);
                    ByteBuffer image = STBImage.stbi_load_from_memory(imageData, width, height, channels, 4);
                    STBImage.stbi_set_flip_vertically_on_load(false);

                    if (image == null) {
                        throw new RuntimeException("STB failed to load image: " + STBImage.stbi_failure_reason());
                    }

                    MinecraftClient.getInstance().execute(() -> {
                        try {
                            GL45C.glBindTexture(GL45C.GL_TEXTURE_2D, textureId);
                            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_MIN_FILTER, GL45C.GL_LINEAR_MIPMAP_LINEAR);
                            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_MAG_FILTER, GL45C.GL_LINEAR);
                            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_WRAP_S, GL45C.GL_CLAMP_TO_EDGE);
                            GL45C.glTexParameteri(GL45C.GL_TEXTURE_2D, GL45C.GL_TEXTURE_WRAP_T, GL45C.GL_CLAMP_TO_EDGE);
                            GL45C.glTexImage2D(GL45C.GL_TEXTURE_2D, 0, GL45C.GL_RGBA, width.get(0), height.get(0), 0, GL45C.GL_RGBA, GL45C.GL_UNSIGNED_BYTE, image);
                            GL45C.glGenerateMipmap(GL45C.GL_TEXTURE_2D);
                            if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
                                float maxAnisotropy = GL45C.glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
                                if (maxAnisotropy > 1.0f) {
                                    GL45C.glTexParameterf(GL45C.GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy);
                                }
                            }
                        } finally {
                            GL45C.glBindTexture(GL45C.GL_TEXTURE_2D, 0);
                            STBImage.stbi_image_free(image);
                        }
                    });
                }
            } catch (Exception e) {
                Attero.LOGGER.error("Failed to async load texture from URL: {}", imageUrl, e);
                MinecraftClient.getInstance().execute(() -> deleteTexture(textureId));
            }
        }, "Texture-Downloader-" + imageUrl.substring(imageUrl.lastIndexOf('/') + 1)).start();

        return textureId;
    }

    public static int loadTexture(String fullResourcePath) {
        if (textureCache.containsKey(fullResourcePath)) {
            return textureCache.get(fullResourcePath);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            InputStream inputStream = GLFWUtil.class.getResourceAsStream(fullResourcePath);
            if (inputStream == null) {
                Attero.LOGGER.error("Resource not found: {}", fullResourcePath);
                return 0;
            }

            ByteBuffer imageData;
            try (ReadableByteChannel channel = Channels.newChannel(inputStream)) {
                imageData = readChannelToBuffer(channel);
            }

            int texId = createTextureFromBuffer(imageData, stack);
            if (texId > 0) {
                textureCache.put(fullResourcePath, texId);
            }

            return texId;
        } catch (Exception e) {
            Attero.LOGGER.error("Failed to load texture: {}", fullResourcePath, e);
            return 0;
        }
    }

    private static ByteBuffer readChannelToBuffer(ReadableByteChannel channel) throws java.io.IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
        while (channel.read(buffer) != -1) {
            if (buffer.remaining() == 0) {
                ByteBuffer newBuffer = ByteBuffer.allocateDirect(buffer.capacity() * 2);
                buffer.flip();
                newBuffer.put(buffer);
                buffer = newBuffer;
            }
        }
        buffer.flip();
        return buffer;
    }

    public static void deleteTexture(int textureId) {
        if (textureId > 0) {
            GL45C.glDeleteTextures(textureId);
            textureCache.entrySet().removeIf(entry -> entry.getValue().equals(textureId));
        }
    }

    public static void clearCache() {
        for (int texId : textureCache.values()) {
            GL45C.glDeleteTextures(texId);
        }
        textureCache.clear();
    }
}