package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.player.MotionEvent;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.GroupSetting;
import fag.ware.client.module.data.setting.impl.RangeNumberSetting;
import fag.ware.client.util.game.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "ScaffoldWalk", description = "Places blocks under you", category = ModuleCategory.PLAYER)
public class ScaffoldWalkModule extends AbstractModule {

    private final GroupSetting rotationGroup = new GroupSetting("Rotations", false);
    private final RangeNumberSetting speed = (RangeNumberSetting) new RangeNumberSetting("Speed", 10, 180, 10, 180).setParent(rotationGroup);

    private float[] rots;

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        checkForBlocks();
        InventoryUtil.switchToBestSlotWithBlocks();

        rots = new float[]{RotationUtil.getAdjustedYaw(), 85};
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        checkForBlocks();

        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item && InventoryUtil.isGoodBlock(item)) {
            BlockPos belowPlayer = mc.player.getBlockPos().down();

            // Check if player is on ground and walking off the edge
            if (mc.player.isOnGround() && !mc.world.getBlockState(belowPlayer).isSolidBlock(mc.world, belowPlayer)) {
                placeBlock(belowPlayer);

                if (MovementUtil.isMoving() && mc.options.jumpKey.isPressed()) {
                    return;
                }

                placeBlock(belowPlayer);
            }
        }
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (mc.player.getMainHandStack().getItem() instanceof BlockItem item && InventoryUtil.isGoodBlock(item)) {
            BlockPos belowPlayer = mc.player.getBlockPos().down();
            Vec3d lookAt = new Vec3d(belowPlayer.getX() + 0.5, belowPlayer.getY() + 0.5, belowPlayer.getZ() + 0.5);

            rots = RotationUtil.toRotation(lookAt, speed.getMinAsFloat(), speed.getMaxAsFloat());
        } else {
            rots = new float[]{mc.player.lastYaw, mc.player.lastPitch};
        }

        if (rots != null) {
            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
        }
    }

    private void placeBlock(BlockPos pos) {
        assert mc.player != null;
        var heldItemStack = mc.player.getActiveItem();

        if (rots != null && heldItemStack != null) {
            BlockHitResult result = new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false);
            if (mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result).isAccepted()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private void checkForBlocks() {
        if (!InventoryUtil.checkHotbarForBlocks()) {
            toggle();
            sendError("You have no valid blocks in your hotbar!");
        }
    }
}