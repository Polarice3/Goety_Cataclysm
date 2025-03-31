package com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.etc.IShieldEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;

public class RoyalDraugrServant extends Summoned implements IShieldEntity {
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState attack2AnimationState = new AnimationState();
    private int shieldCooldownTime = 0;

    public RoyalDraugrServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new Royal_DraugrAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new WanderGoal<>(this, 1.0, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.27F)
                .add(Attributes.ATTACK_DAMAGE, 5.0F)
                .add(Attributes.MAX_HEALTH, 30.0F)
                .add(Attributes.ARMOR, 5.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1F);
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof RoyalDraugrServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.RoyalDraugrLimit.get();
    }

    @Override
    public int xpReward() {
        return 7;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "attack")) {
            return this.attackAnimationState;
        } else if (Objects.equals(input, "attack2")) {
            return this.attack2AnimationState;
        } else {
            return Objects.equals(input, "idle") ? this.idleAnimationState : new AnimationState();
        }
    }

    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getDirectEntity();
        if (damage > 0.0F && this.canBlockDamageSource(source)) {
            this.hurtCurrentlyUsedShield(damage);
            if (!source.is(DamageTypeTags.IS_PROJECTILE) && entity instanceof LivingEntity) {
                this.blockUsingShield((LivingEntity)entity);
            }

            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().random.nextFloat() * 0.4F);
            return false;
        } else {
            return super.hurt(source, damage);
        }
    }

    public int getShieldCooldownTime() {
        return this.shieldCooldownTime;
    }

    public void setShieldCooldownTime(int shieldCooldownTime) {
        this.shieldCooldownTime = shieldCooldownTime;
    }

    public boolean isShieldDisabled() {
        return this.shieldCooldownTime > 0;
    }

    public void disableShield(boolean guaranteeDisable) {
        float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (guaranteeDisable) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            this.shieldCooldownTime = 100;
            this.stopUsingItem();
            this.level().broadcastEntityEvent(this, (byte)30);
            this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level().random.nextFloat() * 0.4F);
        }

    }

    protected void hurtCurrentlyUsedShield(float p_36383_) {
        if (this.useItem.canPerformAction(ToolActions.SHIELD_BLOCK) && p_36383_ >= 3.0F) {
            int i = 1 + Mth.floor(p_36383_);
            InteractionHand interactionhand = this.getUsedItemHand();
            this.useItem.hurtAndBreak(i, this, (p_219739_) -> {
                p_219739_.broadcastBreakEvent(interactionhand);
            });
            if (this.useItem.isEmpty()) {
                this.stopUsingItem();
                if (interactionhand == InteractionHand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }

                this.useItem = ItemStack.EMPTY;
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level().random.nextFloat() * 0.4F);
            }
        }

    }

    public boolean isBlocking() {
        return false;
    }

    protected void blockUsingShield(LivingEntity p_36295_) {
        super.blockUsingShield(p_36295_);
        if (p_36295_.getMainHandItem().canDisableShield(this.useItem, this, p_36295_)) {
            this.disableShield(true);
        }

    }

    public boolean isDraugrBlocking() {
        if (this.isUsingItem() && !this.useItem.isEmpty()) {
            Item item = this.useItem.getItem();
            if (!this.useItem.canPerformAction(ToolActions.SHIELD_BLOCK)) {
                return false;
            } else {
                return item.getUseDuration(this.useItem) - this.useItemRemaining >= 5;
            }
        } else {
            return false;
        }
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        Entity entity = damageSourceIn.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow abstractarrow) {
            if (abstractarrow.getPierceLevel() > 0) {
                flag = true;
            }
        }

        if (!damageSourceIn.is(DamageTypeTags.BYPASSES_SHIELD) && this.isDraugrBlocking() && !flag) {
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

    public void stopAllAnimationStates() {
        this.attackAnimationState.stop();
        this.attack2AnimationState.stop();
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
        if (this.isDraugrBlocking()) {
            this.stopUsingItem();
            this.setShieldCooldownTime(30);
        }

        return super.doHurtTarget(p_219472_);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        RandomSource randomsource = p_34088_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_34089_);
        this.populateDefaultEquipmentEnchantments(randomsource, p_34089_);
        this.setItemSlot(EquipmentSlot.OFFHAND, this.createSpawnShield());
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
        return spawngroupdata;
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219059_, DifficultyInstance p_219060_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    private ItemStack createSpawnWeapon() {
        return this.random.nextBoolean() ? new ItemStack(CataclysmItems.BLACK_STEEL_AXE.get()) : new ItemStack(CataclysmItems.BLACK_STEEL_SWORD.get());
    }

    private ItemStack createSpawnShield() {
        return new ItemStack(CataclysmItems.BLACK_STEEL_TARGE.get());
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
        }

        if (this.shieldCooldownTime > 0) {
            --this.shieldCooldownTime;
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

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
        Item item = itemstack.getItem();
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
            } else if (item == CataclysmItems.BLACK_STEEL_TARGE.get() && this.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()){
                this.setItemSlot(EquipmentSlot.OFFHAND, itemstack.copy());
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    static class Royal_DraugrAttackGoal extends Goal {
        protected final RoyalDraugrServant mob;
        private final double speedModifier;
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

        public Royal_DraugrAttackGoal(RoyalDraugrServant p_25552_, double p_25553_, boolean p_25554_) {
            this.mob = p_25552_;
            this.speedModifier = p_25553_;
            this.followingTargetEvenIfNotSeen = p_25554_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
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

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void stop() {
            LivingEntity livingentity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.mob.setTarget((LivingEntity)null);
            }

            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
            if (this.mob.isDraugrBlocking()) {
                this.mob.stopUsingItem();
                this.mob.setShieldCooldownTime(60);
            }

        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(livingentity);
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if (!this.isShieldDisabled(this.mob) && this.mob.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK) && this.mob.getRandom().nextInt(6) == 0) {
                    this.mob.startUsingItem(InteractionHand.OFF_HAND);
                }

                if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = livingentity.getX();
                    this.pathedTargetY = livingentity.getY();
                    this.pathedTargetZ = livingentity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    if (this.canPenalize) {
                        this.ticksUntilNextPathRecalculation += this.failedPathFindingPenalty;
                        if (this.mob.getNavigation().getPath() != null) {
                            Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                            if (finalPathPoint != null && livingentity.distanceToSqr((double)finalPathPoint.x, (double)finalPathPoint.y, (double)finalPathPoint.z) < 1.0) {
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

                    if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity, d0);
            }

        }

        public boolean isShieldDisabled(RoyalDraugrServant shieldUser) {
            return shieldUser.isShieldDisabled();
        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                if (this.mob.isDraugrBlocking()) {
                    this.mob.stopUsingItem();
                    this.mob.setShieldCooldownTime(30);
                }

                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(p_25557_);
            }

        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.adjustedTickDelay(20);
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            float f = p_25556_.getBbWidth();
            return (double)(f * 2.25F * f * 2.25F + p_25556_.getBbWidth());
        }
    }
}
