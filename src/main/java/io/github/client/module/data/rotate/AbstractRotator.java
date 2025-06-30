package io.github.client.module.data.rotate;

import io.github.client.module.AbstractModule;
import io.github.client.tracker.impl.RotationTracker;

public abstract class AbstractRotator extends AbstractModule {
    private final int priority;

    public AbstractRotator(int priority) {
        this.priority = priority;
        RotationTracker.INSTANCE.list.add(this);
    }

    public int getPriority() {
        return priority;
    }

    public abstract float[] shouldRotate();
}
