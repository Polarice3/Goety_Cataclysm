package com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.projectiles.FlyingItem;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.common.items.revive.ArcaneSpirit;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.config.CMCommonConfig;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.projectile.Ancient_Desert_Stele_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Poison_Dart_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Sandstorm_Projectile;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.lionfishapi.client.model.tools.DynamicChain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class WadjetServant extends InternalAnimationSummon {
    @OnlyIn(Dist.CLIENT)
    public DynamicChain dc;

    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState sleepAnimationState = new AnimationState();
    public AnimationState awakeAnimationState = new AnimationState();
    public AnimationState stabnswingAnimationState = new AnimationState();
    public AnimationState doublswingAnimationState = new AnimationState();
    public AnimationState spearchargeAnimationState = new AnimationState();
    public AnimationState magicAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    public AnimationState blockAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> AWAKEN = SynchedEntityData.defineId(WadjetServant.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> STAB = SynchedEntityData.defineId(WadjetServant.class, EntityDataSerializers.BOOLEAN);
    public static int SLEEP = 1;
    public static int AWAKE = 2;
    public static int SPEAR_CHARGE = 3;
    public static int MAGIC = 4;
    public static int STAB_SWING = 5;
    public static int DOUBLE_SWING = 6;
    public static int DEATH = 7;
    public static int BLOCK = 8;
    private float prevAttackProgress;
    private float AttackProgress;
    private int charge_cooldown = 0;
    public static final int CHARGE_COOLDOWN = 160;
    private int magic_cooldown = 0;
    public static final int MAGIC_COOLDOWN = 160;

    public WadjetServant(EntityType<? extends WadjetServant> entity, Level world) {
        super(entity, world);
        if (world.isClientSide) {
            this.dc = new DynamicChain(this);
        }

        this.setMaxUpStep(1.25F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new WadjetDoNothingGoal());
        this.goalSelector.addGoal(2, new InternalSummonMoveGoal(this, false, 1.0));
        this.goalSelector.addGoal(5, new WanderGoal<>(this, 1.0, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new ChargeAttackGoal(this,0,SPEAR_CHARGE,0,45,15,20,5.5F,16));
        this.goalSelector.addGoal(1, new MagicAttackGoal(this, 0, MAGIC, 0, 35, 15, 3.5F, 12.0F));
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, STAB_SWING, 0, 60, 60, 5.0F) {
            public boolean canUse() {
                return super.canUse() && WadjetServant.this.getStab();
            }

            public void stop() {
                super.stop();
                WadjetServant.this.setStab(WadjetServant.this.random.nextBoolean());
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, DOUBLE_SWING, 0, 55, 55, 5.0F) {
            public boolean canUse() {
                return super.canUse() && !WadjetServant.this.getStab();
            }

            public void stop() {
                super.stop();
                WadjetServant.this.setStab(WadjetServant.this.random.nextBoolean());
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, SLEEP, SLEEP, 0, 0, 0) {
            public void tick() {
                this.entity.setDeltaMovement(0.0, this.entity.getDeltaMovement().y, 0.0);
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, AWAKE, AWAKE, 0, 70,0));
        this.goalSelector.addGoal(0, new InternalSummonAttackGoal(this, SLEEP, AWAKE, 0, 70, 0, 18.0F));
        this.goalSelector.addGoal(0, new InternalSummonStateGoal(this, 8, BLOCK, 0, 20, 0, false));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.WadjetDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.WadjetHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.WadjetArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.WadjetHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.WadjetArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.WadjetDamage.get());
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public int xpReward() {
        return 35;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof WadjetServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.WadjetLimit.get();
    }

    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getDirectEntity();
        if (entity instanceof Poison_Dart_Entity) {
            return false;
        } else if (this.isSleep() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else if (this.canBlockDamageSource(source)) {
            if (entity instanceof AbstractArrow) {
                float f = 170.0F + this.random.nextFloat() * 80.0F;
                entity.setDeltaMovement(entity.getDeltaMovement().scale(1.0));
                entity.setYRot(entity.getYRot() + f);
                entity.hurtMarked = true;
            }

            if (this.getAttackState() == 0) {
                this.playSound(SoundEvents.ANVIL_LAND, 1.0F, 2.0F);
                this.setAttackState(BLOCK);
            }

            return false;
        } else {
            return super.hurt(source, damage);
        }
    }

    @Override
    public void handleDamageEvent(DamageSource damageSource) {
        this.invulnerableTime = 20;
        this.hurtDuration = 10;
        this.hurtTime = this.hurtDuration;
        SoundEvent soundevent = this.getHurtSound(damageSource);
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }

        this.hurt(this.damageSources().generic(), 0.0F);
        this.lastDamageSource = damageSource;
        this.lastDamageStamp = this.level().getGameTime();
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        boolean flag = false;
        if (!this.isNoAi() && damageSourceIn.is(DamageTypeTags.IS_PROJECTILE) && !flag && (this.getAttackState() == 0 || this.getAttackState() == 8)) {
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

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "sleep")) {
            return this.sleepAnimationState;
        } else if (Objects.equals(input, "awake")) {
            return this.awakeAnimationState;
        } else if (Objects.equals(input, "charge")) {
            return this.spearchargeAnimationState;
        } else if (Objects.equals(input, "magic")) {
            return this.magicAnimationState;
        } else if (Objects.equals(input, "stabnswing")) {
            return this.stabnswingAnimationState;
        } else if (Objects.equals(input, "doubleswing")) {
            return this.doublswingAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "death")) {
            return this.deathAnimationState;
        } else {
            return Objects.equals(input, "block") ? this.blockAnimationState : new AnimationState();
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STAB, false);
        this.entityData.define(AWAKEN, true);
    }

    public boolean isSleep() {
        return this.getAwaken() || this.getAttackState() == 2;
    }

    public void setSleep(boolean sleep) {
        this.setAttackState(sleep ? SLEEP : 0);
    }

    public void setStab(boolean stab) {
        this.entityData.set(STAB, stab);
    }

    public boolean getStab() {
        return this.entityData.get(STAB);
    }

    public void setAwaken(boolean necklace) {
        if (necklace){
            this.heal(this.getMaxHealth());
        }
        this.entityData.set(AWAKEN, necklace);
    }

    public boolean getAwaken() {
        return this.entityData.get(AWAKEN);
    }

    public boolean canBeSeenAsEnemy() {
        return !this.isSleep() && super.canBeSeenAsEnemy();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0:
                    this.stopAllAnimationStates();
                    break;
                case 1:
                    this.stopAllAnimationStates();
                    this.sleepAnimationState.startIfStopped(this.tickCount);
                    break;
                case 2:
                    this.stopAllAnimationStates();
                    this.awakeAnimationState.startIfStopped(this.tickCount);
                    break;
                case 3:
                    this.stopAllAnimationStates();
                    this.spearchargeAnimationState.startIfStopped(this.tickCount);
                    break;
                case 4:
                    this.stopAllAnimationStates();
                    this.magicAnimationState.startIfStopped(this.tickCount);
                    break;
                case 5:
                    this.stopAllAnimationStates();
                    this.stabnswingAnimationState.startIfStopped(this.tickCount);
                    break;
                case 6:
                    this.stopAllAnimationStates();
                    this.doublswingAnimationState.startIfStopped(this.tickCount);
                    break;
                case 7:
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
                    break;
                case 8:
                    this.stopAllAnimationStates();
                    this.blockAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.sleepAnimationState.stop();
        this.awakeAnimationState.stop();
        this.blockAnimationState.stop();
        this.spearchargeAnimationState.stop();
        this.magicAnimationState.stop();
        this.stabnswingAnimationState.stop();
        this.doublswingAnimationState.stop();
        this.deathAnimationState.stop();
    }

    public void die(DamageSource p_21014_) {
        if (this.getTrueOwner() != null && GCMobsConfig.WadjetSpirit.get()) {
            ItemStack itemStack = new ItemStack(GCItems.ARCANE_SPIRIT.get());
            ArcaneSpirit.setOwnerName(this.getTrueOwner(), itemStack);
            ArcaneSpirit.setSummon(this, itemStack);
            FlyingItem flyingItem = new FlyingItem(ModEntityType.FLYING_ITEM.get(), this.level(), this.getX(), this.getY(), this.getZ());
            flyingItem.setOwner(this.getTrueOwner());
            flyingItem.setItem(itemStack);
            flyingItem.setParticle(ParticleTypes.ENCHANT);
            flyingItem.setSecondsCool(30);
            this.level().addFreshEntity(flyingItem);
        }
        super.die(p_21014_);
        this.setAttackState(DEATH);
    }

    public int deathTimer() {
        return 60;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Awaken", getAwaken());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setAwaken(compound.getBoolean("Awaken"));
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!this.walkAnimation.isMoving() && this.getAttackState() == 0 && this.getAwaken(), this.tickCount);
        }

        this.prevAttackProgress = this.AttackProgress;
        if (this.isAggressive() && this.AttackProgress < 10.0F) {
            ++this.AttackProgress;
        }

        if (!this.isAggressive() && this.AttackProgress > 0.0F) {
            --this.AttackProgress;
        }

        if (this.charge_cooldown > 0) {
            --this.charge_cooldown;
        }

        if (this.magic_cooldown > 0) {
            --this.magic_cooldown;
        }

    }

    public void aiStep() {
        super.aiStep();
        if (this.getAttackState() == 3) {
            if (this.attackTicks == 18) {
                this.playSound(CataclysmSounds.IGNIS_POKE.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
            }

            if (this.attackTicks == 20) {
                this.AreaAttack(9.0F, 6.0F, 45.0F, 1.0F, 90, false);
            }
        }

        if (this.getAttackState() == 4 && this.attackTicks == 15) {
            this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
        }

        if (this.getAttackState() == 5) {
            if (this.attackTicks == 14) {
                this.playSound(CataclysmSounds.IGNIS_POKE.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
                this.AreaAttack(8.0F, 6.0F, 45.0F, 1.0F, 90, false);
            }

            if (this.attackTicks == 37) {
                this.playSound(CataclysmSounds.SWING.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
                this.AreaAttack(6.0F, 4.0F, 220.0F, 1.0F, 70, true);
            }
        }

        if (this.getAttackState() == 6) {
            if (this.attackTicks == 14) {
                this.playSound(CataclysmSounds.SWING.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
                this.AreaAttack(6.0F, 4.0F, 220.0F, 1.0F, 60, true);
            }

            if (this.attackTicks == 28) {
                this.playSound(CataclysmSounds.SWING.get(), 1.0F, 1.25F + this.getRandom().nextFloat() * 0.1F);
                this.AreaAttack(6.0F, 4.0F, 220.0F, 1.0F, 60, true);
            }
        }

    }

    private void AreaAttack(float range, float height, float arc, float damage, int shieldbreakticks, boolean knockback) {
        List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(range, height, range, range);
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
                    boolean hurt = entityHit.hurt(this.getServantAttack(), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                    if (entityHit.isDamageSourceBlocked(this.getServantAttack()) && shieldbreakticks > 0) {
                        if (shieldbreakticks > 0) {
                            this.disableShield(entityHit, shieldbreakticks);
                        }
                    }
                    double d0 = entityHit.getX() - this.getX();
                    double d1 = entityHit.getZ() - this.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
                    if (hurt && knockback) {
                        entityHit.push(d0 / d2 * 2.25, 0.15, d1 / d2 * 2.25);
                    }
                }
            }
        }
    }

    public float getAttackProgress(float partialTicks) {
        return this.prevAttackProgress + (this.AttackProgress - this.prevAttackProgress) * partialTicks;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.WADJET_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.WADJET_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {
        return this.isSleep() ? super.getAmbientSound() : CataclysmSounds.WADJET_AMBIENT.get();
    }

    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return p_34192_.getEffect() != ModEffect.EFFECTSTUN.get() && p_34192_.getEffect() != ModEffect.EFFECTABYSSAL_CURSE.get() && super.canBeAffected(p_34192_);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if ((itemstack.is(Tags.Items.BONES) || itemstack.is(CataclysmItems.KOBOLETON_BONE.get()) || itemstack.is(Items.BONE_BLOCK)) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                if (itemstack.is(Items.BONE_BLOCK)){
                    this.heal(6.0F);
                } else {
                    this.heal(2.0F);
                }
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

    class WadjetDoNothingGoal extends Goal {
        public WadjetDoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = WadjetServant.this.getTarget();
            return !WadjetServant.this.getAwaken() && !(target != null &&  target.isAlive() && WadjetServant.this.distanceToSqr(target) < 50 && WadjetServant.this.getSensing().hasLineOfSight(target));
        }

        @Override
        public void tick() {
            WadjetServant.this.setDeltaMovement(0,WadjetServant.this.getDeltaMovement().y,0);
        }

        @Override
        public void stop() {
            WadjetServant.this.setAwaken(true);
            WadjetServant.this.setAttackState(2);
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class ChargeAttackGoal extends InternalSummonAttackGoal {
        protected final WadjetServant entity;

        private final int attackshottick;
        private final float attackminrange;


        public ChargeAttackGoal(WadjetServant entity, int getattackstate, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, int attackshottick,float attackminrange,int attackrange) {
            super(entity, getattackstate, attackstate, attackendstate, attackMaxtick, attackseetick, attackrange);
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.attackshottick = attackshottick;
            this.attackminrange = attackminrange;
        }



        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return target != null && this.entity.distanceTo(target) > attackminrange && super.canUse() && this.entity.getRandom().nextFloat() * 100.0F < 16f && this.entity.charge_cooldown <= 0;
        }


        @Override
        public void stop() {
            super.stop();
            this.entity.charge_cooldown = CHARGE_COOLDOWN;
        }


        public void tick() {
            LivingEntity target = entity.getTarget();
            super.tick();
            if (entity.attackTicks == attackseetick) {
                float f1 = (float) Math.cos(Math.toRadians(entity.yBodyRot + 90));
                float f2 = (float) Math.sin(Math.toRadians(entity.yBodyRot + 90));
                if(target != null) {
                    float r = entity.distanceTo(target);
                    r = Mth.clamp(r, 0, 4);
                    entity.push(f1 * 0.3 * r, 0, f2 * 0.3 * r);
                }else{
                    entity.push(f1 * 2.0, 0, f2 * 2.0);
                }
            }
            if (entity.attackTicks == attackshottick) {
                if (target != null) {
                    double d1 = 5.0D;
                    Vec3 vec3 = entity.getViewVector(1.0F);
                    double d2 = target.getX() - (entity.getX() + vec3.x * d1);
                    double d3 = target.getY(0.5D) - entity.getY(0.15D);
                    double d4 = target.getZ() - (entity.getZ() + vec3.z * d1);
                    Sandstorm_Projectile largefireball = new Sandstorm_Projectile(entity, d2, d3, d4, entity.level(),6);
                    largefireball.setState(1);
                    largefireball.setPos(entity.getX() + vec3.x * d1, entity.getY(0.15D), largefireball.getZ() + vec3.z * d1);
                    entity.level().addFreshEntity(largefireball);
                }
            }
        }
    }

    static class MagicAttackGoal extends InternalSummonAttackGoal {
        protected final WadjetServant entity;

        private final float attackminrange;

        public MagicAttackGoal(WadjetServant entity, int getattackstate, int attackstate, int attackendstate, int attackMaxtick, int attackseetick,float attackminrange, float attackrange) {
            super(entity, getattackstate, attackstate, attackendstate, attackMaxtick, attackseetick, attackrange);
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.attackminrange = attackminrange;
        }


        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return target != null && this.entity.distanceTo(target) > attackminrange && super.canUse() && this.entity.getRandom().nextFloat() * 100.0F < 24f && this.entity.magic_cooldown <= 0;
        }


        @Override
        public void stop() {
            super.stop();
            this.entity.magic_cooldown = MAGIC_COOLDOWN;
        }

        public void tick() {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                if (entity.attackTicks == attackseetick) {
                    double d1 = target.getY();
                    float f = (float) Mth.atan2(target.getZ() - this.entity.getZ(), target.getX() - this.entity.getX());

                    for(int k = 0; k < 8; ++k) {
                        float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
                        this.spawnSpikeLine(this.entity.getX() + (double)Mth.cos(f2) * 4.5D, this.entity.getZ() + (double)Mth.sin(f2) * 4.5D, d1, f2, 3);
                    }
                    for (int k = 0; k < 13; ++k) {
                        float f3 = f + (float) k * (float) Math.PI * 2.0F / 13.0F + ((float) Math.PI * 2F / 10F);
                        this.spawnSpikeLine(this.entity.getX() + (double)Mth.cos(f3) * 6.5D, this.entity.getZ() + (double)Mth.sin(f3) * 6.5D, d1, f3, 10);
                    }
                    for (int k = 0; k < 16; ++k) {
                        float f4 = f + (float) k * (float) Math.PI * 2.0F / 16.0F + ((float) Math.PI * 2F / 20F);
                        this.spawnSpikeLine(this.entity.getX() + (double)Mth.cos(f4) * 8.5D, this.entity.getZ() + (double)Mth.sin(f4) * 8.5D, d1, f4, 15);
                    }
                    for (int k = 0; k < 19; ++k) {
                        float f5 = f + (float) k * (float) Math.PI * 2.0F / 19.0F + ((float) Math.PI * 2F / 40F);
                        this.spawnSpikeLine(this.entity.getX() + (double)Mth.cos(f5) * 10.5D, this.entity.getZ() + (double)Mth.sin(f5) * 10.5D, d1, f5, 20);
                    }
                    for (int k = 0; k < 24; ++k) {
                        float f6 = f + (float) k * (float) Math.PI * 2.0F / 24.0F + ((float) Math.PI * 2F / 80F);
                        this.spawnSpikeLine(this.entity.getX() + (double)Mth.cos(f6) * 12.5D, this.entity.getZ() + (double)Mth.sin(f6) * 12.5D, d1, f6, 30);
                    }
                }
            }

        }

        private void spawnSpikeLine(double posX, double posZ, double posY, float rotation, int delay) {
            BlockPos blockpos = BlockPos.containing(posX, posY, posZ);
            double d0 = 0.0D;
            do {
                BlockPos blockpos1 = blockpos.above();
                BlockState blockstate = entity.level().getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(entity.level(), blockpos1, Direction.DOWN)) {
                    if (!entity.level().isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = entity.level().getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(entity.level(), blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }

                    break;
                }

                blockpos = blockpos.above();
            } while (blockpos.getY() < Math.min(entity.level().getMaxBuildHeight(), entity.getBlockY() + 12));
            this.entity.level().addFreshEntity(new Ancient_Desert_Stele_Entity(this.entity.level(), posX, (double)blockpos.getY() + d0 -3, posZ, rotation, delay, (float)CMCommonConfig.Wadjet.AncientDesertSteledamage, this.entity));
        }

    }

}
