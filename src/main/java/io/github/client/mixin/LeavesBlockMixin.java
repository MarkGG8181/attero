package io.github.client.mixin;

import io.github.client.event.impl.render.SpawnLeavesFallingParticlesEvent;
import io.github.client.event.impl.render.SpawnLeavesWaterParticlesEvent;
import net.minecraft.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    @Inject(method = "spawnWaterParticle", at = @At("HEAD"), cancellable = true)
    private static void spawnWaterParticles(CallbackInfo ci) {
        SpawnLeavesWaterParticlesEvent spawnLeavesWaterParticlesEvent = new SpawnLeavesWaterParticlesEvent();
        spawnLeavesWaterParticlesEvent.post();

        if (spawnLeavesWaterParticlesEvent.cancelled) {
            ci.cancel();
        }
    }

    @Inject(method = "spawnLeafParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"), cancellable = true)
    private void spawnLeafParticle(CallbackInfo ci) {
        SpawnLeavesFallingParticlesEvent spawnLeavesFallingParticlesEvent = new SpawnLeavesFallingParticlesEvent();
        spawnLeavesFallingParticlesEvent.post();

        if (spawnLeavesFallingParticlesEvent.cancelled) {
            ci.cancel();
        }
    }
}