package com.Polarice3.goety_cataclysm.common.entities.ally.deepling;

import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class DeeplingAnglerServant extends AbstractDeeplingServant {
    public static final Animation DEEPLING_MELEE = Animation.create(20);
    public static final Animation DEEPLING_HUG = Animation.create(20);
    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(1.225f, 0.65F, false);
    private int hugcooldown = 100;
    public int noLionfish = GCMobsConfig.DeeplingAnglerCatchTime.get();
    public static final int HUG_COOLDOWN = 100;

    public DeeplingAnglerServant(EntityType<? extends AbstractDeeplingServant> entity, Level world) {
        super(entity, world);
        this.moveControl = new DeeplingMoveControl(this, 2.0f);
        this.switchNavigator(false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new AnimationMeleeAttackGoal(this, 1.0f, false));
        this.goalSelector.addGoal(5, new DeeplingGoToBeachGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new DeeplingSwimUpGoal(this, 1.0D, this.level().getSeaLevel()));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.27F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("NoLionfish", this.getNoLionfish());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("NoLionfish")){
            this.setNoLionfish(compound.getInt("NoLionfish"));
        }
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof DeeplingAnglerServant;
    }

    @Override
    public int getSummonLimit(LivingEntity caster) {
        return GCSpellConfig.DeeplingLimit.get();
    }

    @Override
    public int xpReward() {
        return 8;
    }

    public int getNoLionfish() {
        return this.noLionfish;
    }

    public void setNoLionfish(int noLionfish) {
        this.noLionfish = noLionfish;
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219154_, DifficultyInstance p_219155_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        RandomSource randomsource = pLevel.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        LionfishServant fish = GCEntityType.LIONFISH.get().create(level());
        if (fish != null) {
            fish.setTrueOwner(this);
            fish.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
            fish.copyPosition(this);
            fish.setLeashedTo(this, true);
            if (this.hasLifespan()){
                fish.setLimitedLife(this.getLifespan());
            }
            pLevel.addFreshEntity(fish);
        }
        return spawngroupdata;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, DEEPLING_MELEE,DEEPLING_HUG};
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (this.hugcooldown > 0) {
            this.hugcooldown--;
        }
        if (this.getNoLionfish() > 0 && this.isInWater() && this.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.FISHING_ROD)){
            this.noLionfish--;
        }

        if(this.isAlive()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                if (this.getNoLionfish() <= 0 && this.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.FISHING_ROD)) {
                    LionfishServant fish = GCEntityType.LIONFISH.get().create(level());
                    if (fish != null) {
                        fish.setTrueOwner(this);
                        fish.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        fish.copyPosition(this);
                        fish.setLeashedTo(this, true);
                        if (this.hasLifespan()){
                            fish.setLimitedLife(this.getLifespan());
                        }
                        if (serverLevel.addFreshEntity(fish)) {
                            ItemHelper.hurtAndBreak(this.getMainHandItem(), 5, this);
                            this.playSound(SoundEvents.FISHING_BOBBER_RETRIEVE, 1.0F, 0.4F / (this.level().getRandom().nextFloat() * 0.4F + 0.8F));
                            this.setNoLionfish(GCMobsConfig.DeeplingAnglerCatchTime.get());
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
                            if (target.hurt(this.getMobAttack(), damage)) {
                                ItemHelper.hurtAndBreak(this.getMainHandItem(), 1, this);
                            }
                        }
                    }
                }
            }
            if (this.getAnimation() == DEEPLING_HUG) {
                if (this.getAnimationTick() == 9) {
                    this.playSound(CataclysmSounds.DEEPLING_SWING.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
                    if (target != null) {
                        if (this.distanceTo(target) < 3.0F) {
                            float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                            boolean flag = target.hurt(this.getMobAttack(), damage);
                            if (flag) {
                                target.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1), this);
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

    public AABB getSwimmingBox() {
        return new AABB(this.getX()- 1.225f, this.getY(), this.getZ() -1.225f,  this.getX() + 1.225f, this.getY()+ 0.65f, this.getZ() + 1.225f);
    }

    public AABB getNormalBox() {
        return new AABB(this.getX()- 0.65f, this.getY(), this.getZ() -0.65f,  this.getX() + 0.65f, this.getY()+ 2.45f, this.getZ() + 0.65f);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ItemStack itemstack2 = this.getMainHandItem();
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (itemstack.is(Items.FISHING_ROD)) {
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

    static class AnimationMeleeAttackGoal extends MeleeAttackGoal {
        protected final DeeplingAnglerServant mob;

        public AnimationMeleeAttackGoal(DeeplingAnglerServant p_25552_, double p_25553_, boolean p_25554_) {
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
                if( this.mob.hugcooldown <= 0){
                    this.mob.setAnimation(DEEPLING_HUG);
                    this.mob.hugcooldown = HUG_COOLDOWN;
                }else{
                    this.mob.setAnimation(DEEPLING_MELEE);
                }
            }

        }
    }
}
