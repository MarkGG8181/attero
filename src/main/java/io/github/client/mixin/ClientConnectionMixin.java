package io.github.client.mixin;

import io.github.client.event.impl.player.ReceivePacketEvent;
import io.github.client.event.impl.player.SendPacketEvent;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V"), cancellable = true)
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        ReceivePacketEvent receivePacketEvent = new ReceivePacketEvent(packet);
        receivePacketEvent.post();

        if (receivePacketEvent.cancelled) {
            ci.cancel();
        }
    }

    @Inject(method = "sendInternal", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, @Nullable ChannelFutureListener channelFutureListener, boolean flush, CallbackInfo ci) {
        SendPacketEvent sendPacketEvent = new SendPacketEvent(packet);
        sendPacketEvent.post();

        if (sendPacketEvent.cancelled) {
            ci.cancel();
        }
    }
}