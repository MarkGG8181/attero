package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.mixin.MinecraftClientAccessor;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.game.InventoryUtil;
import fag.ware.client.util.game.MovementUtil;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@ModuleInfo(name = "NoFall", category = ModuleCategory.PLAYER, description = "Makes you take 0 fall damage")
public class NoFallModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Vanilla", "Vanilla", "Verus", "MLG");
    private final StringSetting vanillaMode = (StringSetting) new StringSetting("Vanilla mode", "Packet-Full", "Packet-Full", "Packet-OnGround", "Set ground").hide(() -> !mode.is("Vanilla"));
    private final NumberSetting distance = new NumberSetting("Distance", 3.35, 1, 5);

    @Subscribe
    public void onTick(MotionEvent event) {
        if (event.isPre() && mc.player.fallDistance >= distance.toFloat()) {
            switch (mode.getValue()) {
                case "Vanilla" -> {
                    switch (vanillaMode.getValue()) {
                        case "Packet-OnGround" ->
                                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
                        case "Packet-Full" ->
                                mc.player.clientWorld.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), true, mc.player.horizontalCollision));
                        case "Set ground" -> mc.player.setOnGround(true);
                    }
                    mc.player.fallDistance = 0;
                }
                case "Verus" -> {
                    mc.player.setOnGround(true);
                    MovementUtil.setMotionY(0.0);
                    mc.player.fallDistance = 0;
                }
                case "MLG" -> {
                    InventoryUtil.switchToSlot(Items.WATER_BUCKET);
                    float prevPitch = mc.player.getPitch();
                    mc.player.setPitch(90.0F);
                    ((MinecraftClientAccessor) mc).invokeDoItemUse();
                    mc.player.setPitch(prevPitch);
                }
            }
        }
    }
}