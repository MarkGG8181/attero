package fag.ware.client.event.impl;

import fag.ware.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MoveButtonEvent extends Event {
    private boolean forward, back, left, right, jump, sneak;
}