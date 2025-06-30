package io.github.client.event.impl.interact;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KeyEvent extends CancellableEvent {
    public final int key, scancode, modifiers;
    //GLFW_PRESS   = 1
    //GLFW_RELEASE = 0
    //GLFW_REPEAT  = 2
}