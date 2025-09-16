package store.clovr.common.protocol;

import store.clovr.common.protocol.client.*;
import store.clovr.common.protocol.server.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketRegistry {
    private static final Map<Integer, Supplier<? extends Packet>> idToPacket = new HashMap<>();
    private static final Map<Class<? extends Packet>, Integer> packetToId = new HashMap<>();

    static {
        register(0, C2SLoginPacket::new);
        register(1, S2CLoginResponsePacket::new);
        register(2, S2CUserListPacket::new);
        register(3, S2CUserJoinPacket::new);
        register(4, S2CUserLeavePacket::new);
        register(5, C2SUpdateMinecraftUsernamePacket::new);
        register(6, S2CUserUpdatePacket::new);
        register(7, C2SChatMessagePacket::new);
        register(8, S2CChatMessagePacket::new);
        register(9, S2CAdminBroadcastPacket::new);
        register(10, C2SRequestConfigListPacket::new);
        register(11, S2CConfigListPacket::new);
        register(12, C2SRequestConfigDownloadPacket::new);
        register(13, S2CConfigDataPacket::new);

        register(14, C2SLauncherLoginPacket::new);
        register(15, S2CLauncherLoginResponsePacket::new);
        register(16, C2SRequestFilePacket::new);
        register(17, S2CFileDataPacket::new);
    }

    private static <T extends Packet> void register(int id, Supplier<T> constructor) {
        idToPacket.put(id, constructor);
        packetToId.put(constructor.get().getClass(), id);
    }

    public static Packet createPacket(int id) {
        Supplier<? extends Packet> constructor = idToPacket.get(id);
        if (constructor != null) {
            return constructor.get();
        }
        return null;
    }

    public static Integer getPacketId(Class<? extends Packet> clazz) {
        return packetToId.get(clazz);
    }
}