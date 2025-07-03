package com.Polarice3.goety_cataclysm.common.entities.ally.acropolis;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.ally.undead.zombie.DrownedServant;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.OctoInk;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;

public class SymbioctoServant extends Summoned implements RangedAttackMob {
    boolean searchingForLand;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState spitAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(SymbioctoServant.class, EntityDataSerializers.INT);
    public int attackTicks;
    public float interestTime;
    protected final SemiAquaticPathNavigator waterNavigation;
    protected final GroundPathNavigation groundNavigation;

    public SymbioctoServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.waterNavigation = new SemiAquaticPathNavigator(this, worldIn);
        this.groundNavigation = new GroundPathNavigation(this, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new CrossBowShootGoal(this, 0, 1, 0, 34, 19, 16.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new WaterWanderGoal<>(this, 1.0D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void followGoal() {
        this.goalSelector.addGoal(5, new FollowOwnerWaterGoal(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ARMOR, GCAttributesConfig.SymbioctoArmor.get())
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.SymbioctoMeleeDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.SymbioctoHealth.get());
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.SymbioctoHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.SymbioctoArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.SymbioctoMeleeDamage.get());
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public int xpReward() {
        return 5;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof SymbioctoServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.OctoHostLimit.get();
    }

    @Override
    public boolean canUpdateMove() {
        return true;
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

    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(travelVector);
        }

    }

    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
                this.navigation = this.waterNavigation;
                this.moveControl = new SymbioctoSwimControl(this, 4.0F);
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigation;
                this.moveControl = new MoveControl(this);
                this.setSwimming(false);
            }
        }

    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "spit")) {
            return this.spitAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else {
            return Objects.equals(input, "attack") ? this.attackAnimationState : new AnimationState();
        }
    }

    public boolean isCloseEye() {
        return this.getVehicle() instanceof DrownedServant || this.getVehicle() instanceof Player;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_STATE, 0);
    }

    public double getMyRidingOffset() {
        return 0.2D;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0:
                    this.stopAllAnimationStates();
                    break;
                case 1:
                    this.stopAllAnimationStates();
                    this.spitAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public int getAttackState() {
        return this.entityData.get(ATTACK_STATE);
    }

    public void setAttackState(int input) {
        this.attackTicks = 0;
        this.entityData.set(ATTACK_STATE, input);
        this.level().broadcastEntityEvent(this, (byte)(-input));
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.isInvulnerableTo(source);
    }

    public void stopAllAnimationStates() {
        this.spitAnimationState.stop();
    }

    public boolean doHurtTarget(Entity p_219472_) {
        this.level().broadcastEntityEvent(this, (byte)4);
        return super.doHurtTarget(p_219472_);
    }

    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.attackTicks = 0;
        } else if (id == 4) {
            this.attackAnimationState.start(this.tickCount);
        } else if (id == 5){
            this.interestTime = 40;
            this.playSound(SoundEvents.SQUID_AMBIENT, 1.0F, 2.0F);
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        } else {
            super.handleEntityEvent(id);
        }

    }

    protected void addParticlesAroundSelf(ParticleOptions pParticleData) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(pParticleData, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    @Override
    public boolean canRide(LivingEntity livingEntity) {
        if (livingEntity instanceof DrownedServant drownedServant) {
            if (!drownedServant.isBaby()) {
                if (drownedServant.getTrueOwner() == this.getTrueOwner()) {
                    return true;
                }
            }
        }
        return super.canRide(livingEntity);
    }

    @Override
    public boolean startRiding(Entity entity) {
        if (entity instanceof DrownedServant drownedServant) {
            if (drownedServant.getTrueOwner() == this.getTrueOwner()) {
                if (drownedServant.getType() != GCEntityType.DROWNED_HOST_SERVANT.get()) {
                    DrownedHostServant drownedHostServant = drownedServant.convertTo(GCEntityType.DROWNED_HOST_SERVANT.get(), true);
                    if (drownedHostServant != null) {
                        drownedHostServant.setYBodyRot(drownedServant.yBodyRot);
                        drownedHostServant.setYHeadRot(drownedServant.getYHeadRot());
                        drownedHostServant.setYRot(drownedServant.getYRot());
                        float health = drownedServant.getHealth();
                        if (drownedHostServant.getMaxHealth() > drownedServant.getMaxHealth()) {
                            health += drownedHostServant.getMaxHealth() - drownedServant.getMaxHealth();
                        }
                        drownedHostServant.setHealth(health);
                        if (this.getTrueOwner() != null) {
                            drownedHostServant.setTrueOwner(this.getTrueOwner());
                        }
                        this.setYBodyRot(drownedHostServant.yBodyRot);
                        this.setYHeadRot(drownedHostServant.getYHeadRot());
                        this.setYRot(drownedHostServant.getYRot());
                        return super.startRiding(drownedHostServant);
                    }
                }
            }
        }
        return super.startRiding(entity);
    }

    protected boolean canRide(Entity p_20339_) {
        if (this.getTrueOwner() != null) {
            if (p_20339_ instanceof Player player) {
                if (this.getTrueOwner() == player) {
                    return !this.isShiftKeyDown();
                }
            }
        }
        return super.canRide(p_20339_);
    }

    public void tick() {
        super.tick();
        if (this.getAttackState() > 0) {
            ++this.attackTicks;
        }

        if (this.interestTime > 0){
            --this.interestTime;
        }

        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
        } else {
            if (GCMobsConfig.SymbioctoBreath.get()) {
                if (this.getVehicle() instanceof LivingEntity livingEntity && livingEntity.isAlive() && this.getTarget() != livingEntity) {
                    if (livingEntity.getEyeInFluidType() == ForgeMod.WATER_TYPE.get()
                            && livingEntity.canDrownInFluidType(livingEntity.getEyeInFluidType())) {
                        if (livingEntity.getAirSupply() < livingEntity.getMaxAirSupply()) {
                            livingEntity.setAirSupply(Math.min(livingEntity.getAirSupply() + 4, livingEntity.getMaxAirSupply()));
                        }
                    }
                }
            }
        }
    }

    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() instanceof Player player) {
            this.setYBodyRot(player.yBodyRot);
            this.setYHeadRot(player.getYHeadRot());
            this.setYRot(player.getYRot());
        }
    }

    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SQUID_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_33318_) {
        OctoInk octoInk = new OctoInk(this.level(), this);
        octoInk.setDamage(GCAttributesConfig.SymbioctoRangeDamage.get().floatValue());
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333) - octoInk.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2) * 0.2D;
        octoInk.shoot(d0, d1 + d3, d2, 1.5F, 10.0F);
        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SQUID_SQUIRT, this.getSoundSource(), 0.5F, 1.0F);
        }

        this.level().addFreshEntity(octoInk);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (itemstack.is(ItemTags.FISHES) && this.getHealth() < this.getMaxHealth()) {
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
            } else if (pPlayer.getMainHandItem().isEmpty()) {
                if (!this.isPassenger() && pPlayer.isShiftKeyDown()) {
                    this.startRiding(pPlayer);
                } else if (this.getVehicle() == pPlayer) {
                    this.stopRiding();
                } else if (this.interestTime <= 0) {
                    this.interestTime = 40;
                    this.playSound(SoundEvents.SQUID_AMBIENT, 1.0F, 2.0F);
                    this.level().broadcastEntityEvent(this, (byte) 5);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    static class CrossBowShootGoal extends Goal {
        protected final SymbioctoServant entity;
        private final int getattackstate;
        private final int attackstate;
        private final int attackendstate;
        private final int attackMaxtick;
        private final int attackseetick;
        private final float attackrange;

        public CrossBowShootGoal(SymbioctoServant entity, int getattackstate, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange) {
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
            return target != null && target.isAlive() && this.entity.distanceTo(target) < this.attackrange && !this.entity.isWithinMeleeAttackRange(target) && this.entity.getAttackState() == this.getattackstate;
        }

        public void start() {
            this.entity.setAttackState(this.attackstate);
        }

        public void stop() {
            this.entity.setAttackState(this.attackendstate);
            LivingEntity target = this.entity.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
                this.entity.setTarget((LivingEntity)null);
            }

        }

        public boolean canContinueToUse() {
            return this.entity.getAttackState() == this.attackstate && this.entity.attackTicks <= this.attackMaxtick;
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.entity.attackTicks < this.attackseetick && target != null) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                this.entity.lookAt(target, 30.0F, 30.0F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }

            if (target != null && this.entity.attackTicks == 19) {
                this.entity.performRangedAttack(target, 1.0F);
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class SymbioctoSwimControl extends MoveControl {
        private final SymbioctoServant drowned;
        private final float speedMulti;

        public SymbioctoSwimControl(SymbioctoServant p_32433_, float speedMulti) {
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
}
