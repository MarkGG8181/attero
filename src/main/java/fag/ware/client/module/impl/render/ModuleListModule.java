package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.Render2DEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.ColorSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.screen.ClickScreen;
import fag.ware.client.screen.JelloClickScreen;
import fag.ware.client.screen.PanelClickScreen;
import fag.ware.client.screen.data.ImGuiFontManager;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.tracker.impl.ModuleTracker;
import fag.ware.client.util.game.EntityUtil;
import imgui.*;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@ModuleInfo(name = "ModuleList", category = ModuleCategory.RENDER, description = "Draws a list of enabled modules")
public class ModuleListModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Design", "ImGui", "ImGui", "Minecraft");
    private final StringSetting font = (StringSetting) new StringSetting("Font", "Inter", "Inter", "Arial", "Comfortaa").hide(() -> !mode.getValue().equals("ImGui"));
    private final NumberSetting fontSize = (NumberSetting) new NumberSetting("Font size", 21, 21, 30).hide(() -> !mode.is("ImGui"));
    private final ColorSetting textColor = new ColorSetting("Text color", new Color(0x26A07D));
    private final BooleanSetting background = new BooleanSetting("Background", true);
    private final BooleanSetting fontShadow = new BooleanSetting("Font shadow", false);
    private final BooleanSetting suffixes = new BooleanSetting("Suffixes", false);
    private final StringSetting suffixChar = (StringSetting) new StringSetting("Suffix char", " - ", " - ", " ", " # ", " $ ", " @ ", " % ", " & ", " = " , " : ", " :: ", " ; ", " ;; ").hide(() -> !suffixes.getValue());
    private final ColorSetting suffixColor = (ColorSetting) new ColorSetting("Suffix color", new Color(0xFFFFFF)).hide(() -> !suffixes.getValue());
    private final NumberSetting xOffset = new NumberSetting("X offset", 5, 0, 15);
    private final NumberSetting yOffset = new NumberSetting("Y offset", 5, 0, 15);
    private final BooleanSetting animate = new BooleanSetting("Animate", true);

    private ImFont currentFont = ImGuiImpl.inter17;

    public ModuleListModule() {
        font.onChange(set -> {
            ImFont font = ImGuiFontManager.getFont(set, fontSize.toInt());
            if (font != null) currentFont = font;
        });

        fontSize.onChange(set -> {
            ImFont font = ImGuiFontManager.getFont(this.font.getValue(), set.intValue());
            if (font != null) currentFont = font;
        });
    }

    @Override
    public void onInit() {
        this.setEnabled(true);
    }

    @Subscribe
    public void onRender(Render2DEvent event) {
        if (mc.currentScreen instanceof ClickScreen || mc.currentScreen instanceof JelloClickScreen || mc.currentScreen instanceof PanelClickScreen) {
            return;
        }

        final boolean shouldAnimate = animate.toBoolean();

        final List<AbstractModule> modules = new LinkedList<>();
        for (final AbstractModule module : ModuleTracker.getInstance().toList()) {
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
                ImDrawList drawList = ImGui.getForegroundDrawList();

                modules.sort(Comparator.comparingDouble(module -> -ImGui.calcTextSize(getFormattedModuleName(module)).x));

                float alignment = yOffset.toFloat() + (!EntityUtil.hasVisiblePotionEffects(mc.player) ? 0 : 54);
                for (AbstractModule module : modules) {
                    module.getY().update(alignment);

                    final float y = shouldAnimate ? module.getY().getValue() : alignment;
                    if (module == null) continue;

                    String baseName = module.getInfo().name();
                    String suffix = (suffixes.getValue() && module.getSuffix() != null) ? module.getSuffix() : null;

                    ImVec2 baseSize = ImGui.calcTextSize(baseName);
                    ImVec2 suffixSize = suffix != null ? ImGui.calcTextSize(suffixChar.getValue() + suffix) : new ImVec2(0, 0);

                    float textWidth = baseSize.x + suffixSize.x + (suffixes.getValue() ? 1 : 0);
                    float progressiveWidth = textWidth + 10;

                    if (shouldAnimate) {
                        progressiveWidth *= module.getX().getValue() / 100;
                    }

                    float x = io.getDisplaySizeX() - progressiveWidth - xOffset.toFloat() + 10;

                    if (background.getValue()) {
                        drawList.addRectFilled(x - 4, y - 1, x + textWidth + 2, y + ImGui.getFontSize() + 1, ImColor.rgba(0, 0, 0, 150));
                    }

                    float centeredY = y + ((ImGui.getFontSize() - ImGui.calcTextSize("A").y) / 2.0f);

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
                        float lineHeight = ImGui.calcTextSize("M").y;
                        alignment += lineHeight + 2;
                    }
                }

                ImGui.popFont();
            });
            case "Minecraft" -> {
                modules.sort(Comparator.comparingDouble(module -> -mc.textRenderer.getWidth(getFormattedModuleName(module))));

                float y = yOffset.toFloat() + (!EntityUtil.hasVisiblePotionEffects(mc.player) ? 0 : 54);
                for (AbstractModule module : modules) {
                    String baseName = module.getInfo().name();
                    String suffix = (suffixes.getValue() && module.getSuffix() != null) ? module.getSuffix() : null;

                    int baseWidth = mc.textRenderer.getWidth(baseName);
                    int dashWidth = mc.textRenderer.getWidth(suffixChar.getValue());
                    int suffixWidth = suffix != null ? mc.textRenderer.getWidth(suffix) : 0;

                    int totalWidth = baseWidth + (suffix != null ? dashWidth + suffixWidth : 0);

                    float x = event.getDrawContext().getScaledWindowWidth() - totalWidth - xOffset.toInt();

                    if (background.getValue()) {
                        event.getDrawContext().fill(
                                (int) (x - 2),
                                (int) (y - 1),
                                (int) (x + totalWidth + 1),
                                (int) (y + mc.textRenderer.fontHeight),
                                new Color(0, 0, 0, 150).getRGB()
                        );
                    }
                    event.getDrawContext().drawText(mc.textRenderer, baseName, (int) x, (int) (y + 0.5f), textColor.toInt(), fontShadow.getValue());

                    if (suffix != null) {
                        int dashX = (int) (x + baseWidth);
                        int suffixX = dashX + dashWidth;

                        event.getDrawContext().drawText(mc.textRenderer, suffixChar.getValue(), dashX, (int) y, new Color(200, 200, 200).getRGB(), fontShadow.getValue());

                        event.getDrawContext().drawText(mc.textRenderer, suffix, suffixX, (int) y, suffixColor.toInt(), fontShadow.getValue());
                    }

                    y += mc.textRenderer.fontHeight + 1;
                }
            }
        }
    }

    private String getFormattedModuleName(AbstractModule module) {
        String modName = module.getInfo().name();

        if (suffixes.getValue() && module.getSuffix() != null) {
            modName += suffixChar.getValue() + module.getSuffix();
        }

        return modName;
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}