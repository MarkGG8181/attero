package fag.ware.client.module.impl.world;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.event.impl.RunLoopEvent;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.tracker.impl.CombatTracker;
import fag.ware.client.util.game.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("ALL")
@ModuleInfo(name = "CrystalAura", description = "Attacks nearby crystals", category = ModuleCategory.WORLD)
public class CrystalAuraModule extends AbstractModule {

    private final NumberSetting searchRange = new NumberSetting("Search range", 5, 1, 10);
    private final NumberSetting attackRange = new NumberSetting("Attack range", 3, 1, 6);
    private final NumberSetting aimRange = new NumberSetting("Aim range", 4.5, 1, 6);
    private final BooleanSetting raycast = new BooleanSetting("Raycast", true);

    private final HashSet<EndCrystalEntity> targets = new HashSet<>();
    private EndCrystalEntity target;

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (target != null && (!target.isAlive() || !CombatTracker.isWithinRange(target, aimRange.toDouble()))) {
            target = null;
            targets.clear();
        }

        if (target == null) {
            double range = searchRange.toDouble();

            List<EndCrystalEntity> entitiesToConsider = mc.world.getEntitiesByClass(
                    EndCrystalEntity.class,
                    mc.player.getBoundingBox().expand(range),
                    entity -> entity.isAttackable()
                            && CombatTracker.isWithinRange(entity, range)
            );

            entitiesToConsider.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo(entity)));

            targets.clear();
            targets.addAll(entitiesToConsider);

            EndCrystalEntity localTarget = targets.stream().findFirst().orElse(null);
            if (localTarget != null && CombatTracker.isWithinRange(localTarget, aimRange.toDouble())) {
                target = localTarget;
            }
        }
    }

    @Subscribe(priority = 10)
    public void onMotion(MotionEvent event) {
        if (target != null &&
                (raycast.getValue() && mc.player.canSee(target))) {

            float[] rots = RotationUtil.toRotation(target);

            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || mc.player.distanceTo(player) > searchRange.toFloat()) {
                continue;
            }

            BlockPos entityPos = player.getBlockPos();
            BlockPos check = entityPos.add(0, -1, 0);
            BlockState bs = mc.world.getBlockState(check);
            Block block = bs.getBlock();

            if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
                for (int slot = 0; slot < 9; slot++) {
                    Item item = mc.player.getInventory().getStack(slot).getItem();
                    if (item == Items.END_CRYSTAL) {
                        mc.player.getInventory().setSelectedSlot(slot);
                        BlockHitResult rayTrace = new BlockHitResult(check.toCenterPos(), Direction.UP, check, false);
                        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, rayTrace);
                        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
                        break;
                    }
                }
            }
        }
    }

    @Subscribe
    public void onRunLoop(RunLoopEvent event) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        if (target != null && CombatTracker.isWithinRange(target, attackRange.toDouble())) {
            if (mc.player.getAttackCooldownProgress(0) >= 1)
                CombatTracker.attackEntity(target);
        }
    }
}