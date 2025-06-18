package io.github.client.module.data.setting.impl;

import io.github.client.module.data.setting.AbstractSetting;
import lombok.Getter;

public class StringSetting extends AbstractSetting<String> {
    public final String[] values;

    @Getter
    private int index;

    public StringSetting(String name, String value, String... values) {
        super(name, value);
        this.values = values;
        this.index = find(value);
    }

    public StringSetting(String name, String value,  boolean noParent, String... values) {
        super(name, value, noParent);
        this.values = values;
        this.index = find(value);
    }

    public boolean is(String value) {
        return getValue().equals(value);
    }

    @Override
    public String toString() {
        return getValue();
    }

    public int find(String value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getValue() {
        if (index >= 0 && index < values.length) {
            return values[index];
        }

        return null;
    }

    public void setIndex(int index) {
        if (index >= 0 && index < values.length) {
            setValue(values[index]); // this will call onChange
        }
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        int newIndex = find(value);

        if (newIndex >= 0) {
            this.index = newIndex;
        } else {
            this.index = find(getValue());
        }
    }
}