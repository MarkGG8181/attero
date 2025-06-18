package io.github.client.event.impl.player;

import io.github.client.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.Vec3d;

@Getter
@Setter
public class MotionEvent extends Event {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;
    private State state = State.PRE;

    public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public Vec3d getPos() {
        return new Vec3d(x, y, z);
    }

    public boolean isPre() {
        return state.equals(State.PRE);
    }

    public enum State {
        PRE, POST;
    }
}