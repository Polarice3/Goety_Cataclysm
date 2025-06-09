package com.Polarice3.goety_cataclysm.common.entities.ally.deepling;

import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.github.L_Ender.cataclysm.entity.AI.AnimalAIRandomSwimming;
import com.github.L_Ender.cataclysm.entity.etc.AquaticMoveController;
import com.github.L_Ender.cataclysm.entity.etc.path.SemiAquaticPathNavigator;
import com.github.L_Ender.cataclysm.entity.projectile.Lionfish_Spike_Entity;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class LionfishServant extends Summoned implements IAnimatedEntity {
    public static final Animation LIONFISH_BITE = Animation.create(19);
    private int animationTick;
    private Animation currentAnimation;
    public float prevOnLandProgress;
    public float onLandProgress;
    public float LayerBrightness, oLayerBrightness;
    public int LayerTicks;

    public LionfishServant(EntityType<? extends Summoned> monster, Level level) {
        super(monster, level);
        this.moveControl = new AquaticMoveController(this, 1.0F, 15F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new SemiAquaticPathNavigator(this, worldIn);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PUFFER_FISH_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_29628_) {
        return SoundEvents.PUFFER_FISH_HURT;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.PUFFER_FISH_FLOP;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(2, new AnimationMeleeAttackGoal(this, 1.0f, false));
        this.goalSelector.addGoal(4, new AnimalAIRandomSwimming(this, 1F, 12, 5){
            @Nullable
            protected Vec3 getPosition() {
                return LionfishServant.this.isGuardingArea() ? this.randomBoundPos() : super.getPosition();
            }

            public Vec3 randomBoundPos() {
                Vec3 vec3 = null;
                int range = IServant.GUARDING_RANGE / 2;

                for(int i = 0; i < 10; ++i) {
                    BlockPos blockPos = LionfishServant.this.getBoundPos().offset(LionfishServant.this.getRandom().nextIntBetweenInclusive(-range, range), LionfishServant.this.getRandom().nextIntBetweenInclusive(-range, range), LionfishServant.this.getRandom().nextIntBetweenInclusive(-range, range));
                    if (LionfishServant.this.getNavigation() instanceof WaterBoundPathNavigation) {
                        if (GoalUtils.isWater(LionfishServant.this, blockPos)) {
                            vec3 = Vec3.atBottomCenterOf(blockPos);
                            break;
                        }
                    } else {
                        BlockPos blockPos1 = LandRandomPos.movePosUpOutOfSolid(LionfishServant.this, blockPos);
                        if (blockPos1 != null) {
                            vec3 = Vec3.atBottomCenterOf(blockPos1);
                            break;
                        }
                    }
                }

                return vec3;
            }

            public boolean canUse() {
                if (!super.canUse()) {
                    return false;
                } else {
                    return !LionfishServant.this.isStaying() && !LionfishServant.this.isCommanded() || LionfishServant.this.getTrueOwner() == null;
                }
            }
        });
    }

    @Override
    public void followGoal() {
        this.goalSelector.addGoal(3, new FollowOwnerWaterGoal(this, 1.0, 10.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, 12.0D);
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.45F;
    }

    @Override
    public boolean canUpdateMove() {
        return true;
    }

    public boolean hurt(DamageSource p_32820_, float p_32821_) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (!p_32820_.is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !p_32820_.is(DamageTypes.THORNS)) {
                Entity entity = p_32820_.getDirectEntity();
                if (entity instanceof LivingEntity livingentity) {
                    if (livingentity.hurt(damageSources().thorns(this), 1.0F)) {
                        livingentity.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0), this);
                    }
                }

            }

            return super.hurt(p_32820_, p_32821_);
        }
    }

    public void tick(){
        super.tick();
        this.prevOnLandProgress = onLandProgress;
        if (!this.isInWater() && onLandProgress < 5F) {
            onLandProgress++;
        }
        if (this.isInWater() && onLandProgress > 0F) {
            onLandProgress--;
        }

        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.setOnGround(false);
            this.hasImpulse = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }
        AnimationHandler.INSTANCE.updateAnimations(this);

        if (this.level().isClientSide){
            ++LayerTicks;
            this.LayerBrightness += (0.0F - this.LayerBrightness) * 0.8F;
        }
        if (this.isAlive()) {
            if (this.getLeashHolder() instanceof DeeplingAnglerServant anglerServant){
                anglerServant.setNoLionfish(GCMobsConfig.DeeplingAnglerCatchTime.get());
            }
            LivingEntity target = this.getTarget();
            if (this.getAnimation() == LIONFISH_BITE) {
                if (this.getAnimationTick() == 7) {
                    this.playSound(SoundEvents.PHANTOM_BITE, 0.4F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                    if (target != null) {
                        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        target.hurt(this.getMobAttack(), damage);
                    }
                }
            }
        }
    }

    public DamageSource getMobAttack(){
        return this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this);
    }

    protected void handleAirSupply(int p_30344_) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(p_30344_ - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(damageSources().drown(), 0.01F);
            }
        } else {
            this.setAirSupply(1000);
        }

    }

    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(i);
    }

    public void dropLeash(boolean p_21456_, boolean p_21457_) {
        super.dropLeash(p_21456_, !(this.getLeashHolder() instanceof OwnableEntity) && p_21457_);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        int shardCount = 6 + random.nextInt(2);
        if (!this.level().isClientSide) {
            for (int i = 0; i < shardCount; i++) {
                float f = ((i + 1) / (float) shardCount) * 360F;
                Lionfish_Spike_Entity shard = new Lionfish_Spike_Entity(this.level(), this);
                shard.shoot(this.random.nextFloat() * 0.4F * 2.0F - 0.4F, this.random.nextFloat() * 0.25F + 0.1F,this.random.nextFloat() * 0.4F * 2.0F - 0.4F, 0.35F, 1F);
                this.level().addFreshEntity(shard);
            }
        }

    }

    public boolean isPushedByFluid() {
        return false;
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    private boolean isLeashedToAngler() {
        return this.getLeashHolder() instanceof DeeplingAnglerServant;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        animationTick = i;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{LIONFISH_BITE,NO_ANIMATION};
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
        if (itemstack.is(Tags.Items.SHEARS) && this.isLeashed() && this.getLeashHolder() instanceof OwnableEntity ownable && ownable.getOwner() == pPlayer) {
            this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
            this.dropLeash(true, false);
            this.setTrueOwner(pPlayer);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    static class AnimationMeleeAttackGoal extends MeleeAttackGoal {
        protected final LionfishServant mob;

        public AnimationMeleeAttackGoal(LionfishServant p_25552_, double p_25553_, boolean p_25554_) {
            super(p_25552_,p_25553_,p_25554_);
            this.mob = p_25552_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.mob.getAnimation() == NO_ANIMATION) {
                this.mob.setAnimation(LIONFISH_BITE);
            }
        }
    }
}
