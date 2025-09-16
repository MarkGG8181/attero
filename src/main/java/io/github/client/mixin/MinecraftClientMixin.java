package io.github.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.client.Attero;
import io.github.client.event.data.State;
import io.github.client.event.impl.interact.MiddleClickEvent;
import io.github.client.event.impl.game.RunLoopEvent;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.event.impl.render.HasOutlineEvent;
import io.github.client.imgui.ImGuiImpl;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.network.message.ChatVisibility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

/**
 * @author markuss
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Final
    private Window window;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(RunArgs args, CallbackInfo ci) {
        Attero.INSTANCE.onStartup();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initImGui(RunArgs args, CallbackInfo ci) {
        try {
            ImGuiImpl.create(window.getHandle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Unique
    private TickEvent cachedTickEvent = null;

    @Inject(method = "close", at = @At("RETURN"))
    public void onClose(CallbackInfo ci) {
        Attero.INSTANCE.onEnd();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTickPre(CallbackInfo ci) {
        cachedTickEvent = new TickEvent();
        cachedTickEvent.state = State.PRE;
        cachedTickEvent.post();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTickPost(CallbackInfo ci) {
        if (cachedTickEvent != null) {
            cachedTickEvent.state = State.POST;
            cachedTickEvent.post();
        }
    }

    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;render(Z)V"
            )
    )
    public void onRun(CallbackInfo ci) {
        new RunLoopEvent().post();
    }

    @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
    public void setTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(Attero.MOD_ID + " " + SharedConstants.getGameVersion().name());
    }

    @Inject(method = "isDemo", at = @At("HEAD"), cancellable = true)
    public void setIsDemo(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "isMultiplayerEnabled", at = @At("HEAD"), cancellable = true)
    public void setIsMultiplayerEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "isOptionalTelemetryEnabled", at = @At("HEAD"), cancellable = true)
    public void setIsOptionalTelemetryEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "isOptionalTelemetryEnabledByApi", at = @At("HEAD"), cancellable = true)
    public void setIsOptionalTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "isTelemetryEnabledByApi", at = @At("HEAD"), cancellable = true)
    public void setIsTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "isUsernameBanned", at = @At("HEAD"), cancellable = true)
    public void setIsUsernameBanned(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "getChatRestriction", at = @At("HEAD"), cancellable = true)
    public void setChatRestrictions(CallbackInfoReturnable<MinecraftClient.ChatRestriction> cir) {
        if (options.getChatVisibility().getValue() == ChatVisibility.HIDDEN) {
            cir.setReturnValue(MinecraftClient.ChatRestriction.DISABLED_BY_OPTIONS);
        } else {
            cir.setReturnValue(MinecraftClient.ChatRestriction.ENABLED);
        }
    }

    @ModifyReturnValue(method = "hasOutline", at = @At("RETURN"))
    private boolean hasOutlineModifyIsOutline(boolean original, Entity entity) {
        HasOutlineEvent hasOutlineEvent = new HasOutlineEvent(entity);
        hasOutlineEvent.post();

        if (hasOutlineEvent.cancelled) {
            return true;
        }

        return original;
    }

    @Inject(method = "doItemPick", at = @At("HEAD"))
    private void doItemPickHook(CallbackInfo ci) {
        new MiddleClickEvent().post();
    }
}