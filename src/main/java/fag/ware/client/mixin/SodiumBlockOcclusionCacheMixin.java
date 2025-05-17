package fag.ware.client.mixin;

/*
@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache", remap = false)
public class SodiumBlockOcclusionCacheMixin {
    @Inject(method = "shouldDrawSide", at = @At("RETURN"), cancellable = true)
    void shouldDrawSideHook(BlockState state, BlockView view, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (Xray.isEnabled )
            cir.setReturnValue(XRay.isCheckableOre(state.getBlock()));
    }
}
 */