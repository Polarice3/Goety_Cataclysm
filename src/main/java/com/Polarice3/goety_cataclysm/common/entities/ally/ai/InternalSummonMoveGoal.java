package com.Polarice3.goety_cataclysm.common.entities.ally.ai;

import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class InternalSummonMoveGoal extends Goal {
    private final InternalAnimationSummon monster;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private int delayCounter;
    protected final double moveSpeed;

    public InternalSummonMoveGoal(InternalAnimationSummon boss, boolean followingTargetEvenIfNotSeen, double moveSpeed) {
        this.monster = boss;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.moveSpeed = moveSpeed;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    public boolean canUse() {
        LivingEntity target = this.monster.getTarget();
        return target != null && target.isAlive();
    }

    public void stop() {
        this.monster.getNavigation().stop();
        LivingEntity livingentity = this.monster.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.monster.setTarget((LivingEntity)null);
        }

        this.monster.setAggressive(false);
    }

    public boolean canContinueToUse() {
        LivingEntity target = this.monster.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.monster.getNavigation().isDone();
        } else if (!this.monster.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player)target).isCreative();
        }
    }

    public void start() {
        this.monster.getNavigation().moveTo(this.path, this.moveSpeed);
        this.monster.setAggressive(true);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity target = this.monster.getTarget();
        if (target != null) {
            this.monster.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distSq = this.monster.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + this.monster.getRandom().nextInt(7);
                if (distSq > Math.pow(this.monster.getAttribute(Attributes.FOLLOW_RANGE).getValue(), 2.0)) {
                    if (!this.monster.isPathFinding() && !this.monster.getNavigation().moveTo(target, 1.0)) {
                        this.delayCounter += 5;
                    }
                } else {
                    this.monster.getNavigation().moveTo(target, this.moveSpeed);
                }
            }
        }

    }
}
