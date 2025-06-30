package io.github.client.module.impl.combat;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.rotate.AbstractRotator;
import io.github.client.module.data.setting.impl.*;
import io.github.client.util.game.EntityUtil;
import io.github.client.util.java.math.ClickDelayCalculator;
import io.github.client.util.java.math.Timer;
import io.github.client.util.game.RotationUtil;
import net.minecraft.entity.LivingEntity;

@SuppressWarnings("ALL")
@ModuleInfo(name = "KillAura", category = ModuleCategory.COMBAT, description = "Attacks entities in close proximity")
public class KillAuraModule extends AbstractRotator {
    private final GroupSetting sortingGroup = new GroupSetting("Sorting", false);
    public final StringSetting sortBy = new StringSetting("Sort by", "Range", "Range", "Health", "Armor", "Hurt-ticks").setParent(sortingGroup);
    public final NumberSetting searchRange = new NumberSetting("Search range", 5, 1, 6).setParent(sortingGroup);
    public final MultiStringSetting targets = new MultiStringSetting("Targets", new String[]{"Players"}, new String[]{"Players", "Animals", "Monsters", "Invisibles"}).setParent(sortingGroup);

    private final GroupSetting clickGroup = new GroupSetting("Clicking", false);

    private final StringSetting delayMode = new StringSetting("Delay mode", "CPS", "1.9", "CPS").setParent(clickGroup);

    private final ClickDelayCalculator delayCalculator = new ClickDelayCalculator(9, 11);
    private final RangeNumberSetting cps = new RangeNumberSetting("CPS", 9, 11, 1, 20).
            onChange(newValue -> delayCalculator.setMinMax(newValue[0].doubleValue(), newValue[1].doubleValue())).setParent(clickGroup).hide(() -> !delayMode.is("CPS"));

    private final BooleanSetting delayPatterns = new BooleanSetting("Delay patterns", false).onChange(delayCalculator::setPatternEnabled).setParent(clickGroup).hide(() -> !delayMode.is("CPS"));
    private final NumberSetting delayPattern1 = new NumberSetting("Delay pattern 1", 90, 0, 700).onChange(newValue -> delayCalculator.setDelayPattern1(newValue.doubleValue())).setParent(clickGroup).hide(() -> !delayMode.is("CPS") || !delayPatterns.getValue());
    private final NumberSetting delayPattern2 = new NumberSetting("Delay pattern 2", 110, 0, 700).onChange(newValue -> delayCalculator.setDelayPattern2(newValue.doubleValue())).setParent(clickGroup).hide(() -> !delayMode.is("CPS") || !delayPatterns.getValue());
    private final NumberSetting delayPattern3 = new NumberSetting("Delay pattern 3", 130, 0, 700).onChange(newValue -> delayCalculator.setDelayPattern3(newValue.doubleValue())).setParent(clickGroup).hide(() -> !delayMode.is("CPS") || !delayPatterns.getValue());

    private final NumberSetting attackRange = new NumberSetting("Attack range", 3, 1, 6).setParent(clickGroup);

    private final GroupSetting rotationGroup = new GroupSetting("Rotations", false);
    private final RangeNumberSetting speed = new RangeNumberSetting("Speed", 10, 180, 10, 180).setParent(rotationGroup);

    public final NumberSetting aimRange = new NumberSetting("Aim range", 4.5, 1, 6);
    private final BooleanSetting raycast = new BooleanSetting("Raycast", true);

    private final Timer attackTimer = new Timer();
    private LivingEntity target;

    public KillAuraModule() {
        super(100);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        target = EntityUtil.getTarget(target, aimRange.toDouble(), searchRange.toDouble(), sortBy.getValue(),
                targets.enabled("Players"), targets.enabled("Animals"), targets.enabled("Monsters"), targets.enabled("Invisibles"));

        if (target != null && EntityUtil.isWithinRange(target, attackRange.toDouble())) {
            switch (delayMode.getValue()) {
                case "1.9" -> {
                    if (mc.player.getAttackCooldownProgress(0) >= 1)
                        EntityUtil.attackEntity(target);
                }

                case "CPS" -> {
                    if (attackTimer.hasElapsed(delayCalculator.getClickDelay(), true)) {
                        EntityUtil.attackEntity(target);
                    }
                }
            }
        }
    }

    public void onDisable() {
        target = null;
        attackTimer.reset();
        mc.options.attackKey.setPressed(false);
    }

    public void onInit() {
        getKeybinds().add((int) 'R');
    }

    @Override
    public String getSuffix() {
        return "Single";
    }

    @Override
    public float[] shouldRotate() {
        if (target != null &&
                (raycast.getValue() && mc.player.canSee(target))) {

            var minSpeed = speed.getMinAsFloat();
            var maxSpeed = speed.getMaxAsFloat();

            var rots = RotationUtil.toRotation(
                    target,
                    minSpeed,
                    maxSpeed
            );

            return rots;
        }

        return null;
    }
}