package io.github.client.module.impl.player;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.event.impl.player.SneakPacketEvent;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.rotate.AbstractRotator;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.RangeNumberSetting;
import io.github.client.util.game.InventoryUtil;
import io.github.client.util.game.RotationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "ScaffoldWalk", description = "Places blocks under you", category = ModuleCategory.PLAYER)
public class ScaffoldWalkModule extends AbstractRotator {
    private final RangeNumberSetting speed = new RangeNumberSetting("Speed", 10, 180, 10, 180);
    private final BooleanSetting vulcan = new BooleanSetting("Vulcan", false);

    private BlockHitResult result;

    public ScaffoldWalkModule() {
        super(50);
    }

    @Subscribe
    private void onTick(TickEvent ignoredEvent) {
        if (mc.player == null || mc.world == null || !canPerform) return;

        checkForBlocks();

        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item && InventoryUtil.isGoodBlock(item)) {
            var belowPlayer = mc.player.getBlockPos().down();

            if (!mc.world.getBlockState(belowPlayer).isSolidBlock(mc.world, belowPlayer)) {
                placeBlock(belowPlayer);
            }
        } else {
            InventoryUtil.switchToBestSlotWithBlocks();
        }
    }

    @Subscribe
    private void onSneakPacket(SneakPacketEvent event) {
        if (vulcan.getValue()) {
            event.sneaking = true;
        }
    }

    private void placeBlock(BlockPos pos) {
        var heldItemStack = mc.player.getActiveItem();

        if (mc.world.getBlockState(pos).isSolidBlock(mc.world, pos)) {
            return;
        }

        if (shouldRotate() != null && heldItemStack != null) {
            result = findBlockPlaceResult(pos);
            if (result != null && mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result).isAccepted()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private BlockHitResult findBlockPlaceResult(BlockPos targetPos) {
        BlockPos blockBelowTarget = targetPos.down();
        BlockState stateBelowTarget = mc.world.getBlockState(blockBelowTarget);

        if (!stateBelowTarget.isAir() && !stateBelowTarget.isReplaceable()) {
            Vec3d hitVec = Vec3d.ofCenter(blockBelowTarget).add(0.0, 0.5, 0.0);
            return new BlockHitResult(hitVec, Direction.UP, blockBelowTarget, false);
        }

        for (Direction direction : Direction.values()) {
            if (direction == Direction.UP || direction == Direction.DOWN) continue;

            BlockPos neighborPos = targetPos.offset(direction.getOpposite());
            BlockState neighborState = mc.world.getBlockState(neighborPos);

            if (!neighborState.isAir() && !neighborState.isReplaceable()) {
                Vec3d hitVec = Vec3d.ofCenter(neighborPos).add(
                        direction.getOpposite().getOffsetX() * 0.5,
                        direction.getOpposite().getOffsetY() * 0.5,
                        direction.getOpposite().getOffsetZ() * 0.5
                );
                return new BlockHitResult(hitVec, direction, neighborPos, false);
            }
        }
        return null;
    }

    private void checkForBlocks() {
        if (!InventoryUtil.checkHotbarForBlocks()) {
            toggle();
            sendError("You have no valid blocks in your hotbar!");
        }
    }

    public void onEnable() {
        if (mc.player == null || mc.world == null) return;

        checkForBlocks();
        InventoryUtil.switchToBestSlotWithBlocks();
        result = null;
    }

    @Override
    public float[] shouldRotate() {
        if (result == null) {
            return new float[]{mc.player.lastYaw, mc.player.lastPitch};
        }

        return RotationUtil.toRotation(result.getPos(), speed.getMinAsFloat(), speed.getMaxAsFloat());
    }
}