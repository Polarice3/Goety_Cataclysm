package com.Polarice3.goety_cataclysm.common.entities.ally.factory;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.AnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.LLibrarySummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.SimpleSummonAnimationGoal;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.LaserBeamProjectile;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;

public class WatcherServant extends LLibrarySummon {
    public static final Animation WATCHER_BITE = Animation.create(22);
    public static final Animation WATCHER_SHOT = Animation.create(55);
    public static final Animation WATCHER_EXTRA_SHOT = Animation.create(17);

    public WatcherServant(EntityType<? extends AnimationSummon> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.25F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, WATCHER_BITE,WATCHER_EXTRA_SHOT,WATCHER_SHOT};
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new WatcherMoveGoal(this, false,1.0D));
        this.goalSelector.addGoal(5, new WanderGoal<>(this, 1.0D, 80));
        this.goalSelector.addGoal(0, new ShotPrepare(this,WATCHER_SHOT));
        this.goalSelector.addGoal(0, new Shot(this,WATCHER_EXTRA_SHOT));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.WatcherMeleeDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.WatcherHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.WatcherArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.WatcherHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.WatcherArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.WatcherMeleeDamage.get());
    }

    @Override
    public int xpReward() {
        return 8;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.is(CMDamageTypes.EMP)) {
            damage = 1000;
        }
        return super.hurt(source, damage);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    public void tick() {
        super.tick();

        this.setYRot(yBodyRot);
        LivingEntity target = this.getTarget();
        if (this.getAnimation() == WATCHER_BITE) {
            if (this.getAnimationTick() == 13) {
                if (target != null) {
                    if (distanceTo(target) < 3 && this.hasLineOfSight(target)) {
                        float damage = (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        target.hurt(this.getMobAttack(), damage);
                    }
                }
            }
        }
        if (this.getAnimation() == WATCHER_EXTRA_SHOT) {
            if (this.getAnimationTick() == 9) {
                if (!this.isSilent()) {
                    this.playSound(CataclysmSounds.HARBINGER_LASER.get(),1,1.0F);
                }
                if (target != null && target.isAlive()) {
                    double d0 = this.getX();
                    double d1 = this.getY() + this.getBbHeight() * 1 / 2;
                    double d2 = this.getZ();
                    double d3 = target.getX() - d0;
                    double d4 = target.getY() + target.getBbHeight() * 1 / 2 - d1;
                    double d5 = target.getZ() - d2;

                    Vec3 vec3 = new Vec3(d3, d4, d5);
                    LaserBeamProjectile laserBeam = new LaserBeamProjectile(this, d3, d4, d5, this.level(), GCAttributesConfig.WatcherRangeDamage.get().floatValue());
                    float yRot = (float) (Mth.atan2(vec3.z, vec3.x) * (180F / Math.PI)) + 90F;
                    float xRot = (float) -(Mth.atan2(vec3.y, Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z)) * (180F / Math.PI));
                    laserBeam.setYRot(yRot);
                    laserBeam.setXRot(xRot);
                    laserBeam.setPosRaw(d0, d1, d2);

                    this.level().addFreshEntity(laserBeam);
                }
            }

        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.WATCHER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.WATCHER_DEATH.get();
    }

    static class ShotPrepare extends SimpleSummonAnimationGoal<WatcherServant> {

        public ShotPrepare(WatcherServant entity, Animation animation) {
            super(entity, animation);
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public void start() {
            LivingEntity target = this.entity.getTarget();
            if (target != null) {
                this.entity.getLookControl().setLookAt(target, 30, 30);
            }
            super.start();
        }

        public void stop() {
            LivingEntity target = this.entity.getTarget();
            if (target != null) {
                this.entity.getLookControl().setLookAt(target, 30, 30);
            }
            super.stop();
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (target != null) {
                this.entity.getLookControl().setLookAt(target, 30, 30);
                if(this.entity.getAnimationTick() == 45){
                    AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, WATCHER_EXTRA_SHOT);
                }
            }
        }
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (itemstack.is(Tags.Items.INGOTS_IRON) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
                this.heal(5.0F);
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

    static class Shot extends SimpleSummonAnimationGoal<WatcherServant> {

        public Shot(WatcherServant entity, Animation animation) {
            super(entity, animation);
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));

        }

        public void start() {
            LivingEntity target = this.entity.getTarget();
            if (target != null) {
                this.entity.getLookControl().setLookAt(target, 30, 90);
            }
            super.start();
        }

        public void tick() {
            LivingEntity target = this.entity.getTarget();
            if (this.entity.getAnimationTick() < 7 && target != null) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if(target != null){
                if(this.entity.getAnimationTick() == 11) {
                    if (this.entity.getRandom().nextFloat() * 100.0F < 60f) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, WATCHER_EXTRA_SHOT);
                    }
                }
            }
        }
    }

    static class WatcherMoveGoal extends Goal {
        private final WatcherServant watcher;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private int delayCounter;
        protected final double moveSpeed;

        public WatcherMoveGoal(WatcherServant boss, boolean followingTargetEvenIfNotSeen, double moveSpeed) {
            this.watcher = boss;
            this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
            this.moveSpeed = moveSpeed;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }


        public boolean canUse() {
            LivingEntity target = this.watcher.getTarget();
            return target != null && target.isAlive();
        }

        public void stop() {
            this.watcher.getNavigation().stop();
            LivingEntity livingentity = this.watcher.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.watcher.setTarget(null);
            }
            this.watcher.setAggressive(false);
            this.watcher.getNavigation().stop();
        }

        public boolean canContinueToUse() {
            LivingEntity target = this.watcher.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.watcher.getNavigation().isDone();
            } else if (!this.watcher.isWithinRestriction(target.blockPosition())) {
                return false;
            } else {
                return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
            }
        }

        public void start() {
            this.watcher.getNavigation().moveTo(this.path, this.moveSpeed);
            this.watcher.setAggressive(true);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.watcher.getTarget();
            if (target != null) {
                this.watcher.getLookControl().setLookAt(target, 30.0F, 30.0F);
                double distSq = this.watcher.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
                if (--this.delayCounter <= 0) {
                    this.delayCounter = 4 + this.watcher.getRandom().nextInt(7);
                    if (distSq > Math.pow(this.watcher.getAttribute(Attributes.FOLLOW_RANGE).getValue(), 2.0D)) {
                        if (!this.watcher.isPathFinding()) {
                            if (!this.watcher.getNavigation().moveTo(target, 1.0D)) {
                                this.delayCounter += 5;
                            }
                        }
                    } else {
                        this.watcher.getNavigation().moveTo(target, this.moveSpeed);
                    }
                }
                if (target.isAlive()) {
                    if (this.watcher.getAnimation() == NO_ANIMATION) {
                        if (this.watcher.distanceTo(target) < 1.5F) {
                            this.watcher.setAnimation(WATCHER_BITE);
                        } else if (this.watcher.getRandom().nextFloat() * 100.0F < 24f && this.watcher.distanceTo(target) >= 6D) {
                            this.watcher.setAnimation(WATCHER_SHOT);
                        }
                    }
                }
            }
        }
    }
}
