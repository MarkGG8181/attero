package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.mixin.IItemCoolDownAccessor;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModuleInfo(name = "FastPlace", category = ModuleCategory.PLAYER, description = "Places stuff fast")
public class FastPlaceModule extends AbstractModule {
    private final BooleanSetting blocks =  new BooleanSetting("Blocks", true);
    private final BooleanSetting xp = new BooleanSetting("XP", true);

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isHolding(Items.BOW) || mc.player.isUsingItem()) return;

        ItemStack itemstack = mc.player.getMainHandStack();
        if (xp.getValue() && mc.player.isHolding(Items.EXPERIENCE_BOTTLE) || blocks.getValue() && itemstack.getItem() instanceof BlockItem) {
            ((IItemCoolDownAccessor) mc).setItemUseCooldown(0);
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}