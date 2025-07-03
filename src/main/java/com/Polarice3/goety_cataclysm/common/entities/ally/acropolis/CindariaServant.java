package com.Polarice3.goety_cataclysm.common.entities.ally.acropolis;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import com.github.L_Ender.cataclysm.entity.projectile.Water_Spear_Entity;
import com.github.L_Ender.cataclysm.init.ModEffect;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class CindariaServant extends InternalAnimationSummon {
    boolean searchingForLand;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState magic1AnimationState = new AnimationState();
    public AnimationState meleeAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    private int magic_cooldown = 0;
    public static final int CHARGE_COOLDOWN = 100;
    protected final SemiAquaticPathNavigator waterNavigation;
    protected final CMPathNavigateGround groundNavigation;

    public CindariaServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.75F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
        this.waterNavigation = new SemiAquaticPathNavigator(this, world);
        this.groundNavigation = new CMPathNavigateGround(this, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new WaterWanderGoal<>(this, 1.0D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new CindariaMoveGoal(this, 1.0, 8.0F));
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, 2, 0, 39, 13, 3.75F));
        this.goalSelector.addGoal(2, new MagicAttackGoal(this, 0, 1, 0, 50, 15, 4.5F, 16.0F));
    }

    @Override
    public void followGoal() {
        this.goalSelector.addGoal(5, new FollowOwnerWaterGoal(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ARMOR, GCAttributesConfig.CindariaArmor.get())
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.CindariaMeleeDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.CindariaHealth.get());
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.CindariaHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.CindariaArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.CindariaMeleeDamage.get());
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public int xpReward() {
        return 25;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof CindariaServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.CindariaLimit.get();
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
                this.moveControl = new CindariaSwimControl(this, 4.0F);
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
        if (Objects.equals(input, "magic1")) {
            return this.magic1AnimationState;
        } else if (Objects.equals(input, "melee")) {
            return this.meleeAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else {
            return Objects.equals(input, "death") ? this.deathAnimationState : new AnimationState();
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
                    this.magic1AnimationState.startIfStopped(this.tickCount);
                    break;
                case 2:
                    this.stopAllAnimationStates();
                    this.meleeAnimationState.startIfStopped(this.tickCount);
                    break;
                case 3:
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.magic1AnimationState.stop();
        this.meleeAnimationState.stop();
        this.deathAnimationState.stop();
    }

    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.setAttackState(3);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.CINDARIA_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.CINDARIA_DEATH.get();
    }

    public int deathTimer() {
        return 40;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
        }

    }

    public void aiStep() {
        super.aiStep();
        if (this.getAttackState() == 2 && this.attackTicks == 14) {
            this.AreaAttack(4.75F, 4.75F, 100.0F, 1.0F, 0, true);
        }

    }

    private void AreaAttack(float range, float height, float arc, float damage, int shieldbreakticks,boolean knockback) {
        List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(range, height, range, range);
        if (!this.level().isClientSide) {
            for (LivingEntity entityHit : entitiesHit) {
                float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = this.yBodyRot % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX()));
                if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                    if (!MobUtil.areAllies(this, entityHit)) {
                        DamageSource damagesource = this.getServantAttack();
                        boolean hurt = entityHit.hurt(damagesource, (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                        if (entityHit.isDamageSourceBlocked(damagesource) && shieldbreakticks > 0) {
                            disableShield(entityHit, shieldbreakticks);
                        }

                        double d0 = entityHit.getX() - this.getX();
                        double d1 = entityHit.getZ() - this.getZ();
                        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                        if (hurt && knockback) {
                            entityHit.push(d0 / d2 * 2.25D, 0.15D, d1 / d2 * 2.25D);
                        }
                    }
                }
            }
        }
    }

    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return p_34192_.getEffect() != ModEffect.EFFECTABYSSAL_CURSE.get() && super.canBeAffected(p_34192_);
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
            }
        }
        return InteractionResult.PASS;
    }

    public static class CindariaMoveGoal extends Goal {
        private final CindariaServant mob;
        private final double speedModifier;
        private final float attackRadiusSqr;
        private int seeTime;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public CindariaMoveGoal(CindariaServant p_25792_, double p_25793_, float p_25795_) {
            this.mob = p_25792_;
            this.speedModifier = p_25793_;
            this.attackRadiusSqr = p_25795_ * p_25795_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        public boolean canContinueToUse() {
            return this.canUse() || !this.mob.getNavigation().isDone();
        }

        public void start() {
            super.start();
            this.mob.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
            this.seeTime = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }

                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.mob.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (d0 > (double)(this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (d0 < (double)(this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.8F : 0.8F, 0.0F);
                    if (this.mob.getControlledVehicle() instanceof Mob mob1) {
                        mob1.lookAt(livingentity, 30.0F, 30.0F);
                    }

                    this.mob.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }
            }

        }
    }

    static class MagicAttackGoal extends Goal {
        protected final CindariaServant entity;
        private final int getAttackState;
        private final int attackstate;
        private final int attackendstate;
        private final int attackMaxtick;
        private final int attackseetick;
        private final float attackminrange;
        private final float attackrange;

        public MagicAttackGoal(CindariaServant entity, int getAttackState, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackminrange, float attackrange) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.getAttackState = getAttackState;
            this.attackstate = attackstate;
            this.attackendstate = attackendstate;
            this.attackMaxtick = attackMaxtick;
            this.attackseetick = attackseetick;
            this.attackminrange = attackminrange;
            this.attackrange = attackrange;
        }

        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return target != null && this.entity.distanceTo(target) > this.attackminrange && target.isAlive() && this.entity.distanceTo(target) < this.attackrange && this.entity.getAttackState() == this.getAttackState && this.entity.magic_cooldown <= 0;
        }

        public void start() {
            this.entity.setAttackState(this.attackstate);
        }

        public void stop() {
            this.entity.setAttackState(this.attackendstate);
        }

        public boolean canContinueToUse() {
            return this.entity.attackTicks < this.attackMaxtick;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.entity.attackTicks < this.attackseetick && target != null) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                this.entity.lookAt(target, 30.0F, 30.0F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }

            if (this.entity.attackTicks == this.attackseetick && target != null) {
                double d0 = this.entity.getX();
                double d1 = this.entity.getY() + (double)(this.entity.getBbHeight() * 0.5F);
                double d2 = this.entity.getZ();
                double d3 = target.getX() - d0;
                double d4 = target.getY() - d1;
                double d5 = target.getZ() - d2;
                Vec3 vec3 = (new Vec3(d3, d4, d5)).normalize();
                Water_Spear_Entity waterSpear = new Water_Spear_Entity(this.entity, vec3, this.entity.level(), GCAttributesConfig.CindariaRangeDamage.get().floatValue());
                float yRot = (float)(Mth.atan2(vec3.z, vec3.x) * 57.29577951308232) + 90.0F;
                float xRot = (float)(-(Mth.atan2(vec3.y, Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z)) * 57.29577951308232));
                waterSpear.setYRot(yRot);
                waterSpear.setXRot(xRot);
                waterSpear.setPosRaw(d0, d1, d2);
                waterSpear.setTotalBounces(10);
                this.entity.level().addFreshEntity(waterSpear);
            }

        }
    }

    static class CindariaSwimControl extends MoveControl {
        private final CindariaServant drowned;
        private final float speedMulti;

        public CindariaSwimControl(CindariaServant p_32433_, float speedMulti) {
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
