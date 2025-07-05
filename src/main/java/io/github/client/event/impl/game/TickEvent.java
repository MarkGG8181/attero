package io.github.client.event.impl.game;

import io.github.client.event.Event;
import io.github.client.event.data.State;

public class TickEvent extends Event {
    public State state = State.PRE;

    public boolean isPre() {
        return state.equals(State.PRE);
    }
}