package io.github.client.event;

import io.github.client.Attero;

public class Event {
    // Marker base class for all events
    public final void post() {
        Attero.BUS.post(this);
    }
}