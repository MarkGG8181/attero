package io.github.client.module.data.setting.impl;

import io.github.client.module.data.setting.AbstractSetting;

public class GroupSetting extends AbstractSetting<Boolean> {
    public GroupSetting(String name, boolean expanded) {
        super(name, expanded);
    }
}