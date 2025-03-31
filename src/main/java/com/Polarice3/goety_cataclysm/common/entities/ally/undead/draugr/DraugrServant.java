package com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Predicate;

public class DraugrServant extends Summoned {
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState attack2AnimationState = new AnimationState();

    public DraugrServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new DraugrMeleeAttackGoal());
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.ARMOR, 3.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.05D);
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof DraugrServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.DraugrLimit.get();
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
        return super.doHurtTarget(p_219472_);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        return spawngroupdata;
    }

    private ItemStack createSpawnWeapon() {
        return this.random.nextBoolean() ? new ItemStack(CataclysmItems.BLACK_STEEL_AXE.get()) : new ItemStack(CataclysmItems.BLACK_STEEL_SWORD.get());
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(true, this.tickCount);
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
            }
        }
        return InteractionResult.PASS;
    }

    class DraugrMeleeAttackGoal extends MeleeAttackGoal {
        public DraugrMeleeAttackGoal() {
            super(DraugrServant.this, 1.0, true);
        }

        protected double getAttackReachSqr(LivingEntity p_33377_) {
            float f = DraugrServant.this.getBbWidth();
            return (double)(f * 2.25F * f * 2.25F + p_33377_.getBbWidth());
        }
    }
}
