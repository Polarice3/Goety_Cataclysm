package com.Polarice3.goety_cataclysm.common.entities.ally.golem;

import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.goety_cataclysm.common.entities.ally.AnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.LLibraryBossSummon;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.AI.CmAttackGoal;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.projectile.Void_Rune_Entity;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class EnderGolemServant extends LLibraryBossSummon {
    private static final EntityDataAccessor<Boolean> IS_AWAKEN = SynchedEntityData.defineId(EnderGolemServant.class, EntityDataSerializers.BOOLEAN);
    public static final Animation ANIMATION_ATTACK1 = Animation.create(25);
    public static final Animation ANIMATION_ATTACK2 = Animation.create(25);
    public static final Animation ANIMATION_EARTHQUAKE = Animation.create(35);
    public static final Animation VOID_RUNE_ATTACK = Animation.create(83);
    public static final Animation ENDER_GOLEM_DEATH = Animation.create(95);
    private int void_rune_attack_cooldown = 0;
    public float deactivateProgress;
    public float prevDeactivateProgress;
    public int wakeUp = 0;

    public EnderGolemServant(EntityType<? extends AnimationSummon> type, Level worldIn) {
        super(type, worldIn);
        this.setMaxUpStep(1.5F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        setConfigAttribute(this, CMConfig.EnderGolemHealthMultiplier, CMConfig.EnderGolemDamageMultiplier);
    }

    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_ATTACK1, ANIMATION_ATTACK2, ANIMATION_EARTHQUAKE, VOID_RUNE_ATTACK, ENDER_GOLEM_DEATH};
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new AwakenGoal());
        this.goalSelector.addGoal(1, new AttackGoal());
        this.goalSelector.addGoal(2, new CmAttackGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, 10.0F)
                .add(Attributes.MAX_HEALTH, 150.0F)
                .add(Attributes.ARMOR, 12.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    private static Animation getRandomAttack(RandomSource rand) {
        return switch (rand.nextInt(3)) {
            case 0 -> ANIMATION_ATTACK1;
            case 1 -> ANIMATION_ATTACK2;
            default -> ANIMATION_EARTHQUAKE;
        };
    }

    @Override
    public int xpReward() {
        return 15;
    }

    public boolean hurt(DamageSource source, float damage) {
        if ((this.getAnimation() == VOID_RUNE_ATTACK || !this.getIsAwaken()) && (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !source.is(DamageTypes.MAGIC))) {
            damage = (float)((double)damage * 0.5);
        }

        double range = this.calculateRange(source);
        if (range > CMConfig.EndergolemLongRangelimit * CMConfig.EndergolemLongRangelimit && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            Entity entity = source.getDirectEntity();
            if (entity instanceof AbstractGolem) {
                damage = (float)((double)damage * 0.5);
            }

            return super.hurt(source, damage);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_AWAKEN, false);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("is_Awaken", this.getIsAwaken());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setIsAwaken(compound.getBoolean("is_Awaken"));
    }

    public void setIsAwaken(boolean isAwaken) {
        this.entityData.set(IS_AWAKEN, isAwaken);
    }

    public boolean getIsAwaken() {
        return this.entityData.get(IS_AWAKEN);
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof EnderGolemServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.EnderGolemLimit.get();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.MOB_SUMMONED){
            this.setIsAwaken(false);
            this.wakeUp = 20;
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void tick() {
        super.tick();
        this.repelEntities(1.7F, 3.7F, 1.7F, 1.7F);
        LivingEntity target = this.getTarget();
        this.prevDeactivateProgress = this.deactivateProgress;
        if (!this.getIsAwaken() && this.deactivateProgress < 30.0F) {
            ++this.deactivateProgress;
        }

        if (this.getIsAwaken() && this.deactivateProgress > 0.0F) {
            --this.deactivateProgress;
        }

        if (this.deactivateProgress == 0.0F && this.isAlive()) {
            if (target != null && target.isAlive()) {
                if (this.void_rune_attack_cooldown > 0 || this.isNoAi() || this.getAnimation() != NO_ANIMATION || !target.onGround() || (this.random.nextInt(45) != 0 || !(this.distanceTo(target) < 4.0F)) && (this.random.nextInt(24) != 0 || !(this.distanceTo(target) < 10.0F))) {
                    if (this.distanceTo(target) < 4.0F && !this.isNoAi() && this.getAnimation() == NO_ANIMATION) {
                        Animation animation = getRandomAttack(this.random);
                        this.setAnimation(animation);
                    }
                } else {
                    this.void_rune_attack_cooldown = 250;
                    this.setAnimation(VOID_RUNE_ATTACK);
                }
            }

            if (this.getAnimation() == ANIMATION_EARTHQUAKE && this.getAnimationTick() == 19) {
                this.EarthQuake(5.0F, 6);
                this.EarthQuakeParticle();
            }

            if ((this.getAnimation() == ANIMATION_ATTACK1 || this.getAnimation() == ANIMATION_ATTACK2) && this.getAnimationTick() == 13) {
                this.playSound(CataclysmSounds.GOLEMATTACK.get(), 1.0F, 1.0F);
                if (target != null && target.isAlive() && this.distanceTo(target) < 4.75F) {
                    target.hurt(this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) + (float)this.random.nextInt(4));
                    target.knockback(1.25, this.getX() - target.getX(), this.getZ() - target.getZ());
                }
            }

            if (this.getAnimation() == VOID_RUNE_ATTACK) {
                if (this.getAnimationTick() == 22) {
                    this.EarthQuake(4.25F, 4);
                    this.EarthQuakeParticle();
                }

                if (this.getAnimationTick() == 28) {
                    this.VoidRuneAttack();
                }
            }
        }

        if (this.void_rune_attack_cooldown > 0) {
            --this.void_rune_attack_cooldown;
        }

        if (this.wakeUp > 0){
            --this.wakeUp;
        }

        if (!this.level().isClientSide) {
            this.setIsAwaken((!this.isStaying() || this.getTarget() != null) && this.wakeUp <= 0);
        }

    }

    private void EarthQuakeParticle() {
        if (this.level().isClientSide) {
            BlockState block = this.level().getBlockState(this.blockPosition().below());

            for(int i1 = 0; i1 < 20 + this.random.nextInt(12); ++i1) {
                double DeltaMovementX = this.getRandom().nextGaussian() * 0.07;
                double DeltaMovementY = this.getRandom().nextGaussian() * 0.07;
                double DeltaMovementZ = this.getRandom().nextGaussian() * 0.07;
                float angle = 0.017453292F * this.yBodyRot + (float)i1;
                double extraX = 4.0F * Mth.sin((float)(Math.PI + (double)angle));
                double extraY = 0.30000001192092896;
                double extraZ = 4.0F * Mth.cos(angle);
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ, DeltaMovementX, DeltaMovementY, DeltaMovementZ);
                }
            }
        }

    }

    private void EarthQuake(float grow, int damage) {
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1.0F + this.getRandom().nextFloat() * 0.1F);

        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(grow))) {
            if (!MobUtil.areAllies(this, entity)) {
                entity.hurt(this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) + (float) this.random.nextInt(damage));
                this.launch(entity, true);
            }
        }

    }

    private void VoidRuneAttack() {
        LivingEntity target = this.getTarget();
        if (target != null) {
            double d0 = Math.min(target.getY(), this.getY());
            double d1 = Math.max(target.getY(), this.getY()) + 1.0;
            float f = (float)Mth.atan2(target.getZ() - this.getZ(), target.getX() - this.getX());
            float f2 = Mth.cos(this.getYRot() * 0.017453292F) * 2.0F;
            float f3 = Mth.sin(this.getYRot() * 0.017453292F) * 2.0F;

            for (int k = 0; k < 10; ++k) {
                double d2 = 1.5 * (double)(k + 1);
                int j = (int)(1.25F * (float)k);
                this.spawnFangs(this.getX() + (double)f2 + (double)Mth.cos(f) * d2, this.getZ() + (double)f3 + (double)Mth.sin(f) * d2, d0, d1, f, j);
                this.spawnFangs(this.getX() - (double)f2 + (double)Mth.cos(f) * d2, this.getZ() - (double)f3 + (double)Mth.sin(f) * d2, d0, d1, f, j);
            }

            for (int k = 0; k < 6; ++k) {
                float f4 = f + (float)k * 3.1415927F * 2.0F / 6.0F + 0.83775806F;
                this.spawnFangs(this.getX() + (double)Mth.cos(f4) * 2.5, this.getZ() + (double)Mth.sin(f4) * 2.5, d0, d1, f2, 5);
            }

            for (int k = 0; k < 8; ++k) {
                this.spawnFangs(this.getX() + this.random.nextGaussian() * 4.5, this.getZ() + this.random.nextGaussian() * 4.5, d0, d1, f3, 15);
            }
        }

    }

    private void spawnFangs(double x, double z, double minY, double maxY, float rotation, int delay) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0;

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
        } while(blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            this.level().addFreshEntity(new Void_Rune_Entity(this.level(), x, (double)blockpos.getY() + d0, z, rotation, delay, (float)CMConfig.Voidrunedamage, this));
        }

    }

    private void launch(LivingEntity e, boolean huge) {
        double d0 = e.getX() - this.getX();
        double d1 = e.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        float f = huge ? 2.0F : 0.5F;
        e.push(d0 / d2 * (double)f, huge ? 0.5 : 0.20000000298023224, d1 / d2 * (double)f);
    }

    protected void onDeathAIUpdate() {
        super.onDeathAIUpdate();
        this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
        if (this.deathTime == 40) {
            this.playSound(CataclysmSounds.MONSTROSITYLAND.get(), 1.0F, 1.0F);
        }

    }

    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    @Nullable
    public Animation getDeathAnimation() {
        return ENDER_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.GOLEMHURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.GOLEMDEATH.get();
    }

    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        if (!this.level().isClientSide) {
            ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner() && ((itemstack.is(CataclysmItems.VOID_STONE.get()) || itemstack.is(CataclysmItems.VOID_CORE.get())) && this.getHealth() < this.getMaxHealth())) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (itemstack.is(CataclysmItems.VOID_CORE.get())) {
                    this.heal(this.getMaxHealth() / 2.0F);
                    this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.5F);
                }

                if (itemstack.is(CataclysmItems.VOID_STONE.get())) {
                    this.heal(this.getMaxHealth() / 4.0F);
                    this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.5F);
                }

                if (this.level() instanceof ServerLevel serverLevel) {
                    for(int i = 0; i < 7; ++i) {
                        double d0 = serverLevel.random.nextGaussian() * 0.02;
                        double d1 = serverLevel.random.nextGaussian() * 0.02;
                        double d2 = serverLevel.random.nextGaussian() * 0.02;
                        serverLevel.sendParticles(new DustParticleOptions(Vec3.fromRGB24(0x44155a).toVector3f(), 1.0F), this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), 0, d0, d1, d2, 0.5);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }

    class AttackGoal extends Goal {
        public AttackGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean canUse() {
            return EnderGolemServant.this.getAnimation() == EnderGolemServant.ANIMATION_EARTHQUAKE || EnderGolemServant.this.getAnimation() == EnderGolemServant.VOID_RUNE_ATTACK;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void stop() {
            super.stop();
        }

        public void tick() {
            EnderGolemServant.this.setDeltaMovement(0.0, EnderGolemServant.this.getDeltaMovement().y, 0.0);
            LivingEntity target = EnderGolemServant.this.getTarget();
            if (EnderGolemServant.this.getAnimation() == EnderGolemServant.ANIMATION_EARTHQUAKE) {
                if (EnderGolemServant.this.getAnimationTick() < 19 && target != null) {
                    EnderGolemServant.this.getLookControl().setLookAt(target, 30.0F, 30.0F);
                    EnderGolemServant.this.lookAt(target, 30.0F, 30.0F);
                } else {
                    EnderGolemServant.this.setYRot(EnderGolemServant.this.yRotO);
                }
            }

            if (EnderGolemServant.this.getAnimation() == EnderGolemServant.VOID_RUNE_ATTACK) {
                if (EnderGolemServant.this.getAnimationTick() < 22 && target != null) {
                    EnderGolemServant.this.getLookControl().setLookAt(target, 30.0F, 30.0F);
                    EnderGolemServant.this.lookAt(target, 30.0F, 30.0F);
                } else {
                    EnderGolemServant.this.setYRot(EnderGolemServant.this.yRotO);
                }
            }

        }
    }

    class AwakenGoal extends Goal {
        public AwakenGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean canUse() {
            return EnderGolemServant.this.deactivateProgress > 0.0F;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            EnderGolemServant.this.setDeltaMovement(0.0, EnderGolemServant.this.getDeltaMovement().y, 0.0);
        }
    }
}
