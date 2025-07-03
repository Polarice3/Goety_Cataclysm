package com.Polarice3.goety_cataclysm.common.entities.ally.deepling;

import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.projectile.ThrownCoral_Spear_Entity;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class DeeplingServant extends AbstractDeeplingServant{
    public static final Animation DEEPLING_TRIDENT_THROW = Animation.create(40);
    public static final Animation DEEPLING_MELEE = Animation.create(20);
    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(1.15f, 0.6f, false);

    public DeeplingServant(EntityType<? extends AbstractDeeplingServant> entity, Level world) {
        super(entity, world);
        this.moveControl = new DeeplingMoveControl(this, 2.0f);
        this.switchNavigator(false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new DeeplingTridentShoot(this, 0.8D, 10.0F));
        this.goalSelector.addGoal(5, new DeeplingGoToBeachGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new DeeplingSwimUpGoal(this, 1.0D, this.level().getSeaLevel()));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(3, new AnimationMeleeAttackGoal(this, 1.0f, false));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.27F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.DeeplingDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.DeeplingHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.DeeplingArmor.get())
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.DeeplingHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.DeeplingArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.DeeplingDamage.get());
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof DeeplingServant;
    }

    @Override
    public int getSummonLimit(LivingEntity caster) {
        return GCSpellConfig.DeeplingLimit.get();
    }

    @Override
    public int xpReward() {
        return 8;
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219154_, DifficultyInstance p_219155_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CataclysmItems.CORAL_SPEAR.get()));
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
        return new Animation[]{NO_ANIMATION, DEEPLING_TRIDENT_THROW, DEEPLING_MELEE};
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if(this.isAlive()) {
            if (this.getAnimation() == DEEPLING_TRIDENT_THROW) {
                if (target != null) {
                    if (this.getAnimationTick() == 11) {
                        ThrownCoral_Spear_Entity throwntrident = new ThrownCoral_Spear_Entity(this.level(), this, new ItemStack(CataclysmItems.CORAL_SPEAR.get()));
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
            if (this.getAnimation() == DEEPLING_MELEE) {
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
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.9F;
    }

    public EntityDimensions getSwimmingSize() {
        return SWIMMING_SIZE;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ItemStack itemstack2 = this.getMainHandItem();
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (itemstack.is(CataclysmItems.CORAL_SPEAR.get())) {
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

    static class DeeplingTridentShoot extends Goal {
        private final DeeplingServant mob;
        private final double moveSpeedAmp;
        private int attackCooldown;
        private final float maxAttackDistance;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public DeeplingTridentShoot(DeeplingServant mob, double moveSpeedAmpIn, float maxAttackDistanceIn) {
            this.mob = mob;
            this.moveSpeedAmp = moveSpeedAmpIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            return livingentity != null && livingentity.isAlive() && this.mob.getMainHandItem().is(CataclysmItems.CORAL_SPEAR.get()) && this.mob.distanceToSqr(livingentity) >= 36.0D;
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
                    if (this.mob.getAnimation() != DEEPLING_TRIDENT_THROW){
                        this.mob.setAnimation(DEEPLING_TRIDENT_THROW);
                        this.attackTime = this.attackCooldown;
                    }
                }
            }
        }
    }

    static class AnimationMeleeAttackGoal extends MeleeAttackGoal {
        protected final DeeplingServant mob;

        public AnimationMeleeAttackGoal(DeeplingServant p_25552_, double p_25553_, boolean p_25554_) {
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
                this.mob.setAnimation(DEEPLING_MELEE);
            }

        }
    }
}
