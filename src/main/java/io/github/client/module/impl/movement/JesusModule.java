package io.github.client.module.impl.movement;

import com.google.common.collect.Streams;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.CanWalkOnLiquidEvent;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.event.impl.world.ComputeNextCollisionEvent;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.tracker.impl.ModuleTracker;
import io.github.client.util.game.MovementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Meteor client
 * @url <a href="https://github.com/MeteorDevelopment/meteor-client/blob/master/src/main/java/meteordevelopment/meteorclient/systems/modules/movement/Jesus.java">Meteor Client repo</a>
 */
@ModuleInfo(name = "Jesus", description = "Allows you traverse water", category = ModuleCategory.MOVEMENT)
public class JesusModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Verus", "Verus", "Collision");
    private final BooleanSetting boost = new BooleanSetting("Speed boost", false).hide(() -> !mode.is("Verus"));

    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private boolean water;
    private int packetTimer = 0;

    @Subscribe
    private void onComputeCollision(ComputeNextCollisionEvent event) {
        switch (mode.getValue()) {
            case "Collision" -> {
                if (event.state.getFluidState().isEmpty() || mc.options.sneakKey.isPressed() || mc.options.sneakKey.wasPressed())
                    return;

                if ((event.state.getBlock() == Blocks.WATER | event.state.getFluidState().getFluid() == Fluids.WATER) && !mc.player.isTouchingWater() && event.pos.getY() <= mc.player.getY() - 1) {
                    event.shape = VoxelShapes.fullCube();
                }
            }
        }
    }

    @Subscribe
    private void onCanWalkOnLiquid(CanWalkOnLiquidEvent event) {
        if (mc.player != null && mc.player.isSwimming()) return;

        if ((event.fluidState.getFluid() == Fluids.WATER || event.fluidState.getFluid() == Fluids.FLOWING_WATER) && waterShouldBeSolid()) {
            event.walkOnFluid = true;
        }
    }

    @Subscribe
    private void onMotion(MotionEvent event) {
        if (event.isPre()) {
            switch (mode.getValue()) {
                case "Verus" -> {
                    if (mc.options.sneakKey.isPressed() || mc.options.sneakKey.wasPressed()) return;

                    var playerBlockPos = mc.player.getBlockPos();
                    var waterBlockPos = new BlockPos(playerBlockPos.getX(), playerBlockPos.getY() - 1, playerBlockPos.getZ());

                    if (mc.player.isTouchingWater() || mc.world.getBlockState(waterBlockPos).isLiquid()) {
                        water = true;
                        mc.player.setVelocity(mc.player.getVelocity().x, 0.0d, mc.player.getVelocity().z);

                        if (MovementUtil.isMoving()) {
                            MovementUtil.setSpeed(boost.getValue() ? 2 : 0.5);
                        } else {
                            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
                        }
                    } else {
                        if (water) {
                            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
                            water = false;
                        }
                    }
                }
            }
        }
    }

    @Subscribe
    private void onSendPacket(SendPacketEvent event) {
        if (!(event.packet instanceof PlayerMoveC2SPacket packet)) return;
        if (mc.player.isTouchingWater() && !waterShouldBeSolid()) return;

        // Check if packet contains a position
        if (!(packet instanceof PlayerMoveC2SPacket.PositionAndOnGround || packet instanceof PlayerMoveC2SPacket.Full)) return;

        // Check inWater, fallDistance and if over liquid
        if (mc.player.isTouchingWater() || mc.player.fallDistance > 3f || !isOverLiquid()) return;

        // If not actually moving, cancel packet
        if (mc.player.input.getMovementInput().equals(Vec2f.ZERO)) {
            event.cancelled = true;
            return;
        }

        // Wait for timer
        if (packetTimer++ < 4) return;
        packetTimer = 0;

        // Cancel old packet
        event.cancelled = true;

        // Get position
        double x = packet.getX(0);
        double y = packet.getY(0) + 0.05;
        double z = packet.getZ(0);

        // Create new packet
        Packet<?> newPacket;
        if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
            newPacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, true, mc.player.horizontalCollision);
        }
        else {
            newPacket = new PlayerMoveC2SPacket.Full(x, y, z, packet.getYaw(0), packet.getPitch(0), true, mc.player.horizontalCollision);
        }

        // Send new packet
        sendPacket(newPacket);
    }

    private boolean waterShouldBeSolid() {
        if (mc.player.getGameMode() == GameMode.SPECTATOR || mc.player.getAbilities().flying) return false;

        if (mc.player.getVehicle() != null) {
            Entity vehicle = mc.player.getVehicle();
            if (vehicle instanceof AbstractBoatEntity) return false;
        }

        if (mc.player.isOnFire()) return false;
        if (mc.options.sneakKey.isPressed()) return false;

        return mode.is("Collision");
    }

    private boolean isOverLiquid() {
        boolean foundLiquid = false;
        boolean foundSolid = false;

        List<Box> blockCollisions = Streams.stream(mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0, -0.5, 0)))
                .map(VoxelShape::getBoundingBox)
                .collect(Collectors.toCollection(ArrayList::new));

        for (Box bb : blockCollisions) {
            blockPos.set(MathHelper.lerp(0.5D, bb.minX, bb.maxX), MathHelper.lerp(0.5D, bb.minY, bb.maxY), MathHelper.lerp(0.5D, bb.minZ, bb.maxZ));
            BlockState blockState = mc.world.getBlockState(blockPos);

            if ((blockState.getBlock() == Blocks.WATER | blockState.getFluidState().getFluid() == Fluids.WATER))
                foundLiquid = true;
            else if (!blockState.isAir()) foundSolid = true;
        }

        return foundLiquid && !foundSolid;
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}