package com.Polarice3.goety_cataclysm.common.entities.ally.acropolis;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import com.github.L_Ender.cataclysm.entity.projectile.Urchin_Spike_Entity;
import com.github.L_Ender.cataclysm.init.ModItems;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;

public class UrchinkinServant extends Summoned {
    boolean searchingForLand;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState rollAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(UrchinkinServant.class, EntityDataSerializers.INT);
    private int roll_cooldown = 0;
    public static final int ROLL_COOLDOWN = 80;
    public int attackTicks;
    protected final SemiAquaticPathNavigator waterNavigation;
    protected final GroundPathNavigation groundNavigation;

    public UrchinkinServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
        this.waterNavigation = new SemiAquaticPathNavigator(this, world);
        this.groundNavigation = new GroundPathNavigation(this, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new Roll(this, 0, 1, 0, 40, 12, 5.0F, 13, 30, 50.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new WaterWanderGoal<>(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void followGoal() {
        this.goalSelector.addGoal(5, new FollowOwnerWaterGoal(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.37D)
                .add(Attributes.ARMOR, GCAttributesConfig.UrchinkinArmor.get())
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.UrchinkinDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.UrchinkinHealth.get());
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.UrchinkinHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.UrchinkinArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.UrchinkinDamage.get());
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean canUpdateMove() {
        return true;
    }

    @Override
    public int xpReward() {
        return 5;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof UrchinkinServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.UrchinkinLimit.get();
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
                this.moveControl = new UrchinkinSwimControl(this, 4.0F);
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
        if (Objects.equals(input, "roll")) {
            return this.rollAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else {
            return Objects.equals(input, "attack") ? this.attackAnimationState : new AnimationState();
        }
    }

    public boolean isMeatBoy() {
        String s = ChatFormatting.stripFormatting(this.getName().getString());
        return s != null && s.toLowerCase().contains("meatboy");
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_STATE, 0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0:
                    this.stopAllAnimationStates();
                    break;
                case 1:
                    this.stopAllAnimationStates();
                    this.rollAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public int getAttackState() {
        return (Integer)this.entityData.get(ATTACK_STATE);
    }

    public void setAttackState(int input) {
        this.attackTicks = 0;
        this.entityData.set(ATTACK_STATE, input);
        this.level().broadcastEntityEvent(this, (byte)(-input));
    }

    public void stopAllAnimationStates() {
        this.rollAnimationState.stop();
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
        } else {
            super.handleEntityEvent(id);
        }

    }

    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F;
    }

    public void tick() {
        super.tick();
        if (this.getAttackState() > 0) {
            ++this.attackTicks;
        }

        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
        }

        if (this.roll_cooldown > 0) {
            --this.roll_cooldown;
        }

    }

    public void aiStep() {
        super.aiStep();
        if (this.getAttackState() == 1) {
            if (this.attackTicks > 13 && this.attackTicks < 30 && !this.level().isClientSide) {
                for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
                    if (!MobUtil.areAllies(this, livingentity)) {
                        boolean flag = livingentity.hurt(this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        if (flag && !this.level().isClientSide) {
                            MobEffect mobEffect = MobEffects.POISON;
                            if (this.getTrueOwner() != null) {
                                if (CuriosFinder.hasWildRobe(this.getTrueOwner())) {
                                    mobEffect = GoetyEffects.ACID_VENOM.get();
                                }
                            }
                            livingentity.addEffect(new MobEffectInstance(mobEffect, 60, 0), this);
                        }
                    }
                }
            }

            if (this.attackTicks == 13 || this.attackTicks == 16 || this.attackTicks == 19 || this.attackTicks == 22 || this.attackTicks == 25 || this.attackTicks == 28) {
                this.SpikeNansa(3);
            }
        }

    }

    public void die(DamageSource cause) {
        super.die(cause);
        this.SpikeNansa(6);
    }

    private void SpikeNansa(int spike) {
        int shardCount = spike + this.random.nextInt(2);
        if (!this.level().isClientSide) {
            for(int i = 0; i < shardCount; ++i) {
                float f = (float)(i + 1) / (float)shardCount * 360.0F;
                Urchin_Spike_Entity shard = new Urchin_Spike_Entity(this.level(), this);
                if (this.isMeatBoy()) {
                    ItemStack itemstack = new ItemStack(ModItems.BLOOD_CLOT.get());
                    shard.setItem(itemstack);
                }

                shard.shoot(this.random.nextFloat() * 0.4F * 2.0F - 0.4F, (double)(this.random.nextFloat() * 0.25F + 0.1F), (double)(this.random.nextFloat() * 0.4F * 2.0F - 0.4F), 0.35F, 1.0F);
                this.level().addFreshEntity(shard);
            }
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

    static class Roll extends Goal {
        private final UrchinkinServant entity;
        private final int attackrollstart;
        private final int attackrollend;
        private final float random;
        private final int getattackstate;
        private final int attackstate;
        private final int attackendstate;
        private final int attackMaxtick;
        private final int attackseetick;
        private final float attackrange;

        public Roll(UrchinkinServant entity, int getAttackState, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange, int attackroll, int attackrollend, float random) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.getattackstate = getAttackState;
            this.attackstate = attackstate;
            this.attackendstate = attackendstate;
            this.attackMaxtick = attackMaxtick;
            this.attackseetick = attackseetick;
            this.attackrange = attackrange;
            this.attackrollstart = attackroll;
            this.attackrollend = attackrollend;
            this.random = random;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return target != null && target.isAlive() && this.entity.distanceTo(target) < this.attackrange && this.entity.getAttackState() == this.getattackstate && this.entity.getRandom().nextFloat() * 100.0F < this.random && this.entity.getSensing().hasLineOfSight(target) && this.entity.roll_cooldown <= 0;
        }

        public void start() {
            this.entity.setAttackState(this.attackstate);
        }

        public void stop() {
            this.entity.setAttackState(this.attackendstate);
            this.entity.roll_cooldown = 80;
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

            if (this.attackrollstart < this.entity.attackTicks && this.attackrollend > this.entity.attackTicks) {
                Vec3 vector3d = this.entity.getDeltaMovement();
                float f = this.entity.getYRot() * 0.017453292F;
                Vec3 vector3d1 = (new Vec3((double)(-Mth.sin(f)), this.entity.getDeltaMovement().y, (double)Mth.cos(f))).scale(0.35).add(vector3d.scale(0.35));
                this.entity.setDeltaMovement(vector3d1.x, this.entity.getDeltaMovement().y, vector3d1.z);
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class UrchinkinSwimControl extends MoveControl {
        private final UrchinkinServant drowned;
        private final float speedMulti;

        public UrchinkinSwimControl(UrchinkinServant p_32433_, float speedMulti) {
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
