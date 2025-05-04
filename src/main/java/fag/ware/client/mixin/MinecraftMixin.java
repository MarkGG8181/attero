package fag.ware.client.mixin;

import fag.ware.client.Fagware;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.util.imgui.ImGuiImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;
import net.minecraft.network.message.ChatVisibility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {
    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Final
    private Window window;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onInit(RunArgs args, CallbackInfo ci) {
        Fagware.INSTANCE.onStartup();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initImGui(RunArgs args, CallbackInfo ci) {
        try {
            ImGuiImpl.create(window.getHandle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "close", at = @At("RETURN"))
    public void closeImGui(CallbackInfo ci) {
        ImGuiImpl.dispose();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onRun(CallbackInfo ci) {
        new TickEvent().post();
    }

    @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
    public void setTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("fagware :3");
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
}