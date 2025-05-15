package fag.ware.client.util;

import net.minecraft.client.MinecraftClient;

import java.util.HashMap;
import java.util.Map;

public class GLFWUtil {
    private static final Map<Integer, Boolean> lastKeyStates = new HashMap<>();

    /**
     * Util method to check if a key is being held down at this moment
     * @param window The display handle
     * @param key The glfw key code
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
     * @param window The display handle
     * @param key The glfw key code
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
}