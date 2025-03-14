package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.LootingExplosion;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.client.particle.LightningParticle;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class AbyssMine extends Entity {
    private static final EntityDataAccessor<Boolean> ACTIVATE = SynchedEntityData.defineId(AbyssMine.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AbyssMine.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AbyssMine.class, EntityDataSerializers.FLOAT);
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 800;
    private boolean clientSideAttackStarted;
    private LivingEntity caster;
    private UUID casterUuid;
    public float activateProgress;
    public float prevactivateProgress;
    public int time;

    public AbyssMine(EntityType<? extends AbyssMine> p_i50170_1_, Level p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    public AbyssMine(Level worldIn, double x, double y, double z, float p_i47276_8_, int p_i47276_9_ , LivingEntity casterIn) {
        this(GCEntityType.ABYSS_MINE.get(), worldIn);
        this.warmupDelayTicks = p_i47276_9_;

        this.setCaster(casterIn);
        this.setYRot(p_i47276_8_ * (180F / (float)Math.PI));
        this.setPos(x, y, z);
    }

    protected void defineSynchedData() {
        this.entityData.define(ACTIVATE, false);
        this.entityData.define(DAMAGE, 0.0F);
        this.entityData.define(RADIUS, 1.0F);
    }

    protected Component getTypeName() {
        return ModEntities.ABYSS_MINE.get().getDescription();
    }

    public float getDamage() {
        return entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        entityData.set(DAMAGE, damage);
    }

    public float getRadius() {
        return entityData.get(RADIUS);
    }

    public void setRadius(float damage) {
        entityData.set(RADIUS, damage);
    }

    public void setCaster(@Nullable LivingEntity p_190549_1_) {
        this.caster = p_190549_1_;
        this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUUID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.casterUuid);
            if (entity instanceof LivingEntity livingEntity) {
                this.caster = livingEntity;
            }
        }

        return this.caster;
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.warmupDelayTicks = compound.getInt("Warmup");
        if (compound.hasUUID("Owner")) {
            this.casterUuid = compound.getUUID("Owner");
        }
        if (compound.contains("Radius")) {
            this.setRadius(compound.getFloat("Radius"));
        }

    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Warmup", this.warmupDelayTicks);
        if (this.casterUuid != null) {
            compound.putUUID("Owner", this.casterUuid);
        }
        compound.putFloat("Radius", this.getRadius());

    }

    public void tick() {
        super.tick();
        ++time;
        prevactivateProgress = activateProgress;

        if (isActivate() && this.activateProgress > 0F) {
            this.activateProgress--;
        }

        if (this.level().isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (!isActivate() && this.activateProgress < 10F) {
                    this.activateProgress++;
                }
                for(int i = 0; i < 2; ++i) {
                    double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D;
                    double d1 = this.getY() + this.getBbHeight() * 1/2;
                    double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D;
                    double d3 = (this.random.nextGaussian() * 0.3D);
                    double d4 = (this.random.nextGaussian() * 0.3D);
                    double d5 = (this.random.nextGaussian() * 0.3D);
                    this.level().addParticle(new LightningParticle.OrbData(102, 26, 204), d0, d1, d2, d3, d4, d5);
                }

                if (this.lifeTicks == 14) {
                    this.setActivate(true);
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -10) {
                if(isActivate()) {
                    this.setActivate(false);
                }
            }
            if (this.warmupDelayTicks < -20) {
                for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
                    this.explode(livingentity);
                }
            }


            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte)4);
                this.clientSideAttackStarted = true;
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.discard();
            }
        }

    }

    public boolean isActivate() {
        return this.entityData.get(ACTIVATE);
    }

    public void setActivate(boolean Activate) {
        this.entityData.set(ACTIVATE, Activate);
    }

    protected void explode(LivingEntity livingentity) {
        LivingEntity Caster = this.getCaster();
        if(livingentity.isAlive()) {
            if (Caster != null) {
                LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(Caster) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
                if (!MobUtil.areAllies(Caster, livingentity) && livingentity.isAlive()) {
                    ExplosionUtil.lootExplode(this.level(), Caster, this.getX(), this.getY(0.0625D), this.getZ(), this.getRadius(), false, Explosion.BlockInteraction.KEEP, lootMode);
                    livingentity.addEffect(new MobEffectInstance(ModEffect.EFFECTABYSSAL_FEAR.get(), 200, 0));
                    this.discard();
                }
            } else {
                this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 1.0f, Level.ExplosionInteraction.NONE);
                livingentity.addEffect(new MobEffectInstance(ModEffect.EFFECTABYSSAL_FEAR.get(), 200, 0));
                this.discard();
            }
        }


    }

    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            this.clientSideAttackStarted = true;
        }

    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
