package com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.AnimationSummon2;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.projectile.Poison_Dart_Entity;
import com.github.L_Ender.cataclysm.init.ModTag;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class KoboletonServant extends AnimationSummon2 {
    public static final Animation COBOLETON_ATTACK = Animation.create(19);
    public float angryProgress;
    public float prevangryProgress;

    public KoboletonServant(EntityType<? extends AnimationSummon2> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.25F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, COBOLETON_ATTACK};
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AnimationMeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WanderGoal<>(this, 1.0F, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.KoboletonDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.KoboletonHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.KoboletonArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.KoboletonHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.KoboletonArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.KoboletonDamage.get());
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getDirectEntity();
        if (entity instanceof Poison_Dart_Entity) {
            return false;
        }
        return super.hurt(source, damage);
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof KoboletonServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.KoboletonLimit.get();
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    @Override
    public int xpReward() {
        return 8;
    }

    protected SoundEvent getAmbientSound() {
        return CataclysmSounds.KOBOLETON_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.KOBOLETON_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.KOBOLETON_DEATH.get();
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(CataclysmSounds.KOBOLETON_STEP.get(), 0.15F, 0.6F);
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219154_, DifficultyInstance p_219155_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CataclysmItems.KHOPESH.get()));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        RandomSource randomsource = p_34088_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_34089_);
        return spawngroupdata;
    }

    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        prevangryProgress = angryProgress;
        if (this.isAggressive() && angryProgress < 10F) {
            angryProgress++;
        }
        if (!this.isAggressive() && angryProgress > 0F) {
            angryProgress--;
        }
        LivingEntity target = this.getTarget();
        if(this.isAlive()) {
            if (this.getAnimation() == COBOLETON_ATTACK) {
                if (this.getAnimationTick() == 11) {
                    this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                    if (target != null) {
                        if (this.distanceTo(target) < this.getBbWidth() * 2.5F * this.getBbWidth() * 2.5F + target.getBbWidth()) {
                            float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                            target.hurt(this.getServantAttack(), damage);

                            ItemStack offhand = target.getOffhandItem();
                            ItemStack mainhand = target.getMainHandItem();
                            boolean hasGloves = !CuriosFinder.findCurio(target, CataclysmItems.STICKY_GLOVES.get()).isEmpty();
                            if (target instanceof Player && this.random.nextFloat() * 100.0F <= CMConfig.CauseKoboletontoDropItemInHandPercent) {
                                if (hasGloves) {
                                    if (!offhand.isEmpty()) {
                                        if (!offhand.is(ModTag.STICKY_ITEM)) {
                                            int i = offhand.getCount();
                                            this.koboletonStealDrop(offhand.copyWithCount(1), target);
                                            target.setItemSlot(EquipmentSlot.OFFHAND, offhand.split(i - 1));
                                        }
                                    } else {
                                        if (!mainhand.isEmpty()) {
                                            if (!mainhand.is(ModTag.STICKY_ITEM)) {
                                                int i = mainhand.getCount();
                                                this.koboletonStealDrop(mainhand.copyWithCount(1), target);
                                                target.setItemSlot(EquipmentSlot.MAINHAND, mainhand.split(i - 1));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ItemEntity koboletonStealDrop(ItemStack p_36179_, LivingEntity target) {
        if (p_36179_.isEmpty()) {
            return null;
        } else if (this.level().isClientSide) {
            return null;
        } else {
            double d0 = target.getEyeY() - (double)0.3F;
            ItemEntity itementity = new ItemEntity(target.level(), target.getX(), d0, target.getZ(), p_36179_);
            itementity.setDefaultPickUpDelay();
            itementity.setExtendedLifetime();
            float f8 = Mth.sin(target.getXRot() * ((float)Math.PI / 180F));
            float f2 = Mth.cos(target.getXRot() * ((float)Math.PI / 180F));
            float f3 = Mth.sin(target.getYRot() * ((float)Math.PI / 180F));
            float f4 = Mth.cos(target.getYRot() * ((float)Math.PI / 180F));
            float f5 = target.getRandom().nextFloat() * ((float)Math.PI * 2F);
            float f6 = 0.02F * target.getRandom().nextFloat();
            itementity.setDeltaMovement((double)(-f3 * f2 * 0.3F) + Math.cos((double)f5) * (double)f6, (double)(-f8 * 0.3F + 0.1F + (target.getRandom().nextFloat() - target.getRandom().nextFloat()) * 0.1F), (double)(f4 * f2 * 0.3F) + Math.sin((double)f5) * (double)f6);
            this.level().addFreshEntity(itementity);
            return itementity;
        }

    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if ((itemstack.is(Tags.Items.BONES) || itemstack.is(CataclysmItems.KOBOLETON_BONE.get())) && this.getHealth() < this.getMaxHealth()) {
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

    static class AnimationMeleeAttackGoal extends MeleeAttackGoal {
        protected final KoboletonServant mob;

        public AnimationMeleeAttackGoal(KoboletonServant p_25552_, double p_25553_, boolean p_25554_) {
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
                this.mob.setAnimation(COBOLETON_ATTACK);
            }

        }
    }
}
