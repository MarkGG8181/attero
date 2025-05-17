package fag.ware.client.module.impl.combat;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.event.impl.RunLoopEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.*;
import fag.ware.client.tracker.impl.CombatTracker;
import fag.ware.client.util.math.ClickDelayCalculator;
import fag.ware.client.util.math.Timer;
import fag.ware.client.util.game.RotationUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

@SuppressWarnings("ALL")
@ModuleInfo(name = "KillAura", category = ModuleCategory.COMBAT, description = "Attacks entities in close proximity")
public class KillAuraModule extends AbstractModule {
    private final GroupSetting sortingGroup = new GroupSetting("Sorting", false);
    public final StringSetting sortBy = (StringSetting) new StringSetting("Sort by", "Range", "Range", "Health", "Armor", "Hurt-ticks").setParent(sortingGroup);
    public final NumberSetting searchRange = (NumberSetting) new NumberSetting("Search range", 5, 1, 6).setParent(sortingGroup);
    public final MultiStringSetting targets = (MultiStringSetting) new MultiStringSetting("Targets", new String[]{"Players"}, new String[]{"Players", "Animals", "Monsters", "Invisibles"}).setParent(sortingGroup);

    private final GroupSetting clickGroup = new GroupSetting("Clicking", false);

    private final StringSetting delayMode = (StringSetting) new StringSetting("Delay mode", "CPS", "1.9", "CPS").setParent(clickGroup);

    private final ClickDelayCalculator delayCalculator = new ClickDelayCalculator(9, 11);
    private final RangeNumberSetting cps = (RangeNumberSetting) new RangeNumberSetting("CPS", 9, 11, 1, 20).hide(() -> !delayMode.is("CPS")).onChange(newValue -> delayCalculator.setMinMax(newValue[0].doubleValue(), newValue[1].doubleValue())).setParent(clickGroup);

    private final BooleanSetting delayPatterns = (BooleanSetting) new BooleanSetting("Delay patterns", false).hide(() -> !delayMode.is("CPS")).onChange(delayCalculator::setPatternEnabled).setParent(clickGroup);
    private final NumberSetting delayPattern1 = (NumberSetting) new NumberSetting("Delay pattern 1", 90, 0, 700).hide(() -> !delayMode.is("CPS") || !delayPatterns.getValue()).onChange(newValue -> delayCalculator.setDelayPattern1(newValue.floatValue())).setParent(clickGroup);
    private final NumberSetting delayPattern2 = (NumberSetting) new NumberSetting("Delay pattern 2", 110, 0, 700).hide(() -> !delayMode.is("CPS") || !delayPatterns.getValue()).onChange(newValue -> delayCalculator.setDelayPattern2(newValue.floatValue())).setParent(clickGroup);
    private final NumberSetting delayPattern3 = (NumberSetting) new NumberSetting("Delay pattern 3", 130, 0, 700).hide(() -> !delayMode.is("CPS") || !delayPatterns.getValue()).onChange(newValue -> delayCalculator.setDelayPattern3(newValue.floatValue())).setParent(clickGroup);

    private final NumberSetting attackRange = (NumberSetting) new NumberSetting("Attack range", 3, 1, 6).setParent(clickGroup);

    public final NumberSetting aimRange = new NumberSetting("Aim range", 4.5, 1, 6);
    private final BooleanSetting raycast = new BooleanSetting("Raycast", true);

    private final Timer attackTimer = new Timer();

    @Subscribe(priority = 10)
    public void onMotion(MotionEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (CombatTracker.getInstance().target != null &&
                (raycast.getValue() && mc.player.canSee(CombatTracker.getInstance().target))) {

            float[] rots = RotationUtil.toRotation(CombatTracker.getInstance().target);

            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
        }
    }

    @Subscribe
    public void onRunLoop(RunLoopEvent event) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        if (CombatTracker.getInstance().target != null && CombatTracker.isWithinRange(CombatTracker.getInstance().target, attackRange.toDouble())) {
            switch (delayMode.getValue()) {
                case "1.9" -> {
                    if (mc.player.getAttackCooldownProgress(0) >= 1)
                        attackEntity(CombatTracker.getInstance().target);
                }

                case "CPS" -> {
                    if (attackTimer.hasElapsed(delayCalculator.getClickDelay(), true)) {
                        attackEntity(CombatTracker.getInstance().target);
                    }
                }
            }
        }
    }

    private void attackEntity(LivingEntity entity) {
        if (entity != null && entity.isAlive()) {
            mc.interactionManager.attackEntity(MinecraftClient.getInstance().player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    @Override
    public void onDisable() {
        CombatTracker.getInstance().target = null;
        attackTimer.reset();
        mc.options.attackKey.setPressed(false);
    }

    @Override
    public void onInit() {
        getKeybinds().add((int) 'R');
    }
}