package io.github.client.module.data.setting.impl;

import io.github.client.module.data.setting.AbstractSetting;

import java.awt.*;

/**
 * @author markuss
 * @since 05/05/2025
 */
public class ColorSetting extends AbstractSetting<Color> {
    public ColorSetting(String name, Color value) {
        super(name, value);
    }

    @Override
    public Color getValue() {
        return super.getValue();
    }

    public int getRed() {
        return getValue().getRed();
    }

    public int getGreen() {
        return getValue().getGreen();
    }

    public int getBlue() {
        return getValue().getBlue();
    }

    public int getAlpha() {
        return getValue().getAlpha();
    }

    public int toInt() {
        return getValue().getRGB();
    }

    public int toImGuiColor() {
        return (getAlpha() << 24) | (getBlue() << 16) | (getGreen() << 8) | getRed();
    }
}