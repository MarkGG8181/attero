package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.event.impl.SprintEvent;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.game.*;
import fag.ware.client.util.math.Timer;
import net.minecraft.block.AirBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "ScaffoldWalk", description = "Places blocks under you", category = ModuleCategory.PLAYER)
public class ScaffoldWalkModule extends AbstractModule {

    private final StringSetting rotationMode = new StringSetting("Rotation mode", "Simple", "Simple", "Godbridge");
    private final StringSetting towerMode = new StringSetting("Tower mode", "None", "None", "Motion", "Vulcan");
    private final StringSetting sprintMode = new StringSetting("Sprint mode", "None", "None", "Always", "Manual");
    private final StringSetting sneakMode = new StringSetting("Sneak mode", "None", "None", "Eagle", "Always", "Blatant");
    private final StringSetting itemSpoof = new StringSetting("Item spoof", "Switch", "Switch", "None");

    private final BooleanSetting keepY = new BooleanSetting("Keep Y", false);

    private final NumberSetting jumpDistance = (NumberSetting) new NumberSetting("Jump distance", 7, 1, 10)
            .hide(() -> !rotationMode.is("Godbridge"));
    private final NumberSetting sneakDistance = (NumberSetting) new NumberSetting("Sneak distance", 1, 1, 10)
            .hide(() -> sneakMode.is("None") || sneakMode.is("Always"));
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
            } else if (!sneakMode.is("None") && sneakMode.getValue().equals("Blatant")) {
                if (unSneakCounter.hasElapsed(unSneakDelay.getValue().longValue(), true)) {
                    mc.options.sneakKey.setPressed(false);
                }

                if (distCounter.getTravelled() >= sneakDistance.toDouble()) {
                    if (sneakCounter.hasElapsed(sneakDelay.getValue().longValue(), true)) {
                        mc.options.sneakKey.setPressed(true);
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
        lastRots = new float[]{RotationUtil.getAdjustedYaw(), 80.7f};
        posY = mc.player.getY() - 0.9;

        switch (itemSpoof.getValue()) {
            case "Switch":
                InventoryUtil.switchToNextSlot();
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.options.sneakKey.setPressed(false);

        distCounter.reset();
        unSneakCounter.reset();
    }
}