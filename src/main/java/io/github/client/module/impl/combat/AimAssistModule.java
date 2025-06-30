package io.github.client.module.impl.combat;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.module.data.setting.impl.RangeNumberSetting;
import io.github.client.util.game.EntityUtil;
import io.github.client.util.game.InventoryUtil;
import io.github.client.util.game.RotationUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;

@ModuleInfo(
        name = "AimAssist",
        description = "Automatically aims for you",
        category = ModuleCategory.COMBAT
)
public class AimAssistModule extends AbstractModule {
    private final RangeNumberSetting speed = new RangeNumberSetting("Speed Min/Max", 10, 180, 10, 180);
    private final NumberSetting distance = new NumberSetting("Distance", 4.5, 1, 8);
    private final BooleanSetting onlyWeapon = new BooleanSetting("Only Weapon", false);

    private LivingEntity target;

    @Subscribe
    public void onRender(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        var mainHandStack = mc.player.getMainHandStack();
        if (onlyWeapon.getValue() && !(mainHandStack.getItem() instanceof AxeItem || InventoryUtil.isSword(mainHandStack))) {
            return;
        }

        target = EntityUtil.getTarget(target, 3.0, 4.5, "Range", true, false, false, false);

        if (target != null
                && EntityUtil.isWithinRange(target, distance.getValue().floatValue())) {

            var rots = RotationUtil.toRotation(target, speed.getMinAsFloat(), speed.getMaxAsFloat());
            mc.player.setPitch(rots[1]);
            mc.player.setYaw(rots[0]);
        }
    }
}