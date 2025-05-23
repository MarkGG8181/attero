package fag.ware.client.mixin;

import fag.ware.client.event.impl.interact.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window != MinecraftClient.getInstance().getWindow().getHandle()) return;
        if (action != GLFW.GLFW_PRESS) return;

        KeyEvent keyEvent = new KeyEvent(key, scancode, modifiers);
        keyEvent.post();

        if (keyEvent.isCancelled()) {
            ci.cancel();
        }
    }
}