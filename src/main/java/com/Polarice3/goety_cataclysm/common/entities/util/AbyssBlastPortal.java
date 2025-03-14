package com.Polarice3.goety_cataclysm.common.entities.util;

import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.PortalAbyssBlast;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AbyssBlastPortal extends Entity {
    private static final EntityDataAccessor<Boolean> ACTIVATE = SynchedEntityData.defineId(AbyssBlastPortal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AbyssBlastPortal.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HP_DAMAGE = SynchedEntityData.defineId(AbyssBlastPortal.class, EntityDataSerializers.FLOAT);
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks =260;
    private int laserDurations = 160;
    private boolean clientSideAttackStarted;
    private LivingEntity caster;
    private UUID casterUuid;
    public float activateProgress;
    public float prevactivateProgress;

    public AbyssBlastPortal(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    public AbyssBlastPortal(Level worldIn, double x, double y, double z, float p_i47276_8_, int p_i47276_9_, float damage, float hpdamage , LivingEntity casterIn) {
        this(GCEntityType.ABYSS_BLAST_PORTAL.get(), worldIn);
        this.warmupDelayTicks = p_i47276_9_;
        this.setCaster(casterIn);
        this.setYRot(p_i47276_8_ * (180F / (float)Math.PI));
        this.setDamage(damage);
        this.setHpDamage(hpdamage);
        this.setPos(x, y, z);
    }

    protected Component getTypeName() {
        return ModEntities.ABYSS_BLAST_PORTAL.get().getDescription();
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setCaster(@Nullable LivingEntity p_190549_1_) {
        this.caster = p_190549_1_;
        this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUUID();
    }

    public @Nullable LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    public void tick() {
        super.tick();
        this.prevactivateProgress = this.activateProgress;
        if (this.isActivate() && this.activateProgress > 0.0F) {
            --this.activateProgress;
        }

        if (this.level().isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (!this.isActivate() && this.activateProgress < 10.0F) {
                    ++this.activateProgress;
                }

                if (this.lifeTicks == 14) {
                    this.setActivate(true);
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -10) {
                if (this.isActivate()) {
                    this.setActivate(false);
                }
            }

            if (this.warmupDelayTicks == -22) {
                if (this.caster != null) {
                    PortalAbyssBlast DeathBeam1 = new PortalAbyssBlast(GCEntityType.PORTAL_ABYSS_BLAST.get(), this.level(), this.getCaster(), this.getX(), this.getY(), this.getZ(), (float)((double)(this.getYRot() - 90.0F) * Math.PI / 180.0), 1.5707964F, this.laserDurations, 90.0F, this.getDamage(), this.getHpDamage());
                    this.level().addFreshEntity(DeathBeam1);
                } else {
                    PortalAbyssBlast DeathBeam2 = new PortalAbyssBlast(GCEntityType.PORTAL_ABYSS_BLAST.get(), this.level(), this.getX(), this.getY(), this.getZ(), (float)((double)(this.getYRot() - 90.0F) * Math.PI / 180.0), 1.5707964F, this.laserDurations, 90.0F, this.getDamage(), this.getHpDamage());
                    this.level().addFreshEntity(DeathBeam2);
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

    public boolean shouldRenderAtSqrDistance(double p_36837_) {
        double d0 = this.getBoundingBox().getSize() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }

        d0 *= 64.0;
        return p_36837_ < d0 * d0;
    }

    protected void defineSynchedData() {
        this.entityData.define(ACTIVATE, false);
        this.entityData.define(DAMAGE, 0.0F);
        this.entityData.define(HP_DAMAGE, 0.0F);
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    public float getHpDamage() {
        return this.entityData.get(HP_DAMAGE);
    }

    public void setHpDamage(float damage) {
        this.entityData.set(HP_DAMAGE, damage);
    }

    public boolean isActivate() {
        return this.entityData.get(ACTIVATE);
    }

    public void setActivate(boolean Activate) {
        this.entityData.set(ACTIVATE, Activate);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.warmupDelayTicks = compound.getInt("Warmup");
        if (compound.hasUUID("Owner")) {
            this.casterUuid = compound.getUUID("Owner");
        }

        this.setDamage(compound.getFloat("damage"));
        this.setHpDamage(compound.getFloat("Hpdamage"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Warmup", this.warmupDelayTicks);
        if (this.casterUuid != null) {
            compound.putUUID("Owner", this.casterUuid);
        }

        compound.putFloat("damage", this.getDamage());
        compound.putFloat("Hpdamage", this.getHpDamage());
    }

    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            this.clientSideAttackStarted = true;
        }

    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
}
