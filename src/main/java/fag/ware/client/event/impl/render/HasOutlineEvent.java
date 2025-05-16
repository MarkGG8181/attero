package fag.ware.client.event.impl.render;

import fag.ware.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.Entity;

@AllArgsConstructor
@Getter
public class HasOutlineEvent extends CancellableEvent {
    private final Entity entity;
}