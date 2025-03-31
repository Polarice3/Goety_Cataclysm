package com.Polarice3.goety_cataclysm.common.entities.ally.golem;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.client.particle.RingParticle;
import com.github.L_Ender.cataclysm.entity.AI.MobAIFindWater;
import com.github.L_Ender.cataclysm.entity.AI.MobAILeaveWater;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.GroundPathNavigatorWide;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class CoralssusServant extends InternalAnimationSummon {
    private static final EntityDataAccessor<Integer> MOISTNESS = SynchedEntityData.defineId(CoralssusServant.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(CoralssusServant.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> RIGHT = SynchedEntityData.defineId(CoralssusServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CORALSSUS_SWIM = SynchedEntityData.defineId(CoralssusServant.class, EntityDataSerializers.BOOLEAN);
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState angryAnimationState = new AnimationState();
    public AnimationState nantaAnimationState = new AnimationState();
    public AnimationState rightfistAnimationState = new AnimationState();
    public AnimationState leftfistAnimationState = new AnimationState();
    public AnimationState jumpingprepareAnimationState = new AnimationState();
    public AnimationState jumpingAnimationState = new AnimationState();
    public AnimationState jumpingendAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    private int nanta_cooldown = 0;
    public static final int NANTA_COOLDOWN = 160;
    private int moistureAttackTime = 0;
    private int jump_cooldown = 0;
    public static final int JUMP_COOLDOWN = 160;
    private boolean isLandNavigator;
    boolean searchingForLand;

    public CoralssusServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.25F);
        this.moveControl = new CoralssusMoveControl(this, 2.5F);
        this.switchNavigator(false);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new InternalSummonMoveGoal(this, false, 1.0));
        this.goalSelector.addGoal(4, new MobAIFindWater(this, 1.0));
        this.goalSelector.addGoal(4, new MobAILeaveWater(this));
        this.goalSelector.addGoal(5, new WaterWanderGoal<>(this));
        this.goalSelector.addGoal(6, new CoralssusSwimUpGoal(this, 1.0, this.level().getSeaLevel()));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, 1, 2, 40, 17, 6.0F) {
            public boolean canUse() {
                return super.canUse() && CoralssusServant.this.getRandom().nextFloat() * 100.0F < 16.0F && CoralssusServant.this.nanta_cooldown <= 0 && !CoralssusServant.this.getSwim();
            }

            public void start() {
                super.start();
                CoralssusServant.this.playSound(CataclysmSounds.CORALSSUS_ROAR.get(), 1.0F, 1.0F + CoralssusServant.this.getRandom().nextFloat() * 0.1F);
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, 2, 2, 0, 60, 60) {
            public void stop() {
                super.stop();
                CoralssusServant.this.nanta_cooldown = 160;
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, 3, 0, 30, 8, 4.5F) {
            public boolean canUse() {
                return super.canUse() && CoralssusServant.this.getIsRight();
            }

            public void stop() {
                super.stop();
                CoralssusServant.this.setRight(!CoralssusServant.this.getIsRight());
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, 4, 0, 30, 8, 4.5F) {
            public boolean canUse() {
                return super.canUse() && !CoralssusServant.this.getIsRight();
            }

            public void stop() {
                super.stop();
                CoralssusServant.this.setRight(!CoralssusServant.this.getIsRight());
            }
        });
        this.goalSelector.addGoal(1, new CoralssusJumpPrepareAttackGoal(this, 0, 5, 6, 20, 10, 6.5F, 10.0F, 16.0F) {
            public boolean canUse() {
                return super.canUse() && !CoralssusServant.this.getSwim();
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, 6, 6, 7, 100, 100));
        this.goalSelector.addGoal(0, new InternalSummonStateGoal(this, 7, 7, 0, 20, 0) {
            public void stop() {
                super.stop();
                CoralssusServant.this.jump_cooldown = 160;
            }
        });
    }

    public void followGoal() {
        this.goalSelector.addGoal(0, new FollowOwnerWaterGoal(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, 10.0F)
                .add(Attributes.MAX_HEALTH, 160.0F)
                .add(Attributes.ARMOR, 5.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F);
    }

    @Override
    public int xpReward() {
        return 35;
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "nanta")) {
            return this.nantaAnimationState;
        } else if (Objects.equals(input, "angry")) {
            return this.angryAnimationState;
        } else if (Objects.equals(input, "right_fist")) {
            return this.rightfistAnimationState;
        } else if (Objects.equals(input, "left_fist")) {
            return this.leftfistAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "jumping_prepare")) {
            return this.jumpingprepareAnimationState;
        } else if (Objects.equals(input, "jumping")) {
            return this.jumpingAnimationState;
        } else if (Objects.equals(input, "jumping_end")) {
            return this.jumpingendAnimationState;
        } else {
            return Objects.equals(input, "death") ? this.deathAnimationState : new AnimationState();
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOISTNESS, 40000);
        this.entityData.define(VARIANT, Variant.FIRE.id);
        this.entityData.define(RIGHT, false);
        this.entityData.define(CORALSSUS_SWIM, false);
    }

    public boolean isSponge() {
        String s = ChatFormatting.stripFormatting(this.getName().getString());
        return s != null && s.toLowerCase().contains("squarepants") && this.getVariant() == Variant.HORN;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof CoralssusServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.CoralssusLimit.get();
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_29678_, DifficultyInstance p_29679_, MobSpawnType p_29680_, @Nullable SpawnGroupData p_29681_, @Nullable CompoundTag p_29682_) {
        this.setVariant(Variant.byId(this.random.nextInt(3)));
        return super.finalizeSpawn(p_29678_, p_29679_, p_29680_, p_29681_, p_29682_);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0:
                    this.stopAllAnimationStates();
                    break;
                case 1:
                    this.stopAllAnimationStates();
                    this.angryAnimationState.startIfStopped(this.tickCount);
                    break;
                case 2:
                    this.stopAllAnimationStates();
                    this.nantaAnimationState.startIfStopped(this.tickCount);
                    break;
                case 3:
                    this.stopAllAnimationStates();
                    this.rightfistAnimationState.startIfStopped(this.tickCount);
                    break;
                case 4:
                    this.stopAllAnimationStates();
                    this.leftfistAnimationState.startIfStopped(this.tickCount);
                    break;
                case 5:
                    this.stopAllAnimationStates();
                    this.jumpingprepareAnimationState.startIfStopped(this.tickCount);
                    break;
                case 6:
                    this.stopAllAnimationStates();
                    this.jumpingAnimationState.startIfStopped(this.tickCount);
                    break;
                case 7:
                    this.stopAllAnimationStates();
                    this.jumpingendAnimationState.startIfStopped(this.tickCount);
                    break;
                case 8:
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.angryAnimationState.stop();
        this.nantaAnimationState.stop();
        this.rightfistAnimationState.stop();
        this.leftfistAnimationState.stop();
        this.jumpingprepareAnimationState.stop();
        this.jumpingAnimationState.stop();
        this.jumpingendAnimationState.stop();
        this.deathAnimationState.stop();
    }

    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.setAttackState(8);
    }

    @Override
    public int deathTimer() {
        return 30;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
        compound.putInt("Moisture", this.getMoistness());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Variant.byId(compound.getInt("Variant")));
        if (compound.contains("Moisture")) {
            this.setMoistness(compound.getInt("Moisture"));
        }
    }

    public int getMoistness() {
        return this.entityData.get(MOISTNESS);
    }

    public void setMoistness(int p_211137_1_) {
        this.entityData.set(MOISTNESS, p_211137_1_);
    }

    public Variant getVariant() {
        return Variant.byId(this.entityData.get(VARIANT));
    }

    public void setVariant(Variant p_262578_) {
        this.entityData.set(VARIANT, p_262578_.id);
    }

    public void setRight(boolean right) {
        this.entityData.set(RIGHT, right);
    }

    public boolean getIsRight() {
        return this.entityData.get(RIGHT);
    }

    boolean wantsToSwim() {
        if (this.searchingForLand) {
            return true;
        } else {
            LivingEntity livingentity = this.getTarget();
            return livingentity != null && livingentity.isInWater();
        }
    }

    public void travel(Vec3 p_32394_) {
        if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.01F, p_32394_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(p_32394_);
        }

    }

    public void tick() {
        super.tick();
        if (this.isInWater() && this.isLandNavigator) {
            this.switchNavigator(false);
        }

        if (!this.isInWater() && !this.isLandNavigator) {
            this.switchNavigator(true);
        }

        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
        } else if (this.isInWaterRainOrBubble()) {
            if (this.getMoistness() < 40000) {
                this.setMoistness(this.getMoistness() + 2);
            }
        } else {
            int dry = this.level().isDay() ? 2 : 1;
            this.setMoistness(this.getMoistness() - dry);
            if (this.getMoistness() <= 0 && this.moistureAttackTime-- <= 0) {
                this.hurt(this.damageSources().dryOut(), this.random.nextInt(2) == 0 ? 1.0F : 0.0F);
                this.moistureAttackTime = 20;
            }
        }

        boolean flag1 = this.canInFluidType(this.getEyeInFluidType());
        if (flag1) {
            if (this.level().noCollision(this, this.getBoundingBox()) && !this.getSwim()) {
                this.setSwim(true);
            }
        } else if (this.level().noCollision(this, this.getBoundingBox()) && this.getSwim()) {
            this.setSwim(false);
        }

        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!this.walkAnimation.isMoving() && this.getAttackState() == 0, this.tickCount);
        }

        if (this.nanta_cooldown > 0) {
            --this.nanta_cooldown;
        }

        if (this.jump_cooldown > 0) {
            --this.jump_cooldown;
        }

    }

    public void aiStep() {
        super.aiStep();
        float f1 = (float)Math.cos(Math.toRadians(this.getYRot() + 90.0F));
        float f2 = (float)Math.sin(Math.toRadians(this.getYRot() + 90.0F));
        if (this.getAttackState() == 2) {
            if (this.attackTicks == 6 || this.attackTicks == 16 || this.attackTicks == 26 || this.attackTicks == 36 || this.attackTicks == 46) {
                this.push((double)f1 * 0.5, 0.0, (double)f2 * 0.5);
            }

            if (this.attackTicks == 5 || this.attackTicks == 30) {
                this.EarthQuake(3.5F, 2, 60);
                this.MakeParticle(0.5F, 3.15F, 0.2F);
            }

            if (this.attackTicks == 17) {
                this.EarthQuake(3.5F, 2, 60);
                this.MakeParticle(0.5F, 3.15F, -0.2F);
            }

            if (this.attackTicks == 42) {
                this.EarthQuake(3.5F, 2, 60);
                this.MakeParticle(0.5F, 3.15F, -0.2F);
            }
        }

        if (this.getAttackState() == 3 && this.attackTicks == 12) {
            this.EarthQuake(3.5F, 2, 0);
            this.MakeParticle(0.5F, 2.8F, 0.2F);
        }

        if (this.getAttackState() == 4 && this.attackTicks == 12) {
            this.EarthQuake(3.5F, 2, 0);
            this.MakeParticle(0.5F, 2.8F, -0.2F);
        }

        if (this.getAttackState() == 6 && (this.onGround() || !this.getFeetBlockState().getFluidState().isEmpty())) {
            this.setAttackState(7);
        }

        if (this.getAttackState() == 7 && this.attackTicks == 3) {
            this.EarthQuake(4.5F, 5, 120);
            this.MakeParticle(0.5F, 3.1F, -0.4F);
            this.MakeParticle(0.5F, 3.1F, 0.4F);
        }

    }

    private void EarthQuake(float grow, int damage, int shieldbreakticks) {
        ScreenShake_Entity.ScreenShake(this.level(), this.position(), 10.0F, 0.15F, 0, 20);
        this.playSound(SoundEvents.GENERIC_EXPLODE, 0.5F, 1.0F + this.getRandom().nextFloat() * 0.1F);

        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate((double) grow))) {
            if (!MobUtil.areAllies(this, entity)) {
                this.launch(entity, true);
                DamageSource damagesource = this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this);
                entity.hurt(damagesource, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) + (float) this.random.nextInt(damage));
                if (entity.isDamageSourceBlocked(damagesource) && entity instanceof Player player) {
                    if (shieldbreakticks > 0) {
                        this.disableShield(player, shieldbreakticks);
                    }
                }
            }
        }

    }

    private void launch(LivingEntity e, boolean huge) {
        double d0 = e.getX() - this.getX();
        double d1 = e.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        float f = huge ? 2.0F : 0.5F;
        e.push(d0 / d2 * (double)f, huge ? 0.5 : 0.20000000298023224, d1 / d2 * (double)f);
    }

    private void MakeParticle(float size, float vec, float math) {
        if (this.level().isClientSide) {
            float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
            double theta = (yBodyRot) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            for (int i1 = 0; i1 < 80 + random.nextInt(12); i1++) {
                double DeltaMovementX = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementY = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementZ = getRandom().nextGaussian() * 0.07D;
                float angle = (0.01745329251F * this.yBodyRot) + i1;
                double extraX = size * Mth.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = size * Mth.cos(angle);
                int hitX = Mth.floor(getX() + vec * vecX + extraX);
                int hitY = Mth.floor(getY());
                int hitZ = Mth.floor(getZ() + vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = this.level().getBlockState(hit.below());
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), getX() + vec * vecX + extraX + f * math, this.getY() + extraY, getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);
                }
            }
            this.level().addParticle(new RingParticle.RingData(0f, (float)Math.PI/2f, 30, 1.0f, 1.0F,  1.0F, 1.0f, 20f, false, RingParticle.EnumRingBehavior.GROW_THEN_SHRINK), getX() + vec * vecX + f * math, getY() + 0.2f, getZ() + vec * vecZ + f1 * math, 0, 0, 0);
        }

    }

    protected void positionRider(Entity p_289537_, Entity.MoveFunction p_289541_) {
        super.positionRider(p_289537_, p_289541_);
        float f = 0.5F;
        Vec3 vec3 = (new Vec3(0.0, 0.0, (double)f)).yRot(-this.yBodyRot * 0.017453292F);
        p_289541_.accept(p_289537_, this.getX() + vec3.x, this.getY(0.75) + p_289537_.getMyRidingOffset() + 0.0, this.getZ() + vec3.z);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.CORALSSUS_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.CORALSSUS_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {
        return CataclysmSounds.CORALSSUS_AMBIENT.get();
    }

    private boolean canInFluidType(FluidType type) {
        ForgeMod.WATER_TYPE.get();
        return type.canSwim(this.self());
    }

    public boolean isVisuallySwimming() {
        return this.getSwim();
    }

    public void switchNavigator(boolean onLand) {
        if (onLand) {
            this.navigation = new GroundPathNavigatorWide(this, this.level());
            this.isLandNavigator = true;
        } else {
            this.navigation = new SemiAquaticPathNavigator(this, this.level());
            this.isLandNavigator = false;
        }

    }

    public boolean getSwim() {
        return (Boolean)this.entityData.get(CORALSSUS_SWIM);
    }

    public void setSwim(boolean swim) {
        this.entityData.set(CORALSSUS_SWIM, swim);
    }

    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean shouldEnterWater() {
        return this.getMoistness() < 300;
    }

    public boolean shouldLeaveWater() {
        return this.getTarget() != null && !this.getTarget().isInWater();
    }

    public boolean shouldStopMoving() {
        return false;
    }

    public int getWaterSearchRange() {
        return 32;
    }

    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return p_34192_.getEffect() != ModEffect.EFFECTSTUN.get() && p_34192_.getEffect() != ModEffect.EFFECTABYSSAL_CURSE.get() && super.canBeAffected(p_34192_);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }

    protected boolean closeToNextPos() {
        Path path = this.getNavigation().getPath();
        if (path != null) {
            BlockPos blockpos = path.getTarget();
            if (blockpos != null) {
                double d0 = this.distanceToSqr((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
                if (d0 < 4.0) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setSearchingForLand(boolean p_32399_) {
        this.searchingForLand = p_32399_;
    }

    public boolean matchingBasicCoral(ItemStack itemStack){
        if (this.getVariant() == Variant.FIRE){
            return itemStack.is(Items.FIRE_CORAL);
        } else if (this.getVariant() == Variant.HORN){
            return itemStack.is(Items.HORN_CORAL);
        } else if (this.getVariant() == Variant.TUBE){
            return itemStack.is(Items.TUBE_CORAL);
        } else {
            return false;
        }
    }

    public boolean matchingFanCoral(ItemStack itemStack){
        if (this.getVariant() == Variant.FIRE){
            return itemStack.is(Items.FIRE_CORAL_FAN);
        } else if (this.getVariant() == Variant.HORN){
            return itemStack.is(Items.HORN_CORAL_FAN);
        } else if (this.getVariant() == Variant.TUBE){
            return itemStack.is(Items.TUBE_CORAL_FAN);
        } else {
            return false;
        }
    }

    public boolean matchingCoralBlock(ItemStack itemStack){
        if (this.getVariant() == Variant.FIRE){
            return itemStack.is(Items.FIRE_CORAL_BLOCK);
        } else if (this.getVariant() == Variant.HORN){
            return itemStack.is(Items.HORN_CORAL_BLOCK);
        } else if (this.getVariant() == Variant.TUBE){
            return itemStack.is(Items.TUBE_CORAL_BLOCK);
        } else {
            return false;
        }
    }

    public boolean matchingCoral(ItemStack itemStack){
        return this.matchingBasicCoral(itemStack) || this.matchingFanCoral(itemStack) || this.matchingCoralBlock(itemStack);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        if (!this.level().isClientSide) {
            ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner() &&
                    (this.matchingCoral(itemstack) && this.getHealth() < this.getMaxHealth())) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.matchingCoralBlock(itemstack)) {
                    this.heal(this.getMaxHealth() / 4.0F);
                    this.playSound(CataclysmSounds.CORALSSUS_AMBIENT.get(), 1.0F, 1.5F);
                } else if (this.matchingBasicCoral(itemstack) || this.matchingFanCoral(itemstack)) {
                    this.heal((this.getMaxHealth() / 4.0F) / 8.0F);
                    this.playSound(CataclysmSounds.CORALSSUS_AMBIENT.get(), 1.0F, 1.5F);
                }

                if (this.level() instanceof ServerLevel serverLevel) {
                    for(int i = 0; i < 7; ++i) {
                        double d0 = serverLevel.random.nextGaussian() * 0.02;
                        double d1 = serverLevel.random.nextGaussian() * 0.02;
                        double d2 = serverLevel.random.nextGaussian() * 0.02;
                        serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), 0, d0, d1, d2, 0.5);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }

    static class CoralssusMoveControl extends MoveControl {
        private final CoralssusServant drowned;
        private final float speedMulti;

        public CoralssusMoveControl(CoralssusServant p_32433_, float speedMulti) {
            super(p_32433_);
            this.drowned = p_32433_;
            this.speedMulti = speedMulti;
        }

        public void tick() {
            LivingEntity livingentity = this.drowned.getTarget();
            if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
                if (livingentity != null && livingentity.getY() > this.drowned.getY() || this.drowned.searchingForLand) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, 0.002, 0.0));
                }

                if (this.operation != Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
                    this.drowned.setSpeed(0.0F);
                    return;
                }

                double d0 = this.wantedX - this.drowned.getX();
                double d1 = this.wantedY - this.drowned.getY();
                double d2 = this.wantedZ - this.drowned.getZ();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 /= d3;
                float f = (float)(Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F;
                this.drowned.setYRot(this.rotlerp(this.drowned.getYRot(), f, 90.0F));
                this.drowned.yBodyRot = this.drowned.getYRot();
                float f1 = (float)(this.speedModifier * (double)this.speedMulti * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = Mth.lerp(0.125F, this.drowned.getSpeed(), f1);
                this.drowned.setSpeed(f2);
                this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add((double)f2 * d0 * 0.005, (double)f2 * d1 * 0.1, (double)f2 * d2 * 0.005));
            } else {
                if (!this.drowned.onGround()) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, -0.008, 0.0));
                }

                super.tick();
            }

        }
    }

    static class CoralssusSwimUpGoal extends Goal {
        private final CoralssusServant drowned;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;

        public CoralssusSwimUpGoal(CoralssusServant p_32440_, double p_32441_, int p_32442_) {
            this.drowned = p_32440_;
            this.speedModifier = p_32441_;
            this.seaLevel = p_32442_;
        }

        public boolean canUse() {
            return (this.drowned.level().isRaining() || this.drowned.isInWater()) && this.drowned.getY() < (double)(this.seaLevel - 2);
        }

        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }

        public void tick() {
            if (this.drowned.getY() < (double)(this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
                Vec3 vec3 = DefaultRandomPos.getPosTowards(this.drowned, 4, 8, new Vec3(this.drowned.getX(), (double)(this.seaLevel - 1), this.drowned.getZ()), 1.5707963705062866);
                if (vec3 == null) {
                    this.stuck = true;
                    return;
                }

                this.drowned.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
            }

        }

        public void start() {
            this.drowned.setSearchingForLand(true);
            this.stuck = false;
        }

        public void stop() {
            this.drowned.setSearchingForLand(false);
        }
    }

    public static enum Variant implements StringRepresentable {
        FIRE(0, "fire"),
        HORN(1, "horn"),
        TUBE(2, "tube");

        private static final IntFunction<Variant> BY_ID = ByIdMap.sparse(Variant::id, values(), FIRE);
        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        final int id;
        private final String name;

        private Variant(int p_262657_, String p_262679_) {
            this.id = p_262657_;
            this.name = p_262679_;
        }

        public String getSerializedName() {
            return this.name;
        }

        public int id() {
            return this.id;
        }

        public static Variant byId(int p_262665_) {
            return BY_ID.apply(p_262665_);
        }
    }

    class CoralssusJumpPrepareAttackGoal extends InternalSummonAttackGoal {
        private final float attackminrange;
        private final float random;

        public CoralssusJumpPrepareAttackGoal(CoralssusServant entity, int attackstate, int attackendstate, int animationendstate, int attackMaxtick, int attackseetick, float attackminrange, float attackrange, float random) {
            super(entity, attackstate, attackendstate, animationendstate, attackMaxtick, attackseetick, attackrange);
            this.attackminrange = attackminrange;
            this.random = random;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return super.canUse() && target != null && this.entity.distanceTo(target) > this.attackminrange && this.entity.getRandom().nextFloat() * 100.0F < this.random && CoralssusServant.this.jump_cooldown <= 0;
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.entity.attackTicks == 13) {
                if (target != null) {
                    this.entity.getLookControl().setLookAt(target, 60.0F, 30.0F);
                    Vec3 vec3 = (new Vec3(target.getX() - this.entity.getX(), target.getY() - this.entity.getY(), target.getZ() - this.entity.getZ())).normalize();
                    this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(vec3.x * 0.8, 1.0, vec3.z * 0.8));
                } else {
                    this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(0.0, 1.0, 0.0));
                }
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
