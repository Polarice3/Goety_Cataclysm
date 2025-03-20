package com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

public class EliteDraugrServant extends InternalAnimationSummon implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(EliteDraugrServant.class, EntityDataSerializers.BOOLEAN);
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState ReloadAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState attack2AnimationState = new AnimationState();
    public AnimationState swingAnimationState = new AnimationState();
    public AnimationState Shoot1AnimationState = new AnimationState();
    public AnimationState Shoot2AnimationState = new AnimationState();

    public EliteDraugrServant(EntityType<? extends InternalAnimationSummon> entity, Level world) {
        super(entity, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new ReloadedGoal(this, 1, 0, 20, 20.0F));
        this.goalSelector.addGoal(0, new CrossBowShootGoal(this, 0, 4, 0, 23, 15, 12.0F));
        this.goalSelector.addGoal(1, new CrossBowReloadGoal(this, 0, 1, 1, 30, 15, 12.0F));
        this.goalSelector.addGoal(2, new WanderGoal<>(this, 1.0, 80));
        this.goalSelector.addGoal(4, new Elite_DraugrAttackGoal(this, 1.0, 12.0F, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void followGoal() {
        this.goalSelector.addGoal(3, new FollowOwnerGoal<>(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.27F)
                .add(Attributes.ATTACK_DAMAGE, 5.0F)
                .add(Attributes.MAX_HEALTH, 32.0F)
                .add(Attributes.ARMOR, 3.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1F);
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.EliteDraugrLimit.get();
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "attack")) {
            return this.attackAnimationState;
        } else if (Objects.equals(input, "attack2")) {
            return this.attack2AnimationState;
        } else if (Objects.equals(input, "re_load")) {
            return this.ReloadAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "swing")) {
            return this.swingAnimationState;
        } else if (Objects.equals(input, "shoot")) {
            return this.Shoot1AnimationState;
        } else {
            return Objects.equals(input, "shoot2") ? this.Shoot2AnimationState : new AnimationState();
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0:
                    this.stopAllAnimationStates();
                    break;
                case 1:
                    this.stopAllAnimationStates();
                    this.ReloadAnimationState.startIfStopped(this.tickCount);
                    break;
                case 2:
                    this.stopAllAnimationStates();
                    this.Shoot1AnimationState.startIfStopped(this.tickCount);
                    break;
                case 3:
                    this.stopAllAnimationStates();
                    this.swingAnimationState.startIfStopped(this.tickCount);
                    break;
                case 4:
                    this.stopAllAnimationStates();
                    this.Shoot2AnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public void stopAllAnimationStates() {
        this.attackAnimationState.stop();
        this.attack2AnimationState.stop();
        this.ReloadAnimationState.stop();
        this.swingAnimationState.stop();
        this.Shoot1AnimationState.stop();
        this.Shoot2AnimationState.stop();
    }

    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    public void handleEntityEvent(byte p_219360_) {
        if (p_219360_ == 4) {
            if (this.random.nextBoolean()) {
                this.attackAnimationState.start(this.tickCount);
            } else {
                this.attack2AnimationState.start(this.tickCount);
            }
        } else {
            super.handleEntityEvent(p_219360_);
        }

    }

    public boolean doHurtTarget(Entity p_219472_) {
        this.level().broadcastEntityEvent(this, (byte)4);
        return super.doHurtTarget(p_219472_);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        return spawngroupdata;
    }

    private ItemStack createSpawnWeapon() {
        return new ItemStack(Items.CROSSBOW);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
        }

    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.DRAUGR_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.DRAUGR_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {
        return CataclysmSounds.DRAUGR_IDLE.get();
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean p_33302_) {
        this.entityData.set(IS_CHARGING_CROSSBOW, p_33302_);
    }

    public void shootCrossbowProjectile(LivingEntity p_32328_, ItemStack p_32329_, Projectile p_32330_, float p_32331_) {
        this.shootCrossbowProjectile(this, p_32328_, p_32330_, p_32331_, 1.6F);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if ((itemstack.is(Items.ROTTEN_FLESH) || itemstack.is(Tags.Items.BONES)) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                this.heal(2.0F);
                if (this.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    static class Elite_DraugrAttackGoal extends Goal {
        public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);
        private final EliteDraugrServant mob;
        private final double speedModifier;
        private final float attackRadiusSqr;
        private int seeTime;
        private int updatePathDelay;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;

        public Elite_DraugrAttackGoal(EliteDraugrServant p_25814_, double p_25815_, float p_25816_, boolean p_25554_) {
            this.mob = p_25814_;
            this.speedModifier = p_25815_;
            this.attackRadiusSqr = p_25816_ * p_25816_;
            this.followingTargetEvenIfNotSeen = p_25554_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            if (this.isHoldingCrossbow()) {
                return true;
            } else {
                long i = this.mob.level().getGameTime();
                if (i - this.lastCanUseCheck < 20L) {
                    return false;
                } else {
                    this.lastCanUseCheck = i;
                    LivingEntity livingentity = this.mob.getTarget();
                    if (livingentity == null) {
                        return false;
                    } else if (!livingentity.isAlive()) {
                        return false;
                    } else if (this.canPenalize) {
                        if (--this.ticksUntilNextPathRecalculation <= 0) {
                            this.path = this.mob.getNavigation().createPath(livingentity, 0);
                            this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                            return this.path != null;
                        } else {
                            return true;
                        }
                    } else {
                        this.path = this.mob.getNavigation().createPath(livingentity, 0);
                        if (this.path != null) {
                            return true;
                        } else {
                            return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                        }
                    }
                }
            }
        }

        private boolean isHoldingCrossbow() {
            return this.mob.isHolding((is) -> {
                return is.getItem() instanceof CrossbowItem;
            });
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (this.isHoldingCrossbow()) {
                return this.isValidTarget() && (this.canUse() || !this.mob.getNavigation().isDone());
            } else if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (this.isHoldingCrossbow()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
            }
        }

        private boolean isValidTarget() {
            return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        public void stop() {
            super.stop();
            LivingEntity livingentity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.mob.setTarget(null);
            }

            this.mob.setAggressive(false);
            this.seeTime = 0;
            if (this.isHoldingCrossbow() && this.mob.isUsingItem()) {
                this.mob.stopUsingItem();
                this.mob.setChargingCrossbow(false);
                CrossbowItem.setCharged(this.mob.getUseItem(), false);
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                double t0 = this.mob.distanceToSqr(target);
                if (this.isHoldingCrossbow()) {
                    boolean flag = this.mob.getSensing().hasLineOfSight(target);
                    boolean flag1 = this.seeTime > 0;
                    if (flag != flag1) {
                        this.seeTime = 0;
                    }

                    if (flag) {
                        ++this.seeTime;
                    } else {
                        --this.seeTime;
                    }

                    boolean flag2 = t0 > (double)this.attackRadiusSqr || this.seeTime < 5;
                    if (flag2) {
                        --this.updatePathDelay;
                        if (this.updatePathDelay <= 0) {
                            this.mob.getNavigation().moveTo(target, this.speedModifier * 0.5);
                            this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(this.mob.getRandom());
                        }
                    } else {
                        this.updatePathDelay = 0;
                        this.mob.getNavigation().stop();
                    }

                    if (t0 < 10.0) {
                        this.mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CataclysmItems.BLACK_STEEL_SWORD.get()));
                    }

                    this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                    double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(target);
                    this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                    if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(target)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05F)) {
                        this.pathedTargetX = target.getX();
                        this.pathedTargetY = target.getY();
                        this.pathedTargetZ = target.getZ();
                        this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                        if (this.canPenalize) {
                            this.ticksUntilNextPathRecalculation += this.failedPathFindingPenalty;
                            if (this.mob.getNavigation().getPath() != null) {
                                Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                                if (finalPathPoint != null && target.distanceToSqr((double)finalPathPoint.x, (double)finalPathPoint.y, (double)finalPathPoint.z) < 1.0) {
                                    this.failedPathFindingPenalty = 0;
                                } else {
                                    this.failedPathFindingPenalty += 10;
                                }
                            } else {
                                this.failedPathFindingPenalty += 10;
                            }
                        }

                        if (d0 > 1024.0) {
                            this.ticksUntilNextPathRecalculation += 10;
                        } else if (d0 > 256.0) {
                            this.ticksUntilNextPathRecalculation += 5;
                        }

                        if (!this.mob.getNavigation().moveTo(target, this.speedModifier)) {
                            this.ticksUntilNextPathRecalculation += 15;
                        }

                        this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                    }

                    if (t0 > 10.0) {
                        this.mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
                    }

                    this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                    this.checkAndPerformAttack(target, d0);
                }
            }

        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(p_25557_);
            }

        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            float f = p_25556_.getBbWidth();
            return (double)(f * 2.25F * f * 2.25F + p_25556_.getBbWidth());
        }
    }

    static class CrossBowReloadGoal extends Goal {
        protected final EliteDraugrServant entity;
        private final int getattackstate;
        private final int attackstate;
        private final int attackendstate;
        private final int attackMaxtick;
        private final int attackseetick;
        private final float attackrange;
        private CrossbowState crossbowState;

        public CrossBowReloadGoal(EliteDraugrServant entity, int getattackstate, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange) {
            this.crossbowState = EliteDraugrServant.CrossbowState.UNCHARGED;
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.getattackstate = getattackstate;
            this.attackstate = attackstate;
            this.attackendstate = attackendstate;
            this.attackMaxtick = attackMaxtick;
            this.attackseetick = attackseetick;
            this.attackrange = attackrange;
        }

        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return target != null && this.entity.getRandom().nextFloat() * 100.0F < 22.0F && target.isAlive() && this.entity.distanceTo(target) < this.attackrange && this.entity.getAttackState() == this.getattackstate && this.isHoldingCrossbow() && !this.entity.isChargingCrossbow();
        }

        public void start() {
            this.entity.setAttackState(this.attackstate);
        }

        public void stop() {
            this.entity.setAttackState(this.attackendstate);
        }

        public boolean canContinueToUse() {
            return this.entity.getAttackState() == this.attackstate && this.entity.attackTicks <= this.attackMaxtick;
        }

        private boolean isHoldingCrossbow() {
            return this.entity.isHolding((is) -> {
                return is.getItem() instanceof CrossbowItem;
            });
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.entity.attackTicks < this.attackseetick && target != null) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                this.entity.lookAt(target, 30.0F, 30.0F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }

            if (this.entity.attackTicks == 5) {
                this.entity.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.entity, (item) -> {
                    return item instanceof CrossbowItem;
                }));
            }

            int i = this.entity.getTicksUsingItem();
            ItemStack itemstack = this.entity.getUseItem();
            if (i >= CrossbowItem.getChargeDuration(itemstack)) {
                this.entity.releaseUsingItem();
                this.entity.setChargingCrossbow(true);
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class ReloadedGoal extends Goal {
        protected final EliteDraugrServant entity;
        private final int getattackstate;
        private final int attackendstate;
        private final int attackseetick;
        private final float attackrange;

        public ReloadedGoal(EliteDraugrServant entity, int getattackstate, int attackendstate, int attackseetick, float attackrange) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.getattackstate = getattackstate;
            this.attackendstate = attackendstate;
            this.attackseetick = attackseetick;
            this.attackrange = attackrange;
        }

        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return target != null && target.isAlive() && this.entity.distanceTo(target) < this.attackrange && this.entity.getAttackState() == this.getattackstate && this.isHoldingCrossbow() && this.entity.isChargingCrossbow();
        }

        public void start() {
            LivingEntity livingentity = this.entity.getTarget();
            boolean flag = true;
            if (livingentity != null) {
                float f = livingentity.getBbWidth();
                float dis = f * 2.5F * f * 2.5F + livingentity.getBbWidth();
                double d0 = this.entity.getPerceivedTargetDistanceSquareForMeleeAttack(livingentity);
                if (d0 <= (double)dis) {
                    flag = false;
                }
            }

            if (flag) {
                this.entity.setAttackState(2);
            } else {
                this.entity.setAttackState(3);
            }

        }

        public void stop() {
            this.entity.setAttackState(this.attackendstate);
        }

        public boolean canContinueToUse() {
            return (this.entity.getAttackState() == 2 || this.entity.getAttackState() == 3) && this.entity.attackTicks <= 30;
        }

        private boolean isHoldingCrossbow() {
            return this.entity.isHolding((is) -> {
                return is.getItem() instanceof CrossbowItem;
            });
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.entity.attackTicks < this.attackseetick && target != null) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                this.entity.lookAt(target, 30.0F, 30.0F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }

            if (this.entity.getAttackState() == 2 && target != null && this.entity.attackTicks == 10) {
                this.entity.performRangedAttack(target, 1.0F);
                ItemStack itemstack1 = this.entity.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.entity, (item) -> {
                    return item instanceof CrossbowItem;
                }));
                CrossbowItem.setCharged(itemstack1, false);
                this.entity.setChargingCrossbow(false);
            }

            if (this.entity.getAttackState() == 3 && target != null && this.entity.attackTicks == 11) {
                DamageSource damagesource = this.entity.damageSources().mobAttack(this.entity);
                target.hurt(damagesource, (float)this.entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            float f = p_25556_.getBbWidth();
            return (double)(f * 2.5F * f * 2.5F + p_25556_.getBbWidth());
        }
    }

    static class CrossBowShootGoal extends Goal {
        protected final EliteDraugrServant entity;
        private final int getattackstate;
        private final int attackstate;
        private final int attackendstate;
        private final int attackMaxtick;
        private final int attackseetick;
        private final float attackrange;

        public CrossBowShootGoal(EliteDraugrServant entity, int getattackstate, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.getattackstate = getattackstate;
            this.attackstate = attackstate;
            this.attackendstate = attackendstate;
            this.attackMaxtick = attackMaxtick;
            this.attackseetick = attackseetick;
            this.attackrange = attackrange;
        }

        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return target != null && this.entity.getRandom().nextFloat() * 100.0F < 22.0F && target.isAlive() && this.entity.distanceTo(target) < this.attackrange && this.entity.getAttackState() == this.getattackstate && this.isHoldingCrossbow() && this.entity.isChargingCrossbow();
        }

        public void start() {
            this.entity.setAttackState(this.attackstate);
        }

        public void stop() {
            this.entity.setAttackState(this.attackendstate);
        }

        public boolean canContinueToUse() {
            return this.entity.getAttackState() == this.attackstate && this.entity.attackTicks <= this.attackMaxtick;
        }

        private boolean isHoldingCrossbow() {
            return this.entity.isHolding((is) -> {
                return is.getItem() instanceof CrossbowItem;
            });
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.entity.attackTicks < this.attackseetick && target != null) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                this.entity.lookAt(target, 30.0F, 30.0F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }

            if (target != null && this.entity.attackTicks == 11) {
                this.entity.performRangedAttack(target, 1.0F);
                ItemStack itemstack1 = this.entity.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.entity, (item) -> {
                    return item instanceof CrossbowItem;
                }));
                CrossbowItem.setCharged(itemstack1, false);
                this.entity.setChargingCrossbow(false);
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    private static enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;

        private CrossbowState() {
        }
    }
}
