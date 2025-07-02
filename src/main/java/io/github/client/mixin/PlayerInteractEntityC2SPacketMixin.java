package io.github.client.mixin;

import io.github.client.event.impl.player.SneakPacketEvent;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerInteractEntityC2SPacket.class)
public abstract class PlayerInteractEntityC2SPacketMixin {
    @ModifyVariable(method = "<init>(IZLnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket$InteractTypeHandler;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static boolean setSneaking(boolean sneaking) {
        SneakPacketEvent sneakPacketEvent = new SneakPacketEvent(sneaking);
        sneakPacketEvent.post();
        return sneakPacketEvent.sneaking;
    }
}