package io.github.client.mixin;

import io.github.client.event.impl.world.LoadWorldEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author fabric example
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(at = @At("HEAD"), method = "loadWorld")
	private void init(CallbackInfo info) {
		new LoadWorldEvent().post();
	}
}