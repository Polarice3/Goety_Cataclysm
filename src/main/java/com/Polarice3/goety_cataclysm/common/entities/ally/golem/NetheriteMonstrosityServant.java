package com.Polarice3.goety_cataclysm.common.entities.ally.golem;

import com.Polarice3.Goety.api.entities.IAutoRideable;
import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.projectiles.Pyroclast;
import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.goety_cataclysm.common.blocks.GoetyBlocks;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.IABossSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.FlareBomb;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.client.particle.RingParticle;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.effect.Cm_Falling_Block_Entity;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.partentity.Cm_Part_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Flame_Jet_Entity;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModTag;
import com.github.L_Ender.cataclysm.util.CMMathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class NetheriteMonstrosityServant extends IABossSummon implements PlayerRideable, IAutoRideable {
    public int frame;
    public float LayerBrightness, oLayerBrightness;
    public int LayerTicks;
    public static int IDLE = 0;
    public static int SLEEP = 1;
    public static int AWAKEN = 2;
    public static int SMASH_ATTACK = 3;
    public static int PHASE_SMASH_ATTACK = 4;
    public static int DEATH = 5;
    public static int MAGMA_ATTACK = 6;
    public static int DRAIN_LAVA = 7;
    public static int CHARGE_ATTACK = 8;
    public static int EARTHQUAKE_ATTACK = 9;
    public static int FLARE_ATTACK = 10;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState sleepAnimationState = new AnimationState();
    public AnimationState awakeAnimationState = new AnimationState();
    public AnimationState smashsAnimationState = new AnimationState();
    public AnimationState phaseAnimationState = new AnimationState();
    public AnimationState fireAnimationState = new AnimationState();
    public AnimationState drainAnimationState = new AnimationState();
    public AnimationState shouldercheckAnimationState = new AnimationState();
    public AnimationState overpowerAnimationState = new AnimationState();
    public AnimationState flareshotAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    public final NMPart headPart;
    public final NMPart[] monstrosityParts;
    private static final EntityDataAccessor<Boolean> IS_BERSERK = SynchedEntityData.defineId(NetheriteMonstrosityServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_AWAKEN = SynchedEntityData.defineId(NetheriteMonstrosityServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> AUTO_MODE = SynchedEntityData.defineId(NetheriteMonstrosityServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MAGAZINE = SynchedEntityData.defineId(NetheriteMonstrosityServant.class, EntityDataSerializers.INT);

    public boolean Blocking = CMConfig.NetheritemonstrosityBodyBloking;
    private int blockBreakCounter;

    private int shoot_cooldown = 0;
    public static final int SHOOT_COOLDOWN = 240;
    private boolean onLava = false;
    private int check_cooldown = 0;
    public static final int CHECK_COOLDOWN = 80;

    private int overpower_cooldown = 0;
    public static final int OVERPOWER_COOLDOWN = 160;

    private int flare_shoot_cooldown = 0;
    public static final int FLARE_SHOOT_COOLDOWN = 120;

    private int timeWithoutTarget;

    private int wakeUp;
    private boolean intro;
    public boolean clientStop;

    public NetheriteMonstrosityServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(4.5F);
        this.headPart = new NMPart(this, 1.6F, 2.5F);
        this.monstrosityParts = new NMPart[]{this.headPart};
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        setConfigAttribute(this, CMConfig.MonstrosityHealthMultiplier, CMConfig.MonstrosityDamageMultiplier);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new WanderGoal<>(this, 1.0D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new InternalSummonMoveGoal(this, false, 1.0D) {

            @Override
            public boolean canUse() {
                return super.canUse() && NetheriteMonstrosityServant.this.getAttackState() == 0;
            }
        });

        this.goalSelector.addGoal(3, new InternalSummonAttackGoal(this, IDLE, SMASH_ATTACK, IDLE, 58, 12, 6) {
            @Override
            public boolean canUse() {
                return super.canUse() && NetheriteMonstrosityServant.this.getRandom().nextFloat() * 100.0F < 32f;
            }
        });

        //sleep
        this.goalSelector.addGoal(2, new InternalSummonStateGoal(this, SLEEP, SLEEP, AWAKEN, 0, 0) {

            @Override
            public boolean canUse() {
                return super.canUse() && !NetheriteMonstrosityServant.this.getIsAwaken();
            }

            @Override
            public void tick() {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            }

            @Override
            public void stop() {
                super.stop();
                NetheriteMonstrosityServant.this.setIsAwaken(true);
            }

        });

        //awake
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, AWAKEN, AWAKEN, IDLE, 40, 0) {

            @Override
            public boolean canUse() {
                return super.canUse() && NetheriteMonstrosityServant.this.getIsAwaken();
            }

            @Override
            public void tick() {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            }
        });

        this.goalSelector.addGoal(0, new InternalSummonStateGoal(this, SLEEP, AWAKEN, IDLE, 40, 0) {

            @Override
            public boolean canUse() {
                return super.canUse() && NetheriteMonstrosityServant.this.wakeUp <= 0;
            }

            @Override
            public void start() {
                super.start();
                NetheriteMonstrosityServant.this.setIsAwaken(true);
                NetheriteMonstrosityServant.this.wakeUp = 20;
                NetheriteMonstrosityServant.this.intro = true;
            }
        });

        this.goalSelector.addGoal(0, new InternalSummonAttackGoal(this, SLEEP, AWAKEN, IDLE, 40, 0, 15) {

            @Override
            public boolean canUse() {
                LivingEntity target = entity.getTarget();
                return super.canUse() && target != null && this.entity.getSensing().hasLineOfSight(target);
            }

            @Override
            public void start() {
                super.start();
                NetheriteMonstrosityServant.this.setIsAwaken(true);
            }
        });

        //change roar
        this.goalSelector.addGoal(0, new MonstrosityPhaseChangeGoal(this, IDLE, PHASE_SMASH_ATTACK, IDLE, 54));

        //shoot
        this.goalSelector.addGoal(3, new MagmaShoot(this, IDLE, MAGMA_ATTACK, IDLE, 44, 20, 40F, 19, 16F));

        //flare shoot
        this.goalSelector.addGoal(3, new FlareShoot(this, IDLE, FLARE_ATTACK, IDLE, 60, 35, 26F, 35, 18F));

        //drain
        this.goalSelector.addGoal(3, new InternalSummonAttackGoal(this, IDLE, DRAIN_LAVA, IDLE, 60, 25, 1) {

            @Override
            public boolean canUse() {
                LivingEntity target = this.entity.getTarget();
                FluidState below = this.entity.level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getFluidState();
                return target != null && target.isAlive() && this.entity.getAttackState() == 0 && (this.entity.isInLava() || below.is(FluidTags.LAVA)) && NetheriteMonstrosityServant.this.getMagazine() >= CMConfig.Lavabombmagazine;
            }
        });

        //check
        this.goalSelector.addGoal(3, new ShoulderCheck(this, IDLE, CHARGE_ATTACK, IDLE, 70, 19, 16, 19, 49, 12));

        //overpower
        this.goalSelector.addGoal(3, new InternalSummonAttackGoal(this, IDLE, EARTHQUAKE_ATTACK, IDLE, 75, 7, 10) {
            @Override
            public boolean canUse() {
                return super.canUse() && NetheriteMonstrosityServant.this.getRandom().nextFloat() * 100.0F < 40f && NetheriteMonstrosityServant.this.overpower_cooldown <= 0;
            }

            @Override
            public void stop() {
                super.stop();
                NetheriteMonstrosityServant.this.overpower_cooldown = OVERPOWER_COOLDOWN;
            }

        });
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 25.0D)
                .add(Attributes.MAX_HEALTH, 600.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public int xpReward() {
        return 500;
    }

    public boolean attackEntityFromPart(NMPart nmPart, DamageSource source, float amount) {
        return this.hurt(source, amount);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.getAttackState() == PHASE_SMASH_ATTACK && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        double range = calculateRange(source);

        if (range > CMConfig.MonstrosityLongRangelimit * CMConfig.MonstrosityLongRangelimit && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        Entity entity = source.getDirectEntity();
        if (entity instanceof AbstractGolem) {
            damage *= 0.5F;
        }

        return super.hurt(source, damage);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public float DamageCap() {
        return (float) CMConfig.MonstrosityDamageCap;
    }

    public int DamageTime() {
        return CMConfig.MonstrosityDamageTime;
    }

    public boolean canBeCollidedWith() {
        return this.isAlive() && this.Blocking && this.getAttackState() != 8;
    }

    public boolean isPushable() {
        return false;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "sleep")) {
            return this.sleepAnimationState;
        } else if (Objects.equals(input, "awake")) {
            return this.awakeAnimationState;
        } else if (Objects.equals(input, "smash")) {
            return this.smashsAnimationState;
        } else if (Objects.equals(input, "phase_two")) {
            return this.phaseAnimationState;
        } else if (Objects.equals(input, "fire")) {
            return this.fireAnimationState;
        } else if (Objects.equals(input, "death")) {
            return this.deathAnimationState;
        } else if (Objects.equals(input, "drain")) {
            return this.drainAnimationState;
        } else if (Objects.equals(input, "shoulder_check")) {
            return this.shouldercheckAnimationState;
        } else if (Objects.equals(input, "overpower")) {
            return this.overpowerAnimationState;
        } else if (Objects.equals(input, "flare_shot")) {
            return this.flareshotAnimationState;
        } else {
            return new AnimationState();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_BERSERK, false);
        this.entityData.define(IS_AWAKEN, false);
        this.entityData.define(AUTO_MODE, false);
        this.entityData.define(MAGAZINE, 0);
    }

    public boolean isSleep() {
        return this.getAttackState() == SLEEP || this.getAttackState() == AWAKEN;
    }

    public boolean canStandOnFluid(FluidState p_230285_1_) {
        return p_230285_1_.is(FluidTags.LAVA);
    }

    public void setIsInBerserk(boolean isBerserk) {
        this.entityData.set(IS_BERSERK, isBerserk);
    }

    public boolean isInBerserk() {
        return this.entityData.get(IS_BERSERK);
    }

    public void setOnLava(boolean lava) {
        onLava = lava;
    }

    public boolean getOnLava() {
        return onLava;
    }

    public void setIsAwaken(boolean isAwaken) {
        this.entityData.set(IS_AWAKEN, isAwaken);
        if (!isAwaken) {
            this.setAttackState(1);
        }
    }

    public boolean getIsAwaken() {
        return this.entityData.get(IS_AWAKEN);
    }

    public void setMagazine(int isAwaken) {
        this.entityData.set(MAGAZINE, isAwaken);
    }

    public int getMagazine() {
        return this.entityData.get(MAGAZINE);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.MOB_SUMMONED){
            this.setIsAwaken(false);
            this.wakeUp = 20;
        } else {
            this.setIsAwaken(true);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0 -> this.stopAllAnimationStates();
                case 1 -> {
                    this.stopAllAnimationStates();
                    this.sleepAnimationState.startIfStopped(this.tickCount);
                }
                case 2 -> {
                    this.stopAllAnimationStates();
                    this.awakeAnimationState.startIfStopped(this.tickCount);
                }
                case 3 -> {
                    this.stopAllAnimationStates();
                    this.smashsAnimationState.startIfStopped(this.tickCount);
                }
                case 4 -> {
                    this.stopAllAnimationStates();
                    this.phaseAnimationState.startIfStopped(this.tickCount);
                }
                case 5 -> {
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
                }
                case 6 -> {
                    this.stopAllAnimationStates();
                    this.fireAnimationState.startIfStopped(this.tickCount);
                }
                case 7 -> {
                    this.stopAllAnimationStates();
                    this.drainAnimationState.startIfStopped(this.tickCount);
                }
                case 8 -> {
                    this.stopAllAnimationStates();
                    this.shouldercheckAnimationState.startIfStopped(this.tickCount);
                }
                case 9 -> {
                    this.stopAllAnimationStates();
                    this.overpowerAnimationState.startIfStopped(this.tickCount);
                }
                case 10 -> {
                    this.stopAllAnimationStates();
                    this.flareshotAnimationState.startIfStopped(this.tickCount);
                }
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.sleepAnimationState.stop();
        this.awakeAnimationState.stop();
        this.smashsAnimationState.stop();
        this.phaseAnimationState.stop();
        this.deathAnimationState.stop();
        this.fireAnimationState.stop();
        this.drainAnimationState.stop();
        this.overpowerAnimationState.stop();
        this.shouldercheckAnimationState.stop();
        this.flareshotAnimationState.stop();
    }


    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.setAttackState(DEATH);
    }

    public int deathTimer() {
        return 60;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Intro", this.intro);
        compound.putBoolean("is_Berserk", this.isInBerserk());
        compound.putBoolean("is_Awaken", this.getIsAwaken());
        compound.putInt("Magazine", this.getMagazine());
        compound.putInt("WakeUp", this.wakeUp);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Intro")) {
            this.intro = compound.getBoolean("Intro");
        }
        if (compound.contains("is_Berserk")) {
            this.setIsInBerserk(compound.getBoolean("is_Berserk"));
        }
        if (compound.contains("is_Awaken")) {
            this.setIsAwaken(compound.getBoolean("is_Awaken"));
        }
        if (compound.contains("Magazine")) {
            this.setMagazine(compound.getInt("Magazine"));
        }
        if (compound.contains("WakeUp")) {
            this.wakeUp = compound.getInt("WakeUp");
        }
    }

    private void floatStrider() {
        FluidState below = this.level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getFluidState();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.getAttackState() == CHARGE_ATTACK){
            this.setNoGravity(false);
            if (this.isInLava()) {
                CollisionContext lvt_1_1_ = CollisionContext.of(this);
                if (lvt_1_1_.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition().below(), true) && !this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
                    this.setOnLava(true);
                }
            }
        } else if (below.is(FluidTags.LAVA)) {
            this.setOnGround(true);
            if (this.getY() + vec3.y < this.getBlockPosBelowThatAffectsMyMovement().getY() + below.getOwnHeight()) {
                this.setNoGravity(true);
                if (vec3.y < 0) {
                    this.setDeltaMovement(vec3.multiply(1, 0, 1));
                }
                this.setPos(getX(), this.getBlockPosBelowThatAffectsMyMovement().getY() + below.getOwnHeight(), getZ());
            }
        } else if (this.isInLava()){
            if (this.getRandom().nextFloat() < 0.8F) {
                this.setDeltaMovement(vec3.add(0.0D, 0.08D, 0.0D));
            }
        } else {
            this.setNoGravity(false);
        }
    }

    public void tick() {
        super.tick();

        this.floatStrider();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(this.getAttackState() == 0, this.tickCount);
        }
        this.frame++;
        float moveX = (float) (getX() - xo);
        float moveZ = (float) (getZ() - zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        if (!this.isSilent() && this.frame % 25 == 1 && speed > 0.05 && this.getIsAwaken() && this.getAttackState() != 8) {
            playSound(CataclysmSounds.MONSTROSITYSTEP.get(), 1F, 1.0f);
            ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.08f, 0, 5);
        }
        this.BlockBreaking();
        if (this.blockBreakCounter > 0) {
            this.blockBreakCounter--;
        }
        if (this.shoot_cooldown > 0) {
            this.shoot_cooldown--;
        }
        if (this.overpower_cooldown > 0) {
            this.overpower_cooldown--;
        }
        if (this.check_cooldown > 0) {
            this.check_cooldown--;
        }
        if (this.flare_shoot_cooldown > 0) {
            this.flare_shoot_cooldown--;
        }
        if (this.wakeUp > 0 && !this.intro){
            --this.wakeUp;
        }

        LivingEntity target = this.getTarget();
        if (!this.level().isClientSide) {
            if (this.timeWithoutTarget > 0) {
                --this.timeWithoutTarget;
            }

            if (target != null) {
                this.timeWithoutTarget = 200;
            }
        }

        if (!this.isNoAi() && !this.getIsAwaken()) {
            if (this.tickCount % 4 == 0) {
                this.heal((float) CMConfig.MonstrosityNatureHealing);
            }
        }
        this.setHeadPart();
        if (this.level().isClientSide) {
            ++this.LayerTicks;
            this.LayerBrightness += (0.0F - this.LayerBrightness) * 0.8F;
        }

        if (!this.level().isClientSide) {
            if (this.getAttackState() != 0) {
                this.level().broadcastEntityEvent(this, (byte) 6);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
        }

    }

    protected void onInsideBlock(BlockState p_20005_) {
        if (p_20005_.is(Blocks.SOUL_FIRE) || (p_20005_.is(Blocks.SOUL_CAMPFIRE) && p_20005_.hasProperty(CampfireBlock.LIT) && p_20005_.getValue(CampfireBlock.LIT))) {
            if (this.timeWithoutTarget <= 0
                    && !this.isNoAi()
                    && CMConfig.MonstrosityNatureHealing > 0.0
                    && this.tickCount % 20 == 0) {
                this.heal((float) CMConfig.MonstrosityNatureHealing);
            }
        }
    }

    private void setHeadPart() {
        if (!this.isNoAi()) {
            float f17 = (this.yBodyRot) * ((float) Math.PI / 180F);
            float pitch = this.getXRot() * ((float) Math.PI / 180F);
            float f3 = Mth.sin(f17) * (1 - Math.abs(this.getXRot() / 90F));
            float f18 = Mth.cos(f17) * (1 - Math.abs(this.getXRot() / 90F));

            Vec3[] avector3d = new Vec3[this.monstrosityParts.length];
            for (int j = 0; j < this.monstrosityParts.length; ++j) {
                avector3d[j] = new Vec3(this.monstrosityParts[j].getX(), this.monstrosityParts[j].getY(), this.monstrosityParts[j].getZ());
            }

            float headY = 0F;
            float headxz = 0F;
            if (this.getAttackState() == SMASH_ATTACK) {
                int end = 40;
                float f;
                if (this.attackTicks > end) {
                    f = CMMathUtil.cullAnimationTick(this.attackTicks, 1.2F, 1.0F, 13, end);
                } else {
                    f = CMMathUtil.cullAnimationTick(this.attackTicks, 2.0F, 1.0F, 13, end);
                }
                headxz = -1.6F * f;
                headY = -2.2F * f;
            }

            if (this.getAttackState() == MAGMA_ATTACK) {
                float f = CMMathUtil.cullAnimationTick(this.attackTicks, 0.5F, 1.0F, 0, 40);
                headxz = 4 * f;
                headY = 1.2F * f;
            }

            if (this.getAttackState() == CHARGE_ATTACK) {
                float f = CMMathUtil.cullAnimationTick(this.attackTicks, 2, 1.0F, 0, 30);
                headxz = -4 * f;
            }


            this.setPartPosition(this.headPart, f3 * -1.65F + f3 * headxz, pitch + 2.4F + headY, -f18 * -1.65F - f18 * headxz);

            for (int l = 0; l < this.monstrosityParts.length; ++l) {
                this.monstrosityParts[l].xo = avector3d[l].x;
                this.monstrosityParts[l].yo = avector3d[l].y;
                this.monstrosityParts[l].zo = avector3d[l].z;
                this.monstrosityParts[l].xOld = avector3d[l].x;
                this.monstrosityParts[l].yOld = avector3d[l].y;
                this.monstrosityParts[l].zOld = avector3d[l].z;
            }
        }

    }

    public void aiStep() {
        super.aiStep();
        if (this.getAttackState() == AWAKEN) {
            if (this.attackTicks == 2) {
                this.playSound(CataclysmSounds.MONSTROSITYAWAKEN.get(), 10, 1);
            }
        }

        if (this.getAttackState() == SMASH_ATTACK) {
            if (this.attackTicks == 19) {
                EarthQuake(6.25D);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 20, 0.3f, 0, 20);
                MakeParticle(4.75f, 2.5f);
                MakeParticle(4.75f, -2.5f);
            }
        }

        if (this.getAttackState() == PHASE_SMASH_ATTACK) {
            if (this.attackTicks == 10) {
                this.playSound(CataclysmSounds.MONSTROSITYGROWL.get(), 3, 1);
            }
            if (this.attackTicks == 17) {
                berserkBlockBreaking(8, 8, 8);
                EarthQuake(6.25D);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 20, 0.3f, 0, 20);
                MakeParticle(4.4f, 2.0f);
                MakeParticle(4.4f, -2.0f);
            }
        }
        if (this.getAttackState() == DEATH) {
            if (this.attackTicks == 26) {
                this.playSound(CataclysmSounds.MONSTROSITYLAND.get(), 1, 1);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 20, 0.3f, 0, 20);
            }
        }
        if (this.getAttackState() == MAGMA_ATTACK) {
            if (this.attackTicks == 19) {
                this.playSound(CataclysmSounds.MONSTROSITYSHOOT.get(), 3, 0.75f);
            }
        }

        if (this.getAttackState() == DRAIN_LAVA) {
            if (this.attackTicks == 24) {
                this.setMagazine(0);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 20, 0.3f, 0, 20);
                this.doAbsorptionEffects(4, 1, 4);
                this.playSound(SoundEvents.BUCKET_FILL_LAVA, 6f, 0.5F);
                this.heal(15F * (float) CMConfig.MonstrosityHealingMultiplier);
            }
            if (this.attackTicks == 26) {
                this.doAbsorptionEffects(8, 2, 8);
                this.heal(15F * (float) CMConfig.MonstrosityHealingMultiplier);
            }
            if (this.attackTicks == 28) {
                this.doAbsorptionEffects(16, 4, 16);
                this.heal(15F * (float) CMConfig.MonstrosityHealingMultiplier);
            }
        }

        if (this.getAttackState() == CHARGE_ATTACK) {
            if (this.attackTicks == 22
                    || this.attackTicks == 27
                    || this.attackTicks == 32
                    || this.attackTicks == 37
                    || this.attackTicks == 42
                    || this.attackTicks == 47) {
                ScreenShake_Entity.ScreenShake(level(), this.position(), 20, 0.15f, 0, 6);
                this.playSound(CataclysmSounds.MONSTROSITYSTEP.get(), 1F, 1.0f);
            }

            if (this.attackTicks > 19 && this.attackTicks < 49) {
                if (GCMobsConfig.NetheriteMonstrosityGriefing.get()) {
                    if (!this.level().isClientSide) {
                        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                            this.ChargeBlockBreaking();
                        }
                    }
                }

                double yaw = Math.toRadians(this.getYRot() + 90);
                double xExpand = 2.0F * Math.cos(yaw);
                double zExpand = 2.0F * Math.sin(yaw);
                AABB attackRange = this.getBoundingBox().inflate(0.75D, 0.75D, 0.75D).expandTowards(xExpand, 0, zExpand);
                for (LivingEntity Lentity : this.level().getEntitiesOfClass(LivingEntity.class, attackRange)) {
                    if (!MobUtil.areAllies(this, Lentity)) {
                        DamageSource damagesource = this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this);
                        boolean flag = Lentity.hurt(damagesource, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.4F);
                        if (flag) {
                            double theta = (this.yBodyRot) * (Math.PI / 180);
                            theta += Math.PI / 2;
                            double vec = -2.5D;
                            double vecX = Math.cos(theta);
                            double vecZ = Math.sin(theta);

                            double d0 = Lentity.getX() - (this.getX() + vec * vecX);
                            double d1 = Lentity.getZ() - (this.getZ() + vec * vecZ);
                            double d2 = Math.max(d0 * d0 + d1 * d1, 0.05D);
                            double vel = 4.0D;
                            Lentity.push(d0 / d2 * vel, 0.3D, d1 / d2 * vel);
                            Lentity.addEffect(new MobEffectInstance(ModEffect.EFFECTBONE_FRACTURE.get(), 100));
                        }
                    }
                }

            }
        }

        if (this.getAttackState() == EARTHQUAKE_ATTACK) {
            if (this.attackTicks == 9) {
                this.OverPowerKnockBack(7D);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 20, 0.3f, 0, 20);
                this.MakeParticle(-0.3f, 3.4f);
                this.MakeParticle(-0.3F, -3.4f);
                this.CircleFlameJet(-0.3f, 3.4f,this.isInBerserk() ? 14 : 7,this.isInBerserk() ? 8 : 4,3);
                this.CircleFlameJet(-0.3F, -3.4f,this.isInBerserk() ? 14 : 7,this.isInBerserk() ? 8 : 4,3);
                this.playSound(CataclysmSounds.REMNANT_STOMP.get(), 1, 0.7F);
            }
            if (this.attackTicks == 26) {
                this.playSound(CataclysmSounds.MONSTROSITYGROWL.get(), 3, 1);

            }

            for (int l = 31; l <= 41; l = l + 2) {
                if (this.attackTicks == l) {
                    int d = l - 27;
                    int d2 = l - 26;
                    float ds = (d + d2) / 2.0F;
                    this.StompDamage(0.6f, d, 5, 1.05F, -2.0f, 0, 0, 0.7f);
                    this.StompDamage(0.6f, d2, 5, 1.05F, -2.0f, 0, 0, 0.7f);
                    this.StompSound(ds, 0F);
                }
            }
        }

        if (this.getAttackState() == FLARE_ATTACK) {
            if (this.attackTicks == 35) {
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.08f, 0, 10);
                this.playSound(CataclysmSounds.MONSTROSITYSHOOT.get(), 3, 0.75f);
            }
        }

    }

    private void CircleFlameJet(float vec, float math,int vertexrune, int rune,double time) {
        float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F)) ;
        float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F)) ;
        double theta = (this.yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);

        for (int i = 0; i < vertexrune; i++) {
            float throwAngle = i * Mth.PI / (vertexrune / 2.0F);
            for (int k = 0; k < rune; ++k) {
                double d2 = 1.1D * (double) (k + 1);
                int d3 = (int) (time * (k + 1));
                this.spawnJet(this.getX() + vec * vecX  + f * math + (double) Mth.cos(throwAngle) * 1.25D * d2, this.getZ() + vec * vecZ  + f1 * math + (double) Mth.sin(throwAngle) * 1.25D * d2, this.getY() -2, this.getY() + 2, throwAngle, d3);
            }

        }
    }

    private void spawnJet(double x, double z, double minY, double maxY, float rotation, int delay) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level(), blockpos1, Direction.UP)) {
                if (!this.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            this.level().addFreshEntity(new Flame_Jet_Entity(this.level(), x, (double) blockpos.getY() + d0, z, rotation, delay, (float)CMConfig.FlameJetDamage,this));
        }
    }

    private void StompSound(float distance, float math) {
        double theta = (yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
        float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
        this.level().playSound(null, this.getX() + distance * vecX + f * math, this.getY(), this.getZ() + distance * vecZ + f1 * math, CataclysmSounds.REMNANT_STOMP.get(), this.getSoundSource(), 0.6f, 1.0f);
    }

    private void StompDamage(float spreadarc, int distance, int height, float mxy, float vec,float math, int shieldbreakticks, float damage) {
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        double spread = Math.PI * spreadarc;
        int arcLen = Mth.ceil(distance * spread);
        float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F)) ;
        float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F)) ;
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = this.getX() + vx * distance + vec * Math.cos((yBodyRot + 90) * Math.PI / 180) + f * math;
            double pz = this.getZ() + vz * distance + vec * Math.sin((yBodyRot + 90) * Math.PI / 180  + f1 * math);
            float factor = 1 - distance / (float) 12;
            int hitX = Mth.floor(px);
            int hitZ = Mth.floor(pz);
            BlockPos pos = new BlockPos(hitX, hitY + height, hitZ);
            BlockState block = this.level().getBlockState(pos);

            int maxDepth = 30;
            for (int depthCount = 0; depthCount < maxDepth; depthCount++) {
                if (block.getRenderShape() == RenderShape.MODEL) {
                    break;
                }
                pos = pos.below();
                block = this.level().getBlockState(pos);
            }

            if (block.getRenderShape() != RenderShape.MODEL) {
                block = Blocks.AIR.defaultBlockState();
            }
            this.spawnBlocks(hitX,hitY + height ,hitZ, (int) (this.getY() - height),block, px, pz, mxy, vx, vz, factor, shieldbreakticks, damage);
        }
    }

    private boolean notLavaCliff(double distance) {
        double theta = (this.yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        double px = this.getX() + vecX * distance;
        double pz = this.getZ() + vecZ * distance;
        double checkHeight = -2.5D;
        Vec3 forwardPosition = new Vec3(
                px,
                this.getY() + checkHeight,
                pz
        );
        BlockState blockStateBelow = this.level().getBlockState(BlockPos.containing(forwardPosition));
        return !blockStateBelow.isAir();
    }

    private void spawnBlocks(int hitX, int hitY, int hitZ, int lowestYCheck,BlockState blockState,double px,double pz,float mxy,double vx,double vz,float factor, int shieldbreakticks,float damage) {
        BlockPos blockpos = new BlockPos(hitX, hitY, hitZ);
        BlockState block = level().getBlockState(blockpos);
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level(), blockpos1, Direction.UP)) {
                if (!this.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(lowestYCheck) - 1);


        Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(level(), hitX + 0.5D, (double)blockpos.getY() + d0 + 0.5D, hitZ + 0.5D, blockState, 10);

        double b0 = hitX - this.getX();
        double b1 = hitZ - this.getZ();
        double b2 = Math.max(b0 * b0 + b1 * b1, 0.001);
        fallingBlockEntity.push(b0 / b2 * 1.5d, 0.2D + this.getRandom().nextGaussian() * 0.04D, b1 / b2 * 1.5d);
        this.level().addFreshEntity(fallingBlockEntity);

        AABB selection = new AABB(px - 0.5, (double)blockpos.getY() + d0 -1, pz - 0.5, px + 0.5, (double)blockpos.getY() + d0 + mxy, pz + 0.5);
        List<LivingEntity> hit = level().getEntitiesOfClass(LivingEntity.class, selection);
        for (LivingEntity entity : hit) {
            if (!MobUtil.areAllies(this, entity)) {
                DamageSource damagesource = this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this);
                boolean flag = entity.hurt(damagesource, (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                if (entity.isDamageSourceBlocked(damagesource) && entity instanceof Player player  && shieldbreakticks > 0) {
                    this.disableShield(player, shieldbreakticks);
                }

                if (flag) {
                    double magnitude = 10;
                    double x = vx * Math.max(factor, 0.2) * magnitude;
                    double y = 0;
                    if (entity.onGround()) {
                        y += 0.15;
                    }
                    double z = vz * Math.max(factor, 0.2) * magnitude;
                    entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
                }
            }
        }
    }

    private void doAbsorptionEffects(int x, int y, int z) {
        int MthX = Mth.floor(this.getX());
        int MthY = Mth.floor(this.getY());
        int MthZ = Mth.floor(this.getZ());
        for (int k2 = -x; k2 <= x; ++k2) {
            for (int l2 = -z; l2 <= z; ++l2) {
                for (int j = -y; j <= y; ++j) {
                    int i3 = MthX + k2;
                    int k = MthY + j;
                    int l = MthZ + l2;
                    BlockPos blockpos = new BlockPos(i3, k, l);
                    this.doAbsorptionEffect(blockpos);
                }
            }
        }
    }

    private void doAbsorptionEffect(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        if (!this.level().isClientSide) {
            if (state.is(Blocks.LAVA)) {
                this.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    private void EarthQuake(double area) {
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1.5f, 1F + this.getRandom().nextFloat() * 0.1F);
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(area))) {
            if (!MobUtil.areAllies(this, entity)) {
                DamageSource damagesource = this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this);
                boolean flag = entity.hurt(damagesource, (float) ((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) + Math.min(this.getAttributeValue(Attributes.ATTACK_DAMAGE), entity.getMaxHealth() * CMConfig.MonstrositysHpdamage)));
                if (entity.isDamageSourceBlocked(damagesource) && entity instanceof Player player) {
                    this.disableShield(player, 120);
                }

                if (flag) {
                    this.launch(entity, 2D,0.6D);
                    if (this.isInBerserk()) {
                        entity.setSecondsOnFire(6);
                    }
                }
            }
        }
    }

    private void OverPowerKnockBack(double area) {
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(area))) {
            if (!MobUtil.areAllies(this, entity)) {
                this.launch(entity, 3D,0.35D);
            }
        }
    }


    private void MakeParticle(float vec, float math) {
        if (this.level().isClientSide) {
            float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F)) ;
            float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F)) ;
            double theta = (yBodyRot) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            for (int i1 = 0; i1 < 80 + random.nextInt(12); i1++) {
                double DeltaMovementX = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementY = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementZ = getRandom().nextGaussian() * 0.07D;
                float angle = (0.01745329251F * this.yBodyRot) + i1;
                double extraX = 2F * Mth.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = 2F * Mth.cos(angle);
                int hitX = Mth.floor(getX() + vec * vecX+ extraX);
                int hitY = Mth.floor(getY());
                int hitZ = Mth.floor(getZ() + vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = level().getBlockState(hit.below());
                if (this.isInBerserk()) {
                    this.level().addParticle(ParticleTypes.FLAME, getX() + vec * vecX + extraX + f * math, this.getY() + extraY, getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);
                } else {
                    if (block.getRenderShape() != RenderShape.INVISIBLE) {
                        this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), getX() + vec * vecX + extraX + f * math, this.getY() + extraY, getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);
                    }
                }
            }
            if (this.isInBerserk()) {
                this.level().addParticle(new RingParticle.RingData(0f, (float) Math.PI / 2f, 35, 0.8f, 0.305f, 0.02f, 1f, 30f, false, RingParticle.EnumRingBehavior.GROW), getX() + vec * vecX + f * math, getY() + 0.2f, getZ() + vec * vecZ + f1 * math, 0, 0, 0);
            } else {
                this.level().addParticle(new RingParticle.RingData(0f, (float) Math.PI / 2f, 35, 1f, 1f, 1f, 1f, 30f, false, RingParticle.EnumRingBehavior.GROW), getX() + vec * vecX + f * math, getY() + 0.2f, getZ() + vec * vecZ + f1 * math, 0, 0, 0);
            }
        }
    }

    private void launch(Entity e, double XZpower,double Ypower) {
        double d0 = e.getX() - this.getX();
        double d1 = e.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        e.push(d0 / d2 * XZpower, Ypower, d1 / d2 * XZpower);
    }

    private void ChargeBlockBreaking(){
        boolean flag = false;
        AABB aabb = this.getBoundingBox().inflate(0.5D, 0.2D, 0.5D);
        for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(this.getY()), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (!blockstate.isAir() && blockstate.canEntityDestroy(this.level(), blockpos, this) && !blockstate.is(ModTag.NETHERITE_MONSTROSITY_IMMUNE) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                if (this.random.nextInt(6) == 0 && !blockstate.hasBlockEntity()) {
                    Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(level(), blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, blockstate, 20);
                    flag = this.level().destroyBlock(blockpos, false, this) || flag;
                    fallingBlockEntity.setDeltaMovement(fallingBlockEntity.getDeltaMovement().add(this.position().subtract(fallingBlockEntity.position()).multiply((-1.2D + random.nextDouble()) / 3, 0.2D + getRandom().nextGaussian() * 0.15D, (-1.2D + random.nextDouble()) / 3)));
                    this.level().addFreshEntity(fallingBlockEntity);
                } else {
                    flag = this.level().destroyBlock(blockpos, false, this) || flag;
                }
            }
        }
    }

    private void berserkBlockBreaking(int x, int y, int z) {
        int MthX = Mth.floor(this.getX());
        int MthY = Mth.floor(this.getY());
        int MthZ = Mth.floor(this.getZ());
        if (!this.level().isClientSide) {
            if (GCMobsConfig.NetheriteMonstrosityGriefing.get() && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                for (int k2 = -x; k2 <= x; ++k2) {
                    for (int l2 = -z; l2 <= z; ++l2) {
                        for (int j = 0; j <= y; ++j) {
                            int i3 = MthX + k2;
                            int k = MthY + j;
                            int l = MthZ + l2;
                            BlockPos blockpos = new BlockPos(i3, k, l);
                            BlockState block = level().getBlockState(blockpos);
                            BlockEntity tileEntity = level().getBlockEntity(blockpos);
                            if (!block.isAir() && !block.is(ModTag.NETHERITE_MONSTROSITY_IMMUNE)) {
                                if (tileEntity == null && random.nextInt(4) + 1 == 4) {
                                    this.level().removeBlock(blockpos, true);
                                    Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(level(), i3 + 0.5D, k + 0.5D, l + 0.5D, block,5);
                                    this.level().setBlock(blockpos, block.getFluidState().createLegacyBlock(), 3);
                                    fallingBlockEntity.setDeltaMovement(fallingBlockEntity.getDeltaMovement().add(this.position().subtract(fallingBlockEntity.position()).multiply((-1.2D + random.nextDouble()) / 3, (-1.1D + random.nextDouble()) / 3, (-1.2D + random.nextDouble()) / 3)));
                                    this.level().addFreshEntity(fallingBlockEntity);
                                } else {
                                    this.level().destroyBlock(new BlockPos(i3, k, l), shouldDropItem(tileEntity));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void BlockBreaking() {
        if(!this.isNoAi()) {
            if (!this.level().isClientSide && this.blockBreakCounter == 0) {
                if (GCMobsConfig.NetheriteMonstrosityGriefing.get() && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                    for (int a = (int) Math.round(this.getBoundingBox().minX); a <= (int) Math.round(this.getBoundingBox().maxX); a++) {
                        for (int b = (int) Math.round(this.getBoundingBox().minY); (b <= (int) Math.round(this.getBoundingBox().maxY) + 1) && (b <= 127); b++) {
                            for (int c = (int) Math.round(this.getBoundingBox().minZ); c <= (int) Math.round(this.getBoundingBox().maxZ); c++) {
                                BlockPos blockpos = new BlockPos(a, b, c);
                                BlockState block = this.level().getBlockState(blockpos);
                                BlockEntity tileEntity = this.level().getBlockEntity(blockpos);
                                if (!block.isAir() && !block.is(ModTag.NETHERITE_MONSTROSITY_IMMUNE)) {
                                    boolean flag = this.level().destroyBlock(new BlockPos(a, b, c), shouldDropItem(tileEntity));
                                    if (flag) {
                                        this.blockBreakCounter = 10;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }


    private Vec3 rotateOffsetVec(Vec3 offset, float xRot, float yRot) {
        return offset.xRot(-xRot * ((float) Math.PI / 180F)).yRot(-yRot * ((float) Math.PI / 180F));
    }

    private boolean shouldDropItem(BlockEntity tileEntity) {
        if (tileEntity == null) {
            return this.random.nextInt(3) + 1 == 3;
        }
        return true;
    }

    public boolean shouldGoBerserk() {
        return this.getHealth() <= (this.getMaxHealth() * 0.4F);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack stack) {
        ItemEntity itementity = this.spawnAtLocation(stack,0.0f);
        if (itementity != null) {
            itementity.setDeltaMovement(itementity.getDeltaMovement().multiply(0.0, 3.5, 0.0));
            itementity.setGlowingTag(true);
            itementity.setExtendedLifetime();
        }
        return itementity;
    }

    private void setPartPosition(NMPart part, double offsetX, double offsetY, double offsetZ) {
        part.setPos(this.getX() + offsetX * part.scale, this.getY() + offsetY * part.scale, this.getZ() + offsetZ * part.scale);
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.monstrosityParts;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Cm_Part_Entity.assignPartIDs(this);
    }

    public void setAutonomous(boolean autonomous) {
        this.entityData.set(AUTO_MODE, autonomous);
        if (autonomous) {
            this.playSound(SoundEvents.ARROW_HIT_PLAYER);
            if (!this.isWandering()) {
                this.setWandering(true);
                this.setStaying(false);
            }
        }
    }

    public boolean isAutonomous() {
        return this.entityData.get(AUTO_MODE);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        if (!this.isNoAi()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof Mob mob){
                return mob;
            } else if (entity instanceof LivingEntity
                    && !this.isAutonomous()) {
                return (LivingEntity)entity;
            }
        }

        return null;
    }

    public boolean isControlledByLocalInstance() {
        return this.isEffectiveAi();
    }

    public double getPassengersRidingOffset() {
        float f = Math.min(0.25F, this.walkAnimation.speed());
        float f1 = this.walkAnimation.position();
        return (double)this.getBbHeight() - 0.3 + (double)(0.12F * Mth.cos(f1 * 0.7F) * 0.7F * f);
    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isAlive()) {
            LivingEntity rider = this.getControllingPassenger();
            FluidState below = this.level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getFluidState();
            if (this.isVehicle()
                    && rider instanceof Player player
                    && !this.clientStopMoving()
                    && !this.isAutonomous()) {
                this.setYRot(rider.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(rider.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float speed = this.getRiddenSpeed(player);
                float f = rider.xxa * speed;
                float f1 = rider.zza * speed;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }

                Vec3 vec3 = new Vec3(f, pTravelVector.y, f1);
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (this.isInLava() || below.is(FluidTags.LAVA) ? 0.2F : 1F));
                if (this.isInLava() || below.is(FluidTags.LAVA)) {
                    this.moveRelative(this.getSpeed(), vec3);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
                } else {
                    super.travel(vec3);
                }
                this.lerpSteps = 0;

                this.calculateEntityAnimation(false);
            } else {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (this.isInLava() || below.is(FluidTags.LAVA) ? 0.2F : 1F));
                if (this.isInLava()) {
                    this.moveRelative(this.getSpeed(), pTravelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
                } else {
                    super.travel(pTravelVector);
                }
            }
        }

    }

    public boolean clientStopMoving(){
        if (this.level().isClientSide) {
            return this.clientStop;
        } else {
            return this.getAttackState() != 0;
        }
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if (p_21375_ == 6){
            this.clientStop = true;
        } else if (p_21375_ == 7){
            this.clientStop = false;
        } else {
            super.handleEntityEvent(p_21375_);
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.MONSTROSITYHURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.MONSTROSITYDEATH.get();
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.level().isClientSide) {
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
                if ((itemstack.is(Items.LAVA_BUCKET)
                        || itemstack.is(Tags.Items.STORAGE_BLOCKS_NETHERITE)
                        || itemstack.is(GoetyBlocks.REINFORCED_REDSTONE_BLOCK.get().asItem())
                        || itemstack.is(CataclysmItems.LAVA_POWER_CELL.get()))
                        && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.getAbilities().instabuild) {
                        if (itemstack.is(Items.LAVA_BUCKET)){
                            this.playSound(SoundEvents.BUCKET_EMPTY_LAVA, 1.0F, 1.0F);
                            ItemHelper.addAndConsumeItem(pPlayer, pHand, new ItemStack(Items.BUCKET));
                        } else {
                            itemstack.shrink(1);
                        }

                    }
                    if (itemstack.is(GoetyBlocks.REINFORCED_REDSTONE_BLOCK.get().asItem()) || itemstack.is(Tags.Items.STORAGE_BLOCKS_NETHERITE)){
                        this.heal(this.getMaxHealth() / 4.0F);
                        this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.25F);
                    } else if (itemstack.is(Items.LAVA_BUCKET) || itemstack.is(CataclysmItems.LAVA_POWER_CELL.get())) {
                        this.heal((this.getMaxHealth() / 4.0F) / 8.0F);
                        this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 0.25F, 0.75F);
                    }
                    if (this.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 7; ++i) {
                            double d0 = serverLevel.random.nextGaussian() * 0.02D;
                            double d1 = serverLevel.random.nextGaussian() * 0.02D;
                            double d2 = serverLevel.random.nextGaussian() * 0.02D;
                            serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    //Don't care, let them ride in the air lol
                    if (this.getFirstPassenger() != null && this.getFirstPassenger() != pPlayer){
                        this.getFirstPassenger().stopRiding();
                        return InteractionResult.SUCCESS;
                    } else if (!(pPlayer.getItemInHand(pHand).getItem() instanceof IWand)){
                        this.doPlayerRide(pPlayer);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    static class MonstrosityPhaseChangeGoal extends Goal {
        protected final NetheriteMonstrosityServant entity;
        private final int getAttackState;
        private final int attackState;
        private final int attackEndState;
        private final int attackMaxTick;

        public MonstrosityPhaseChangeGoal(NetheriteMonstrosityServant entity, int getAttackState, int attackState, int attackEndState, int attackMaxTick) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.getAttackState = getAttackState;
            this.attackState = attackState;
            this.attackEndState = attackEndState;
            this.attackMaxTick = attackMaxTick;
        }

        @Override
        public boolean canUse() {
            return !this.entity.isInBerserk()
                    && this.entity.getAttackState() == this.getAttackState
                    && this.entity.shouldGoBerserk();
        }

        @Override
        public boolean canContinueToUse() {
            return this.attackMaxTick > 0 ? this.entity.attackTicks <= this.attackMaxTick : this.canUse();
        }

        @Override
        public void start() {
            this.entity.setIsInBerserk(true);
            if (this.getAttackState != this.attackState) {
                this.entity.setAttackState(this.attackState);
            }
        }

        @Override
        public void stop() {
            this.entity.setAttackState(this.attackEndState);
        }
    }

    static class MagmaShoot extends InternalSummonAttackGoal {
        private final NetheriteMonstrosityServant entity;
        private final int attackshot;
        private final float random;

        public MagmaShoot(NetheriteMonstrosityServant entity, int getAttackState, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange, int attackshot, float random) {
            super(entity, getAttackState, attackstate, attackendstate, attackMaxtick, attackseetick, attackrange);
            this.entity = entity;
            this.attackshot = attackshot;
            this.random = random;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return super.canUse() && target != null && this.entity.distanceTo(target) >= 14F && this.entity.getRandom().nextFloat() * 100.0F < random && this.entity.getSensing().hasLineOfSight(target) && this.entity.getMagazine() < CMConfig.Lavabombmagazine && this.entity.shoot_cooldown <= 0;
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.shoot_cooldown = SHOOT_COOLDOWN;
            this.entity.setMagazine(this.entity.getMagazine() + 1);
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            super.tick();
            int shots = CMConfig.Lavabombamount;

            if (target !=null) {
                if (this.entity.attackTicks == this.attackshot) {
                    for (int i = 0; i < shots; ++i) {
                        Pyroclast lava = new Pyroclast(this.entity, this.entity.level());
                        lava.setExplosionPower(CMConfig.Lavabombradius);
                        double d0 = target.getX() - this.entity.headPart.getX();
                        double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - lava.getY();
                        double d2 = target.getZ() - this.entity.headPart.getZ();
                        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
                        lava.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.0F, 24 - this.entity.level().getDifficulty().getId() * 4);
                        this.entity.level().addFreshEntity(lava);
                    }
                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class FlareShoot extends InternalSummonAttackGoal {
        private final NetheriteMonstrosityServant entity;
        private final int attackshot;
        private final float random;

        public FlareShoot(NetheriteMonstrosityServant entity, int getAttackState, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange, int attackshot, float random) {
            super(entity, getAttackState, attackstate, attackendstate, attackMaxtick, attackseetick, attackrange);
            this.entity = entity;
            this.attackshot = attackshot;
            this.random = random;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return super.canUse() && target != null && this.entity.distanceTo(target) >= 10F && this.entity.getRandom().nextFloat() * 100.0F < random && this.entity.getSensing().hasLineOfSight(target) && this.entity.flare_shoot_cooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.flare_shoot_cooldown = FLARE_SHOOT_COOLDOWN;
        }

        @Override
        public void tick() {
            LivingEntity target = entity.getTarget();
            super.tick();
            int shots = 5;

            if(target !=null) {
                if (this.entity.attackTicks == attackshot) {
                    for (int i = 0; i < shots; ++i) {
                        float f = Mth.cos( this.entity.yBodyRot * ((float)Math.PI / 180F)) ;
                        float f1 = Mth.sin( this.entity.yBodyRot * ((float)Math.PI / 180F)) ;
                        double theta = (this.entity.yBodyRot) * (Math.PI / 180);
                        theta += Math.PI / 2;
                        double vecX = Math.cos(theta);
                        double vecZ = Math.sin(theta);
                        double vec = 2.2;
                        double math = 3.4D;
                        FlareBomb lava = new FlareBomb(GCEntityType.FLARE_BOMB.get(), this.entity.level(), this.entity);
                        lava.setPosRaw(this.entity.getX() + vec * vecX + f * math, this.entity.getY(0.65), this.entity.getZ() + vec * vecZ + f1 * math);

                        double d0 = target.getX() - lava.getX() ;
                        double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - lava.getY();
                        double d2 = target.getZ() - lava.getZ();
                        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
                        lava.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.0F, 1 + i * 8);
                        this.entity.level().addFreshEntity(lava);
                    }
                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class ShoulderCheck extends InternalSummonAttackGoal {
        private final NetheriteMonstrosityServant entity;
        private final int attackShot;
        private final int attackEndShot;
        private final float random;

        public ShoulderCheck(NetheriteMonstrosityServant entity, int getAttackState, int attackState, int attackEndState, int attackMaxTick, int attackSeeTick, float attackRange, int attackShot, int attackEndShot, float random) {
            super(entity, getAttackState, attackState, attackEndState, attackMaxTick, attackSeeTick, attackRange);
            this.entity = entity;
            this.attackShot = attackShot;
            this.attackEndShot = attackEndShot;
            this.random = random;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return super.canUse()
                    && !this.entity.isStaying()
                    && target != null
                    && this.entity.distanceTo(target) >= 5.75F
                    && this.entity.getRandom().nextFloat() * 100.0F < this.random
                    && this.entity.getSensing().hasLineOfSight(target)
                    && this.entity.check_cooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.check_cooldown = CHECK_COOLDOWN;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.entity.attackTicks > attackShot && this.entity.attackTicks < this.attackEndShot) {
                if (this.entity.onGround() || this.entity.getOnLava()) {
                    if (this.entity.notLavaCliff(2)) {
                        Vec3 vector3d = this.entity.getDeltaMovement();
                        float f = this.entity.getYRot() * ((float) Math.PI / 180F);
                        Vec3 vector3d1 = new Vec3(-Mth.sin(f), this.entity.getDeltaMovement().y, Mth.cos(f)).scale(0.5D).add(vector3d.scale(0.5D));
                        this.entity.setDeltaMovement(vector3d1.x, this.entity.getDeltaMovement().y, vector3d1.z);
                    }
                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

}
