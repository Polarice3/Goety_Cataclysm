package com.Polarice3.goety_cataclysm.common.entities.ally.golem;

import com.github.L_Ender.cataclysm.entity.partentity.Cm_Part_Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.gameevent.GameEvent;

public class NMPart extends Cm_Part_Entity<NetheriteMonstrosityServant> {
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

    protected void defineSynchedData() {
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean isPickable() {
        return this.getParent().isAlive();
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

    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    protected void setRot(float yaw, float pitch) {
        this.setYRot(yaw % 360.0F);
        this.setXRot(pitch % 360.0F);
    }

    protected boolean canRide(Entity entityIn) {
        return false;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException("fuck u");
    }

    @Override
    public boolean isAlliedTo(Entity p_20355_) {
        if (p_20355_ == this.getParent()){
            return true;
        }
        return super.isAlliedTo(p_20355_);
    }

    public EntityDimensions getDimensions(Pose poseIn) {
        return this.size;
    }
}
