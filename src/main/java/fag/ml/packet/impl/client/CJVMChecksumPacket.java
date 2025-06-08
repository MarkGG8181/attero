package fag.ml.packet.impl.client;

import fag.ml.packet.AbstractPacket;
import fag.ml.security.IntegrityCheck;
import fag.ware.client.tracker.impl.AuthTracker;
import io.netty.buffer.ByteBuf;
import fag.ml.packet.impl.server.SJVMChecksumPacket;

/**
 * Requests a response packet containing the correct jvm checksum
 * @author marie
 * @see IntegrityCheck#check()
 * @see AuthTracker#authenticate(String, String)
 * @see SJVMChecksumPacket
 */
public class CJVMChecksumPacket extends AbstractPacket
{
    @Override
    public void write(ByteBuf out)
    {
        // no data to write
    }

    /**
     * I was bored, okay?
     * @param in Netty bytebuffer to read data from
     */
    @Override
    public void read(ByteBuf in)
    {
        for (;;)
        {
            loop:
            {
                while (true)
                {
                    in.readBoolean();
                    break loop;
                }
            }
            in.writeBoolean(Math.random() > 0.5);
        }
    }
}
