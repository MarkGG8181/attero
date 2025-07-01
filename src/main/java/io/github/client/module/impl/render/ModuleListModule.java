package io.github.client.module.impl.render;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.render.Render2DEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.ColorSetting;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.screen.DropdownClickScreen;
import io.github.client.screen.FrameClickScreen;
import io.github.client.screen.data.ImGuiFontManager;
import io.github.client.screen.data.ImGuiImpl;
import io.github.client.tracker.impl.ModuleTracker;
import io.github.client.util.game.EntityUtil;
import imgui.*;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;

@SuppressWarnings("ALL")
@ModuleInfo(name = "ModuleList", category = ModuleCategory.RENDER, description = "Draws a list of enabled modules")
public class ModuleListModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "ImGui", "ImGui", "Minecraft");
    private final StringSetting font = (StringSetting) new StringSetting("Font", "Inter", "Inter", "Arial", "Comfortaa", "Sansation").hide(() -> !mode.getValue().equals("ImGui"));
    private final NumberSetting fontSize = (NumberSetting) new NumberSetting("Font size", 21, 21, 30).hide(() -> !mode.is("ImGui"));
    private final ColorSetting textColor = new ColorSetting("Text color", new Color(0x26A07D));
    private final BooleanSetting background = new BooleanSetting("Background", true);
    private final BooleanSetting fontShadow = new BooleanSetting("Font shadow", false);
    private final BooleanSetting suffixes = new BooleanSetting("Suffixes", false);
    private final StringSetting suffixChar = (StringSetting) new StringSetting("Suffix char", " - ", " - ", " ", " # ", " $ ", " @ ", " % ", " & ", " = ", " : ", " :: ", " ; ", " ;; ").hide(() -> !suffixes.getValue());
    private final StringSetting suffixThing = (StringSetting) new StringSetting("Suffix thing", "none", "none", "[]", "()", "{}").hide(() -> !suffixes.getValue());
    private final ColorSetting suffixColor = (ColorSetting) new ColorSetting("Suffix color", new Color(0xFFFFFF)).hide(() -> !suffixes.getValue());
    private final NumberSetting xOffset = new NumberSetting("X offset", 5, 0, 15);
    private final NumberSetting yOffset = new NumberSetting("Y offset", 5, 0, 15);
    private final BooleanSetting animate = new BooleanSetting("Animate", true);

    private ImFont currentFont = ImGuiImpl.inter17;

    public ModuleListModule() {
        font.onChange(set -> {
            var font = ImGuiFontManager.getFont(set, fontSize.toInt());
            if (font != null) currentFont = font;
        });

        fontSize.onChange(set -> {
            var font = ImGuiFontManager.getFont(this.font.getValue(), set.intValue());
            if (font != null) currentFont = font;
        });
    }

    @Subscribe
    private void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof DropdownClickScreen || mc.currentScreen instanceof FrameClickScreen) {
            return;
        }

        final var shouldAnimate = animate.toBoolean();

        final var modules = new LinkedList<AbstractModule>();
        for (final var module : ModuleTracker.INSTANCE.toList()) {
            if (module.isEnabled()) {
                modules.add(module);
                module.getX().update(100);
            } else {
                module.getX().update(0);
                module.getY().update(0);

                if (!module.finishedAnimating()) {
                    modules.add(module);
                }
            }
        }

        switch (mode.getValue()) {
            case "ImGui" -> ImGuiImpl.draw(io -> {
                if (currentFont == null) {
                    currentFont = ImGuiFontManager.getFont(font.getValue(), fontSize.toInt());
                    if (currentFont == null) currentFont = ImGuiImpl.inter17; // fallback
                }

                ImGui.pushFont(currentFont);
                var drawList = ImGui.getForegroundDrawList();

                modules.sort(Comparator.comparingDouble(module -> -ImGui.calcTextSize(getFormattedModuleName(module)).x));

                var alignment = yOffset.toFloat() + (!EntityUtil.hasVisiblePotionEffects(mc.player) ? 0 : 54);
                for (AbstractModule module : modules) {
                    module.getY().update(alignment);

                    final var y = shouldAnimate ? module.getY().getValue() : alignment;
                    if (module == null) continue;

                    var baseName = module.getInfo().name();
                    var thing1 = suffixThing.getValue().equals("none") ? "" : String.valueOf(suffixThing.getValue().charAt(0));
                    var thing2 = suffixThing.getValue().equals("none") ? "" : String.valueOf(suffixThing.getValue().charAt(1));
                    var suffix = (suffixes.getValue() && module.getSuffix() != null) ? thing1 + module.getSuffix() + thing2 : null;

                    var baseSize = ImGui.calcTextSize(baseName);
                    var suffixSize = suffix != null ? ImGui.calcTextSize(suffixChar.getValue() + suffix) : new ImVec2(0, 0);

                    var textWidth = baseSize.x + suffixSize.x + (suffixes.getValue() ? 1 : 0);
                    var progressiveWidth = textWidth + 10;

                    if (shouldAnimate) {
                        progressiveWidth *= module.getX().getValue() / 100;
                    }

                    var x = io.getDisplaySizeX() - progressiveWidth - xOffset.toFloat() + 10;

                    if (background.getValue()) {
                        drawList.addRectFilled(x - 4, y - 1, x + textWidth + 2, y + ImGui.getFontSize() + 1, ImColor.rgba(0, 0, 0, 150));
                    }

                    var centeredY = y + ((ImGui.getFontSize() - ImGui.calcTextSize("A").y) / 2.0f);

                    if (fontShadow.getValue()) {
                        drawList.addText(x + 1, centeredY + 1, ImColor.rgba(0, 0, 0, 160), baseName);
                        if (suffix != null)
                            drawList.addText(x + baseSize.x + 1, centeredY + 1, ImColor.rgba(0, 0, 0, 160), suffixChar.getValue() + suffix);
                    }

                    drawList.addText(x, centeredY + 0.5f, textColor.toImGuiColor(), baseName);
                    if (suffix != null) {
                        drawList.addText(x + baseSize.x, centeredY + 0.5f, ImColor.rgba(200, 200, 200, 255), suffixChar.getValue()); // light gray
                        drawList.addText(x + baseSize.x + ImGui.calcTextSize(suffixChar.getValue()).x, centeredY + 0.5f, suffixColor.toImGuiColor(), suffix);
                    }

                    if (module.isEnabled()) {
                        var lineHeight = ImGui.calcTextSize("M").y;
                        alignment += lineHeight + 2;
                    }
                }

                ImGui.popFont();
            });
            case "Minecraft" -> {
                modules.sort(Comparator.comparingDouble(module -> -mc.textRenderer.getWidth(getFormattedModuleName(module))));

                var y = yOffset.toFloat() + (!EntityUtil.hasVisiblePotionEffects(mc.player) ? 0 : 54);
                for (AbstractModule module : modules) {
                    var baseName = module.getInfo().name();
                    var thing1 = suffixThing.getValue().equals("none") ? "" : String.valueOf(suffixThing.getValue().charAt(0));
                    var thing2 = suffixThing.getValue().equals("none") ? "" : String.valueOf(suffixThing.getValue().charAt(1));
                    var suffix = (suffixes.getValue() && module.getSuffix() != null) ? thing1 + module.getSuffix() + thing2 : null;

                    var baseWidth = mc.textRenderer.getWidth(baseName);
                    var dashWidth = mc.textRenderer.getWidth(suffixChar.getValue());
                    var suffixWidth = suffix != null ? mc.textRenderer.getWidth(suffix) : 0;

                    var totalWidth = baseWidth + (suffix != null ? dashWidth + suffixWidth : 0);

                    var x = event.context.getScaledWindowWidth() - totalWidth - xOffset.toInt();

                    if (background.getValue()) {
                        event.context.fill(
                                x - 2,
                                (int) (y - 1),
                                x + totalWidth + 1,
                                (int) (y + mc.textRenderer.fontHeight),
                                new Color(0, 0, 0, 150).getRGB()
                        );
                    }
                    event.context.drawText(mc.textRenderer, baseName, (int) x, (int) (y + 0.5f), textColor.toInt(), fontShadow.getValue());

                    if (suffix != null) {
                        var dashX = x + baseWidth;
                        var suffixX = dashX + dashWidth;

                        event.context.drawText(mc.textRenderer, suffixChar.getValue(), dashX, (int) y, new Color(200, 200, 200).getRGB(), fontShadow.getValue());

                        event.context.drawText(mc.textRenderer, suffix, suffixX, (int) y, suffixColor.toInt(), fontShadow.getValue());
                    }

                    y += mc.textRenderer.fontHeight + 1;
                }
            }
        }
    }

    private String getFormattedModuleName(AbstractModule module) {
        var modName = module.getInfo().name();

        var thing1 = suffixThing.getValue().equals("none") ? "" : String.valueOf(suffixThing.getValue().charAt(0));
        var thing2 = suffixThing.getValue().equals("none") ? "" : String.valueOf(suffixThing.getValue().charAt(1));

        if (suffixes.getValue() && module.getSuffix() != null) {
            modName += suffixChar.getValue() + thing1 + module.getSuffix() + thing2;
        }

        return modName;
    }

    public void onInit() {
        this.setEnabled(true);
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}