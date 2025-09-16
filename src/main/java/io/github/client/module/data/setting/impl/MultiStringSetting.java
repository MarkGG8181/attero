package io.github.client.module.data.setting.impl;

import io.github.client.module.data.setting.AbstractSetting;
import lombok.Getter;
import lombok.Setter;

/**
 * @author markuss
 * @since 15/05/2025
 */
@Getter
@Setter
public class MultiStringSetting extends AbstractSetting<String[]> {
    private String[] all;

    public MultiStringSetting(String name, String[] enabled, String[] all) {
        super(name, enabled);
        this.all = all;
    }

    public boolean enabled(String value) {
        for (String s : getValue()) {
            if (s.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] getValue() {
        return super.getValue();
    }

    @Override
    public String toString() {
        return getValue().length + " Enabled";
    }
}
