package fag.ware.client.mixin;

import fag.ware.client.event.impl.interact.MoveButtonEvent;
import fag.ware.client.event.impl.interact.MoveInputEvent;
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
                moveButtonEvent.isForward(),
                moveButtonEvent.isBack(),
                moveButtonEvent.isLeft(),
                moveButtonEvent.isRight(),
                moveButtonEvent.isJump(),
                moveButtonEvent.isSneak(),
                this.settings.sprintKey.isPressed()
        );

        float moveForward = 0.0f;
        float moveStrafe = 0.0f;

        if (moveButtonEvent.isForward()) moveForward += 1.0f;
        if (moveButtonEvent.isBack()) moveForward -= 1.0f;
        if (moveButtonEvent.isLeft()) moveStrafe += 1.0f;
        if (moveButtonEvent.isRight()) moveStrafe -= 1.0f;

        MoveInputEvent moveInputEvent = new MoveInputEvent(
                moveForward, moveStrafe,
                moveButtonEvent.isJump(),
                moveButtonEvent.isSneak(),
                0.3f
        );
        moveInputEvent.post();

        float f = moveInputEvent.getForward();
        float g = moveInputEvent.getStrafe();

        if (moveInputEvent.isSneaking()) {
            f *= moveInputEvent.getSneakFactor();
            g *= moveInputEvent.getSneakFactor();
        }

        this.movementVector = new Vec2f(g, f).normalize();
    }
}