package io.github.client.module.impl.player;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.GroupSetting;
import io.github.client.module.data.setting.impl.RangeNumberSetting;
import io.github.client.util.game.InventoryUtil;
import io.github.client.util.game.RotationUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@SuppressWarnings("ALL")
@ModuleInfo(name = "ScaffoldWalk", description = "Places blocks under you", category = ModuleCategory.PLAYER)
public class ScaffoldWalkModule extends AbstractModule {
    private final GroupSetting rotationGroup = new GroupSetting("Rotations", false);
    private final RangeNumberSetting speed = (RangeNumberSetting) new RangeNumberSetting("Speed", 10, 180, 10, 180).setParent(rotationGroup);

    private float[] rots;

    @Subscribe
    public void onTick(TickEvent ignoredEvent) {
        if (mc.player == null || mc.world == null) return;

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
    public void onMotion(MotionEvent event) {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item && InventoryUtil.isGoodBlock(item)) {
            var currentYaw = event.getYaw();

            var belowPlayer = mc.player.getBlockPos().down();
            var lookAt = new Vec3d(belowPlayer.getX() + 0.5, belowPlayer.getY() + 0.5, belowPlayer.getZ() + 0.5);

            var targetRots = RotationUtil.toRotation(lookAt, speed.getMinAsFloat(), speed.getMaxAsFloat());

            rots = new float[]{currentYaw, targetRots[1]};
        } else {
            rots = new float[]{mc.player.lastYaw, mc.player.lastPitch};
        }

        event.setYaw(rots[0]);
        event.setPitch(rots[1]);
    }

    private void placeBlock(BlockPos pos) {
        var heldItemStack = mc.player.getActiveItem();

        if (mc.world.getBlockState(pos).isSolidBlock(mc.world, pos)) {
            return;
        }

        if (rots != null && heldItemStack != null) {
            BlockHitResult result = findBlockPlaceResult(pos);
            if (result != null && mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result).isAccepted()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private BlockHitResult findBlockPlaceResult(BlockPos pos) {
        return new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false);
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

        rots = new float[]{RotationUtil.getAdjustedYaw(), 85};
    }
}