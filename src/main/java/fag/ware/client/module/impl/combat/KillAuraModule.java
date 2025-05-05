package fag.ware.client.module.impl.combat;

import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;

@ModuleInfo(name = "KillAura", category = ModuleCategory.COMBAT, description = "Attacks entities in close proximity")
public class KillAuraModule extends Module {
    private final StringSetting sortBy = new StringSetting("Sort by", "Range", "Range", "Health", "Armor", "Hurt-ticks");
    private final NumberSetting range = new NumberSetting("Range", 3, 1, 6);

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
