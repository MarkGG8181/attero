package io.github.client.tracker.impl;

import io.github.client.module.data.rotate.AbstractRotator;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.java.interfaces.IMinecraft;

@SuppressWarnings("ALL")
public class RotationTracker extends AbstractTracker<AbstractRotator> implements IMinecraft {
    public static final RotationTracker INSTANCE = new RotationTracker();

    public static float prevYaw, prevPitch;
    public static float yaw, pitch;
}