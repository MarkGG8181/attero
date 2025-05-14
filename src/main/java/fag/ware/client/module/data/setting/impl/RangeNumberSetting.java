package fag.ware.client.module.data.setting.impl;

import fag.ware.client.module.data.setting.AbstractSetting;
import lombok.Getter;

@Getter
public class RangeNumberSetting extends AbstractSetting<Number[]> {
    private final Number absoluteMin, absoluteMax;

    public RangeNumberSetting(String name, Number minValue, Number maxValue, Number absoluteMin, Number absoluteMax) {
        super(name, new Number[]{minValue, maxValue});
        this.absoluteMin = absoluteMin;
        this.absoluteMax = absoluteMax;
    }

    public RangeNumberSetting(String name, Number minValue, Number maxValue, Number absoluteMin, Number absoluteMax, boolean noParent) {
        super(name, new Number[]{minValue, maxValue}, noParent);
        this.absoluteMin = absoluteMin;
        this.absoluteMax = absoluteMax;
    }

    public float getMinAsFloat() {
        return getValue()[0].floatValue();
    }

    public float getMaxAsFloat() {
        return getValue()[1].floatValue();
    }

    public double getMinAsDouble() {
        return getValue()[0].doubleValue();
    }

    public double getMaxAsDouble() {
        return getValue()[1].doubleValue();
    }

    public int getMinAsInt() {
        return getValue()[0].intValue();
    }

    public int getMaxAsInt() {
        return getValue()[1].intValue();
    }

    public void setRange(Number min, Number max) {
        if (min.doubleValue() < absoluteMin.doubleValue()) min = absoluteMin;
        if (max.doubleValue() > absoluteMax.doubleValue()) max = absoluteMax;
        if (min.doubleValue() > max.doubleValue()) min = max;

        this.setValue(new Number[]{min, max});
    }

    @Override
    public String toString() {
        return getMinAsDouble() + " - " + getMaxAsDouble();
    }
}
