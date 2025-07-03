package com.Polarice3.goety_cataclysm.common.entities.ally.acropolis;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModParticle;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
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

public class HippocamtusServant extends InternalAnimationSummon {
    boolean searchingForLand;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState swing1AnimationState = new AnimationState();
    public AnimationState swing2AnimationState = new AnimationState();
    public AnimationState stabAnimationState = new AnimationState();
    public AnimationState guardAnimationState = new AnimationState();
    public AnimationState guardcounterAnimationState = new AnimationState();
    public AnimationState parryingAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    public static final EntityDataAccessor<Boolean> STAB = SynchedEntityData.defineId(HippocamtusServant.class, EntityDataSerializers.BOOLEAN);
    private int block_stage = 0;
    private int charge_cooldown = 0;
    public static final int CHARGE_COOLDOWN = 100;
    private int guard_cooldown = 0;
    public static final int GUARD_COOLDOWN = 160;
    public static int SWING_1 = 1;
    public static int SWING_2 = 2;
    public static int STAB_ATTACK = 3;
    public static int GUARD = 4;
    public static int PARRYING = 5;
    public static int GUARD_COUNTER = 6;
    public static int DEATH = 7;
    protected final SemiAquaticPathNavigator waterNavigation;
    protected final CMPathNavigateGround groundNavigation;

    public HippocamtusServant(EntityType<? extends Summoned> entity, Level world) {
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
        this.goalSelector.addGoal(3, new InternalSummonMoveGoal(this, false, 1.0));
        this.goalSelector.addGoal(2, new ChargeAttackGoal(this, 0, STAB_ATTACK, 0, 41, 13, 4.5F, 16.0F));
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, GUARD, 0, 50, 50, 4.0F) {
            public boolean canUse() {
                return super.canUse() && HippocamtusServant.this.getRandom().nextFloat() * 100.0F < 48.0F && HippocamtusServant.this.guard_cooldown <= 0;
            }

            public boolean canContinueToUse() {
                return super.canContinueToUse() && HippocamtusServant.this.block_stage == 0;
            }

            public void stop() {
                HippocamtusServant.this.guard_cooldown = 160;
                if (HippocamtusServant.this.block_stage == 1) {
                    this.entity.setAttackState(5);
                } else if (HippocamtusServant.this.block_stage == 2) {
                    this.entity.setAttackState(6);
                } else {
                    super.stop();
                }

            }
        });
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, SWING_1, 0, 45, 8, 4.0F) {
            public boolean canUse() {
                return super.canUse() && HippocamtusServant.this.getStab();
            }

            public void stop() {
                super.stop();
                HippocamtusServant.this.setStab(HippocamtusServant.this.random.nextBoolean());
            }
        });
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, SWING_2, 0, 39, 8, 4.0F) {
            public boolean canUse() {
                return super.canUse() && !HippocamtusServant.this.getStab();
            }

            public void stop() {
                super.stop();
                HippocamtusServant.this.setStab(HippocamtusServant.this.random.nextBoolean());
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, PARRYING, PARRYING, 0, 64, 64) {
            public void stop() {
                super.stop();
                HippocamtusServant.this.block_stage = 0;
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, GUARD_COUNTER, GUARD_COUNTER, 0, 51, 51) {
            public void stop() {
                super.stop();
                HippocamtusServant.this.block_stage = 0;
            }
        });
    }

    @Override
    public void followGoal() {
        this.goalSelector.addGoal(5, new FollowOwnerWaterGoal(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.HippocamtusDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.HippocamtusHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.HippocamtusArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.HippocamtusHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.HippocamtusArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.HippocamtusDamage.get());
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public int xpReward() {
        return 35;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof HippocamtusServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.HippocamtusLimit.get();
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
                this.moveControl = new HippocamtusSwimControl(this, 4.0F);
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigation;
                this.moveControl = new MoveControl(this);
                this.setSwimming(false);
            }
        }
    }

    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getDirectEntity();
        if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && !this.isNoAi() && this.getAttackState() == GUARD) {
            Vec3 vector3d2 = source.getSourcePosition();
            if (vector3d2 != null) {
                Vec3 vector3d = this.getViewVector(1.0F);
                Vec3 vector3d1 = vector3d2.vectorTo(this.position()).normalize();
                vector3d1 = new Vec3(vector3d1.x, 0.0, vector3d1.z);
                if (vector3d1.dot(vector3d) < 0.0) {
                    if (this.attackTicks >= 7 && this.attackTicks <= 17) {
                        if (entity instanceof LivingEntity living) {
                            this.block_stage = 1;
                            if (!this.level().isClientSide) {
                                living.addEffect(new MobEffectInstance(ModEffect.EFFECTSTUN.get(), 50));
                            }

                            this.playSound(CataclysmSounds.PARRY.get(), 0.8F, 1.0F);
                        }

                        return false;
                    }

                    if (this.attackTicks > 17 && this.attackTicks <= 43) {
                        if (entity instanceof LivingEntity living) {
                            this.block_stage = 2;
                            living.knockback(0.5, this.getX() - living.getX(), this.getZ() - living.getZ());
                        }

                        return false;
                    }

                    return super.hurt(source, damage);
                }
            }
        }

        return super.hurt(source, damage);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "swing1")) {
            return this.swing1AnimationState;
        } else if (Objects.equals(input, "swing2")) {
            return this.swing2AnimationState;
        } else if (Objects.equals(input, "stab")) {
            return this.stabAnimationState;
        } else if (Objects.equals(input, "guard")) {
            return this.guardAnimationState;
        } else if (Objects.equals(input, "guardcounter")) {
            return this.guardcounterAnimationState;
        } else if (Objects.equals(input, "parry")) {
            return this.parryingAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else {
            return Objects.equals(input, "death") ? this.deathAnimationState : new AnimationState();
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STAB, false);
    }

    public void setStab(boolean stab) {
        this.entityData.set(STAB, stab);
    }

    public boolean getStab() {
        return (Boolean)this.entityData.get(STAB);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0:
                    this.stopAllAnimationStates();
                    break;
                case 1:
                    this.stopAllAnimationStates();
                    this.swing1AnimationState.startIfStopped(this.tickCount);
                    break;
                case 2:
                    this.stopAllAnimationStates();
                    this.swing2AnimationState.startIfStopped(this.tickCount);
                    break;
                case 3:
                    this.stopAllAnimationStates();
                    this.stabAnimationState.startIfStopped(this.tickCount);
                    break;
                case 4:
                    this.stopAllAnimationStates();
                    this.guardAnimationState.startIfStopped(this.tickCount);
                    break;
                case 5:
                    this.stopAllAnimationStates();
                    this.parryingAnimationState.startIfStopped(this.tickCount);
                    break;
                case 6:
                    this.stopAllAnimationStates();
                    this.guardcounterAnimationState.startIfStopped(this.tickCount);
                    break;
                case 7:
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.swing1AnimationState.stop();
        this.swing2AnimationState.stop();
        this.stabAnimationState.stop();
        this.guardAnimationState.stop();
        this.guardcounterAnimationState.stop();
        this.parryingAnimationState.stop();
        this.deathAnimationState.stop();
    }

    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.setAttackState(DEATH);
    }

    public int deathTimer() {
        return 60;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.HIPPOCAMTUS_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.HIPPOCAMTUS_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {
        return CataclysmSounds.HIPPOCAMTUS_IDLE.get();
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(this.getAttackState() != DEATH, this.tickCount);
        }

        if (this.charge_cooldown > 0) {
            --this.charge_cooldown;
        }

        if (this.guard_cooldown > 0) {
            --this.guard_cooldown;
        }

    }

    public void aiStep() {
        super.aiStep();
        if (this.getAttackState() == STAB_ATTACK) {
            if (this.attackTicks == 16) {
                this.playSound(CataclysmSounds.IGNIS_POKE.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
            }

            if (this.attackTicks == 18) {
                this.AreaAttack(8.5F, 6.0F, 45.0F, 1.0F, 90, false, false);
            }
        }

        if (this.getAttackState() == SWING_1 && this.attackTicks == 11) {
            this.playSound(CataclysmSounds.SWING.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
            this.AreaAttack(5.0F, 5.0F, 200.0F, 1.0F, 70, false, false);
        }

        if (this.getAttackState() == SWING_2 && this.attackTicks == 11) {
            this.playSound(CataclysmSounds.SWING.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
            this.AreaAttack(5.0F, 5.0F, 200.0F, 1.0F, 60, false, false);
        }

        if (this.getAttackState() == PARRYING) {
            if (this.attackTicks == 1) {
                this.parryParticle(1.5F, 0.9F, -0.1F);
            }

            if (this.attackTicks == 35 || this.attackTicks == 42 || this.attackTicks == 48) {
                this.playSound(CataclysmSounds.SWING.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
                this.AreaAttack(7.0F, 6.0F, 45.0F, 0.9F, 90, false, true);
            }
        }

        if (this.getAttackState() == GUARD_COUNTER && (this.attackTicks == 10 || this.attackTicks == 17 || this.attackTicks == 24)) {
            this.playSound(CataclysmSounds.SWING.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
            this.AreaAttack(7.0F, 6.0F, 45.0F, 0.9F, 90, false, true);
        }

    }

    private void AreaAttack(float range, float height, float arc, float damage, int shieldbreakticks,boolean knockback, boolean penetrate) {
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
                        DamageSource damagesource = penetrate ? CMDamageTypes.causePenetrateDamage(this) : this.getServantAttack();
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


    private void parryParticle(float height, float vec,float math) {
        if (this.level().isClientSide) {
            double d0 = this.getX();
            double d1 = this.getY() + height;
            double d2 = this.getZ();

            float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));

            double theta = (yBodyRot) * (Math.PI / 180);  // 엔티티의 Y축 회전
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);

            double theta2 = theta + Math.PI / 2;
            double X = Math.cos(theta2);
            double Z = Math.sin(theta2);

            for (int i = 0; i < 12; i++) {
                float throwAngle = i * Mth.PI / 6F;
                double y = 2 * Math.sin(throwAngle);
                double xz = 2 * Math.cos(throwAngle);
                double d3 = xz * vecX + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
                double d4 = y + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
                double d5 = xz * vecZ + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
                double speed = 0.35;
                this.level().addParticle(ModParticle.SPARK.get(), d0 + vec * X + f * math, d1, d2 + vec * Z  + f1 * math, d3 * speed, d4 * speed, d5 * speed);
            }
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return p_34192_.getEffect() != ModEffect.EFFECTABYSSAL_CURSE.get() && super.canBeAffected(p_34192_);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0){
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
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

    static class ChargeAttackGoal extends Goal {
        protected final HippocamtusServant entity;
        private final int getAttackState;
        private final int attackstate;
        private final int attackendstate;
        private final int attackMaxtick;
        private final int attackseetick;
        private final float attackminrange;
        private final float attackrange;

        public ChargeAttackGoal(HippocamtusServant entity, int getAttackState, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackminrange, float attackrange) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK,Flag.JUMP));
            this.getAttackState = getAttackState;
            this.attackstate = attackstate;
            this.attackendstate = attackendstate;
            this.attackMaxtick = attackMaxtick;
            this.attackseetick = attackseetick;
            this.attackminrange = attackminrange;
            this.attackrange = attackrange;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return target != null && this.entity.distanceTo(target) > attackminrange && target.isAlive() && this.entity.distanceTo(target) < attackrange && this.entity.getAttackState() == getAttackState && this.entity.getRandom().nextFloat() * 100.0F < 16f && this.entity.charge_cooldown <= 0;
        }

        @Override
        public void start() {
            this.entity.setAttackState(attackstate);
        }

        @Override
        public void stop() {
            this.entity.setAttackState(attackendstate);
            this.entity.charge_cooldown = CHARGE_COOLDOWN;
        }

        @Override
        public boolean canContinueToUse() {
            return this.entity.attackTicks < attackMaxtick;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = entity.getTarget();
            if (entity.attackTicks < attackseetick && target != null) {
                entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                entity.setYRot(entity.yBodyRot);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (entity.attackTicks == attackseetick) {
                float f1 = (float) Math.cos(Math.toRadians(entity.getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(entity.getYRot() + 90));
                if(target != null) {
                    float r = entity.distanceTo(target);
                    r = Mth.clamp(r, 0, 5);
                    entity.push(f1 * 0.4 * r, 0, f2 * 0.4 * r);
                }else{
                    entity.push(f1 * 2.0, 0, f2 * 2.0);
                }
            }
        }
    }
    static class HippocamtusSwimControl extends MoveControl {
        private final HippocamtusServant drowned;
        private final float speedMulti;

        public HippocamtusSwimControl(HippocamtusServant p_32433_, float speedMulti) {
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
                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.drowned.setYRot(this.rotlerp(this.drowned.getYRot(), f, 90.0F));
                this.drowned.yBodyRot = this.drowned.getYRot();
                float f1 = (float)(this.speedModifier * speedMulti * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = Mth.lerp(0.125F, this.drowned.getSpeed(), f1);
                this.drowned.setSpeed(f2);
                this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add((double)f2 * d0 * 0.005D, (double)f2 * d1 * 0.1D, (double)f2 * d2 * 0.005D));
            } else {
                if (!this.drowned.onGround()) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
                }

                super.tick();
            }

        }
    }
}
