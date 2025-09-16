package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;
import store.clovr.common.protocol.launcher.OwnedProductInfo;

import java.util.ArrayList;
import java.util.List;

public class S2CLauncherLoginResponsePacket implements Packet {
    public boolean success;
    public List<OwnedProductInfo> ownedProducts;

    public S2CLauncherLoginResponsePacket() {}

    public S2CLauncherLoginResponsePacket(boolean success, List<OwnedProductInfo> ownedProducts) {
        this.success = success;
        this.ownedProducts = ownedProducts;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(success);
        if (success) {
            buf.writeInt(ownedProducts.size());
            for (OwnedProductInfo info : ownedProducts) {
                buf.writeLong(info.productId);
                writeString(buf, info.productName);
                writeString(buf, info.productVersion);
            }
        }
    }

    @Override
    public void read(ByteBuf buf) {
        this.success = buf.readBoolean();
        if (success) {
            int size = buf.readInt();
            this.ownedProducts = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                this.ownedProducts.add(new OwnedProductInfo(buf.readLong(), readString(buf), readString(buf)));
            }
        }
    }
}