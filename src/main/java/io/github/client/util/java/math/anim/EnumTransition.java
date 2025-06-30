package io.github.client.util.java.math.anim;

/**
 * An enum that contains all code that actually does math to keep the actual rendering code cleaner
 * Also this kinda proves that I have no idea of how to name stuff properly
 *
 * @author marie
 */
public enum EnumTransition
{
    @Deprecated LINEAR((current, target, progress) -> current + (target - current) * progress), // looks shit, mostly just to show how things work
    SQRT((current, target, progress) -> current + (target - current) * (float) Math.sqrt(progress)),
    EASE_IN_OUT((current, target, progress) -> {
        float t = Math.min(1, progress);
        t = t * t * (3 - 2 * t);
        return current + (target - current) * t;
    });

    private final ITransitionFunction transitionFunction;

    EnumTransition(final ITransitionFunction transitionFunction)
    {
        this.transitionFunction = transitionFunction;
    }

    public float apply(final float current,
                       final float target,
                       final float progress)
    {
        return transitionFunction.apply(current, target, progress);
    }
}