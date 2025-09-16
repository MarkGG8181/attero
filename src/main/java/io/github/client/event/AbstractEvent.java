package io.github.client.event;

import io.github.client.Attero;

public abstract class AbstractEvent {
    public final void post() {
        Attero.BUS.post(this);
    }
}