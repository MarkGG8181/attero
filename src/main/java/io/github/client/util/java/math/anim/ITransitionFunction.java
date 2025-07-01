package io.github.client.util.java.math.anim;

/**
 * basically a TriFunction but java doesn't have that shit
 *
 * @author marie
 */
public interface ITransitionFunction {
    float apply(final float current, final float target, final float progress);
}