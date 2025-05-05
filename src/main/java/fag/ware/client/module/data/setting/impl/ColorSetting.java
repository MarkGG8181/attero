package fag.ware.client.module.data.setting.impl;

import fag.ware.client.module.data.setting.Setting;

import java.awt.*;

public class ColorSetting extends Setting<Color> {
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
}