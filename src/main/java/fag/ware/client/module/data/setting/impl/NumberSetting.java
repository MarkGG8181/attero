package fag.ware.client.module.data.setting.impl;

import fag.ware.client.module.data.setting.Setting;
import lombok.Getter;

@Getter
public class NumberSetting extends Setting<Number> {
    private final Number min, max;

    public NumberSetting(String name, Number value, Number min, Number max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public Number getValue() {
        return super.getValue();
    }

    public float toFloat() {
        return getValue().floatValue();
    }

    public double toDouble() {
        return getValue().doubleValue();
    }

    public int toInt() {
        return getValue().intValue();
    }

    public String toString() {
        return String.valueOf(getValue());
    }
}