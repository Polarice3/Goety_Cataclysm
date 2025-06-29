package com.Polarice3.goety_cataclysm.common.network.server;

import com.github.L_Ender.cataclysm.client.particle.RingParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SRingParticlePacket {
    public float yaw;
    public float pitch;
    public int duration;
    public int red;
    public int green;
    public int blue;
    public float alpha;
    public float scale;
    public boolean grow;
    public int mode = 0;
    public double x;
    public double y;
    public double z;

    public SRingParticlePacket(float yaw,
                               float pitch,
                               int duration,
                               int red,
                               int green,
                               int blue,
                               float alpha,
                               float scale,
                               boolean grow,
                               int mode,
                               double x,
                               double y,
                               double z){
        this.yaw = yaw;
        this.pitch = pitch;
        this.duration = duration;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.scale = scale;
        this.grow = grow;
        this.mode = mode;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(SRingParticlePacket packet, FriendlyByteBuf buffer) {
        buffer.writeFloat(packet.yaw);
        buffer.writeFloat(packet.pitch);
        buffer.writeInt(packet.duration);
        buffer.writeInt(packet.red);
        buffer.writeInt(packet.green);
        buffer.writeInt(packet.blue);
        buffer.writeFloat(packet.alpha);
        buffer.writeFloat(packet.scale);
        buffer.writeBoolean(packet.grow);
        buffer.writeInt(packet.mode);
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
    }

    public static SRingParticlePacket decode(FriendlyByteBuf buffer) {
        return new SRingParticlePacket(buffer.readFloat(),
                buffer.readFloat(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readBoolean(),
                buffer.readInt(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble());
    }

    public static void consume(SRingParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel clientWorld = Minecraft.getInstance().level;

            if (clientWorld != null){
                RingParticle.EnumRingBehavior behavior = RingParticle.EnumRingBehavior.SHRINK;
                if (packet.mode == 1) {
                    behavior = RingParticle.EnumRingBehavior.GROW;
                } else if (packet.mode == 2) {
                    behavior = RingParticle.EnumRingBehavior.CONSTANT;
                } else if (packet.mode == 3) {
                    behavior = RingParticle.EnumRingBehavior.GROW_THEN_SHRINK;
                }
                clientWorld.addParticle(new RingParticle.RingData(packet.yaw, packet.pitch, packet.duration, packet.red/255F, packet.green/255F, packet.blue/255F, packet.alpha, packet.scale, packet.grow, behavior), packet.x, packet.y, packet.z, 0, 0, 0);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
