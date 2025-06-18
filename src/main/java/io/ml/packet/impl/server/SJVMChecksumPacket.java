package io.ml.packet.impl.server;

import io.ml.packet.AbstractPacket;
import io.ml.security.IntegrityCheck;
import io.github.client.screen.data.ImGuiImpl;
import io.github.client.tracker.impl.AuthTracker;
import io.netty.buffer.ByteBuf;
import io.ml.packet.impl.client.CJVMChecksumPacket;

/**
 * Packet containing the correct jvm checksum
 * @author marie
 * @see IntegrityCheck#check()
 * @see AuthTracker#authenticate(String, String)
 * @see CJVMChecksumPacket
 */
public class SJVMChecksumPacket extends AbstractPacket {
    @Override
    public void write(ByteBuf out) {
        for (;;); // no
    }

    @Override
    public void read(ByteBuf in) {
        ImGuiImpl.correctChecksum = in.readLong();
    }
}
