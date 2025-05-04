package fag.ware.client.mixin;

import fag.ware.client.event.impl.UpdateEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isLoaded()Z", shift = At.Shift.AFTER))
    public void onUpdate(CallbackInfo ci) {
        new UpdateEvent().post();
    }
}