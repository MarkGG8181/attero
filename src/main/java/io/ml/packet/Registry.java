package io.ml.packet;

import io.ml.packet.impl.client.CAuthPacket;
import io.ml.packet.impl.client.CFetchConfigsPacket;
import io.ml.packet.impl.client.CJVMChecksumPacket;
import io.ml.packet.impl.client.CLoadConfigPacket;
import io.ml.packet.impl.server.SAuthPacket;
import io.ml.packet.impl.server.SFetchConfigsPacket;
import io.ml.packet.impl.server.SJVMChecksumPacket;
import io.ml.packet.impl.server.SLoadConfigPacket;

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
        register(6, CJVMChecksumPacket.class);
        register(7, SJVMChecksumPacket.class);
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