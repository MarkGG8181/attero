package io.github.client.event.impl.interact;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KeyEvent extends AbstractCancellableEvent {
    public final int key, scancode, modifiers;
    //GLFW_PRESS   = 1
    //GLFW_RELEASE = 0
    //GLFW_REPEAT  = 2
}