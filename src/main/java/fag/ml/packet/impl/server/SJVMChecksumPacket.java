package fag.ml.packet.impl.server;

import fag.ml.packet.AbstractPacket;
import fag.ml.security.IntegrityCheck;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.tracker.impl.AuthTracker;
import io.netty.buffer.ByteBuf;
import fag.ml.packet.impl.client.CJVMChecksumPacket;

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
