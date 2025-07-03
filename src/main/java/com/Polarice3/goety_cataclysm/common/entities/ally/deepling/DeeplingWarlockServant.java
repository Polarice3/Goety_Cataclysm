package com.Polarice3.goety_cataclysm.common.entities.ally.deepling;

import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.SimpleSummonAnimationGoal;
import com.Polarice3.goety_cataclysm.common.entities.util.AbyssMark;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.config.CMConfig;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class DeeplingWarlockServant extends AbstractDeeplingServant{
    public static final Animation DEEPLING_MELEE = Animation.create(20);
    public static final Animation DEEPLING_MAGIC = Animation.create(90);
    private int lightcooldown = 200;
    public static final int LIGHT_COOLDOWN = 400;
    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(1.15f, 0.6F, false);

    public DeeplingWarlockServant(EntityType<? extends AbstractDeeplingServant> entity, Level world) {
        super(entity, world);
        this.switchNavigator(false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new MagicTrackingGoal(this,DEEPLING_MAGIC));
        this.goalSelector.addGoal(2, new DeeplingMagicGoal(this));
        this.goalSelector.addGoal(3, new AnimationMeleeAttackGoal(this, 1.0f, false));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.27F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.DeeplingPriestDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.DeeplingPriestHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.DeeplingPriestArmor.get())
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.DeeplingPriestHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.DeeplingPriestArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.DeeplingPriestDamage.get());
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof DeeplingWarlockServant;
    }

    @Override
    public int getSummonLimit(LivingEntity caster) {
        return GCSpellConfig.DeeplingCasterLimit.get();
    }

    @Override
    public int xpReward() {
        return 10;
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219154_, DifficultyInstance p_219155_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CataclysmItems.ATHAME.get()));
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
        return new Animation[]{NO_ANIMATION, DEEPLING_MELEE,DEEPLING_MAGIC};
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (this.lightcooldown > 0) {
            this.lightcooldown--;
        }

        if (this.isAlive()) {
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
            if (itemstack.is(CataclysmItems.ATHAME.get())) {
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

    static class DeeplingMagicGoal extends Goal {
        private final DeeplingWarlockServant warlock;

        public DeeplingMagicGoal(DeeplingWarlockServant warlock) {
            this.warlock = warlock;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity target = this.warlock.getTarget();
            return this.warlock.lightcooldown <= 0 && this.warlock.getAnimation() == NO_ANIMATION && target != null && this.warlock.distanceToSqr(target) >= 25.0D && target.isAlive() && this.warlock.getRandom().nextFloat() * 100.0F < 12f;
        }

        public void start() {
            super.start();
            this.warlock.setAnimation(DEEPLING_MAGIC);
            this.warlock.lightcooldown = LIGHT_COOLDOWN;
            this.warlock.navigation.stop();
        }

        public void stop() {
            super.stop();
        }
    }

    static class MagicTrackingGoal extends SimpleSummonAnimationGoal<DeeplingWarlockServant> {
        private final DeeplingWarlockServant warlock;

        public MagicTrackingGoal(DeeplingWarlockServant entity, Animation animation) {
            super(entity, animation);
            this.warlock = entity;
            this.setFlags(EnumSet.of(Flag.MOVE,Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public void start() {
            LivingEntity target = this.warlock.getTarget();
            if (target != null) {
                this.warlock.getLookControl().setLookAt(target, 30, 30);
            }
            super.start();
        }

        public void tick() {
            LivingEntity target = this.warlock.getTarget();
            if (target != null) {
                this.warlock.getLookControl().setLookAt(target, 30, 30);

                if(this.warlock.getAnimationTick() == 18) {
                    double sx = this.warlock.getX();
                    double sy = this.warlock.getY();
                    double sz = this.warlock.getZ();
                    AbyssMark fireball = new AbyssMark(this.warlock.level(), sx,sy,sz,80,(float) CMConfig.AbyssBlastdamage,(float)CMConfig.AbyssBlastHpdamage,this.warlock.getUUID(),target);
                    this.warlock.level().addFreshEntity(fireball);
                }

            }
        }
    }


    static class AnimationMeleeAttackGoal extends MeleeAttackGoal {
        protected final DeeplingWarlockServant mob;

        public AnimationMeleeAttackGoal(DeeplingWarlockServant p_25552_, double p_25553_, boolean p_25554_) {
            super(p_25552_,p_25553_,p_25554_);
            this.mob = p_25552_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }


        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.mob.getAnimation() == NO_ANIMATION) {
                this.mob.setAnimation(DEEPLING_MELEE);
            }

        }
    }
}
