package fag.ware.client.event;

import fag.ware.client.Fagware;

public class Event {
    // Marker base class for all events
    public void post() {
        Fagware.BUS.post(this);
    }
}