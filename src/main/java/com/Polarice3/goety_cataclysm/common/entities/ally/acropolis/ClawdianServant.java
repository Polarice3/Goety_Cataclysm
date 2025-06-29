package com.Polarice3.goety_cataclysm.common.entities.ally.acropolis;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.projectiles.FlyingItem;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.particle.Not_Spin_TrailParticle;
import com.github.L_Ender.cataclysm.client.particle.RingParticle;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.AcropolisMonsters.ClawdianMoveController;
import com.github.L_Ender.cataclysm.entity.effect.Cm_Falling_Block_Entity;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.effect.Wave_Entity;
import com.github.L_Ender.cataclysm.entity.etc.IHoldEntity;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import com.github.L_Ender.cataclysm.entity.projectile.Accretion_Entity;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModParticle;
import com.github.L_Ender.cataclysm.init.ModTag;
import com.github.L_Ender.cataclysm.message.MessageEntityCamera;
import com.github.L_Ender.lionfishapi.server.animation.LegSolverQuadruped;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClawdianServant extends InternalAnimationSummon implements IHoldEntity {
    boolean searchingForLand;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState verticalswingAnimationState = new AnimationState();
    public AnimationState horizontalswingAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    public AnimationState chargeReadyAnimationState = new AnimationState();
    public AnimationState chargeLoopAnimationState = new AnimationState();
    public AnimationState chargeEndAnimationState = new AnimationState();
    public AnimationState waveStompAnimationState = new AnimationState();
    public AnimationState ClawPunchAnimationState = new AnimationState();
    public AnimationState GrabAndThrowAnimationState = new AnimationState();
    public AnimationState BackstepAnimationState = new AnimationState();
    public static int VERTICAL_SWING = 1;
    public static int HORIZONTAL_SWING = 2;
    public static int DEATH = 3;
    public static int CHARGE_READY = 4;
    public static int CHARGE_LOOP = 5;
    public static int CHARGE_END = 6;
    public static int WAVE_STOMP = 7;
    public static int CLAW_PUNCH = 8;
    public static int GRAB_AND_THROW = 9;
    public static int BACKSTEP = 10;
    public LegSolverQuadruped legSolver = new LegSolverQuadruped(-0.1F, 0.45F, 1.4F, 1.4F, 1.0F);
    private int charge_cooldown = 0;
    public static final int CHARGE_COOLDOWN = 200;
    private int wave_cooldown = 0;
    public static final int WAVE_COOLDOWN = 250;
    private int accretion_cooldown = 0;
    public static final int ACCRETION_COOLDOWN = 120;
    private int backstep_cooldown = 0;
    public static final int BACKSTEP_COOLDOWN = 200;
    private static final EntityDataAccessor<Optional<BlockState>> HOLD_STATE = SynchedEntityData.defineId(ClawdianServant.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
    private static final EntityDataAccessor<Integer> BACKSTEP_METER = SynchedEntityData.defineId(ClawdianServant.class, EntityDataSerializers.INT);
    protected final SemiAquaticPathNavigator waterNavigation;
    protected final CMPathNavigateGround groundNavigation;

    public ClawdianServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(2.5F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
        this.waterNavigation = new SemiAquaticPathNavigator(this, world);
        this.groundNavigation = new CMPathNavigateGround(this, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new InternalSummonMoveGoal(this, false, 1.0));
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, CLAW_PUNCH, 0, 47, 19, 3.3F) {
            public boolean canUse() {
                return super.canUse() && ClawdianServant.this.getRandom().nextFloat() * 100.0F < 35.0F;
            }
        });
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, CHARGE_READY, CHARGE_LOOP, 30, 30, 18.0F) {
            public boolean canUse() {
                return super.canUse() && ClawdianServant.this.getRandom().nextFloat() * 100.0F < 17.0F && ClawdianServant.this.charge_cooldown <= 0 && this.entity.onGround() && !this.entity.isSwimming();
            }
        });
        this.goalSelector.addGoal(2, new InternalSummonStateGoal(this, CHARGE_LOOP, CHARGE_LOOP, CHARGE_END, 45, 45) {
            public void tick() {
                LivingEntity target = this.entity.getTarget();
                if (this.entity.attackTicks < this.attackseetick && target != null) {
                    this.entity.getLookControl().setLookAt(target, 2.0F, 30.0F);
                    this.entity.lookAt(target, 2.0F, 30.0F);
                } else {
                    this.entity.setYRot(this.entity.yRotO);
                }

                BlockPos currentPos = this.entity.blockPosition();
                float yaw = this.entity.getYRot() * 0.017453292F;
                float dx = -Mth.sin(yaw) * 2.0F;
                float dz = Mth.cos(yaw) * 2.0F;
                BlockPos targetPos = currentPos.offset((int)dx, 0, (int)dz);
                if (this.entity.onGround() && !this.isDangerousFallZone(this.entity, targetPos)) {
                    Vec3 motion = this.entity.getDeltaMovement();
                    Vec3 push = (new Vec3((double)(-Mth.sin(yaw)), motion.y, (double)Mth.cos(yaw))).scale(0.5).add(motion.scale(0.5));
                    this.entity.setDeltaMovement(push.x, motion.y, push.z);
                }

            }

            private boolean isDangerousFallZone(PathfinderMob mob, BlockPos pos) {
                PathNavigation navigation = mob.getNavigation();
                NodeEvaluator evaluator = navigation.getNodeEvaluator();
                if (evaluator == null) {
                    return false;
                } else {
                    BlockPathTypes type = evaluator.getBlockPathType(mob.level(), Mth.floor((float)pos.getX()), Mth.floor((float)pos.getY()), Mth.floor((float)pos.getZ()), mob);
                    int safeDrop = 2;
                    BlockPos.MutableBlockPos checkPos = pos.mutable();

                    for(int i = 1; i <= safeDrop; ++i) {
                        checkPos.move(Direction.DOWN);
                        if (!mob.level().getBlockState(checkPos).isAir()) {
                            return false;
                        }
                    }

                    return type == BlockPathTypes.DAMAGE_OTHER || type == BlockPathTypes.OPEN || type == BlockPathTypes.DANGER_OTHER;
                }
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, CHARGE_END, CHARGE_END, 0, 10, 0) {
            public void stop() {
                super.stop();
                ClawdianServant.this.charge_cooldown = 200;
            }
        });
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, HORIZONTAL_SWING, 0, 75, 48, 5.5F) {
            public boolean canUse() {
                return super.canUse() && ClawdianServant.this.getRandom().nextFloat() * 100.0F < 27.0F;
            }

            public void tick() {
                LivingEntity target = this.entity.getTarget();
                if (this.entity.attackTicks < this.attackseetick && target != null) {
                    this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                    this.entity.lookAt(target, 30.0F, 30.0F);
                } else {
                    this.entity.setYRot(this.entity.yRotO);
                }

            }
        });
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, VERTICAL_SWING, 0, 46, 19, 5.5F) {
            public boolean canUse() {
                return super.canUse() && ClawdianServant.this.getRandom().nextFloat() * 100.0F < 30.0F;
            }
        });
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, WAVE_STOMP, 0, 53, 25, 10.0F) {
            public boolean canUse() {
                return super.canUse() && ClawdianServant.this.getRandom().nextFloat() * 100.0F < 20.0F && ClawdianServant.this.wave_cooldown <= 0;
            }

            public void stop() {
                super.stop();
                ClawdianServant.this.wave_cooldown = 250;
            }
        });
        this.goalSelector.addGoal(2, new Clawdian_Accretion(this, 0, GRAB_AND_THROW, 0, 70, 30, 4.0F, 8.5F, 46.0F, 14.0F));
        this.goalSelector.addGoal(2, new InternalSummonAttackGoal(this, 0, BACKSTEP, 0, 34, 33, 6.0F) {
            public boolean canUse() {
                return super.canUse() && ClawdianServant.this.getRandom().nextFloat() * 100.0F < (float)(7 * ClawdianServant.this.getBackstep()) && ClawdianServant.this.backstep_cooldown <= 0;
            }

            public void tick() {
                LivingEntity target = this.entity.getTarget();
                if (this.entity.attackTicks < this.attackseetick && target != null) {
                    this.entity.getLookControl().setLookAt(target, 2.0F, 30.0F);
                    this.entity.lookAt(target, 2.0F, 30.0F);
                } else {
                    this.entity.setYRot(this.entity.yRotO);
                }

                if (this.entity.attackTicks < 24) {
                    this.entity.getMoveControl().strafe(-2.0F, 0.0F);
                }

            }

            public void stop() {
                super.stop();
                ClawdianServant.this.backstep_cooldown = 200;
                ClawdianServant.this.setBackstep(0);
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
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.ClawdianDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.ClawdianHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.ClawdianArmor.get())
                .add(Attributes.ARMOR_TOUGHNESS, 3.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.ClawdianHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.ClawdianArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.ClawdianDamage.get());
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public int xpReward() {
        return 100;
    }

    protected void blockedByShield(LivingEntity entity) {
        if (this.getAttackState() == CLAW_PUNCH) {
            double d0 = entity.getX() - this.getX();
            double d1 = entity.getZ() - this.getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            entity.push(d0 / d2 * 9.0, 0.2, d1 / d2 * 9.0);
            entity.hurtMarked = true;
        }

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
                this.moveControl = new ClawdianSwimControl(this, 6.0F);
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigation;
                this.moveControl = new ClawdianMoveController(this);
                this.setSwimming(false);
            }
        }

    }

    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getDirectEntity();
        if (this.canBlockDamageSource(source)) {
            this.playSound(CataclysmSounds.PARRY.get(), 0.2F, 1.4F);
            return false;
        } else {
            if (!this.getPassengers().isEmpty()) {
                Entity rider = this.getPassengers().get(0);
                if (rider.equals(entity)) {
                    return false;
                }
            }

            boolean flag = super.hurt(source, damage);
            if (flag && this.getBackstep() < 10) {
                this.setBackstep(this.getBackstep() + 1);
            }

            return flag;
        }
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        boolean flag = false;
        if (!damageSourceIn.is(DamageTypeTags.BYPASSES_SHIELD) && !flag && this.getAttackState() == CHARGE_LOOP) {
            Vec3 vector3d2 = damageSourceIn.getSourcePosition();
            if (vector3d2 != null) {
                Vec3 vector3d = this.getViewVector(1.0F);
                Vec3 vector3d1 = vector3d2.vectorTo(this.position()).normalize();
                vector3d1 = new Vec3(vector3d1.x, 0.0, vector3d1.z);
                return vector3d1.dot(vector3d) < 0.0;
            }
        }

        return false;
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "vertical_swing")) {
            return this.verticalswingAnimationState;
        } else if (Objects.equals(input, "horizontal_swing")) {
            return this.horizontalswingAnimationState;
        } else if (Objects.equals(input, "charge_ready")) {
            return this.chargeReadyAnimationState;
        } else if (Objects.equals(input, "charge_loop")) {
            return this.chargeLoopAnimationState;
        } else if (Objects.equals(input, "charge_end")) {
            return this.chargeEndAnimationState;
        } else if (Objects.equals(input, "death")) {
            return this.deathAnimationState;
        } else if (Objects.equals(input, "wave_stomp")) {
            return this.waveStompAnimationState;
        } else if (Objects.equals(input, "claw_punch")) {
            return this.ClawPunchAnimationState;
        } else if (Objects.equals(input, "grab_and_throw")) {
            return this.GrabAndThrowAnimationState;
        } else {
            return Objects.equals(input, "backstep") ? this.BackstepAnimationState : new AnimationState();
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOLD_STATE, Optional.empty());
        this.entityData.define(BACKSTEP_METER, 0);
    }

    public void setHoldBlock(@Nullable BlockState state) {
        this.entityData.set(HOLD_STATE, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getHoldBlock() {
        return this.entityData.get(HOLD_STATE).orElse(null);
    }

    public int getBackstep() {
        return (Integer)this.entityData.get(BACKSTEP_METER);
    }

    public void setBackstep(int hurt) {
        this.entityData.set(BACKSTEP_METER, hurt);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0:
                    this.stopAllAnimationStates();
                    break;
                case 1:
                    this.stopAllAnimationStates();
                    this.verticalswingAnimationState.startIfStopped(this.tickCount);
                    break;
                case 2:
                    this.stopAllAnimationStates();
                    this.horizontalswingAnimationState.startIfStopped(this.tickCount);
                    break;
                case 3:
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
                    break;
                case 4:
                    this.stopAllAnimationStates();
                    this.chargeReadyAnimationState.startIfStopped(this.tickCount);
                    break;
                case 5:
                    this.stopAllAnimationStates();
                    this.chargeLoopAnimationState.startIfStopped(this.tickCount);
                    break;
                case 6:
                    this.stopAllAnimationStates();
                    this.chargeEndAnimationState.startIfStopped(this.tickCount);
                    break;
                case 7:
                    this.stopAllAnimationStates();
                    this.waveStompAnimationState.startIfStopped(this.tickCount);
                    break;
                case 8:
                    this.stopAllAnimationStates();
                    this.ClawPunchAnimationState.startIfStopped(this.tickCount);
                    break;
                case 9:
                    this.stopAllAnimationStates();
                    this.GrabAndThrowAnimationState.startIfStopped(this.tickCount);
                    break;
                case 10:
                    this.stopAllAnimationStates();
                    this.BackstepAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.verticalswingAnimationState.stop();
        this.horizontalswingAnimationState.stop();
        this.deathAnimationState.stop();
        this.chargeReadyAnimationState.stop();
        this.chargeLoopAnimationState.stop();
        this.chargeEndAnimationState.stop();
        this.waveStompAnimationState.stop();
        this.ClawPunchAnimationState.stop();
        this.GrabAndThrowAnimationState.stop();
        this.BackstepAnimationState.stop();
    }

    public void die(DamageSource p_21014_) {
        if (this.getTrueOwner() != null) {
            ItemStack itemStack = new ItemStack(CataclysmItems.CHITIN_CLAW.get());
            FlyingItem flyingItem = new FlyingItem(ModEntityType.FLYING_ITEM.get(), this.level(), this.getX(), this.getY(), this.getZ());
            flyingItem.setOwner(this.getTrueOwner());
            flyingItem.setItem(itemStack);
            flyingItem.setParticle(ModParticle.SPARK.get());
            flyingItem.setSecondsCool(0);
            this.level().addFreshEntity(flyingItem);
        }
        super.die(p_21014_);
        this.setAttackState(DEATH);
    }

    public int deathTimer() {
        return 45;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        BlockState blockstate = this.getHoldBlock();
        if (blockstate != null) {
            compound.put("holdBlockState", NbtUtils.writeBlockState(blockstate));
        }

        compound.putInt("backstep", this.getBackstep());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        BlockState blockstate = null;
        if (compound.contains("holdBlockState", 10)) {
            blockstate = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("holdBlockState"));
            if (blockstate.isAir()) {
                blockstate = null;
            }
        }

        this.setHoldBlock(blockstate);
        this.setBackstep(compound.getInt("backstep"));
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
        }
        if (this.charge_cooldown > 0) {
            this.charge_cooldown--;
        }
        if (this.wave_cooldown > 0) {
            this.wave_cooldown--;
        }
        if (this.accretion_cooldown > 0) {
            this.accretion_cooldown--;
        }
        if (this.backstep_cooldown > 0) {
            this.backstep_cooldown--;
        }
        this.legSolver.update(this, this.yBodyRot, this.getScale());
        float dis = this.getBbWidth() * 0.75F;
        if (this.getAttackState() != CHARGE_LOOP) {
            repelEntities(dis, this.getBbHeight(), dis, dis);
        }
    }

    public void aiStep() {
        super.aiStep();

        if (this.getAttackState() == VERTICAL_SWING) {
            if (this.attackTicks == 22) {
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.2f, 0, 20);
                this.playSound(CataclysmSounds.STRONGSWING.get(), 2.0f, 0.75F + this.getRandom().nextFloat() * 0.1F);
                this.playSound(SoundEvents.GENERIC_EXPLODE, 2.0f, 0.95F + this.getRandom().nextFloat() * 0.1F);
                AreaAttack(8.5f,8.5f,50,1,140,false,2.25);
                MakeParticle(2f, 7.3f, 0.35f);
            }

            if (this.attackTicks == 22) {
                BlockSmashDamage(2.0F, 1, 2.5F, 7.3f, 160, 1.0F, 0.15f);

            }
            if (this.attackTicks == 25) {
                BlockSmashDamage(2.0F, 2, 2.5F, 7.3f, 160, 1.0F, 0.15f);
            }

        }

        if (this.getAttackState() == HORIZONTAL_SWING) {
            if (this.attackTicks == 22) {
                this.playSound(CataclysmSounds.STRONGSWING.get(), 2.0f, 0.75F + this.getRandom().nextFloat() * 0.1F);
                AreaAttack(7.5f,7.5f,200,0.9F,100,true,2.25);
            }
            if (this.attackTicks == 50) {
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.2f, 0, 20);
                this.playSound(CataclysmSounds.STRONGSWING.get(), 2.0f, 0.75F + this.getRandom().nextFloat() * 0.1F);
                this.playSound(SoundEvents.GENERIC_EXPLODE, 2.0f, 0.95F + this.getRandom().nextFloat() * 0.1F);
                AreaAttack(8.5f,8.5f,50,1.15F,160,false,2.25);
                MakeParticle(2f, 7.3f, 0.35f);
            }
            if (this.attackTicks == 50) {
                BlockSmashDamage(2.0F, 1, 2.5F, 7.3f, 160, 1.0F, 0.15f);
            }
            if (this.attackTicks == 53) {
                BlockSmashDamage(2.0F, 2, 2.5F, 7.3f, 160, 1.0F, 0.15f);
            }
        }
        if (this.getAttackState() == CHARGE_LOOP) {
            if (!this.level().isClientSide) {
                if (CMConfig.KobolediatorBlockBreaking) {
                    ChargeBlockBreaking();
                } else {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                        ChargeBlockBreaking();
                    }
                }

                if (this.tickCount % 2 == 0) {
                    for (LivingEntity Lentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D))) {
                        if (!MobUtil.areAllies(this, Lentity)) {
                            boolean flag = Lentity.hurt(this.getMobAttack(), (float) ((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.6F));
                            if (flag) {
                                if (Lentity.onGround()) {
                                    double d0 = Lentity.getX() - this.getX();
                                    double d1 = Lentity.getZ() - this.getZ();
                                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                                    float f = 1.5F;
                                    Lentity.push(d0 / d2 * f, 0.4F, d1 / d2 * f);
                                }
                            }
                        }
                    }
                }
            }

        }

        if (this.getAttackState() == WAVE_STOMP) {
            if (this.attackTicks == 27) {
                AreaAttack(3.5f, 3.5f, 120, 1.35F, 200, false,2.25);
                MakeParticle(2f, 0.9f, 0.0f);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 30, 0.25f, 0, 20);
                this.playSound(SoundEvents.GENERIC_EXPLODE, 2.0f, 0.95F + this.getRandom().nextFloat() * 0.1F);

                double theta = (yBodyRot) * (Math.PI / 180);

                theta += Math.PI / 2;
                double vecX = Math.cos(theta);
                double vecZ = Math.sin(theta);
                double vec = 2.0D;
                if (!this.level().isClientSide()) {
                    int numberOfWaves = 6;
                    float angleStep = 35.0f;

                    double firstAngleOffset = (numberOfWaves - 1) / 2.0 * angleStep;
                    for (int i = 0; i < numberOfWaves; i++) {
                        double angle = yBodyRot - firstAngleOffset + (i * angleStep);
                        double rad = Math.toRadians(angle);
                        double dx = -Math.sin(rad);
                        double dz = Math.cos(rad);
                        double spawnX = this.getX() + vecX * vec;
                        double spawnY = this.getY();
                        double spawnZ = this.getZ() + vecZ * vec;
                        Wave_Entity WaveEntity = new Wave_Entity(this.level(), this, 80, 9);
                        WaveEntity.setPos(spawnX, spawnY, spawnZ);
                        WaveEntity.setState(1);
                        WaveEntity.setYRot(-(float) (Mth.atan2(dx, dz) * (180F / Math.PI)));
                        this.level().addFreshEntity(WaveEntity);
                    }
                }else{
                    int numberOfWaves = 14;
                    float angleStep = 15.0f;

                    double firstAngleOffset = (numberOfWaves - 1) / 2.0 * angleStep;
                    for (int i = 0; i < numberOfWaves; i++) {
                        double angle = yBodyRot - firstAngleOffset + (i * angleStep);
                        double rad = Math.toRadians(angle);
                        double dx = -Math.sin(rad);
                        double dz = Math.cos(rad);
                        double spawnX = this.getX() + vecX * vec;
                        double spawnY = this.getY();
                        double spawnZ = this.getZ() + vecZ * vec;
                        double extraX = spawnX + dx * (1 + random.nextDouble() /2);
                        double extraY = spawnY + 0.9d + random.nextDouble() * 0.5;
                        double extraZ = spawnZ + dz * (1 + random.nextDouble()/2);
                        this.level().addParticle(new Not_Spin_TrailParticle.NSTData(113 / 255F, 194 / 255F, 240 / 255F, 0.05F, 0.5F + random.nextFloat() * 0.3F, 0.4F + random.nextFloat() * 0.2F, 0, 120), spawnX, spawnY, spawnZ, extraX, extraY, extraZ);

                    }
                }

            }

            for (int l = 26; l <= 28; l = l + 2) {
                if (this.attackTicks == l) {
                    int d = l - 24;
                    int d2 = l - 25;
                    BlockSmashDamage(0.6F, d, 2.5F, 2F, 160, 1.0F, 0.15f);
                    BlockSmashDamage(0.6F, d2, 2.5F, 2F, 160, 1.0F, 0.15f);
                }
            }

            if (this.attackTicks == 31) {
                if (!this.level().isClientSide()) {
                    double theta = (yBodyRot) * (Math.PI / 180);

                    theta += Math.PI / 2;
                    double vecX = Math.cos(theta);
                    double vecZ = Math.sin(theta);
                    int numberOfSkulls = 5;
                    float angleStep = 35.0f;

                    double firstAngleOffset = (numberOfSkulls - 1) / 2.0 * angleStep;
                    for (int i = 0; i < numberOfSkulls; i++) {
                        double angle = yBodyRot - firstAngleOffset + (i * angleStep);
                        double rad = Math.toRadians(angle);
                        double dx = -Math.sin(rad);
                        double dz = Math.cos(rad);
                        double spawnX = this.getX() + vecX * 2;
                        double spawnY = this.getY();
                        double spawnZ = this.getZ() + vecZ * 2;
                        Wave_Entity WaveEntity = new Wave_Entity(this.level(), this, 80, 9);
                        WaveEntity.setPos(spawnX, spawnY, spawnZ);
                        WaveEntity.setState(1);
                        WaveEntity.setYRot(-(float) (Mth.atan2(dx, dz) * (180F / Math.PI)));
                        this.level().addFreshEntity(WaveEntity);
                    }
                }
            }
        }
        if (this.getAttackState() == CLAW_PUNCH) {
            if (this.attackTicks == 18) {
                this.playSound(CataclysmSounds.CRAB_BITE.get(), 1.0f, 1F + this.getRandom().nextFloat() * 0.1F);
            }
            if (this.attackTicks == 21) {
                AreaAttack(4.5f, 4.5f, 140, 1.0F, 200, true,2.25);
            }
        }

        if (this.getAttackState() == GRAB_AND_THROW) {
            if (this.attackTicks == 16) {
                this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 1.0f, 1F + this.getRandom().nextFloat() * 0.1F);
                MakeParticle(0.6f, 2.5F, -0.5f);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 30, 0.25f, 0, 20);
                HoldAttack(4.5F,4.5F,60,1,120);
            }
            if (!this.getPassengers().isEmpty() && this.getPassengers().get(0).isShiftKeyDown()) {
                this.getPassengers().get(0).setShiftKeyDown(false);
            }
        }
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
                    this.level().addParticle(new BlockParticleOption(ModParticle.DUST_PILLAR.get(), block), getX() + vec * vecX + extraX + f * math, this.getY() + extraY, getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);
                }
            }
            this.level().addParticle(new RingParticle.RingData(0f, (float)Math.PI/2f, 30, 1F, 1F,  1F, 1.0f, 20f, false, RingParticle.EnumRingBehavior.CONSTANT), getX() + vec * vecX + f * math, getY() + 0.2f, getZ() + vec * vecZ + f1 * math, 0, 0, 0);

        }
    }

    private void BlockSmashDamage(float spreadarc, int distance, float mxy, float vec, int shieldbreakticks, float damage, float airborne) {
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        double spread = Math.PI * spreadarc;
        int arcLen = Mth.ceil(distance * spread);
        double minY = this.getY() - 1;
        double maxY = this.getY() + mxy;
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = this.getX() + vx * distance + vec * Math.cos((yBodyRot + 90) * Math.PI / 180);
            double pz = this.getZ() + vz * distance + vec * Math.sin((yBodyRot + 90) * Math.PI / 180);
            int hitX = Mth.floor(px);
            int hitZ = Mth.floor(pz);
            BlockPos pos = new BlockPos(hitX, hitY, hitZ);
            BlockState block = level().getBlockState(pos);

            int maxDepth = 256;
            for (int depthCount = 0; depthCount < maxDepth; depthCount++) {
                if (block.getRenderShape() == RenderShape.MODEL) {
                    break;
                }
                pos = pos.below();
                block = level().getBlockState(pos);
            }

            if (block.getRenderShape() != RenderShape.MODEL) {
                block = Blocks.AIR.defaultBlockState();
            }
            if (!this.level().isClientSide) {
                Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(level(), hitX + 0.5D, hitY + 1.0D, hitZ + 0.5D, block, 10);
                fallingBlockEntity.push(0, 0.2D + getRandom().nextGaussian() * 0.15D, 0);
                level().addFreshEntity(fallingBlockEntity);

                AABB selection = new AABB(px - 0.5, minY, pz - 0.5, px + 0.5, maxY, pz + 0.5);
                List<LivingEntity> hit = level().getEntitiesOfClass(LivingEntity.class, selection);
                for (LivingEntity entity : hit) {
                    if (!MobUtil.areAllies(this, entity)) {
                        DamageSource damagesource = this.getMobAttack();
                        boolean flag = entity.hurt(damagesource, (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                        if (entity.isDamageSourceBlocked(damagesource) && shieldbreakticks > 0) {
                            disableShield(entity, shieldbreakticks);
                        }

                        if (flag) {
                            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, airborne + level().random.nextDouble() * 0.15, 0.0D));
                        }

                    }
                }
            }
        }
    }

    private void AreaAttack(float range, float height, float arc, float damage, int shieldbreakticks,boolean knockback,double xz) {
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
                        DamageSource damagesource = this.getMobAttack();
                        boolean hurt = entityHit.hurt(damagesource, (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                        if (entityHit.isDamageSourceBlocked(damagesource) && shieldbreakticks > 0) {
                            disableShield(entityHit, shieldbreakticks);
                        }

                        double d0 = entityHit.getX() - this.getX();
                        double d1 = entityHit.getZ() - this.getZ();
                        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                        if (hurt && knockback) {
                            entityHit.push(d0 / d2 * xz, 0.15D, d1 / d2 * xz);
                        }
                    }
                }
            }
        }
    }

    private void HoldAttack(float range, float height, float arc, float damage, int shieldbreakticks) {
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
                        DamageSource damagesource = this.damageSources().mobAttack(this);
                        if (!entityHit.getType().is(ModTag.IGNIS_CANT_POKE) && entityHit.isAlive() && this.getPassengers().isEmpty()) {
                            if (entityHit.isShiftKeyDown()) {
                                entityHit.setShiftKeyDown(false);
                            }

                            if (entityHit.hurt(damagesource, 1)) {
                                entityHit.startRiding(this, true);
                                Cataclysm.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityHit), new MessageEntityCamera(entityHit.getId(), true));
                            }
                        } else if (!isAlliedTo(entityHit)) {
                            entityHit.hurt(damagesource, (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                        }
                    }
                }
            }
        }
    }

    private void ChargeBlockBreaking(){
        boolean flag = false;
        AABB aabb = this.getBoundingBox().inflate(0.5D, 0.2D, 0.5D);
        for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(this.getY()), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (!blockstate.isAir() && blockstate.canEntityDestroy(this.level(), blockpos, this) && !blockstate.is(ModTag.REMNANT_IMMUNE) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                if (random.nextInt(6) == 0 && !blockstate.hasBlockEntity()) {
                    Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(level(), blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, blockstate, 20);
                    flag = this.level().destroyBlock(blockpos, false, this) || flag;
                    fallingBlockEntity.setDeltaMovement(fallingBlockEntity.getDeltaMovement().add(this.position().subtract(fallingBlockEntity.position()).multiply((-1.2D + random.nextDouble()) / 3, 0.2D + getRandom().nextGaussian() * 0.15D, (-1.2D + random.nextDouble()) / 3)));
                    level().addFreshEntity(fallingBlockEntity);
                } else {
                    flag = this.level().destroyBlock(blockpos, false, this) || flag;
                }
            }
        }
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    public void positionRider(Entity passenger, MoveFunction moveFunc) {
        double theta = (yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);

        double px = this.getX() + 2.5F * vecX;
        double pz = this.getZ() + 2.5F * vecZ;

        double PosY = this.getY() + this.getBbHeight() * 0.8D;
        if (this.hasPassenger(passenger)) {
            moveFunc.accept(passenger, px, PosY, pz);
        }
    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return p_34192_.getEffect() != ModEffect.EFFECTSTUN.get() && p_34192_.getEffect() != ModEffect.EFFECTABYSSAL_CURSE.get() && super.canBeAffected(p_34192_);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
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

    static class Clawdian_Accretion extends InternalSummonAttackGoal {
        private final ClawdianServant entity;
        private final float meleerandom;
        private final float rangerandom;
        private final int getattackstate;
        private final int attackseetick;
        private final float Meleeattackrange;
        private final float attackrange;

        public Clawdian_Accretion(ClawdianServant entity, int getAttackState, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float Meleeattackrange,float attackrange, float meleerandom, float rangerandom) {
            super(entity, getAttackState, attackstate, attackendstate, attackMaxtick, attackseetick, attackrange);
            this.entity = entity;
            this.meleerandom = meleerandom;
            this.rangerandom = rangerandom;
            this.getattackstate = getAttackState;
            this.attackseetick = attackseetick;
            this.Meleeattackrange = Meleeattackrange;
            this.attackrange = attackrange;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return target != null && target.isAlive() && entity.accretion_cooldown <= 0 && this.entity.getAttackState() == getattackstate  && this.entity.onGround() && !this.entity.isSwimming()  && (this.entity.distanceTo(target) < Meleeattackrange &&  this.entity.getRandom().nextFloat() * 100.0F < meleerandom||this.entity.distanceTo(target) > attackrange &&  this.entity.getRandom().nextFloat() * 100.0F < rangerandom);
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
            entity.accretion_cooldown = ACCRETION_COOLDOWN;
        }

        @Override
        public void tick() {
            LivingEntity target = entity.getTarget();
            if (entity.attackTicks > attackseetick && target != null) {
                entity.getLookControl().setLookAt(target, 30.0F, 90.0F);
                entity.lookAt(target, 30.0F, 90.0F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            double theta = (entity.yBodyRot) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            if (this.entity.attackTicks == 29) {

                double vec = 2.0;
                int hitX = Mth.floor(entity.getX() + vec * vecX);
                int hitY = Mth.floor(entity.getY());
                int hitZ = Mth.floor(entity.getZ() + vec * vecZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = entity.level().getBlockState(hit.below());
                if (block.getRenderShape() == RenderShape.MODEL) {
                    entity.setHoldBlock(block.getBlock().defaultBlockState());
                }else{
                    entity.setHoldBlock(Blocks.STONE.defaultBlockState());
                }
            }
            this.entity.getNavigation().stop();
            if (this.entity.attackTicks == 45) {
                int count = 4;
                if (target != null) {
                    double vec = 2.5;
                    double offsetangle = Math.toRadians(4 + entity.random.nextInt(5));
                    for (int i = 0; i <= (count - 1); ++i) {
                        Accretion_Entity acc = new Accretion_Entity(ModEntities.ACCRETION.get(), entity.level(), entity);
                        double angle = (i - ((count - 1) / 2.0)) * offsetangle;
                        double d1 = target.getX() - this.entity.getX();

                        double d3 = target.getZ() - this.entity.getZ();
                        double x = d1 * Math.cos(angle) + d3 * Math.sin(angle);
                        double z = -d1 * Math.sin(angle) + d3 * Math.cos(angle);
                        double distance = Math.sqrt(x * x + z * z);
                        double d2 = target.getY(0.2D) - acc.getY() + (entity.random.nextFloat() - 0.5) * i;
                        double PosX = entity.getX() + vecX * vec;
                        double PosY = entity.getY() + entity.getBbHeight() * 0.8D;
                        double PosZ = entity.getZ() + vecZ * vec;
                        acc.setPosRaw(PosX, PosY, PosZ);
                        acc.shoot(x, d2 + distance * (double) 0.2F, z, 1.4F, 4);
                        acc.setDamage(15);
                        acc.setBlockState(entity.getHoldBlock());
                        acc.level().addFreshEntity(acc);
                    }
                }else {
                    double tempvec = 12;
                    double vec = 2.5;
                    double d1 = entity.getX() + vecX * tempvec - this.entity.getX();
                    double d3 = entity.getZ() + vecZ * tempvec - this.entity.getZ();
                    double offsetangle = Math.toRadians(4 +entity.random.nextInt(5));
                    for (int i = 0; i <= (count - 1); ++i) {

                        Accretion_Entity acc = new Accretion_Entity(ModEntities.ACCRETION.get(), entity.level(), entity);
                        double angle = (i - ((count - 1) / 2.0)) * offsetangle;
                        double x = d1 * Math.cos(angle) + d3 * Math.sin(angle);
                        double z = -d1 * Math.sin(angle) + d3 * Math.cos(angle);
                        double distance = Math.sqrt(x * x + z * z);
                        double d2 = entity.getY(0.2D) - acc.getY() + (entity.random.nextFloat() -0.5) * i;
                        double PosX = entity.getX() + vecX * vec;
                        double PosY = entity.getY() + entity.getBbHeight() * 0.8D;
                        double PosZ = entity.getZ() + vecZ * vec;
                        acc.setPosRaw(PosX, PosY, PosZ);
                        acc.shoot(x, d2 + distance * (double) 0.2F, z, 1.4F, 4);
                        acc.setDamage(15);
                        acc.setBlockState(entity.getHoldBlock());
                        acc.level().addFreshEntity(acc);
                    }
                }
                if (!entity.getPassengers().isEmpty()) {
                    Entity rider = entity.getPassengers().get(0);
                    double vec = 2.5;
                    if (rider.equals(target) || target == null) {
                        double tempvec = 12;
                        double d1 = entity.getX() + vecX * tempvec - this.entity.getX();
                        double d2 = entity.getY(0.2D) - rider.getY();
                        double d3 = entity.getZ() + vecZ * tempvec - this.entity.getZ();
                        double distance = Mth.sqrt((float) (d1 * d1 + d3 * d3));
                        double PosX = entity.getX() + vecX * vec;
                        double PosY = entity.getY() + entity.getBbHeight() * 0.8D;
                        double PosZ = entity.getZ() + vecZ * vec;
                        Accretion_Entity acc = new Accretion_Entity(ModEntities.ACCRETION.get(), entity.level(), entity);
                        acc.setPosRaw(PosX, PosY, PosZ);
                        acc.shoot(d1, d2 + distance * (double) 0.2F, d3, 1.4F, 4);
                        acc.setDamage(15);
                        acc.setBlockState(entity.getHoldBlock());
                        rider.startRiding(acc,true);
                        acc.level().addFreshEntity(acc);
                    } else {
                        double d1 = target.getX() - entity.getX();
                        double d3 = target.getZ() - entity.getZ();

                        double d2 = target.getY(0.2D) - rider.getY();
                        double distance = Mth.sqrt((float) (d1 * d1 + d3 * d3));
                        double PosX = entity.getX() + vecX * vec;
                        double PosY = entity.getY() + entity.getBbHeight() * 0.8D;
                        double PosZ = entity.getZ() + vecZ * vec;
                        Accretion_Entity acc = new Accretion_Entity(ModEntities.ACCRETION.get(), entity.level(), entity);
                        acc.setPosRaw(PosX, PosY, PosZ);
                        acc.shoot(d1, d2 + distance * (double) 0.2F, d3, 1.4F, 4);
                        acc.setDamage(15);
                        acc.setBlockState(entity.getHoldBlock());
                        rider.startRiding(acc,true);
                        acc.level().addFreshEntity(acc);
                    }


                }
            }
            if (this.entity.attackTicks == 46) {
                entity.setHoldBlock((BlockState)null);
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class ClawdianSwimControl extends ClawdianMoveController {
        private final ClawdianServant drowned;
        private final float speedMulti;

        public ClawdianSwimControl(ClawdianServant p_32433_, float speedMulti) {
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
