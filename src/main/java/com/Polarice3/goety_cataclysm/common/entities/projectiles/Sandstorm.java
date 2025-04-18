package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.particle.StormParticle;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Sandstorm extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> CREATOR_ID = SynchedEntityData.defineId(Sandstorm.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(Sandstorm.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> OFFSET = SynchedEntityData.defineId(Sandstorm.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(Sandstorm.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(Sandstorm.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> EXTRA_DAMAGE = SynchedEntityData.defineId(Sandstorm.class, EntityDataSerializers.FLOAT);
    public AnimationState SpawnAnimationState = new AnimationState();
    public AnimationState DespawnAnimationState = new AnimationState();

    public Sandstorm(EntityType<? extends Sandstorm> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public Sandstorm(Level worldIn, double x, double y, double z, int lifespan, float offset, UUID casterIn) {
        this(GCEntityType.SANDSTORM.get(), worldIn);
        this.setCreatorEntityUUID(casterIn);
        this.setLifespan(lifespan);
        this.setPos(x, y, z);
        this.setState(1);
        this.setOffset(offset);
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.SANDSTORM.get().getDescription();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public void tick() {
        super.tick();
        updateMotion();
        Entity owner = getCreatorEntity();
        if (owner != null && !owner.isAlive()) discard();
        if(level().isClientSide) {
            float ran = 0.04f;
            float r = 0.89F + random.nextFloat() * ran;
            float g = 0.85f + random.nextFloat() * ran;
            float b = 0.69f + random.nextFloat() * ran * 1.5F;
            this.level().addParticle((new StormParticle.OrbData(r, g, b,2.75f + random.nextFloat() * 0.6f,3.75F + random.nextFloat() * 0.6f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
            this.level().addParticle((new StormParticle.OrbData(r, g, b,2.5f + random.nextFloat() * 0.45f,3.0F + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
            this.level().addParticle((new StormParticle.OrbData(r, g, b,2.25f + random.nextFloat() * 0.45f,2.25F + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
            this.level().addParticle((new StormParticle.OrbData(r, g, b,1.25f + random.nextFloat() * 0.45f,1.25f + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);


            if(this.getState() == 1) {
                if (this.getLifespan() < 295) {
                    this.setState(0);
                }
            }
            if(this.getState() == 0) {
                if(this.getLifespan() < 10) {
                    this.setState(2);
                }
            }
        }

        if (!this.isSilent() && level().isClientSide) {
            Cataclysm.PROXY.playWorldSound(this, (byte) 2);
        }

        for(LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
            if(entity != owner) {
                if (entity.isAlive() && !entity.isInvulnerable() ) {
                    float damage = (GCSpellConfig.SandstormDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get()) + this.getExtraDamage();
                    if (this.tickCount % 3 == 0) {
                        if (owner == null) {
                            boolean flag =  entity.hurt(this.damageSources().magic(), damage);
                            if (flag) {
                                MobEffectInstance effectinstance = new MobEffectInstance(ModEffect.EFFECTCURSE_OF_DESERT.get(), 200, 0);
                                entity.addEffect(effectinstance);
                            }
                        } else {
                            if (MobUtil.areAllies(owner, entity)) {
                                return;
                            }
                            boolean flag = entity.hurt(this.damageSources().indirectMagic(this, owner), damage);
                            if (flag) {
                                MobEffectInstance effectinstance = new MobEffectInstance(ModEffect.EFFECTCURSE_OF_DESERT.get(), 200, 0);
                                entity.addEffect(effectinstance);
                            }
                        }
                    }
                }
            }

        }

        this.setLifespan(this.getLifespan() - 1);

        if (this.getLifespan() <= 0) {
            Cataclysm.PROXY.clearSoundCacheFor(this);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int i) {
        this.entityData.set(LIFESPAN, i);
    }

    public float getOffset() {
        return this.entityData.get(OFFSET);
    }

    public void setOffset(float i) {
        this.entityData.set(OFFSET, i);
    }

    public float getExtraDamage() {
        return this.entityData.get(EXTRA_DAMAGE);
    }

    public void setExtraDamage(float pDamage) {
        this.entityData.set(EXTRA_DAMAGE, pDamage);
    }

    public UUID getCreatorEntityUUID() {
        return this.entityData.get(CREATOR_ID).orElse(null);
    }

    public void setCreatorEntityUUID(UUID id) {
        this.entityData.set(CREATOR_ID, Optional.ofNullable(id));
    }

    public Entity getCreatorEntity() {
        UUID uuid = getCreatorEntityUUID();
        if (uuid != null && this.level() instanceof ServerLevel serverLevel){
            return serverLevel.getEntity(uuid);
        }
        return null;
    }

    private void updateMotion() {
        Entity owner = getCreatorEntity();
        if(owner !=null) {
            Vec3 center = owner.position().add(0.0, 0, 0.0);
            float radius = 6;
            float speed = this.tickCount * 0.04f;
            float offset = this.getOffset();
            Vec3 orbit = new Vec3(center.x + Math.cos((double) (speed + offset)) * (double) radius, center.y, center.z + Math.sin((double) (speed + offset)) * (double) radius);
            this.moveTo(orbit);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CREATOR_ID, Optional.empty());
        this.entityData.define(LIFESPAN, 300);
        this.entityData.define(OFFSET,0f);
        this.entityData.define(STATE,0);
        this.entityData.define(EXTRA_DAMAGE, 0.0F);
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "spawn")) {
            return this.SpawnAnimationState;
        } else if (Objects.equals(input, "despawn")) {
            return this.DespawnAnimationState;
        }else {
            return new AnimationState();
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (STATE.equals(p_21104_)) {
            if (this.level().isClientSide)
                switch (this.getState()) {
                    case 0 -> this.stopAllAnimationStates();
                    case 1 -> {
                        stopAllAnimationStates();
                        this.SpawnAnimationState.startIfStopped(this.tickCount);
                    }
                    case 2 -> {
                        stopAllAnimationStates();
                        this.DespawnAnimationState.startIfStopped(this.tickCount);
                    }
                }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.DespawnAnimationState.stop();
        this.SpawnAnimationState.stop();
    }

    public int getState() {
        return entityData.get(STATE);
    }

    public void setState(int state) {
        entityData.set(STATE, state);
    }


    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setLifespan(compound.getInt("Lifespan"));
        if (compound.hasUUID("Owner")) {
            this.setCreatorEntityUUID(compound.getUUID("Owner"));
        }
        if (compound.contains("ExtraDamage")) {
            this.setExtraDamage(compound.getFloat("ExtraDamage"));
        }
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Lifespan", getLifespan());
        if (this.getCreatorEntityUUID() != null) {
            compound.putUUID("Owner", this.getCreatorEntityUUID());
        }
        compound.putFloat("ExtraDamage", this.getExtraDamage());
    }
}
