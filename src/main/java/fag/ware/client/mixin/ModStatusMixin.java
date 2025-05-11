package fag.ware.client.mixin;

import net.minecraft.util.ModStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

/**
 * @author markuss
 */
@Mixin(ModStatus.class)
public class ModStatusMixin {
    @Inject(method = "check", at = @At("HEAD"), cancellable = true)
    private static void setCheck(String vanillaBrand, Supplier<String> brandSupplier, String environment, Class<?> clazz, CallbackInfoReturnable<ModStatus> cir) {
        cir.setReturnValue(new ModStatus(ModStatus.Confidence.PROBABLY_NOT, environment + " jar signature and brand is untouched"));
    }
}