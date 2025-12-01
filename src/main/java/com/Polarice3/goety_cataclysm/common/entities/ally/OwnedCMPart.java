package com.Polarice3.goety_cataclysm.common.entities.ally;

import com.Polarice3.Goety.api.entities.IOwned;
import com.github.L_Ender.cataclysm.entity.partentity.Cm_Part_Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

public abstract class OwnedCMPart<T extends LivingEntity & IOwned> extends Cm_Part_Entity<T> {
    public OwnedCMPart(T parent) {
        super(parent);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {

    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    public boolean isPickable() {
        return this.getParent().isAlive();
    }

    @Override
    protected void setSize(EntityDimensions size) {
        super.setSize(size);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    protected void setRot(float yaw, float pitch) {
        this.setYRot(yaw % 360.0F);
        this.setXRot(pitch % 360.0F);
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean isAlliedTo(Entity p_20355_) {
        if (p_20355_ == this.getParent()){
            return true;
        }
        return super.isAlliedTo(p_20355_);
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }
}
