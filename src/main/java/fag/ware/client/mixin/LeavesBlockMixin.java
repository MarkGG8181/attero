package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.SpawnLeavesFallingParticles;
import fag.ware.client.event.impl.render.SpawnLeavesWaterParticles;
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
        SpawnLeavesWaterParticles spawnLeavesWaterParticles = new SpawnLeavesWaterParticles();
        spawnLeavesWaterParticles.post();

        if (spawnLeavesWaterParticles.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "spawnLeafParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"), cancellable = true)
    private void spawnLeafParticle(CallbackInfo ci) {
        SpawnLeavesFallingParticles spawnLeavesFallingParticles = new SpawnLeavesFallingParticles();
        spawnLeavesFallingParticles.post();

        if (spawnLeavesFallingParticles.isCancelled()) {
            ci.cancel();
        }
    }
}