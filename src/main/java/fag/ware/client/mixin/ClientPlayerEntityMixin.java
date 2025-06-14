package fag.ware.client.mixin;

import com.mojang.authlib.GameProfile;
import fag.ware.client.event.impl.player.MotionEvent;
import fag.ware.client.event.impl.player.SprintEvent;
import fag.ware.client.event.impl.player.UpdateEvent;
import fag.ware.client.tracker.impl.CombatTracker;
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

        if (cachedSprintEvent.isCancelled()) {
            cachedMoveForward = this.input.getMovementInput().y;
            final float x = this.input.getMovementInput().x;
            this.input.movementVector = new Vec2f(x, 0);
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;allowFlying:Z", ordinal = 0))
    private void tickMovementHook2(CallbackInfo ci) {
        if (cachedSprintEvent != null && cachedSprintEvent.isCancelled()) {
            final float x = this.input.getMovementInput().x;
            this.input.movementVector = new Vec2f(x, cachedMoveForward);
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPackets(CallbackInfo ci) {
        ci.cancel();

        MotionEvent motionEvent = new MotionEvent(getX(), getY(), getZ(), getYaw(), getPitch(), isOnGround());
        motionEvent.setState(MotionEvent.State.PRE);
        motionEvent.post();

        this.sendSprintingPacket();

        if (this.isCamera()) {
            double d = motionEvent.getX() - this.lastXClient;
            double e = motionEvent.getY() - this.lastYClient;
            double f = motionEvent.getZ() - this.lastZClient;
            double g = motionEvent.getYaw() - this.lastYawClient;
            double h = motionEvent.getPitch() - this.lastPitchClient;
            this.ticksSinceLastPositionPacketSent++;

            boolean bl = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20;
            boolean bl2 = g != 0.0 || h != 0.0;

            if (bl && bl2) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(motionEvent.getPos(), motionEvent.getYaw(), motionEvent.getPitch(), motionEvent.isOnGround(), this.horizontalCollision));
            } else if (bl) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(motionEvent.getPos(), motionEvent.isOnGround(), this.horizontalCollision));
            } else if (bl2) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(motionEvent.getYaw(), motionEvent.getPitch(), motionEvent.isOnGround(), this.horizontalCollision));
            } else if (this.lastOnGround != motionEvent.isOnGround() || this.lastHorizontalCollision != this.horizontalCollision) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(motionEvent.isOnGround(), this.horizontalCollision));
            }

            if (bl) {
                this.lastXClient = motionEvent.getX();
                this.lastYClient = motionEvent.getY();
                this.lastZClient = motionEvent.getZ();
                this.ticksSinceLastPositionPacketSent = 0;
            }

            if (bl2) {
                this.lastYawClient = motionEvent.getYaw();
                this.lastPitchClient = motionEvent.getPitch();
            }

            this.lastOnGround = motionEvent.isOnGround();
            this.lastHorizontalCollision = this.horizontalCollision;
            this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
        }

        motionEvent.setState(MotionEvent.State.POST);
        CombatTracker.getInstance().prevYaw = CombatTracker.getInstance().yaw;
        CombatTracker.getInstance().yaw = motionEvent.getYaw();
        CombatTracker.getInstance().prevPitch = CombatTracker.getInstance().pitch;
        CombatTracker.getInstance().pitch = motionEvent.getPitch();
        motionEvent.post();
    }
}