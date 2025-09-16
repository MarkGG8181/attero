package io.github.client.mixin;

import io.github.client.Attero;
import io.github.client.screen.LoginScreen;
import io.github.client.tracker.impl.AuthTracker;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Unique
    private final ModMetadata metadata = FabricLoader.getInstance()
            .getModContainer(Attero.MOD_ID)
            .orElseThrow()
            .getMetadata();

    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        if (!AuthTracker.INSTANCE.connected) {
            MinecraftClient.getInstance().setScreen(new LoginScreen());
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int y = 2;
        int lineHeight = textRenderer.fontHeight + 2;

        context.drawTextWithShadow(textRenderer, "Authors:", 2, y, -1);
        y += lineHeight;

        for (Person author : metadata.getAuthors()) {
            context.drawTextWithShadow(textRenderer, "- " + author.getName(), 2, y, -1);
            y += lineHeight;
        }
    }
}