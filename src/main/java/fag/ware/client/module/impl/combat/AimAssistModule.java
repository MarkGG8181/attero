package fag.ware.client.module.impl.combat;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.RangeNumberSetting;
import fag.ware.client.tracker.impl.CombatTracker;
import fag.ware.client.util.game.InventoryUtil;
import fag.ware.client.util.game.RotationUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

@ModuleInfo(
        name = "AimAssist",
        description = "Automatically aims for you",
        category = ModuleCategory.COMBAT
)
public class AimAssistModule extends AbstractModule {
    private final RangeNumberSetting speed = new RangeNumberSetting("Speed Min/Max", 10, 180, 10, 180);
    private final NumberSetting fov = new NumberSetting("FOV", 70, 180, 360);
    private final NumberSetting distance = new NumberSetting("Distance", 4.5, 1, 8);
    private final BooleanSetting onlyWeapon = new BooleanSetting("Only Weapon", false);

    private LivingEntity target;

    @Subscribe
    public void onRender(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        // Weapon check
        ItemStack mainHandStack = mc.player.getMainHandStack();
        if (onlyWeapon.getValue() && !(mainHandStack.getItem() instanceof AxeItem || InventoryUtil.isSword(mainHandStack)))
            return;

        // Get the target from CombatTracker
        target = CombatTracker.getInstance().target;

        if (target != null
                && CombatTracker.isWithinRange(target, distance.getValue().floatValue())) {
              //  && CombatTracker.getFovToEntity(target) <= fov.getValue().doubleValue()) {

            float[] rots = RotationUtil.toRotation(target, speed.getMinAsFloat(), speed.getMaxAsFloat());
            mc.player.setPitch(rots[1]);
            mc.player.setYaw(rots[0]);
        }
    }
}