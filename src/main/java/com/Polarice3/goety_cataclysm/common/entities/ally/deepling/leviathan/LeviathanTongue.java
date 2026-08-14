package com.Polarice3.goety_cataclysm.common.entities.ally.deepling.leviathan;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.projectiles.SpellEntity;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LeviathanTongue extends SpellEntity {
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(LeviathanTongue.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_DURATION = SynchedEntityData.defineId(LeviathanTongue.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> COMING_BACK = SynchedEntityData.defineId(LeviathanTongue.class, EntityDataSerializers.BOOLEAN);
    private int destroyBlocksTick;

    public LeviathanTongue(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DURATION, 0);
        this.entityData.define(MAX_DURATION, 0);
        this.entityData.define(COMING_BACK, false);
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.THE_LEVIATHAN_TONGUE.get().getDescription();
    }

    public void tick() {
        super.tick();
        Entity controller = this.getOwner();
        Entity target = this.getTarget();

        if (!this.getPassengers().isEmpty() && this.getPassengers().get(0).isShiftKeyDown()) {
            this.getPassengers().get(0).setShiftKeyDown(false);
        }

        if(this.getDuration() <= this.getMaxDuration()) {
            this.setDuration(this.getDuration() + 1);
        }


        if (!this.level().isClientSide) {
            if (GCMobsConfig.LeviathanGriefing.get()) {
                blockbreak(0.25d,0.25d,0.25d);
            }
        }

        if (controller instanceof LeviathanServant levi) {
            levi.setTongueUUID(this.getUUID());
            if (!level().isClientSide) {
                LivingEntity e = levi.getTarget();
                if (e != null && e.isAlive()) {
                    this.setTarget(e);
                }
            }


            boolean attacking = !this.getComingBack() && target != null && target.isAlive();
            Vec3 vec3 = attacking ? target.getEyePosition() : levi.getTonguePosition();
            float speed = attacking ? 0.095f : 0.15f;
            Vec3 want = vec3.subtract(this.position());
            if (target != null && !this.getComingBack()) {
                if (want.length() < target.getBbWidth() + 1F) {
                    hurtEntity(levi, target);
                    this.setComingBack(true);
                }
            }
            directMovementTowards(vec3, speed);

            if (this.getDuration() >= this.getMaxDuration() / 2) {
                if (!this.getComingBack()) {
                    this.setComingBack(true);
                }
            }
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.9F));
    }

    private void hurtEntity(LivingEntity holder, Entity target) {
        DamageSource damageSource = damageSources().mobAttack(holder);
        if (holder instanceof Summoned servant) {
            damageSource = servant.getServantAttack();
        }
        if(target.hurt(damageSource, 6)){
            if (!this.level().isClientSide) {
                target.startRiding(this);
            }
        }
    }

    private void blockbreak(double x, double y, double z) {
        if (!this.level().isClientSide) {
            if (this.destroyBlocksTick > 0) {
                --this.destroyBlocksTick;
                return;
            }

            boolean flag = false;
            AABB aabb = this.getBoundingBox().inflate(x, y, z);
            for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                BlockState blockstate = this.level().getBlockState(blockpos);
                if (!blockstate.isAir() && blockstate.canEntityDestroy(this.level(), blockpos, this) && !blockstate.is(ModTag.LEVIATHAN_IMMUNE)) {
                    flag = this.level().destroyBlock(blockpos, false, this) || flag;
                }
            }
            if (flag) {
                destroyBlocksTick = 15;
            }
        }
    }

    private boolean shouldDropItem(BlockEntity tileEntity) {
        if (tileEntity == null) {
            return random.nextInt(3) + 1 == 3;
        }
        return true;
    }


    private void directMovementTowards(Vec3 moveTo, float speed) {
        Vec3 want = moveTo.subtract(this.position());
        if (want.length() > 1F) {
            want = want.normalize();
        }
        float targetXRot = (float) (-(Mth.atan2(want.y, want.horizontalDistance()) * (double) (180F / (float) Math.PI)));
        float targetYRot = (float) (-Mth.atan2(want.x, want.z) * (double) (180F / (float) Math.PI));
        this.setXRot(Mth.approachDegrees(this.getXRot(), targetXRot, 5F));
        this.setYRot(Mth.approachDegrees(this.getYRot(), targetYRot, 5F));
        this.setDeltaMovement(this.getDeltaMovement().add(want.scale(speed)));

    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDuration(tag.getInt("Duration"));
        this.setDuration(tag.getInt("Max_Duration"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Duration", getDuration());
        tag.putInt("Max_Duration", getMaxDuration());
    }

    public int getDuration() {
        return this.entityData.get(DURATION);
    }

    public void setDuration(int i) {
        this.entityData.set(DURATION, i);
    }

    public int getMaxDuration() {
        return this.entityData.get(MAX_DURATION);
    }

    public void setMaxDuration(int i) {
        this.entityData.set(MAX_DURATION, i);
    }

    public boolean getComingBack() {
        return this.entityData.get(COMING_BACK);
    }

    public void setComingBack(boolean i) {
        this.entityData.set(COMING_BACK, i);
    }
}
