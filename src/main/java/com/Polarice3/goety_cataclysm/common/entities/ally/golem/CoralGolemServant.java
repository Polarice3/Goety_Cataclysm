package com.Polarice3.goety_cataclysm.common.entities.ally.golem;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.LLibrarySummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.AttackSummonAnimationGoal1;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.SimpleSummonAnimationGoal;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.AI.CmAttackGoal;
import com.github.L_Ender.cataclysm.entity.AI.MobAIFindWater;
import com.github.L_Ender.cataclysm.entity.AI.MobAILeaveWater;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.etc.ISemiAquatic;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.GroundPathNavigatorWide;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class CoralGolemServant extends LLibrarySummon implements ISemiAquatic {
    private static final EntityDataAccessor<Boolean> GOLEMSWIM = SynchedEntityData.defineId(CoralGolemServant.class, EntityDataSerializers.BOOLEAN);
    private boolean isLandNavigator;
    boolean searchingForLand;
    public static final Animation CORAL_GOLEM_LEAP = Animation.create(100);
    public static final Animation CORAL_GOLEM_SMASH = Animation.create(23);
    public static final Animation CORAL_GOLEM_LEFT_SMASH = Animation.create(36);
    public static final Animation CORAL_GOLEM_RIGHT_SMASH = Animation.create(36);
    public static final int LEAP_ATTACK_COOLDOWN = 160;
    private int leap_attack_cooldown = 0;

    public CoralGolemServant(EntityType<? extends LLibrarySummon> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.5F);
        this.moveControl = new GolemMoveControl(this, 2.0F);
        this.switchNavigator(false);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, CORAL_GOLEM_SMASH, CORAL_GOLEM_LEFT_SMASH, CORAL_GOLEM_RIGHT_SMASH, CORAL_GOLEM_LEAP};
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new AttackSummonAnimationGoal1<>(this, CORAL_GOLEM_LEFT_SMASH, 16, true));
        this.goalSelector.addGoal(0, new AttackSummonAnimationGoal1<>(this, CORAL_GOLEM_RIGHT_SMASH, 16, true));
        this.goalSelector.addGoal(0, new Leap(this, CORAL_GOLEM_LEAP));
        this.goalSelector.addGoal(0, new SimpleSummonAnimationGoal<>(this, CORAL_GOLEM_SMASH));
        this.goalSelector.addGoal(2, new CmAttackGoal(this, 1.0));
        this.goalSelector.addGoal(4, new MobAIFindWater(this, 1.0));
        this.goalSelector.addGoal(4, new MobAILeaveWater(this));
        this.goalSelector.addGoal(5, new WaterWanderGoal<>(this));
        this.goalSelector.addGoal(5, new GolemGoToBeachGoal(this, 1.0));
        this.goalSelector.addGoal(6, new GolemSwimUpGoal(this, 1.0, this.level().getSeaLevel()));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public void followGoal() {
        this.goalSelector.addGoal(0, new FollowOwnerWaterGoal(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.CoralGolemDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.CoralGolemHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.CoralGolemArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8F);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.CoralGolemHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.CoralGolemArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.CoralGolemDamage.get());
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    public boolean hurt(DamageSource source, float damage) {
        return super.hurt(source, damage);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GOLEMSWIM, false);
    }

    @Override
    public int xpReward() {
        return 15;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof CoralGolemServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.CoralGolemLimit.get();
    }

    boolean wantsToSwim() {
        if (this.searchingForLand) {
            return true;
        } else if (this.getTarget() != null && this.getTarget().isInWater()) {
            return true;
        } else {
            return this.getTrueOwner() != null && this.isFollowing() && (this.getTrueOwner().isInWater() || (this.isInWater() && this.getTrueOwner().getY() > this.getY()));
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

        boolean flag1 = this.canInFluidType(this.getEyeInFluidType());
        if (flag1) {
            if (this.level().noCollision(this, this.getBoundingBox()) && !this.getSwim()) {
                this.setSwim(true);
            }
        } else if (this.level().noCollision(this, this.getBoundingBox()) && this.getSwim()) {
            this.setSwim(false);
        }

        if (this.leap_attack_cooldown > 0) {
            --this.leap_attack_cooldown;
        }

        LivingEntity target = this.getTarget();
        if (this.isAlive() && target != null && target.isAlive()) {
            if (!this.getSwim() && this.leap_attack_cooldown <= 0 && !this.isNoAi() && this.getAnimation() == NO_ANIMATION && target.onGround() && this.random.nextInt(25) == 0 && this.distanceTo(target) <= 15.0F) {
                this.leap_attack_cooldown = 160;
                this.setAnimation(CORAL_GOLEM_LEAP);
            } else if (this.distanceTo(target) < 3.75F && !this.isNoAi() && this.getAnimation() == NO_ANIMATION) {
                Animation animation = getRandomAttack(this.random);
                this.setAnimation(animation);
            }
        }

    }

    public void aiStep() {
        super.aiStep();
        if (this.getAnimation() == CORAL_GOLEM_RIGHT_SMASH && this.getAnimationTick() == 16) {
            this.EarthQuake(3.0F, 2);
            this.Makeparticle(2.0F, -0.5F);
        }

        if (this.getAnimation() == CORAL_GOLEM_LEFT_SMASH && this.getAnimationTick() == 16) {
            this.EarthQuake(3.0F, 2);
            this.Makeparticle(2.0F, 0.5F);
        }

        if (this.getAnimation() == CORAL_GOLEM_SMASH && this.getAnimationTick() == 2) {
            this.EarthQuake(4.0F, 4);
            this.Makeparticle(2.25F, 1.25F);
            this.Makeparticle(2.25F, -1.25F);
        }

    }

    private void Makeparticle(float vec, float math) {
        if (this.level().isClientSide) {
            for (int i1 = 0; i1 < 80 + random.nextInt(12); i1++) {
                double DeltaMovementX = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementY = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementZ = getRandom().nextGaussian() * 0.07D;
                float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
                float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
                float angle = (0.01745329251F * this.yBodyRot) + i1;
                double extraX = 0.75F * Mth.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = 0.75F * Mth.cos(angle);
                double theta = (yBodyRot) * (Math.PI / 180);
                theta += Math.PI / 2;
                double vecX = Math.cos(theta);
                double vecZ = Math.sin(theta);
                int hitX = Mth.floor(getX() + vec * vecX + extraX);
                int hitY = Mth.floor(getY());
                int hitZ = Mth.floor(getZ() + vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = level().getBlockState(hit.below());
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), getX() + vec * vecX + extraX + f * math, this.getY() + extraY, getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);
                }
            }
        }
    }


    protected void positionRider(Entity p_289537_, Entity.MoveFunction p_289541_) {
        super.positionRider(p_289537_, p_289541_);
        float radius = 0.5F;
        float angle = (0.01745329251F * this.yBodyRot);
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        p_289541_.accept(p_289537_, this.getX() + extraX, this.getY(0.65D) + p_289537_.getMyRidingOffset() + 0.0D, this.getZ()+ extraZ);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        return null;
    }

    private void EarthQuake(float grow, int damage) {
        ScreenShake_Entity.ScreenShake(this.level(), this.position(), 10.0F, 0.15F, 0, 20);
        this.playSound(SoundEvents.GENERIC_EXPLODE, 0.5F, 1.0F + this.getRandom().nextFloat() * 0.1F);

        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate((double) grow))) {
            if (!MobUtil.areAllies(this, entity) && entity != this) {
                entity.hurt(this.getServantAttack(), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) + (float) this.random.nextInt(damage));
                this.launch(entity, true);
            }
        }

    }

    private static Animation getRandomAttack(RandomSource rand) {
        return switch (rand.nextInt(2)) {
            case 0 -> CORAL_GOLEM_RIGHT_SMASH;
            case 1 -> CORAL_GOLEM_LEFT_SMASH;
            default -> CORAL_GOLEM_RIGHT_SMASH;
        };
    }

    private void launch(LivingEntity e, boolean huge) {
        double d0 = e.getX() - this.getX();
        double d1 = e.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        float f = huge ? 2.0F : 0.5F;
        e.push(d0 / d2 * (double)f, huge ? 0.5 : 0.20000000298023224, d1 / d2 * (double)f);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.CORAL_GOLEM_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.CORAL_GOLEM_DEATH.get();
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
        return (Boolean)this.entityData.get(GOLEMSWIM);
    }

    public void setSwim(boolean swim) {
        this.entityData.set(GOLEMSWIM, swim);
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean shouldEnterWater() {
        return false;
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

    public boolean isCoral(ItemStack itemStack){
        return itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().is(BlockTags.CORALS);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        if (!this.level().isClientSide) {
            ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner() &&
                    (this.isCoral(itemstack) && this.getHealth() < this.getMaxHealth())) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.isCoral(itemstack)) {
                    this.heal((this.getMaxHealth() / 4.0F) / 8.0F);
                    this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.5F);
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

    static class GolemMoveControl extends MoveControl {
        private final CoralGolemServant drowned;
        private final float speedMulti;

        public GolemMoveControl(CoralGolemServant p_32433_, float speedMulti) {
            super(p_32433_);
            this.drowned = p_32433_;
            this.speedMulti = speedMulti;
        }

        public void tick() {
            LivingEntity livingentity = this.drowned.getTarget();
            LivingEntity owner = this.drowned.getTrueOwner();
            if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
                if ((livingentity != null && livingentity.getY() > this.drowned.getY())
                        || this.drowned.searchingForLand
                        || (owner != null && owner.getY() > this.drowned.getY() && this.drowned.isFollowing())) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
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

    static class GolemGoToBeachGoal extends MoveToBlockGoal {
        private final CoralGolemServant drowned;

        public GolemGoToBeachGoal(CoralGolemServant p_32409_, double p_32410_) {
            super(p_32409_, p_32410_, 8, 2);
            this.drowned = p_32409_;
        }

        public boolean canUse() {
            return super.canUse() && this.drowned.level().isRaining() && this.drowned.isInWater() && this.drowned.getY() >= (double)(this.drowned.level().getSeaLevel() - 3);
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected boolean isValidTarget(LevelReader p_32413_, BlockPos p_32414_) {
            BlockPos blockpos = p_32414_.above();
            return p_32413_.isEmptyBlock(blockpos) && p_32413_.isEmptyBlock(blockpos.above()) ? p_32413_.getBlockState(p_32414_).entityCanStandOn(p_32413_, p_32414_, this.drowned) : false;
        }

        public void start() {
            this.drowned.setSearchingForLand(false);
            super.start();
        }

        public void stop() {
            super.stop();
        }
    }

    static class GolemSwimUpGoal extends Goal {
        private final CoralGolemServant drowned;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;

        public GolemSwimUpGoal(CoralGolemServant p_32440_, double p_32441_, int p_32442_) {
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

    static class Leap extends SimpleSummonAnimationGoal<CoralGolemServant> {
        private final CoralGolemServant drowned;

        public Leap(CoralGolemServant entity, Animation animation) {
            super(entity, animation);
            this.drowned = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public void tick() {
            LivingEntity target = this.drowned.getTarget();
            if (target != null) {
                this.drowned.lookAt(target, 30.0F, 30.0F);
                this.drowned.getLookControl().setLookAt(target, 30.0F, 30.0F);
                if (this.drowned.getAnimationTick() == 22) {
                    double d0 = target.getX() - this.drowned.getX();
                    double d1 = target.getY() - this.drowned.getY();
                    double d2 = target.getZ() - this.drowned.getZ();
                    this.drowned.setDeltaMovement(d0 * 0.15, 0.75 + Mth.clamp(d1 * 0.05, 0.0, 10.0), d2 * 0.15);
                }
            } else if (this.drowned.getAnimationTick() == 22) {
                this.drowned.setDeltaMovement(0.0, 0.75, 0.0);
            }

            if (this.drowned.getAnimationTick() > 22 && this.drowned.onGround()) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this.drowned, CoralGolemServant.CORAL_GOLEM_SMASH);
            }

        }
    }
}
