package store.clovr.common.protocol.server;


import store.clovr.common.user.ClovrUser;

public class S2CUserUpdatePacket extends S2CUserJoinPacket {
    public S2CUserUpdatePacket() {}

    public S2CUserUpdatePacket(ClovrUser user) {
        super(user);
    }
}