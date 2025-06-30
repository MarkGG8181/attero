package io.github.client.event.impl.player;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

@AllArgsConstructor
public class ShouldApplyStatusEffectEvent extends CancellableEvent {
    public RegistryEntry<StatusEffect> effect;
}