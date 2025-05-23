package fag.ware.client.module.impl.movement;

import com.google.common.collect.Streams;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.player.MotionEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.math.Timer;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.util.stream.Stream;

@ModuleInfo(name = "Parkour", description = "Jumps on the edge of blocks", category = ModuleCategory.MOVEMENT)
public class ParkourModule extends AbstractModule {
    private final NumberSetting delay = new NumberSetting("Jump delay", 30, 0, 300);

    private final Timer timer = new Timer();

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (event.isPre()) return;
        if (!mc.player.isOnGround() || mc.options.jumpKey.isPressed()) return;
        if (mc.player.isSneaking() || mc.options.sneakKey.isPressed()) return;

        Box box = mc.player.getBoundingBox();
        Box adjustedBox = box.offset(0, -0.5, 0).expand(-0.001, 0, -0.001);

        Stream<VoxelShape> blockCollisions = Streams.stream(mc.world.getBlockCollisions(mc.player, adjustedBox));

        if (blockCollisions.findAny().isPresent()) return;

        if (timer.hasElapsed(delay.toInt(), true))
            mc.player.jump();
    }
}
