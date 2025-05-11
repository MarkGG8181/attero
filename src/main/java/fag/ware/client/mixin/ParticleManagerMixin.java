package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.BlockBreakParticleEvent;
import fag.ware.client.event.impl.render.BlockBreakingParticleEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Graph
 */
@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addBlockBreakingParticles", at = @At("HEAD"), cancellable = true)
    private void hook(BlockPos position, Direction direction, CallbackInfo ci) {
        BlockBreakingParticleEvent blockBreakingParticleEvent = new BlockBreakingParticleEvent();
        blockBreakingParticleEvent.post();

        if (blockBreakingParticleEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "addBlockBreakParticles", at = @At("HEAD"), cancellable = true)
    private void hookBreaking(BlockPos pos, BlockState state, CallbackInfo ci) {
        BlockBreakParticleEvent blockParticlesEvent = new BlockBreakParticleEvent();
        blockParticlesEvent.post();

        if (blockParticlesEvent.isCancelled()) {
            ci.cancel();
        }
    }
}