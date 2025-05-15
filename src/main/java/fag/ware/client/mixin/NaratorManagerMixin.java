package fag.ware.client.mixin;

import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.util.NarratorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NarratorManager.class)
public class NaratorManagerMixin {
    @Inject(method = "getNarratorMode", at = @At("HEAD"), cancellable = true)
    public void setNarratorMode(CallbackInfoReturnable<NarratorMode> cir) {
        cir.setReturnValue(NarratorMode.OFF);
    }
}