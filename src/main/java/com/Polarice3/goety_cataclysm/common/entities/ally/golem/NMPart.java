package com.Polarice3.goety_cataclysm.common.entities.ally.golem;

import com.Polarice3.goety_cataclysm.common.entities.ally.OwnedCMPart;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.gameevent.GameEvent;

public class NMPart extends OwnedCMPart<NetheriteMonstrosityServant> {
    private final EntityDimensions size;
    public float scale = 1.0F;

    public NMPart(NetheriteMonstrosityServant parent, float sizeX, float sizeY) {
        super(parent);
        this.size = EntityDimensions.scalable(sizeX, sizeY);
        this.refreshDimensions();
    }

    public NMPart(NetheriteMonstrosityServant nm, float sizeX, float sizeY, EntityDimensions size) {
        super(nm);
        this.size = size;
    }

    protected void setSize(EntityDimensions size) {
        super.setSize(size);
    }

    public boolean hurt(DamageSource source, float amount) {
        boolean flag = this.getParent() != null && this.getParent().attackEntityFromPart(this, source, amount * 1.5F);
        if (flag) {
            this.gameEvent(GameEvent.ENTITY_DAMAGE);
        }

        return flag;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException("fuck u");
    }

    public EntityDimensions getDimensions(Pose poseIn) {
        return this.size;
    }
}
