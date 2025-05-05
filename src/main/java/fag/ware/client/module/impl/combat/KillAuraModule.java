package fag.ware.client.module.impl.combat;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.player.RotationUtil;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(name = "KillAura", category = ModuleCategory.COMBAT, description = "Attacks entities in close proximity")
public class KillAuraModule extends Module {
    public final StringSetting sortBy = new StringSetting("Sort by", "Range", "Range", "Health", "Armor", "Hurt-ticks");

    private final NumberSetting attackRange = new NumberSetting("Attack range", 3, 1, 6);
    public final NumberSetting searchRange = new NumberSetting("Search range", 5, 1, 6);
    public final NumberSetting aimRange = new NumberSetting("Aim range", 4.5, 1, 6);

    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting animals = new BooleanSetting("Animals", false);
    public final BooleanSetting monsters = new BooleanSetting("Monsters", false);
    public final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (event.isPre()) {
            if (Fagware.INSTANCE.combatTracker.target != null) {
                Fagware.INSTANCE.combatTracker.target.setGlowing(true);
                float[] rots = RotationUtil.toRotation(Fagware.INSTANCE.combatTracker.target);

                mc.player.setYaw(rots[0]);
                mc.player.setPitch(rots[1]);
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        Fagware.INSTANCE.combatTracker.target = null;
    }

    @Override
    public void onInit() {
        setKeybind(GLFW.GLFW_KEY_R);
    }
}
