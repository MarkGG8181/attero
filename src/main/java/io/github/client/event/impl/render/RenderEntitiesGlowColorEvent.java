package io.github.client.event.impl.render;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;

import java.awt.*;

@AllArgsConstructor
@Getter
@Setter
public class RenderEntitiesGlowColorEvent extends CancellableEvent {
    private final Entity entity;
    private Color color;
}