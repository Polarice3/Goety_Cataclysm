package com.Polarice3.goety_cataclysm.common.entities.ally;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AnimationSummon extends Summoned {
    protected boolean dropAfterDeathAnim = false;
    public int killDataRecentlyHit;
    public DamageSource killDataCause;
    public Player killDataAttackingPlayer;
    public int attackTicks;

    public Vec3[] socketPosArray;

    public AnimationSummon(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        if (world.isClientSide) {
            this.socketPosArray = new Vec3[0];
        }
    }

    public static void setConfigAttribute(LivingEntity entity, double hpconfig, double dmgconfig) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            double difference = maxHealthAttr.getBaseValue() * hpconfig - maxHealthAttr.getBaseValue();
            maxHealthAttr.addTransientModifier(new AttributeModifier(UUID.fromString("9513569b-57b6-41f5-814e-bdc49b81799f"), "Health config multiplier", difference, AttributeModifier.Operation.ADDITION));
            entity.setHealth(entity.getMaxHealth());
        }

        AttributeInstance attackDamageAttr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttr != null) {
            double difference = attackDamageAttr.getBaseValue() * dmgconfig - attackDamageAttr.getBaseValue();
            attackDamageAttr.addTransientModifier(new AttributeModifier(UUID.fromString("5b17d7cb-294e-4379-88ab-136c372bec9b"), "Attack config multiplier", difference, AttributeModifier.Operation.ADDITION));
        }

    }

    public double calculateRange(DamageSource damagesource) {
        return damagesource.getEntity() != null ? this.distanceToSqr(damagesource.getEntity()) : -1.0;
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getZ() - first.getZ(), second.getX() - first.getX()) * 57.29577951308232 + 90.0;
    }

    public void disableShield(LivingEntity livingEntity, int ticks) {
        if (livingEntity instanceof Player player) {
            if (player.isBlocking() && !player.level().isClientSide) {
                player.getCooldowns().addCooldown(player.getUseItem().getItem(), ticks);
                player.stopUsingItem();
                player.level().broadcastEntityEvent(this, (byte) 30);
            }
        } else {
            MobUtil.disableShield(livingEntity);
        }

    }

    protected void tickDeath() {
        this.onDeathUpdate(this.deathTimer());
    }

    public int deathTimer() {
        return 20;
    }

    protected void onDeathAIUpdate() {
    }

    public void onDeathUpdate(int deathDuration) {
        this.onDeathAIUpdate();
        ++this.deathTime;
        if (this.deathTime >= deathDuration && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(RemovalReason.KILLED);
        }

    }

    public void die(DamageSource cause) {
        if (!ForgeHooks.onLivingDeath(this, cause)) {
            if (!this.dead) {
                Entity entity = cause.getEntity();
                LivingEntity livingentity = this.getKillCredit();
                if (this.deathScore >= 0 && livingentity != null) {
                    livingentity.awardKillScore(this, this.deathScore, cause);
                }

                if (this.isSleeping()) {
                    this.stopSleeping();
                }

                this.dead = true;
                this.getCombatTracker().recheckStatus();
                if (this.level() instanceof ServerLevel && (entity == null || entity.killedEntity((ServerLevel)this.level(), this))) {
                    this.gameEvent(GameEvent.ENTITY_DIE);
                    this.createWitherRose(livingentity);
                    this.AfterDefeatBoss(livingentity);
                    if (!this.dropAfterDeathAnim) {
                        this.dropAllDeathLoot(cause);
                    }
                }

                this.killDataCause = cause;
                this.killDataRecentlyHit = this.lastHurtByPlayerTime;
                this.killDataAttackingPlayer = this.lastHurtByPlayer;
                this.level().broadcastEntityEvent(this, (byte)3);
                this.setPose(Pose.DYING);
                if (!this.level().isClientSide && this.hasCustomName() && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getTrueOwner() instanceof ServerPlayer) {
                    this.getTrueOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
                }
            }

        }
    }

    protected void AfterDefeatBoss(@Nullable LivingEntity p_21269_) {
    }

    public void circleEntity(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset, float moveSpeedMultiplier) {
        int directionInt = direction ? 1 : -1;
        double t = (double)(directionInt * circleFrame) * 0.5 * (double)speed / (double)radius + (double)offset;
        Vec3 movePos = target.position().add((double)radius * Math.cos(t), 0.0, (double)radius * Math.sin(t));
        this.getNavigation().moveTo(movePos.x, movePos.y, movePos.z, (double)(speed * moveSpeedMultiplier));
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<LivingEntity> nearbyEntities = this.getEntityLivingBaseNearby((double)x, (double)y, (double)z, (double)radius);

        for (LivingEntity nearbyEntity : nearbyEntities) {
            if (nearbyEntity.isPickable() && !nearbyEntity.noPhysics && !MobUtil.areAllies(this, nearbyEntity)) {
                double angle = (this.getAngleBetweenEntities(this, nearbyEntity) + 90.0) * Math.PI / 180.0;
                nearbyEntity.setDeltaMovement(-0.1 * Math.cos(angle), nearbyEntity.getDeltaMovement().y, -0.1 * Math.sin(angle));
            }
        }

    }

    public void setSocketPosArray(int index, Vec3 pos) {
        if (this.socketPosArray != null && this.socketPosArray.length > index) {
            this.socketPosArray[index] = pos;
        }

    }

    public boolean canBePushedByEntity(Entity entity) {
        return true;
    }

    public void push(Entity entityIn) {
        if (!this.isSleeping() && !this.isPassengerOfSameVehicle(entityIn) && !entityIn.noPhysics && !this.noPhysics) {
            double d0 = entityIn.getX() - this.getX();
            double d1 = entityIn.getZ() - this.getZ();
            double d2 = Mth.absMax(d0, d1);
            if (d2 >= 0.009999999776482582) {
                d2 = (double)Mth.sqrt((float)d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0 / d2;
                if (d3 > 1.0) {
                    d3 = 1.0;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806;
                d1 *= 0.05000000074505806;
                if (!this.isVehicle() && this.canBePushedByEntity(entityIn)) {
                    this.push(-d0, 0.0, -d1);
                }

                if (!entityIn.isVehicle()) {
                    entityIn.push(d0, 0.0, d1);
                }
            }
        }

    }

    public List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return this.level().getEntitiesOfClass(entityClass, this.getBoundingBox().inflate(dX, dY, dZ), (e) -> {
            return e != this && (double)this.distanceTo(e) <= r + (double)(e.getBbWidth() / 2.0F) && e.getY() <= this.getY() + dY;
        });
    }

    @Override
    public boolean canUpdateMove() {
        return true;
    }
}
