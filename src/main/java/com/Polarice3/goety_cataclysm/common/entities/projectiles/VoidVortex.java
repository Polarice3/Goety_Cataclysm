package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.LootingExplosion;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.client.particle.StormParticle;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class VoidVortex extends Entity {
    protected static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(VoidVortex.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(VoidVortex.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(VoidVortex.class, EntityDataSerializers.FLOAT);
    private boolean madeOpenNoise = false;
    private boolean madeCloseNoise = false;
    @Nullable
    private LivingEntity owner;

    public VoidVortex(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public VoidVortex(Level worldIn, double x, double y, double z, float p_i47276_8_, LivingEntity casterIn,int span) {
        this(GCEntityType.VOID_VORTEX.get(), worldIn);
        this.setLifespan(span);
        this.setOwner(casterIn);
        this.setYRot(p_i47276_8_ * (180F / (float)Math.PI));
        this.setPos(x, y, z);
        if (!worldIn.isClientSide) {
            this.setCasterID(casterIn.getId());
        }
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.VOID_VORTEX.get().getDescription();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public void tick() {
        super.tick();
        if (this.tickCount == 1) {
            if (this.getLifespan() == 0){
                this.setLifespan(60);
            }
            if (this.level().isClientSide) {
                if (this.level().getEntity(this.getCasterID()) instanceof LivingEntity livingEntity){
                    this.owner = livingEntity;
                }
            }
        }
        if (!this.madeOpenNoise){
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.playSound(SoundEvents.END_PORTAL_SPAWN, 1.0F, 1 + random.nextFloat() * 0.2F);
            this.madeOpenNoise = true;
        }

        if(Math.min(this.tickCount, this.getLifespan()) >= 16){
            if(this.level().isClientSide) {
                float r = 0.4F;
                float g = 0.1f;
                float b = 0.8f;
                this.level().addParticle((new StormParticle.OrbData(r, g, b,2.5F + random.nextFloat() * 0.9f,5 + random.nextFloat() * 0.9f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
                this.level().addParticle((new StormParticle.OrbData(r, g, b,2.25f + random.nextFloat() * 0.6f,4.25F + random.nextFloat() * 0.6f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
                this.level().addParticle((new StormParticle.OrbData(r, g, b,2f + random.nextFloat() * 0.45f,3.5F + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
                this.level().addParticle((new StormParticle.OrbData(r, g, b,1.5f + random.nextFloat() * 0.25f,2.75F + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
                this.level().addParticle((new StormParticle.OrbData(r, g, b,1.25f + random.nextFloat() * 0.25f,2.0F + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
                this.level().addParticle((new StormParticle.OrbData(r, g, b,1.0f + random.nextFloat() * 0.25f,1.25F + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
                this.level().addParticle((new StormParticle.OrbData(r, g, b,0.75f + random.nextFloat() * 0.25f,0.5F + random.nextFloat() * 0.45f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
            }

            float radius = 3.0F;
            AABB screamBox = new AABB(this.getX() - radius, this.getY(), this.getZ() - radius, this.getX() + radius, this.getY() + (radius * 5), this.getZ() + radius);

            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, screamBox)) {
                if (this.getOwner() != null && entity != this.getOwner() && !MobUtil.areAllies(this.owner, entity)) {
                    if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
                        Vec3 diff = entity.position().subtract(position().add(0, 0, 0));
                        diff = diff.normalize().scale(0.075);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0, -2, 0).subtract(diff));
                    }
                }
            }
        }

        this.setLifespan(this.getLifespan() - 1);
        if (this.getLifespan() <= 16){
            if (!this.madeCloseNoise){
                this.gameEvent(GameEvent.ENTITY_PLACE);
                this.madeCloseNoise = true;
            }
        }
        if (this.getLifespan() <= 0) {
            LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(this.getOwner()) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
            ExplosionUtil.lootExplode(this.level(), this.getOwner(), this.getX(), this.getY(), this.getZ(), this.getRadius(), false, Explosion.BlockInteraction.KEEP, lootMode);
            this.discard();
        }
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int i) {
        this.entityData.set(LIFESPAN, i);
    }

    public int getCasterID() {
        return this.entityData.get(CASTER);
    }

    public void setCasterID(int id) {
        this.entityData.set(CASTER, id);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(float damage) {
        this.entityData.set(RADIUS, damage);
    }

    public void setOwner(@Nullable LivingEntity p_19719_) {
        this.owner = p_19719_;
        this.setCasterID(p_19719_ == null ? 0 : p_19719_.getId());
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && getCasterID() != 0 && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(getCasterID());
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFESPAN, 300);
        this.entityData.define(CASTER, -1);
        this.entityData.define(RADIUS, 2.0F);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setLifespan(compound.getInt("Lifespan"));
        this.setCasterID(compound.getInt("CasterId"));
        this.setRadius(compound.getFloat("Radius"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Lifespan", getLifespan());
        compound.putInt("CasterId", getCasterID());
        compound.putFloat("Radius", this.getRadius());
    }
}
