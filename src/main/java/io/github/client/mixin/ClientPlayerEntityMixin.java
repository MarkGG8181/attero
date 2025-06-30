package io.github.client.mixin;

import com.mojang.authlib.GameProfile;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.event.impl.player.SprintEvent;
import io.github.client.event.impl.player.UpdateEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    protected abstract void sendSprintingPacket();

    @Shadow
    protected abstract boolean isCamera();

    @Shadow
    private int ticksSinceLastPositionPacketSent;

    @Shadow
    private double lastXClient;

    @Shadow
    private double lastYClient;

    @Shadow
    private double lastZClient;

    @Shadow
    private float lastYawClient;

    @Shadow
    private float lastPitchClient;

    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;

    @Shadow
    private boolean lastOnGround;

    @Shadow
    private boolean lastHorizontalCollision;

    @Shadow
    private boolean autoJumpEnabled;

    @Shadow
    @Final
    protected MinecraftClient client;

    @Shadow public Input input;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isLoaded()Z", shift = At.Shift.AFTER))
    public void onUpdate(CallbackInfo ci) {
        new UpdateEvent().post();
    }

    @Override
    public Vec3d getRotationVector() {
        return this.getRotationVec(1.0f);
    }

    @Unique
    private SprintEvent cachedSprintEvent = null;
    @Unique
    private float cachedMoveForward = 0;

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;canStartSprinting()Z", ordinal = 0))
    private void tickMovementHook(CallbackInfo ci) {
        cachedMoveForward = this.input.getMovementInput().y;
        cachedSprintEvent = new SprintEvent();
        cachedSprintEvent.post();

        if (cachedSprintEvent.cancelled) {
            cachedMoveForward = this.input.getMovementInput().y;
            final float x = this.input.getMovementInput().x;
            this.input.movementVector = new Vec2f(x, 0);
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;allowFlying:Z", ordinal = 0))
    private void tickMovementHook2(CallbackInfo ci) {
        if (cachedSprintEvent != null && cachedSprintEvent.cancelled) {
            final float x = this.input.getMovementInput().x;
            this.input.movementVector = new Vec2f(x, cachedMoveForward);
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPackets(CallbackInfo ci) {
        ci.cancel();

        MotionEvent motionEvent = new MotionEvent(getX(), getY(), getZ(), getYaw(), getPitch(), isOnGround());
        motionEvent.state = MotionEvent.State.PRE;
        motionEvent.post();

        this.sendSprintingPacket();

        if (this.isCamera()) {
            double d = motionEvent.x - this.lastXClient;
            double e = motionEvent.y - this.lastYClient;
            double f = motionEvent.z - this.lastZClient;
            double g = motionEvent.yaw - this.lastYawClient;
            double h = motionEvent.pitch - this.lastPitchClient;
            this.ticksSinceLastPositionPacketSent++;

            boolean bl = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20;
            boolean bl2 = g != 0.0 || h != 0.0;

            if (bl && bl2) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(motionEvent.getPos(), motionEvent.yaw, motionEvent.pitch, motionEvent.onGround, this.horizontalCollision));
            } else if (bl) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(motionEvent.getPos(), motionEvent.onGround, this.horizontalCollision));
            } else if (bl2) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(motionEvent.yaw, motionEvent.pitch, motionEvent.onGround, this.horizontalCollision));
            } else if (this.lastOnGround != motionEvent.onGround || this.lastHorizontalCollision != this.horizontalCollision) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(motionEvent.onGround, this.horizontalCollision));
            }

            if (bl) {
                this.lastXClient = motionEvent.x;
                this.lastYClient = motionEvent.y;
                this.lastZClient = motionEvent.z;
                this.ticksSinceLastPositionPacketSent = 0;
            }

            if (bl2) {
                this.lastYawClient = motionEvent.yaw;
                this.lastPitchClient = motionEvent.pitch;
            }

            this.lastOnGround = motionEvent.onGround;
            this.lastHorizontalCollision = this.horizontalCollision;
            this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
        }

        motionEvent.state = MotionEvent.State.POST;
        motionEvent.post();
    }
}