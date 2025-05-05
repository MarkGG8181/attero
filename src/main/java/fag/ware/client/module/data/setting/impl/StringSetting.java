package fag.ware.client.module.data.setting.impl;

import fag.ware.client.module.data.setting.Setting;

public class StringSetting extends Setting<String> {
    private final String[] values;
    private int index;

    public StringSetting(String name, String value, String[] values) {
        super(name, value);
        this.values = values;
        this.index = find(value);
    }

    public boolean is(String value) {
        return getValue().equals(value);
    }

    @Override
    public String toString() {
        return super.getValue();
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