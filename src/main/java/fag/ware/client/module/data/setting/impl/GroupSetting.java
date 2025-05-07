package fag.ware.client.module.data.setting.impl;

import fag.ware.client.module.data.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class GroupSetting extends Setting<Void> {
    private final List<Setting<?>> children = new ArrayList<>();

    public GroupSetting(String name) {
        super(name, null);
    }

    public void addChild(Setting<?> setting) {
        children.add(setting);
    }

    public List<Setting<?>> getChildren() {
        return children;
    }
}