package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.player.MotionEvent;
import fag.ware.client.event.impl.player.SprintEvent;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.game.*;
import fag.ware.client.util.math.Timer;
import net.minecraft.block.AirBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "ScaffoldWalk", description = "Places blocks under you", category = ModuleCategory.PLAYER)
public class ScaffoldWalkModule extends AbstractModule {
    private final StringSetting rotationMode = new StringSetting("Rotation mode", "Simple", "Simple", "Godbridge");
    private final StringSetting towerMode = new StringSetting("Tower mode", "None", "None", "Motion");
    private final StringSetting sprintMode = new StringSetting("Sprint mode", "None", "None", "Always", "Manual");
    private final StringSetting sneakMode = new StringSetting("Sneak mode", "None", "None", "Eagle", "Always");
    private final StringSetting itemSpoof = new StringSetting("Item spoof", "Switch", "Switch", "None");

    private final BooleanSetting keepY = new BooleanSetting("Keep Y", false);

    private final NumberSetting jumpDistance = (NumberSetting) new NumberSetting("Jump distance", 7, 1, 10)
            .hide(() -> !rotationMode.is("Godbridge"));
    private final NumberSetting sneakDelay = (NumberSetting) new NumberSetting("Sneak delay", 57, 0, 300)
            .hide(() -> sneakMode.is("None") || sneakMode.is("Always"));
    private final NumberSetting unSneakDelay = (NumberSetting) new NumberSetting("Unsneak delay", 287, 0, 300)
            .hide(() -> sneakMode.is("None") || sneakMode.is("Always"));

    private final Timer unSneakCounter = new Timer(), sneakCounter = new Timer();
    private final DistanceCounter distCounter = new DistanceCounter();
    private float[] lastRots, rots;
    private double posY;

    private BlockCache cache;

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player != null) {
            if (sprintMode.getValue().equals("Always")) {
                mc.options.sprintKey.setPressed(true);
            }

            if (mc.player.getActiveItem() == null) {
                switch (itemSpoof.getValue()) {
                    case "Switch":
                        InventoryUtil.switchToNextSlot();
                        break;
                }
            }

            if (!MovementUtil.isMoving() && mc.options.jumpKey.isPressed() && !mc.options.jumpKey.wasPressed()) {
                switch (towerMode.getValue()) {
                    case "Motion":
                        MovementUtil.setMotionY(0.42);
                        break;
                }
            }

            distCounter.tick(mc.player);
            if (rotationMode.is("Godbridge")) {
                if (distCounter.getTravelled() >= jumpDistance.toDouble()) {
                    if (mc.player.isOnGround()) {
                        mc.player.jump();
                    }
                    distCounter.reset();
                }
            }

            if (mc.player.getY() < posY || (!mc.player.isOnGround() && !MovementUtil.isMoving()) || mc.player.getY() - posY > 6 || !keepY.getValue()) {
                posY = mc.player.getY() - 0.9;
            }
            BlockPos playerBlockPos = new BlockPos((int) mc.player.getX(), (int) posY, (int) mc.player.getZ());
            cache = BlockCache.getCache(playerBlockPos);
        }
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (mc.player == null || !event.isPre()) {
            return;
        }

        switch (sneakMode.getValue()) {
            case "Eagle":
                if (mc.world.getBlockState(new BlockPos((int) mc.player.getX(), (int) (mc.player.getY() - 1.0), (int) mc.player.getZ())).getBlock() instanceof AirBlock && mc.player.isOnGround()) {
                    if (sneakCounter.hasElapsed(sneakDelay.getValue().longValue(), true)) {
                        mc.options.sneakKey.setPressed(true);
                    }
                } else {
                    if (unSneakCounter.hasElapsed(unSneakDelay.getValue().longValue(), true)) {
                        mc.options.sneakKey.setPressed(false);
                    }
                }
                break;
            case "Always":
                mc.options.sneakKey.setPressed(true);
                break;
        }

        if (mc.player != null && mc.player.getActiveItem() != null && cache != null) {
            ItemStack heldItemStack = mc.player.getActiveItem();

            if (rots != null && heldItemStack != null) {
                BlockHitResult result = new BlockHitResult(cache.getVec3d(), cache.facing(), cache.pos(), false);
                if (mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, result).isAccepted()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }

        if (cache != null && lastRots != null) {
            switch (rotationMode.getValue()) {
                case "Simple":
                    rots = RotationUtil.getSimpleRotations(cache, lastRots);
                    break;

                case "Godbridge":
                    rots = RotationUtil.getGodbridgeRotations(cache, lastRots);
                    break;
            }

            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
            lastRots = new float[]{event.getYaw(), event.getPitch()};
        } else {
            if (lastRots != null) {
                rots = lastRots;

                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
                lastRots = new float[]{event.getYaw(), event.getPitch()};
            }
        }
    }

    @Subscribe
    public void onSprint(SprintEvent event) {
        if (sprintMode.is("None") && rots != null) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onInit() {
        rotationMode.onChange(val -> {
            if (val.equals("Godbridge")) {
                keepY.setValue(false);
                sneakMode.setValue("None");
            }
        });
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            return;
        }

        boolean blocks = false;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            if (itemStack != null && itemStack.getItem() instanceof BlockItem) {
                blocks = true;
                break;
            }
        }

        if (blocks) {
            switch (itemSpoof.getValue()) {
                case "Switch":
                    InventoryUtil.switchToNextSlot();
                    break;
            }

            lastRots = new float[]{RotationUtil.getAdjustedYaw(), 90};
            posY = mc.player.getY() - 0.9;
        } else {
            toggle();
            sendError("You have no valid blocks in your hotbar!");
        }
    }

    @Override
    public void onDisable() {
        mc.options.sneakKey.setPressed(false);

        distCounter.reset();
        unSneakCounter.reset();
    }

    @Override
    public String getSuffix() {
        return rotationMode.getValue();
    }
}