package io.github.client.screen.data;

import imgui.ImFont;

import java.util.*;

/**
 * @author mark
 */
public class ImGuiFontManager {
    private static final Map<String, List<ImFont>> fontRegistry = new HashMap<>();

    public static void register(String name, ImFont font, int size) {
        fontRegistry.computeIfAbsent(name.toLowerCase(), k -> new ArrayList<>()).add(font);
        fontRegistry.get(name.toLowerCase()).sort(Comparator.comparingInt(f -> (int) f.getFontSize()));
    }

    public static ImFont getFont(String name, int size) {
        List<ImFont> fonts = fontRegistry.get(name.toLowerCase());
        if (fonts == null || fonts.isEmpty()) return null;

        // Return closest font by size
        return fonts.stream()
                .min(Comparator.comparingDouble(f -> Math.abs(f.getFontSize() - size)))
                .orElse(fonts.getFirst());
    }
}
