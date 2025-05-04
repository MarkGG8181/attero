package fag.ware.client.event.impl;

import fag.ware.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KeyEvent extends CancellableEvent {
    private final int key, scancode, action, modifiers;
    //GLFW_PRESS   = 1
    //GLFW_RELEASE = 0
    //GLFW_REPEAT  = 2
}