package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.Polarice3.goety_cataclysm.util.GCDamageSource;
import com.github.L_Ender.cataclysm.client.particle.LightningParticle;
import com.github.L_Ender.cataclysm.client.tool.ControlledAnimation;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PortalAbyssBlast extends Entity {
    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(PortalAbyssBlast.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(PortalAbyssBlast.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(PortalAbyssBlast.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(PortalAbyssBlast.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> BEAM_DIRECTION = SynchedEntityData.defineId(PortalAbyssBlast.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(PortalAbyssBlast.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HP_DAMAGE = SynchedEntityData.defineId(PortalAbyssBlast.class, EntityDataSerializers.FLOAT);
    public static final double RADIUS = 50.0D;
    public LivingEntity caster;
    public double endPosX;
    public double endPosY;
    public double endPosZ;
    public double collidePosX;
    public double collidePosY;
    public double collidePosZ;
    public double prevCollidePosX;
    public double prevCollidePosY;
    public double prevCollidePosZ;
    public float renderYaw;
    public float renderPitch;
    public ControlledAnimation appear = new ControlledAnimation(3);
    public boolean on = true;
    public Direction blockSide = null;
    public float prevYaw;
    public float prevPitch;

    public PortalAbyssBlast(EntityType<? extends PortalAbyssBlast> type, Level world) {
        super(type, world);
        this.noCulling = true;
    }

    public PortalAbyssBlast(EntityType<? extends PortalAbyssBlast> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration, float direction, float damage, float HpDamage) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setBeamDirection(direction);
        this.setPos(x, y, z);
        this.setDamage(damage);
        this.setHpDamage(HpDamage);
        this.calculateEndPos();
        if (!world.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }

    public PortalAbyssBlast(EntityType<? extends PortalAbyssBlast> type, Level world, double x, double y, double z, float yaw, float pitch, int duration, float direction, float damage, float HpDamage) {
        this(type, world);
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setBeamDirection(direction);
        this.setPos(x, y, z);
        this.setDamage(damage);
        this.setHpDamage(HpDamage);
        this.calculateEndPos();
    }

    protected Component getTypeName() {
        return ModEntities.PORTAL_ABYSS_BLAST.get().getDescription();
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void tick() {
        super.tick();
        super.tick();
        this.prevCollidePosX = this.collidePosX;
        this.prevCollidePosY = this.collidePosY;
        this.prevCollidePosZ = this.collidePosZ;
        this.prevYaw = this.renderYaw;
        this.prevPitch = this.renderPitch;
        this.renderYaw = this.getYaw();
        this.renderPitch = this.getPitch();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.tickCount == 1 && this.level().isClientSide) {
            if (this.level().getEntity(this.getCasterID()) instanceof LivingEntity livingEntity){
                this.caster = livingEntity;
            }
        }

        if (!this.on && this.appear.getTimer() == 0) {
            this.discard();
        }
        if (this.on && this.tickCount > 20) {
            this.appear.increaseTimer();
        } else {
            this.appear.decreaseTimer();
        }

        if (this.tickCount == 20){
            this.level().playSound(null, this, CataclysmSounds.PORTAL_ABYSS_BLAST.get(), SoundSource.HOSTILE, 0.5f, 1.0f);
        }

        if (this.caster != null && !this.caster.isAlive()) {
            this.discard();
        }

        if (this.tickCount > 20) {
            this.calculateEndPos();
            List<LivingEntity> hit = this.raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ)).entities;
            if (this.blockSide != null) {
                this.spawnExplosionParticles(3);
                if (!this.level().isClientSide) {
                    for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(collidePosX - 0.5F), Mth.floor(collidePosY - 0.5F), Mth.floor(collidePosZ - 0.5F), Mth.floor(collidePosX + 0.5F), Mth.floor(collidePosY + 0.5F), Mth.floor(collidePosZ + 0.5F))) {
                        BlockState block = this.level().getBlockState(pos);
                        if (!block.isAir() && !block.is(ModTag.LEVIATHAN_IMMUNE) && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this) && GCSpellConfig.AbyssalBeamGriefing.get()) {
                            this.level().destroyBlock(pos, false);
                        }
                    }
                }
            }
            if (!this.level().isClientSide) {
                for (LivingEntity target : hit) {
                    if (this.caster != null) {
                        if (!MobUtil.areAllies(this.caster, target) && target != this.caster) {
                            boolean flag = target.hurt(GCDamageSource.deathLaser(this, this.caster), (float) (this.getDamage() + Math.min(this.getDamage(), target.getMaxHealth() * this.getHpDamage() * 0.01)));
                            if (flag) {
                                MobEffectInstance effectInstance1 = target.getEffect(ModEffect.EFFECTABYSSAL_BURN.get());
                                int i = 1;
                                if (effectInstance1 != null) {
                                    i += effectInstance1.getAmplifier();
                                    target.removeEffectNoUpdate(ModEffect.EFFECTABYSSAL_BURN.get());
                                } else {
                                    --i;
                                }

                                i = Mth.clamp(i, 0, 3);
                                MobEffectInstance effectInstance = new MobEffectInstance(ModEffect.EFFECTABYSSAL_BURN.get(), 160, i, false, true, true);
                                target.addEffect(effectInstance);
                            }
                        }
                    }

                }
            }
        }
        if (this.tickCount - 20 > this.getDuration()) {
            this.on = false;
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 1.0F;
            float yaw = (float) (this.random.nextFloat() * 2.0F * Math.PI);
            float motionY = this.random.nextFloat() * 0.08F;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            this.level().addParticle((new LightningParticle.OrbData(102, 26, 204)), collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }

    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(YAW, 0.0F);
        this.entityData.define(PITCH, 0.0F);
        this.entityData.define(DURATION, 0);
        this.entityData.define(CASTER, -1);
        this.entityData.define(BEAM_DIRECTION, 90.0F);
        this.entityData.define(DAMAGE, 0.0F);
        this.entityData.define(HP_DAMAGE, 0.0F);
    }

    public float getDamage() {
        return entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        entityData.set(DAMAGE, damage);
    }

    public float getHpDamage() {
        return entityData.get(HP_DAMAGE);
    }

    public void setHpDamage(float damage) {
        entityData.set(HP_DAMAGE, damage);
    }

    public float getYaw() {
        return entityData.get(YAW);
    }

    public void setYaw(float yaw) {
        entityData.set(YAW, yaw);
    }

    public float getPitch() {
        return entityData.get(PITCH);
    }

    public void setPitch(float pitch) {
        entityData.set(PITCH, pitch);
    }

    public int getDuration() {
        return entityData.get(DURATION);
    }

    public void setDuration(int duration) {
        entityData.set(DURATION, duration);
    }

    public float getBeamDirection() {
        return entityData.get(BEAM_DIRECTION);
    }

    public void setBeamDirection(float beamDirection) {
        entityData.set(BEAM_DIRECTION, beamDirection);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setYaw(compound.getFloat("Yaw"));
        this.setPitch(compound.getFloat("Pitch"));
        this.setDuration(compound.getInt("Duration"));
        this.setBeamDirection(compound.getFloat("BeamDirection"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Yaw", this.getYaw());
        compound.putFloat("Pitch", this.getPitch());
        compound.putInt("Duration", this.getDuration());
        compound.putFloat("BeamDirection", this.getBeamDirection());
    }

    public int getCasterID() {
        return this.entityData.get(CASTER);
    }

    public void setCasterID(int id) {
        this.entityData.set(CASTER, id);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void calculateEndPos() {
        if (this.level().isClientSide()) {
            this.endPosX = getX() + RADIUS * Math.cos(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosZ = getZ() + RADIUS * Math.sin(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosY = getY() + RADIUS * Math.sin(this.renderPitch);
        } else {
            this.endPosX = getX() + RADIUS * Math.cos(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosZ = getZ() + RADIUS * Math.sin(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosY = getY() + RADIUS * Math.sin(this.getPitch());
        }
    }

    public LaserbeamHitResult raytraceEntities(Level world, Vec3 from, Vec3 to) {
        LaserbeamHitResult result = new LaserbeamHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            this.collidePosX = hitVec.x;
            this.collidePosY = hitVec.y;
            this.collidePosZ = hitVec.z;
            this.blockSide = result.blockHit.getDirection();
        } else {
            this.collidePosX = this.endPosX;
            this.collidePosY = this.endPosY;
            this.collidePosZ = this.endPosZ;
            this.blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(this.getX(), this.collidePosX), Math.min(this.getY(), this.collidePosY), Math.min(this.getZ(), this.collidePosZ), Math.max(this.getX(), this.collidePosX), Math.max(this.getY(), this.collidePosY), Math.max(this.getZ(), this.collidePosZ)).inflate(1.0F));
        for (LivingEntity entity : entities) {
            if (entity == this.caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 1.3F;
            AABB aabb = entity.getBoundingBox().inflate(pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_36837_) {
        double d0 = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return p_36837_ < d0 * d0;
    }

    public static class LaserbeamHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
