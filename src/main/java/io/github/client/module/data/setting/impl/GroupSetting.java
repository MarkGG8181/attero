package io.github.client.module.data.setting.impl;

import io.github.client.module.data.setting.AbstractSetting;

/**
 * @author markuss
 * @since 07/05/2025
 */
public class GroupSetting extends AbstractSetting<Boolean> {
    public GroupSetting(String name, boolean expanded) {
        super(name, expanded);
    }
}