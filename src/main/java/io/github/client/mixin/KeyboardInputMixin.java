package io.github.client.mixin;

import io.github.client.event.impl.interact.MoveButtonEvent;
import io.github.client.event.impl.interact.MoveInputEvent;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {
    @Shadow
    @Final
    private GameOptions settings;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        ci.cancel();

        MoveButtonEvent moveButtonEvent = new MoveButtonEvent(
                this.settings.forwardKey.isPressed(),
                this.settings.backKey.isPressed(),
                this.settings.leftKey.isPressed(),
                this.settings.rightKey.isPressed(),
                this.settings.jumpKey.isPressed(),
                this.settings.sneakKey.isPressed()
        );
        moveButtonEvent.post();

        this.playerInput = new PlayerInput(
                moveButtonEvent.forward,
                moveButtonEvent.back,
                moveButtonEvent.left,
                moveButtonEvent.right,
                moveButtonEvent.jump,
                moveButtonEvent.sneak,
                this.settings.sprintKey.isPressed()
        );

        float moveForward = 0.0f;
        float moveStrafe = 0.0f;

        if (moveButtonEvent.forward) moveForward += 1.0f;
        if (moveButtonEvent.back) moveForward -= 1.0f;
        if (moveButtonEvent.left) moveStrafe += 1.0f;
        if (moveButtonEvent.right) moveStrafe -= 1.0f;

        MoveInputEvent moveInputEvent = new MoveInputEvent(
                moveForward, moveStrafe,
                moveButtonEvent.jump,
                moveButtonEvent.sneak,
                0.3f
        );
        moveInputEvent.post();

        float f = moveInputEvent.forward;
        float g = moveInputEvent.strafe;

        if (moveInputEvent.sneaking) {
            f *= moveInputEvent.sneakFactor;
            g *= moveInputEvent.sneakFactor;
        }

        this.movementVector = new Vec2f(g, f).normalize();
    }
}