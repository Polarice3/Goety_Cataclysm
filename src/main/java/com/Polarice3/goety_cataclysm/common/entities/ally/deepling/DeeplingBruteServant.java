package com.Polarice3.goety_cataclysm.common.entities.ally.deepling;

import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.projectile.ThrownCoral_Bardiche_Entity;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DeeplingBruteServant extends AbstractDeeplingServant{
    public static final Animation DEEPLING_BRUTE_TRIDENT_THROW = Animation.create(45);
    public static final Animation DEEPLING_BRUTE_MELEE = Animation.create(20);
    private int SpinAttackTicks;
    private static final EntityDataAccessor<Boolean> SPIN_ATTACK = SynchedEntityData.defineId(DeeplingBruteServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(1.3f, 0.7F, false);

    public DeeplingBruteServant(EntityType<? extends AbstractDeeplingServant> entity, Level world) {
        super(entity, world);
        this.moveControl = new DeeplingMoveControl(this, 2.0f);
        this.switchNavigator(false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new DeeplingBruteTridentShoot(this, 1.0D, 15.0F));
        this.goalSelector.addGoal(5, new DeeplingGoToBeachGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new DeeplingSwimUpGoal(this, 1.0D, this.level().getSeaLevel()));
        this.goalSelector.addGoal(3, new AnimationMeleeAttackGoal(this, 1.1f, false));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.29F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.DeeplingBruteDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.DeeplingBruteHealth.get())
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.ARMOR, GCAttributesConfig.DeeplingBruteArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.35D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.DeeplingBruteHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.DeeplingBruteArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.DeeplingBruteDamage.get());
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof DeeplingBruteServant;
    }

    @Override
    public int getSummonLimit(LivingEntity caster) {
        return GCSpellConfig.DeeplingBruteLimit.get();
    }

    @Override
    public int xpReward() {
        return 15;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPIN_ATTACK, false);
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219154_, DifficultyInstance p_219155_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CataclysmItems.CORAL_BARDICHE.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        RandomSource randomsource = p_34088_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_34089_);
        return spawngroupdata;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, DEEPLING_BRUTE_TRIDENT_THROW, DEEPLING_BRUTE_MELEE};
    }

    public boolean getSpinAttack() {
        return this.entityData.get(SPIN_ATTACK);
    }

    public void setSpinAttack(boolean p_211137_1_) {
        this.entityData.set(SPIN_ATTACK, p_211137_1_);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if(this.isAlive()) {
            if (this.getAnimation() == DEEPLING_BRUTE_TRIDENT_THROW) {
                if (target != null) {
                    if (this.getAnimationTick() == 11) {
                        if(this.isInWaterOrRain() && !this.isPassenger()) {
                            double f1 = target.getX() - this.getX();
                            double f2 = target.getY() - this.getY();
                            double f3 = target.getZ() - this.getZ();
                            double f4 = Mth.sqrt((float) (f1 * f1 + f2 * f2 + f3 * f3));
                            float f5 = 2.0F;
                            f1 *= f5 / f4;
                            f2 *= f5 / f4;
                            f3 *= f5 / f4;
                            this.push((double)f1, (double)f2, (double)f3);
                            this.startAutoSpinAttack(20);
                            if (this.onGround()) {
                                this.move(MoverType.SELF, new Vec3(0.0D, (double)1.1999999F, 0.0D));
                            }
                            this.playSound(SoundEvents.TRIDENT_RIPTIDE_3,1.0F, 1.0F);
                            ItemHelper.hurtAndBreak(this.getMainHandItem(), 1, this);
                        } else {
                            ThrownCoral_Bardiche_Entity throwntrident = new ThrownCoral_Bardiche_Entity(this.level(), this, new ItemStack(CataclysmItems.CORAL_BARDICHE.get()));
                            double p0 = target.getX() - this.getX();
                            double p1 = target.getY(0.3333333333333333D) - throwntrident.getY();
                            double p2 = target.getZ() - this.getZ();
                            double p3 = Math.sqrt(p0 * p0 + p2 * p2);
                            throwntrident.shoot(p0, p1 + p3 * (double) 0.2F, p2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
                            this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                            if (this.level().addFreshEntity(throwntrident)){
                                ItemHelper.hurtAndBreak(this.getMainHandItem(), 1, this);
                            }
                        }
                    }
                }
            }
            if (this.getAnimation() == DEEPLING_BRUTE_MELEE) {
                if (this.getAnimationTick() == 5) {
                    this.playSound(CataclysmSounds.DEEPLING_SWING.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                    if (target != null) {
                        if (this.distanceTo(target) < 3.0F) {
                            float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                            if (target.hurt(this.getServantAttack(), damage)) {
                                ItemHelper.hurtAndBreak(this.getMainHandItem(), 1, this);
                            }
                        }
                    }
                }
            }
        }
        AABB aabb = this.getBoundingBox();
        if (this.SpinAttackTicks > 0) {
            --this.SpinAttackTicks;
            this.checkAutoSpinAttack(aabb, this.getBoundingBox());
        }

    }


    public void startAutoSpinAttack(int p_204080_) {
        this.SpinAttackTicks = p_204080_;
        if (!this.level().isClientSide) {
            this.setSpinAttack(true);
        }

    }

    protected void checkAutoSpinAttack(AABB p_21072_, AABB p_21073_) {
        AABB aabb = p_21072_.minmax(p_21073_);
        List<Entity> list = this.level().getEntities(this, aabb);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof LivingEntity && !MobUtil.areAllies(this, entity)) {
                    entity.hurt(this.getServantAttack(), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.SpinAttackTicks = 0;
                    this.setDeltaMovement(this.getDeltaMovement().scale(-0.2D));
                    break;
                }
            }
        } else if (this.horizontalCollision) {
            this.SpinAttackTicks = 0;
        }

        if (!this.level().isClientSide && this.SpinAttackTicks <= 0) {
            this.setSpinAttack(false);
        }

    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.9F;
    }

    public AABB getSwimmingBox() {
        return new AABB(this.getX()- 1.3f, this.getY(), this.getZ() -1.3f,  this.getX() + 1.3f, this.getY()+ 0.7f, this.getZ() + 1.3f);
    }

    public AABB getNormalBox() {
        return new AABB(this.getX()- 0.7f, this.getY(), this.getZ() - 0.7f,  this.getX() + 0.7f, this.getY()+ 2.6f, this.getZ() + 0.7f);
    }

    public EntityDimensions getSwimmingSize() {
        return SWIMMING_SIZE;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ItemStack itemstack2 = this.getMainHandItem();
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (itemstack.is(CataclysmItems.CORAL_BARDICHE.get())) {
                this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.copy());
                this.dropEquipment(EquipmentSlot.MAINHAND, itemstack2);
                this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    static class DeeplingBruteTridentShoot extends Goal {
        private final DeeplingBruteServant mob;
        private final double moveSpeedAmp;
        private int attackCooldown;
        private final float maxAttackDistance;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;


        public DeeplingBruteTridentShoot(DeeplingBruteServant mob, double moveSpeedAmpIn, float maxAttackDistanceIn) {
            this.mob = mob;
            this.moveSpeedAmp = moveSpeedAmpIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            return livingentity != null && livingentity.isAlive() && this.mob.getMainHandItem().is(CataclysmItems.CORAL_BARDICHE.get()) && this.mob.distanceToSqr(livingentity) >= 36.0D;
        }


        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) ;
        }

        public void start() {
            super.start();
            this.mob.setAggressive(true);
            this.mob.startUsingItem(InteractionHand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
            this.seeTime = 0;
            this.attackTime = -1;
            this.mob.stopUsingItem();
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();

            if (livingentity != null) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }

                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                    this.mob.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mob.getNavigation().moveTo(livingentity, this.moveSpeedAmp);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (d0 > (double)(this.maxAttackDistance * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (d0 < (double)(this.maxAttackDistance * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mob.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }
                if (!flag && this.seeTime < -60) {
                    this.mob.stopUsingItem();
                } else if (flag) {
                    if (this.mob.getAnimation() != DEEPLING_BRUTE_TRIDENT_THROW){
                        this.mob.setAnimation(DEEPLING_BRUTE_TRIDENT_THROW);
                        this.attackTime = this.attackCooldown;
                    }
                }
            }
        }
    }

    static class AnimationMeleeAttackGoal extends MeleeAttackGoal {
        protected final DeeplingBruteServant mob;

        public AnimationMeleeAttackGoal(DeeplingBruteServant p_25552_, double p_25553_, boolean p_25554_) {
            super(p_25552_,p_25553_,p_25554_);
            this.mob = p_25552_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            return (double)(this.mob.getBbWidth() * 2.5F * this.mob.getBbWidth() * 2.5F + p_25556_.getBbWidth());
        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.mob.getAnimation() == NO_ANIMATION) {
                this.mob.setAnimation(DEEPLING_BRUTE_MELEE);
            }

        }
    }
}
