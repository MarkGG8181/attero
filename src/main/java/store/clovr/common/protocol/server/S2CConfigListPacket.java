package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;
import store.clovr.common.protocol.config.ConfigInfo;

import java.util.ArrayList;
import java.util.List;

public class S2CConfigListPacket implements Packet {
    public List<ConfigInfo> configs;
    public S2CConfigListPacket() {}
    public S2CConfigListPacket(List<ConfigInfo> configs) { this.configs = configs; }
    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(configs.size());
        for (ConfigInfo info : configs) {
            buf.writeLong(info.id);
            writeString(buf, info.name);
            writeString(buf, info.description);
            writeString(buf, info.uploaderUsername);
            buf.writeInt(info.upvotes);
            buf.writeInt(info.downvotes);
        }
    }
    @Override
    public void read(ByteBuf buf) {
        int size = buf.readInt();
        this.configs = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.configs.add(new ConfigInfo(buf.readLong(), readString(buf), readString(buf), readString(buf), buf.readInt(), buf.readInt()));
        }
    }
}