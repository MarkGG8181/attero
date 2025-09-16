package io.github.client.event.impl.game;

import io.github.client.event.AbstractEvent;
import io.github.client.event.data.State;

public class TickEvent extends AbstractEvent {
    public State state = State.PRE;

    public boolean isPre() {
        return state.equals(State.PRE);
    }
}