package io.github.client.mixin;

import io.github.client.util.java.interfaces.ITimer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author markuss
 */
@Mixin(RenderTickCounter.Dynamic.class)
public class RenderTickCounterDynamicMixin implements ITimer {
    @Shadow private float dynamicDeltaTicks;
    @Unique
    private float timerSpeed = 1.0F;

    @Inject(
            method = "beginRenderTick(J)I",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastTimeMillis:J",
                    ordinal = 1
            )
    )
    private void modifyDeltaTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        dynamicDeltaTicks = dynamicDeltaTicks * getTimerSpeed();
    }

    @Override
    public float getTimerSpeed() {
        return this.timerSpeed;
    }

    @Override
    public void setTimerSpeed(float timerSpeed) {
        this.timerSpeed = timerSpeed;
    }
}