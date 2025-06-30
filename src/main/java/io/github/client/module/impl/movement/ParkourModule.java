package io.github.client.module.impl.movement;

import com.google.common.collect.Streams;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.util.java.math.Timer;

@SuppressWarnings("ALL")
@ModuleInfo(name = "Parkour", description = "Jumps on the edge of blocks", category = ModuleCategory.MOVEMENT)
public class ParkourModule extends AbstractModule {
    private final NumberSetting delay = new NumberSetting("Jump delay", 30, 0, 300);

    private final Timer timer = new Timer();

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (event.isPre()) return;
        if (!mc.player.isOnGround() || mc.options.jumpKey.isPressed()) return;
        if (mc.player.isSneaking() || mc.options.sneakKey.isPressed()) return;

        var box = mc.player.getBoundingBox();
        var adjustedBox = box.offset(0, -0.5, 0).expand(-0.001, 0, -0.001);

        var blockCollisions = Streams.stream(mc.world.getBlockCollisions(mc.player, adjustedBox));

        if (blockCollisions.findAny().isPresent()) return;

        if (timer.hasElapsed(delay.toInt(), true)) {
            mc.player.jump();
        }
    }
}
