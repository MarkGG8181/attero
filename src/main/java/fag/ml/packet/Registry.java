package fag.ml.packet;

import fag.ml.packet.impl.*;

import java.util.HashMap;
import java.util.Map;

public final class Registry {
    private static final Map<Integer, Class<? extends AbstractPacket>> REGISTRY = new HashMap<>();

    static {
        register(0, CAuthPacket.class);
        register(1, SAuthPacket.class);
        register(2, CLoadConfigPacket.class);
        register(3, SLoadConfigPacket.class);
        register(4, CFetchConfigsPacket.class);
        register(5, SFetchConfigsPacket.class);
    }

    public static void register(final int id,
                                final Class<? extends AbstractPacket> clazz) {
        REGISTRY.put(id, clazz);
    }

    public static AbstractPacket create(final int id) throws Exception
    {
        final Class<? extends AbstractPacket> clazz = REGISTRY.get(id);
        if (clazz == null) throw new IllegalArgumentException("Unknown packet " + id);

        return clazz.getDeclaredConstructor().newInstance();
    }

    public static int getPacketId(final AbstractPacket packet)
    {
        for (final Map.Entry<Integer, Class<? extends AbstractPacket>> entry : REGISTRY.entrySet())
        {
            if (entry.getValue() == packet.getClass())
            {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException("Packet not registered: " + packet.getClass());
    }
}