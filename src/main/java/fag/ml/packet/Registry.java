package fag.ml.packet;

import fag.ml.packet.impl.CAuthPacket;
import fag.ml.packet.impl.CLoadConfigPacket;
import fag.ml.packet.impl.SAuthPacket;
import fag.ml.packet.impl.SLoadConfigPacket;

import java.util.HashMap;
import java.util.Map;

public class Registry {
    private static final Map<Integer, Class<? extends AbstractPacket>> REGISTRY = new HashMap<>();

    static {
        register(0, CAuthPacket.class);
        register(1, SAuthPacket.class);
        register(2, CLoadConfigPacket.class);
        register(3, SLoadConfigPacket.class);
    }

    public static void register(int id, Class<? extends AbstractPacket> clazz) {
        REGISTRY.put(id, clazz);
    }

    public static AbstractPacket create(int id) throws Exception {
        Class<? extends AbstractPacket> clazz = REGISTRY.get(id);
        if (clazz == null) throw new IllegalArgumentException("Unknown packet " + id);

        return clazz.getDeclaredConstructor().newInstance();
    }

    public static int getPacketId(AbstractPacket packet) {
        for (Map.Entry<Integer, Class<? extends AbstractPacket>> entry : REGISTRY.entrySet()) {
            if (entry.getValue() == packet.getClass()) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Packet not registered: " + packet.getClass());
    }
}