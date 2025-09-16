package io.github.client.event.impl.player;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

@AllArgsConstructor
public class ShouldApplyStatusEffectEvent extends AbstractCancellableEvent {
    public RegistryEntry<StatusEffect> effect;
}