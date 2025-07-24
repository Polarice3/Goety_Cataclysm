package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.factory.ProwlerServant;
import com.Polarice3.goety_cataclysm.util.GCDamageSource;
import com.github.L_Ender.cataclysm.blocks.EMP_Block;
import com.github.L_Ender.cataclysm.client.particle.LightningParticle;
import com.github.L_Ender.cataclysm.client.tool.ControlledAnimation;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.init.ModBlocks;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
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

public class DeathLaserBeam extends Entity {
    public static final double RADIUS = 30;
    public LivingEntity caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float renderYaw, renderPitch;
    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FIRE = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IMMEDIATE = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HPDAMAGE = SynchedEntityData.defineId(DeathLaserBeam.class, EntityDataSerializers.FLOAT);

    public float prevYaw;
    public float prevPitch;

    private Vec3[] attractorPos;

    public DeathLaserBeam(EntityType<? extends DeathLaserBeam> type, Level world) {
        super(type, world);
        noCulling = true;
        if (world.isClientSide) {
            attractorPos = new Vec3[] {new Vec3(0, 0, 0)};
        }
    }

    public DeathLaserBeam(EntityType<? extends DeathLaserBeam> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration, float damage, float Hpdamage) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.setDamage(damage);
        this.setHpDamage(Hpdamage);
        this.calculateEndPos();
        if (!world.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.DEATH_LASER_BEAM.get().getDescription();
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public boolean isActivated(){
        return this.tickCount > 20 || this.isImmediate();
    }

    @Override
    public void tick() {
        super.tick();
        this.prevCollidePosX = this.collidePosX;
        this.prevCollidePosY = this.collidePosY;
        this.prevCollidePosZ = this.collidePosZ;
        this.prevYaw = this.renderYaw;
        this.prevPitch = this.renderPitch;
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.tickCount == 1 && this.level().isClientSide) {
            if (this.level().getEntity(this.getCasterID()) instanceof LivingEntity livingEntity) {
                this.caster = livingEntity;
            }
        }

        if (!this.level().isClientSide) {
            if (this.caster instanceof ProwlerServant) {
                this.updateWithProwler();
            } else if(this.caster != null) {
                this.updateWithCaster();
            }
        }

        if (this.caster != null) {
            this.renderYaw = (float) ((this.caster.yHeadRot + 90.0d) * Math.PI / 180.0d);
            this.renderPitch = (float) (-this.caster.getXRot() * Math.PI / 180.0d);
        }

        if (!this.on && this.appear.getTimer() == 0) {
            this.discard();
        }
        if (this.on && this.isActivated()) {
            this.appear.increaseTimer();
        } else {
            this.appear.decreaseTimer();
        }

        if (this.caster != null && !this.caster.isAlive()) {
            this.discard();
        }

        if (this.isActivated()) {
            this.calculateEndPos();
            List<LivingEntity> hit = this.raytraceEntities(this.level(), new Vec3(this.getX(), this.getY(), this.getZ()), new Vec3(this.endPosX, this.endPosY, this.endPosZ), false, true, true).entities;
            if (this.blockSide != null) {
                this.spawnExplosionParticles(5);
                if (!this.level().isClientSide) {
                    for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.collidePosX - 0.5F), Mth.floor(this.collidePosY - 0.5F), Mth.floor(this.collidePosZ - 0.5F), Mth.floor(this.collidePosX + 0.5F), Mth.floor(this.collidePosY + 0.5F), Mth.floor(this.collidePosZ + 0.5F))) {
                        BlockState block = this.level().getBlockState(pos);
                        if (!block.isAir() && block.is(ModTag.CM_GLASS) && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                            this.level().destroyBlock(pos, true);
                        }
                    }
                    for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.collidePosX - 2.5F), Mth.floor(this.collidePosY - 2.5F), Mth.floor(this.collidePosZ - 2.5F), Mth.floor(this.collidePosX + 2.5F), Mth.floor(this.collidePosY + 2.5F), Mth.floor(this.collidePosZ + 2.5F))) {
                        BlockState block = level().getBlockState(pos);
                        if (block.is(ModBlocks.EMP.get())) {
                            if (!block.getValue(EMP_Block.POWERED) && block.getValue(EMP_Block.OVERLOAD)) {
                                this.level().setBlockAndUpdate(pos, block.setValue(EMP_Block.OVERLOAD, false));
                            }
                        }
                    }
                    if (this.getFire()) {
                        BlockPos blockpos1 = BlockPos.containing(this.collidePosX, this.collidePosY, this.collidePosZ);
                        if (CMConfig.HarbingerLightFire) {
                            if (this.level().isEmptyBlock(blockpos1)) {
                                this.level().setBlockAndUpdate(blockpos1, BaseFireBlock.getState(this.level(), blockpos1));
                            }
                        } else {
                            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                                if (this.level().isEmptyBlock(blockpos1)) {
                                    this.level().setBlockAndUpdate(blockpos1, BaseFireBlock.getState(this.level(), blockpos1));
                                }
                            }
                        }

                    }
                }
            }
            if (!this.level().isClientSide) {
                for (LivingEntity target : hit) {
                    if (this.caster != null) {
                        if (!MobUtil.areAllies(this.caster, target)) {
                            boolean flag = target.hurt(GCDamageSource.deathLaser(this, this.caster), (float) (this.getDamage() + Math.min(this.getDamage(), target.getMaxHealth() * this.getHpDamage() * 0.01)));
                            if (this.getFire()) {
                                if (flag) {
                                    target.setSecondsOnFire(5);
                                }
                            }
                        }
                    }
                }
            }
        }
        int count = this.tickCount - 20;
        if (this.isImmediate()) {
            count = this.tickCount;
        }
        if (count > this.getDuration()) {
            this.on = false;
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 1.5F;
            float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
            float motionY = this.random.nextFloat() * 0.8F;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            this.level().addParticle((new LightningParticle.OrbData(255, 26,  0)), collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(YAW, 0F);
        this.entityData.define(PITCH, 0F);
        this.entityData.define(DURATION, 0);
        this.entityData.define(CASTER, -1);
        this.entityData.define(HEAD, 0);
        this.entityData.define(FIRE, false);
        this.entityData.define(IMMEDIATE, false);
        this.entityData.define(DAMAGE, 0F);
        this.entityData.define(HPDAMAGE, 0F);
    }

    public float getDamage() {
        return entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    public float getHpDamage() {
        return this.entityData.get(HPDAMAGE);
    }

    public void setHpDamage(float damage) {
        this.entityData.set(HPDAMAGE, damage);
    }

    public float getYaw() {
        return this.entityData.get(YAW);
    }

    public void setYaw(float yaw) {
        this.entityData.set(YAW, yaw);
    }

    public float getPitch() {
        return this.entityData.get(PITCH);
    }

    public void setPitch(float pitch) {
        this.entityData.set(PITCH, pitch);
    }

    public int getDuration() {
        return this.entityData.get(DURATION);
    }

    public void setDuration(int duration) {
        this.entityData.set(DURATION, duration);
    }

    public int getHead() {
        return this.entityData.get(HEAD);
    }

    public void setHead(int head) {
        this.entityData.set(HEAD, head);
    }

    public int getCasterID() {
        return this.entityData.get(CASTER);
    }

    public void setCasterID(int id) {
        this.entityData.set(CASTER, id);
    }

    public boolean getFire() {
        return this.entityData.get(FIRE);
    }

    public void setFire(boolean fire) {
        this.entityData.set(FIRE, fire);
    }

    public boolean isImmediate() {
        return this.entityData.get(IMMEDIATE);
    }

    public void setImmediate(boolean immediate) {
        this.entityData.set(IMMEDIATE, immediate);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {}

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void calculateEndPos() {
        if (this.level().isClientSide()) {
            this.endPosX = this.getX() + RADIUS * Math.cos(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosZ = this.getZ() + RADIUS * Math.sin(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosY = this.getY() + RADIUS * Math.sin(this.renderPitch);
        } else {
            this.endPosX = this.getX() + RADIUS * Math.cos(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosZ = this.getZ() + RADIUS * Math.sin(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosY = this.getY() + RADIUS * Math.sin(this.getPitch());
        }
    }

    public LaserbeamHitResult raytraceEntities(Level world, Vec3 from, Vec3 to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
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
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(getX(), collidePosX), Math.min(getY(), collidePosY), Math.min(getZ(), collidePosZ), Math.max(getX(), collidePosX), Math.max(getY(), collidePosY), Math.max(getZ(), collidePosZ)).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == this.caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
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
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    private void updateWithHarbinger() {
        this.setYaw((float) ((this.caster.yHeadRot + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-this.caster.getXRot() * Math.PI / 180.0d));
        this.setPos(this.caster.getX() ,this.caster.getY() + 2.7 , this.caster.getZ());
    }

    private void updateWithProwler() {
        this.setYaw((float) ((this.caster.yHeadRot + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-this.caster.getXRot() * Math.PI / 180.0d));
        this.setPos(this.caster.getX() ,this.caster.getY() + 1.8, this.caster.getZ());
    }

    private void updateWithCaster() {
        this.setYaw((float) ((this.caster.yHeadRot + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-this.caster.getXRot() * Math.PI / 180.0d));
        this.setPos(this.caster.getX() ,this.caster.getEyeY() - 0.2D, this.caster.getZ());
    }

    public static class LaserbeamHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return this.blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
                this.blockHit = (BlockHitResult) rayTraceResult;
            }
        }

        public void addEntityHit(LivingEntity entity) {
            this.entities.add(entity);
        }
    }
}
