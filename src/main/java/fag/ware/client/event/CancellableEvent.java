package fag.ware.client.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancellableEvent extends Event {
    private boolean cancelled;
}
