package fag.ware.client.module.data.setting.impl;

import fag.ware.client.module.data.setting.Setting;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean value) {
        super(name, value);
    }

    @Override
    public Boolean getValue() {
        return super.getValue();
    }

    public boolean toBoolean() {
        return super.getValue();
    }

    public String toString() {
        return String.valueOf(toBoolean());
    }
}