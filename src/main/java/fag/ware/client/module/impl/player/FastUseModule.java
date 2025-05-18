package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.mixin.MinecraftClientAccessor;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.math.Timer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(name = "FastUse", category = ModuleCategory.PLAYER, description = "Makes item use faster")
public class FastUseModule extends AbstractModule {
    public final NumberSetting delay = new NumberSetting("Delay", 100, 0, 750);
    private final BooleanSetting blocks = new BooleanSetting("Blocks", true);
    private final BooleanSetting xp = new BooleanSetting("XP", true);
    private final BooleanSetting legitClicking = new BooleanSetting("Legit Clicking", true);
    Timer timer = new Timer();
    private boolean should;

    @Subscribe
    public void onTick(TickEvent event) {
        // mc.player.isHolding(Items.BOW)
        if (mc.player == null || mc.world == null || mc.player.isUsingItem()) return;

        should = holdingItems();

        if (!delay()) return;

        if (should) {
            if (!legitClicking.getValue()) {
                ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
            if (legitClicking.getValue()) {
                ((MinecraftClientAccessor) mc).invokeDoItemUse();
               }
            }
        }
    }

    public boolean holdingItems() {
        ItemStack itemstack = mc.player.getMainHandStack();
        return mc.player.isHolding(Items.EXPERIENCE_BOTTLE) && xp.getValue() || blocks.getValue() && itemstack.getItem() instanceof BlockItem;
    }


    public boolean delay() {
        return timer.hasElapsed(delay.toInt(), true);
    }
}