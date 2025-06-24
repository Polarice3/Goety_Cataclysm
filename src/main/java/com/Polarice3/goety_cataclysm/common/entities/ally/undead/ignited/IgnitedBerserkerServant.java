package com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.projectiles.FlyingItem;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.common.items.revive.IgnitedSoul;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class IgnitedBerserkerServant extends InternalAnimationSummon {
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState xslashAnimationState = new AnimationState();
    public AnimationState mixerstartAnimationState = new AnimationState();
    public AnimationState mixeridleAnimationState = new AnimationState();
    public AnimationState mixerfinishAnimationState = new AnimationState();
    public AnimationState sworddanceleftAnimationState = new AnimationState();
    public AnimationState sworddancerightAnimationState = new AnimationState();
    private int sword_dance_cooldown = 0;
    public static final int SWORD_DANCE_COOLDOWN = 40;

    private int spin_cooldown = 0;
    public static final int SPIN_COOLDOWN = 80;

    public IgnitedBerserkerServant(EntityType<? extends InternalAnimationSummon> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.25F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WanderGoal<>(this, 1.0F, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new InternalSummonMoveGoal(this, false, 1.0D));

        //x slash
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,1,0,50,15,3.6F){
            @Override
            public boolean canUse() {
                return super.canUse() && IgnitedBerserkerServant.this.getRandom().nextFloat() * 100.0F < 12f;
            }

        });

        //sword dance left
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,5,0,50,50,4.5F) {
            @Override
            public boolean canUse() {
                return super.canUse() && IgnitedBerserkerServant.this.getRandom().nextFloat() * 100.0F < 16f && IgnitedBerserkerServant.this.sword_dance_cooldown <= 0;
            }
            @Override
            public void stop() {
                super.stop();
                IgnitedBerserkerServant.this.sword_dance_cooldown = SWORD_DANCE_COOLDOWN;
            }

        });
        //sword dance right
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,6,0,55,55,4.5F){
            @Override
            public boolean canUse() {
                return super.canUse() && IgnitedBerserkerServant.this.getRandom().nextFloat() * 100.0F < 16f && IgnitedBerserkerServant.this.sword_dance_cooldown <= 0;
            }
            @Override
            public void stop() {
                super.stop();
                IgnitedBerserkerServant.this.sword_dance_cooldown = SWORD_DANCE_COOLDOWN;
            }
        });

        //mixer start
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,2,3,30,25,8F){
            @Override
            public boolean canUse() {
                return super.canUse() && IgnitedBerserkerServant.this.getRandom().nextFloat() * 100.0F < 12f && IgnitedBerserkerServant.this.spin_cooldown <= 0;
            }

        });
        //mixer idle
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this,3,3,4,90,0, false));

        //mixer stop
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this,4,4,0,40,0){

            @Override
            public void stop() {
                super.stop();
                IgnitedBerserkerServant.this.spin_cooldown = SPIN_COOLDOWN;
            }
        });
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.IgnitedBerserkerDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.IgnitedBerserkerHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.IgnitedBerserkerArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.IgnitedBerserkerHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.IgnitedBerserkerArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.IgnitedBerserkerDamage.get());
    }

    @Override
    public int xpReward() {
        return 20;
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof IgnitedBerserkerServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.IgnitedBerserkerLimit.get();
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "x_slash")) {
            return this.xslashAnimationState;
        } else if (Objects.equals(input, "mixer_start")) {
            return this.mixerstartAnimationState;
        } else if (Objects.equals(input, "mixer_idle")) {
            return this.mixeridleAnimationState;
        } else if (Objects.equals(input, "mixer_finish")) {
            return this.mixerfinishAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "sword_dance_left")) {
            return this.sworddanceleftAnimationState;
        } else if (Objects.equals(input, "sword_dance_right")) {
            return this.sworddancerightAnimationState;
        }else {
            return new AnimationState();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0 -> this.stopAllAnimationStates();
                case 1 -> {
                    this.stopAllAnimationStates();
                    this.xslashAnimationState.startIfStopped(this.tickCount);
                }
                case 2 -> {
                    this.stopAllAnimationStates();
                    this.mixerstartAnimationState.startIfStopped(this.tickCount);
                }
                case 3 -> {
                    this.stopAllAnimationStates();
                    this.mixeridleAnimationState.startIfStopped(this.tickCount);
                }
                case 4 -> {
                    this.stopAllAnimationStates();
                    this.mixerfinishAnimationState.startIfStopped(this.tickCount);
                }
                case 5 -> {
                    this.stopAllAnimationStates();
                    this.sworddanceleftAnimationState.startIfStopped(this.tickCount);
                }
                case 6 -> {
                    this.stopAllAnimationStates();
                    this.sworddancerightAnimationState.startIfStopped(this.tickCount);
                }
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.xslashAnimationState.stop();
        this.mixerstartAnimationState.stop();
        this.mixeridleAnimationState.stop();
        this.mixerfinishAnimationState.stop();
        this.sworddanceleftAnimationState.stop();
        this.sworddancerightAnimationState.stop();
    }

    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    public void die(DamageSource p_21014_) {
        if (this.getTrueOwner() != null && GCMobsConfig.IgnitedBerserkerSoul.get()) {
            ItemStack itemStack = new ItemStack(GCItems.IGNITED_SOUL.get());
            IgnitedSoul.setOwnerName(this.getTrueOwner(), itemStack);
            IgnitedSoul.setSummon(this, itemStack);
            FlyingItem flyingItem = new FlyingItem(ModEntityType.FLYING_ITEM.get(), this.level(), this.getX(), this.getY(), this.getZ());
            flyingItem.setOwner(this.getTrueOwner());
            flyingItem.setItem(itemStack);
            flyingItem.setParticle(ParticleTypes.FLAME);
            flyingItem.setSecondsCool(30);
            this.level().addFreshEntity(flyingItem);
        }
        super.die(p_21014_);
        this.setAttackState(0);
    }

    public void tick() {
        super.tick();
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!this.walkAnimation.isMoving() && this.getAttackState() == 0, this.tickCount);
        }
        if (sword_dance_cooldown > 0) {
            sword_dance_cooldown--;
        }
        if (spin_cooldown > 0) {
            spin_cooldown--;
        }

        float dis = this.getBbWidth() * 0.75F;
        this.repelEntities(dis, this.getBbHeight(), dis, dis);
    }

    public void aiStep() {
        super.aiStep();
        float dis = this.getBbWidth();

        float f1 = (float) Math.cos(Math.toRadians(this.getYRot() + 90));
        float f2 = (float) Math.sin(Math.toRadians(this.getYRot() + 90));

        if(this.getAttackState() == 1) {
            if (this.attackTicks == 17) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 4.35f,dis * 4.35f,45,1.25F,60);
            }
            if (this.attackTicks == 35) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 3.6f,dis * 3.6f,200,1.25F,0);
            }
        }
        if(this.getAttackState() == 3) {
            if (this.tickCount % 4 == 0) {
                for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.0D))) {
                    if (!MobUtil.areAllies(this, entity)) {
                        boolean flag = entity.hurt(this.getMobAttack(), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        if (flag) {
                            double d0 = entity.getX() - this.getX();
                            double d1 = entity.getZ() - this.getZ();
                            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                            entity.push(d0 / d2 * 0.5D, 0.1D, d1 / d2 * 0.5D);
                        }
                    }
                }
            }
        }

        if(this.getAttackState() == 5) {
            if (this.attackTicks == 11 || this.attackTicks == 18 || this.attackTicks == 23 || this.attackTicks == 28  ) {
                this.push(f1 * 0.45, 0, f2 * 0.45);
            }
            if (this.attackTicks == 13) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 4.5f,dis * 4.5f,50,1,0);
            }
            if (this.attackTicks == 20) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 3.5f,dis * 3.5f,60,1,0);
            }
            if (this.attackTicks == 25) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 4.5f,dis * 4.5f,60,1,0);
            }
            if (this.attackTicks == 30) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 3.25f,dis * 3.25f,60,1,0);
            }
        }
        if(this.getAttackState() == 6) {
            if (this.attackTicks == 15 || this.attackTicks == 123 || this.attackTicks == 26 || this.attackTicks == 33) {
                this.push(f1 * 0.45, 0, f2 * 0.45);
            }
            if (this.attackTicks == 17) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 4.5f,dis * 4.5f,40,1,0);
            }
            if (this.attackTicks == 25) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 3.25f,dis * 3.25f,55,1,0);
            }
            if (this.attackTicks == 28) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 5f,dis * 5f,60,1,0);
            }
            if (this.attackTicks == 35) {
                this.playSound(CataclysmSounds.SWING.get(), 1F, 1.2f);
                AreaAttack(dis * 3.5f,dis * 3.5f,40,1,0);
            }
        }

    }


    private void AreaSwordAttack(float range, float height,float degree, float arc, float damage) {
        List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(range, height, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - degree) % 360);
            float entityAttackingAngle = this.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX()));
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                if (!isAlliedTo(entityHit) && entityHit != this) {
                    boolean flag = entityHit.hurt(CMDamageTypes.causeSwordDanceDamage(this), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                    if (flag) {
                        MobEffectInstance effectinstance1 = entityHit.getEffect(ModEffect.EFFECTBLAZING_BRAND.get());
                        int i = 1;
                        if (effectinstance1 != null) {
                            i += effectinstance1.getAmplifier();
                            entityHit.removeEffectNoUpdate(ModEffect.EFFECTBLAZING_BRAND.get());
                        } else {
                            --i;
                        }
                        i = Mth.clamp(i, 0, 1);
                        MobEffectInstance effectinstance = new MobEffectInstance(ModEffect.EFFECTBLAZING_BRAND.get(), 120, i, false, true, true);
                        entityHit.addEffect(effectinstance);
                        this.heal(3 * (i + 1));
                    }
                }
            }
        }
    }

    private void AreaAttack(float range, float height, float arc, float damage, int shieldbreakticks) {
        List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(range, height, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX()));
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                if (!MobUtil.areAllies(this, entityHit)) {
                    DamageSource damagesource = CMDamageTypes.causeSwordDanceDamage(this);
                    boolean flag = entityHit.hurt(damagesource, (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                    if (entityHit.isDamageSourceBlocked(damagesource) && shieldbreakticks > 0) {
                        disableShield(entityHit, shieldbreakticks);
                    }

                    if (flag) {
                        MobEffectInstance effectinstance1 = entityHit.getEffect(ModEffect.EFFECTBLAZING_BRAND.get());
                        int i = 1;
                        if (effectinstance1 != null) {
                            i += effectinstance1.getAmplifier();
                            entityHit.removeEffectNoUpdate(ModEffect.EFFECTBLAZING_BRAND.get());
                        } else {
                            --i;
                        }
                        i = Mth.clamp(i, 0, 1);
                        MobEffectInstance effectinstance = new MobEffectInstance(ModEffect.EFFECTBLAZING_BRAND.get(), 100, i, false, true, true);
                        entityHit.addEffect(effectinstance);
                        this.heal(2F * (i + 1));
                    }
                }
            }
        }
    }

    protected @NotNull SoundEvent getAmbientSound() {
        return CataclysmSounds.REVENANT_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.REVENANT_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.REVENANT_DEATH.get();
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.level().isClientSide){
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            Item item = itemstack.getItem();
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
                if ((itemstack.is(Tags.Items.BONES) || itemstack.is(Tags.Items.RODS_BLAZE) || item == Items.BLAZE_POWDER) && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(CataclysmSounds.REVENANT_IDLE.get(), 1.0F, 1.25F);
                    float healAmount = 1.0F;
                    if (itemstack.is(Tags.Items.BONES)){
                        healAmount = 2.0F;
                    }
                    if (itemstack.is(Tags.Items.RODS_BLAZE)){
                        healAmount = 4.0F;
                    }
                    this.heal(healAmount);
                    if (this.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                        }
                    }
                    pPlayer.swing(pHand);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }
}
