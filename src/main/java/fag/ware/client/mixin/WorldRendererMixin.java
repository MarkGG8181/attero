package fag.ware.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import fag.ware.client.event.impl.render.RenderWorldBorderEvent;
import net.minecraft.client.render.WorldBorderRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author markuss
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @WrapWithCondition(method = "method_62216", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldBorderRendering;render(Lnet/minecraft/world/border/WorldBorder;Lnet/minecraft/util/math/Vec3d;DD)V"))
    private boolean shouldRenderWorldBorder(WorldBorderRendering instance, WorldBorder border, Vec3d cameraPos, double viewDistanceBlocks, double farPlaneDistance) {
        RenderWorldBorderEvent renderWorldBorderEvent = new RenderWorldBorderEvent();
        renderWorldBorderEvent.post();

        return !renderWorldBorderEvent.isCancelled();
    }
}