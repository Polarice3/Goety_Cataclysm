package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractIgnisFireball extends AbstractHurtingProjectile {
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(AbstractIgnisFireball.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> OWNER_CLIENT_ID = SynchedEntityData.defineId(AbstractIgnisFireball.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Optional<UUID>> TARGET_UNIQUE_ID = SynchedEntityData.defineId(AbstractIgnisFireball.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> TARGET_CLIENT_ID = SynchedEntityData.defineId(AbstractIgnisFireball.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> DATA_EXTRA_DAMAGE = SynchedEntityData.defineId(AbstractIgnisFireball.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AbstractIgnisFireball.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> FIRED = SynchedEntityData.defineId(AbstractIgnisFireball.class, EntityDataSerializers.BOOLEAN);
    public int timer;

    public AbstractIgnisFireball(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public AbstractIgnisFireball(EntityType<? extends AbstractHurtingProjectile> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        super(p_36817_, p_36818_, p_36819_, p_36820_, p_36821_, p_36822_, p_36823_, p_36824_);
    }

    public AbstractIgnisFireball(EntityType<? extends AbstractHurtingProjectile> p_36826_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_) {
        super(p_36826_, p_36827_, p_36828_, p_36829_, p_36830_, p_36831_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(OWNER_CLIENT_ID, -1);
        this.entityData.define(TARGET_UNIQUE_ID, Optional.empty());
        this.entityData.define(TARGET_CLIENT_ID, -1);
        this.entityData.define(DATA_EXTRA_DAMAGE, 0.0F);
        this.entityData.define(FIRED, false);
        this.defineRadius();
    }

    public void defineRadius(){
        this.entityData.define(RADIUS, 1.0F);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Owner")) {
            this.setOwnerId(compound.getUUID("Owner"));
        }

        if (compound.contains("OwnerClient")){
            this.setOwnerClientId(compound.getInt("OwnerClient"));
        }

        if (compound.hasUUID("Target")) {
            this.setTargetId(compound.getUUID("Target"));
        }

        if (compound.contains("TargetClient")){
            this.setTargetClientId(compound.getInt("TargetClient"));
        }

        if (compound.contains("ExtraDamage")) {
            this.setExtraDamage(compound.getFloat("ExtraDamage"));
        }
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
        if (this.getOwnerClientId() > -1) {
            compound.putInt("OwnerClient", this.getOwnerClientId());
        }
        if (this.getTargetId() != null) {
            compound.putUUID("Target", this.getTargetId());
        }
        if (this.getTargetClientId() > -1) {
            compound.putInt("TargetClient", this.getTargetClientId());
        }
        compound.putFloat("ExtraDamage", this.getExtraDamage());
    }

    @Nullable
    public LivingEntity getOwner() {
        if (!this.level().isClientSide){
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } else {
            int id = this.getOwnerClientId();
            return id <= -1 ? null : this.level().getEntity(this.getOwnerClientId()) instanceof LivingEntity living ? living : null;
        }
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.getOwnerId();
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public int getOwnerClientId(){
        return this.entityData.get(OWNER_CLIENT_ID);
    }

    public void setOwnerClientId(int id){
        this.entityData.set(OWNER_CLIENT_ID, id);
    }

    @Nullable
    public LivingEntity getTarget() {
        if (!this.level().isClientSide){
            UUID uuid = this.getTargetId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } else {
            int id = this.getTargetClientId();
            return id <= -1 ? null : this.level().getEntity(this.getTargetClientId()) instanceof LivingEntity living ? living : null;
        }
    }

    @Nullable
    public UUID getTargetId() {
        return this.entityData.get(TARGET_UNIQUE_ID).orElse(null);
    }

    public void setTargetId(@Nullable UUID p_184754_1_) {
        this.entityData.set(TARGET_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public int getTargetClientId(){
        return this.entityData.get(TARGET_CLIENT_ID);
    }

    public void setTargetClientId(int id){
        this.entityData.set(TARGET_CLIENT_ID, id);
    }

    public void setOwner(LivingEntity livingEntity){
        if (livingEntity != null) {
            this.setOwnerId(livingEntity.getUUID());
            this.setOwnerClientId(livingEntity.getId());
        }
    }

    public void setTarget(@Nullable LivingEntity livingEntity) {
        if (livingEntity != null) {
            this.setTargetId(livingEntity.getUUID());
            this.setTargetClientId(livingEntity.getId());
        }
    }

    public float getExtraDamage() {
        return this.entityData.get(DATA_EXTRA_DAMAGE);
    }

    public void setExtraDamage(float pDamage) {
        this.entityData.set(DATA_EXTRA_DAMAGE, pDamage);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(float pDamage) {
        this.entityData.set(RADIUS, pDamage);
    }

    public boolean isOnFire() {
        return false;
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            --this.timer;
            if (this.timer <= 0 && !this.getFired()) {
                this.setFired(true);
            }
        }

        if (this.timer < -160) {
            this.discard();
        }

    }

    public void setUp(int delay) {
        this.setFired(false);
        this.timer = delay;
    }

    public void setFired(boolean fired) {
        this.entityData.set(FIRED, fired);
    }

    public boolean getFired() {
        return this.entityData.get(FIRED);
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity);
            } else {
                if(MobUtil.areAllies(this.getOwner(), pEntity)){
                    return false;
                }
                if (this.getOwner() instanceof Enemy && pEntity instanceof Enemy){
                    return false;
                }
                if (pEntity instanceof Projectile projectile && projectile.getOwner() == this.getOwner()){
                    return false;
                }
                if (pEntity instanceof IOwned owned0 && this.getOwner() instanceof IOwned owned1){
                    return !MobUtil.ownerStack(owned0, owned1);
                }
            }
        }
        return super.canHitEntity(pEntity);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
