package io.github.client.module.data.rotate;

import io.github.client.module.AbstractModule;
import io.github.client.tracker.impl.RotationTracker;

/**
 * @author markuss
 * @since 30/06/2025
 */
public abstract class AbstractRotator extends AbstractModule {
    public final int priority;
    public boolean canPerform;

    public AbstractRotator(int priority) {
        this.priority = priority;
        RotationTracker.INSTANCE.list.add(this);
    }

    public abstract float[] shouldRotate();
}