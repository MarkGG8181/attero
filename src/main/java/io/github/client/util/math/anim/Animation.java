package io.github.client.util.math.anim;

import io.github.client.tracker.impl.AuthTracker;
import lombok.Getter;

/**
 * Holy shit this took me way longer than I'd like to admit
 *
 * @author marie
 */
public final class Animation {
    private final float transitionSpeed;
    private final EnumTransition transitionType;
    @Getter
    private float value;
    private long lastUpdateTime;

    public Animation(final float initialValue,
                     final EnumTransition transitionType,
                     final float transitionSpeed)
    {
        this.value = initialValue;
        this.transitionType = transitionType;
        this.transitionSpeed = transitionSpeed;
        this.lastUpdateTime = System.nanoTime();
    }

    public void update(final float target)
    {
        final long currentTime = System.nanoTime();
        final float deltaTime = (currentTime - lastUpdateTime) / AuthTracker.getInstance().values[4]; // this is the right value. source: allah
        lastUpdateTime = currentTime;

        float progress = transitionSpeed * deltaTime;
        value = transitionType.apply(value, target, progress);
    }
}