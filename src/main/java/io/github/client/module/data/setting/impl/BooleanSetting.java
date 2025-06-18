package io.github.client.module.data.setting.impl;

import io.github.client.module.data.setting.AbstractSetting;

public class BooleanSetting extends AbstractSetting<Boolean> {
    public BooleanSetting(String name, boolean value) {
        super(name, value);
    }

    public BooleanSetting(String name, boolean value, boolean noParent) {
        super(name, value, noParent);
    }

    @Override
    public Boolean getValue() {
        return super.getValue();
    }

    public boolean toBoolean() {
        return getValue();
    }

    public String toString() {
        return String.valueOf(toBoolean());
    }
}