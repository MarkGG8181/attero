package io.github.client.event;

import io.github.client.Attero;

public class Event {
    public final void post() {
        Attero.BUS.post(this);
    }
}