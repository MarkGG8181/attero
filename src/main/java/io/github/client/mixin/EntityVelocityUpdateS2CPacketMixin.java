package io.github.client.mixin;

import io.github.client.util.interfaces.IVelocity;
import lombok.Setter;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author markuss
 */
@Setter
@Mixin(EntityVelocityUpdateS2CPacket.class)
public class EntityVelocityUpdateS2CPacketMixin implements IVelocity {
    @Shadow
    @Mutable
    private int velocityX;
    @Shadow
    @Mutable
    private int velocityY;
    @Shadow
    @Mutable
    private int velocityZ;
}