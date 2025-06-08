package fag.ware.client.event.impl.render;

import fag.ware.client.event.CancellableEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandSwingDurationEvent extends CancellableEvent {
    private int speed;
}