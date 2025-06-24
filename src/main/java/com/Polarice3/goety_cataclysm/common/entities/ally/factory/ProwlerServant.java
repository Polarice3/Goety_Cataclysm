package com.Polarice3.goety_cataclysm.common.entities.ally.factory;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.projectiles.FlyingItem;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.DeathLaserBeam;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.WitherHomingMissile;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.common.items.revive.MechanizedCore;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class ProwlerServant extends InternalAnimationSummon {
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState stunAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    public AnimationState laserAnimationState = new AnimationState();
    public AnimationState spinAnimationState = new AnimationState();
    public AnimationState meleeAnimationState = new AnimationState();
    public AnimationState strongAttackAnimationState = new AnimationState();
    public AnimationState pierceAnimationState = new AnimationState();
    public static final int STUNNED = 1;
    public static final int LASER = 2;
    public static final int DEATH = 3;
    public static final int SPIN = 4;
    public static final int MELEE = 5;
    public static final int STRONG = 6;
    public static final int PIERCE = 7;
    public static final int SPIN_COOLDOWN = 80;
    public static final int LASER_COOLDOWN = 200;
    private int spin_cooldown = 0;
    private int laser_cooldown = 100;

    public ProwlerServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.25F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new InternalSummonMoveGoal(this, false, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new WanderGoal<>(this, 1.0D, 80));

        this.goalSelector.addGoal(0, new InternalSummonStateGoal(this, STUNNED, STUNNED, 0, 60, 0));

        //laser
        this.goalSelector.addGoal(1, new LaserShoot(this, 0, LASER, 0, 90, 20, 8F, 20, 100F));
        //spin
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,SPIN,0,50,22,4.75F){
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.getRandom().nextFloat() * 100.0F < 26 && ProwlerServant.this.spin_cooldown <= 0;
            }

            @Override
            public void stop() {
                super.stop();
                ProwlerServant.this.spin_cooldown = SPIN_COOLDOWN;
            }
        });

        //melee
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,MELEE,0,50,38,5F){
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.getRandom().nextFloat() * 100.0F < 20 ;
            }
        });
        //strong
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,STRONG,0,55,45,6F){
            @Override
            public boolean canUse() {
                LivingEntity target = this.entity.getTarget();
                return super.canUse() && this.entity.getRandom().nextFloat() * 100.0F < 20 && target !=null && this.entity.distanceTo(target) >= 2.75D;
            }
        });
        //pierce
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this,0,PIERCE,0,80,38,4.25F){
            @Override
            public boolean canUse() {
                LivingEntity target = this.entity.getTarget();
                return super.canUse() && this.entity.getRandom().nextFloat() * 100.0F < 24 && target !=null;
            }
        });
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.ProwlerMeleeDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.ProwlerHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.ProwlerArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.ProwlerHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.ProwlerArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.ProwlerMeleeDamage.get());
    }

    @Override
    public int xpReward() {
        return 20;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.is(CMDamageTypes.EMP) && this.getAttackState() != STUNNED) {
            this.setAttackState(STUNNED);
        }
        double range = calculateRange(source);
        if (range > CMConfig.ProwlerLongRangelimit * CMConfig.ProwlerLongRangelimit) {
            return false;
        }

        return super.hurt(source, damage);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "stun")) {
            return this.stunAnimationState;
        } else if (Objects.equals(input, "laser")) {
            return this.laserAnimationState;
        } else if (Objects.equals(input, "death")) {
            return this.deathAnimationState;
        } else if (Objects.equals(input, "spin")) {
            return this.spinAnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "melee")) {
            return this.meleeAnimationState;
        } else if (Objects.equals(input, "strong_attack")) {
            return this.strongAttackAnimationState;
        } else if (Objects.equals(input, "pierce")) {
            return this.pierceAnimationState;
        } else {
            return new AnimationState();
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0 -> this.stopAllAnimationStates();
                case 1 -> {
                    this.stopAllAnimationStates();
                    this.stunAnimationState.startIfStopped(this.tickCount);
                }
                case 2 -> {
                    this.stopAllAnimationStates();
                    this.laserAnimationState.startIfStopped(this.tickCount);
                }
                case 3 -> {
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
                }
                case 4 -> {
                    this.stopAllAnimationStates();
                    this.spinAnimationState.startIfStopped(this.tickCount);
                }
                case 5 -> {
                    this.stopAllAnimationStates();
                    this.meleeAnimationState.startIfStopped(this.tickCount);
                }
                case 6 -> {
                    this.stopAllAnimationStates();
                    this.strongAttackAnimationState.startIfStopped(this.tickCount);
                }
                case 7 -> {
                    this.stopAllAnimationStates();
                    this.pierceAnimationState.startIfStopped(this.tickCount);
                }
            }
        }
        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.laserAnimationState.stop();
        this.stunAnimationState.stop();
        this.spinAnimationState.stop();
        this.meleeAnimationState.stop();
        this.strongAttackAnimationState.stop();
        this.deathAnimationState.stop();
        this.pierceAnimationState.stop();
    }

    public void die(DamageSource p_21014_) {
        if (this.getTrueOwner() != null && GCMobsConfig.ProwlerCore.get()) {
            ItemStack itemStack = new ItemStack(GCItems.MECHANIZED_CORE.get());
            MechanizedCore.setOwnerName(this.getTrueOwner(), itemStack);
            MechanizedCore.setSummon(this, itemStack);
            FlyingItem flyingItem = new FlyingItem(ModEntityType.FLYING_ITEM.get(), this.level(), this.getX(), this.getY(), this.getZ());
            flyingItem.setOwner(this.getTrueOwner());
            flyingItem.setItem(itemStack);
            flyingItem.setParticle(ModParticleTypes.ELECTRIC.get());
            flyingItem.setSecondsCool(30);
            this.level().addFreshEntity(flyingItem);
        }
        super.die(p_21014_);
        this.setAttackState(DEATH);
    }

    public int deathTimer() {
        return 40;
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(this.getAttackState() != 1, this.tickCount);
        }
        if (this.laser_cooldown > 0) {
            this.laser_cooldown--;
        }
        if (this.spin_cooldown > 0) {
            this.spin_cooldown--;
        }

    }

    public void aiStep() {
        super.aiStep();
        LivingEntity target = this.getTarget();
        if (this.getAttackState() == LASER) {
            if (this.attackTicks == 38) {
                this.level().playSound((Player) null, this, CataclysmSounds.DEATH_LASER.get(), this.getSoundSource(), 1.0f, 1.0f);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 20, 0.2f, 0, 10);
            }
        }


        if (this.getAttackState() == STUNNED) {
            if (this.level().isClientSide) {
                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
            }
        }
        if (this.getAttackState() == SPIN) {
            if (this.attackTicks == 23 || this.attackTicks == 32) {
                this.AreaAttack(6.0f, 6.0F, 180, 1.0F);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CataclysmSounds.PROWLER_SAW_SPIN_ATTACK.get(), this.getSoundSource(), 1.5f, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            }
            if (this.attackTicks == 23) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CataclysmSounds.PROWLER_SAW_SPIN_ATTACK.get(), this.getSoundSource(), 1.5f, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            }
        }

        if (this.getAttackState() == MELEE) {
            if (this.attackTicks == 27){
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CataclysmSounds.PROWLER_SAW_SPIN_ATTACK.get(), this.getSoundSource(), 1.5f, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            }
            if (this.attackTicks == 20 || this.attackTicks == 26 || this.attackTicks == 32 || this.attackTicks == 38 || this.attackTicks == 44) {
                this.AreaAttack(5.4f, 5.5F, 110, 0.5F);
            }
        }
        float f1 = (float) Math.cos(Math.toRadians(this.getYRot() + 90));
        float f2 = (float) Math.sin(Math.toRadians(this.getYRot() + 90));
        if (this.getAttackState() == STRONG) {
            if (this.attackTicks == 18) {
                this.push(f1 * 1.5, 0, f2 * 1.5);
            }
            if (this.attackTicks == 17){
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CataclysmSounds.PROWLER_SAW_SPIN_ATTACK.get(), this.getSoundSource(), 1.5f, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            }
            if (this.attackTicks == 25) {
                this.AreaAttack(5.5f, 5.5f, 70, 1.5F);
            }
        }
        if (this.getAttackState() == PIERCE) {
            if(target !=null) {
                if (this.attackTicks == 12) {
                    this.MissileLaunch(2.0f, 0.5F, target);
                }
                if (this.attackTicks == 15) {
                    this.MissileLaunch(2.3f, 0.5F, target);
                }
                if (this.attackTicks == 18) {
                    this.MissileLaunch(2.6f, 0.5F, target);
                }
            }
            if (this.attackTicks == 18) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CataclysmSounds.PROWLER_SAW_ATTACK.get(), this.getSoundSource(), 1.5f, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            }
            if (this.attackTicks == 25 || this.attackTicks == 32 || this.attackTicks == 40) {
                this.AreaAttack(5.5F, 5.5F, 60, 0.5F);
            }

            if (this.attackTicks == 64) {
                this.AreaAttack(5.5F, 5.5F, 140, 1.0F);

            }
        }
    }

    private void AreaAttack(float range, float height, float arc, float damage) {
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
                if (!MobUtil.areAllies(this, entityHit) && entityHit != this) {
                    entityHit.hurt(CMDamageTypes.causeShredderDamage(this), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));

                }
            }
        }
    }


    private void MissileLaunch(float y, float math, LivingEntity target) {
        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), CataclysmSounds.ROCKET_LAUNCH.get(), this.getSoundSource(), 1.5f, 1F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        }
        float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F)) ;
        float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F)) ;

        double theta = (yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);

        double d0 = this.getX() + 0.5f * vecX + f * math;
        double d1 = this.getY() + y;
        double d2 = this.getZ() + 0.5f * vecZ + f1 * math;

        WitherHomingMissile missile = new WitherHomingMissile(this.level(), this,target);
        missile.setPosRaw(d0, d1, d2);
        missile.setDamage(GCAttributesConfig.ProwlerMissileDamage.get().floatValue());
        this.level().addFreshEntity(missile);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.PROWLER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.PROWLER_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {
        return CataclysmSounds.PROWLER_IDLE.get();
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return p_34192_.getEffect() != ModEffect.EFFECTSTUN.get() && p_34192_.getEffect() != ModEffect.EFFECTABYSSAL_CURSE.get() && super.canBeAffected(p_34192_);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if ((itemstack.is(Tags.Items.INGOTS_IRON) || itemstack.is(Tags.Items.STORAGE_BLOCKS_IRON)) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);

                if (itemstack.is(Tags.Items.STORAGE_BLOCKS_IRON)) {
                    this.heal(45.0F);
                }
                if (itemstack.is(Tags.Items.INGOTS_IRON)) {
                    this.heal(5.0F);
                }
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

    static class LaserShoot extends InternalSummonAttackGoal {
        private final ProwlerServant entity;
        private final int attackshot;
        private final float random;

        public LaserShoot(ProwlerServant entity, int getAttackState, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange, int attackshot, float random) {
            super(entity, getAttackState, attackstate, attackendstate, attackMaxtick, attackseetick, attackrange);
            this.entity = entity;
            this.attackshot = attackshot;
            this.random = random;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.entity.getTarget();
            return super.canUse() && target != null && this.entity.getRandom().nextFloat() * 100.0F < this.random && this.entity.getSensing().hasLineOfSight(target) && this.entity.laser_cooldown <= 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.entity.laser_cooldown = LASER_COOLDOWN;
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            super.tick();
            if (this.entity.attackTicks == this.attackshot) {
                DeathLaserBeam DeathBeam = new DeathLaserBeam(GCEntityType.DEATH_LASER_BEAM.get(), this.entity.level(), this.entity, this.entity.getX(), this.entity.getY() + 1.8, this.entity.getZ(), (float) ((this.entity.yHeadRot + 90) * Math.PI / 180), (float) (-this.entity.getXRot() * Math.PI / 180), 28, GCSpellConfig.DeathLaserDamage.get().floatValue(),GCSpellConfig.DeathLaserHPDamage.get().floatValue());
                this.entity.level().addFreshEntity(DeathBeam);
            }
            if (this.entity.attackTicks >= this.attackshot) {
                if (target != null) {
                    this.entity.getLookControl().setLookAt(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 2, 90);
                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
