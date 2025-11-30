package com.Polarice3.goety_cataclysm.common.entities.ally.ai;

import com.Polarice3.Goety.api.entities.ally.IServant;
import com.github.L_Ender.cataclysm.entity.AI.AnimalAIRandomSwimming;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class Water3DWanderGoal<T extends PathfinderMob & IServant> extends AnimalAIRandomSwimming {
    public T mob;

    public Water3DWanderGoal(T creature, double speed, int chance, int xzSpread) {
        super(creature, speed, chance, xzSpread);
        this.mob = creature;
    }

    public Water3DWanderGoal(T creature, double speed, int chance, int xzSpread, boolean submerged) {
        super(creature, speed, chance, xzSpread, submerged);
        this.mob = creature;
    }

    public Water3DWanderGoal(T creature, double speed, int chance, int xzSpread, int ySpread, boolean submerged) {
        super(creature, speed, chance, xzSpread, ySpread, submerged);
        this.mob = creature;
    }

    @Nullable
    protected Vec3 getPosition() {
        return this.mob.isGuardingArea() ? this.randomBoundPos() : super.getPosition();
    }

    public Vec3 randomBoundPos() {
        Vec3 vec3 = null;
        int range = IServant.GUARDING_RANGE / 2;

        for(int i = 0; i < 10; ++i) {
            BlockPos blockPos = this.mob.getBoundPos().offset(this.mob.getRandom().nextIntBetweenInclusive(-range, range), this.mob.getRandom().nextIntBetweenInclusive(-range, range), this.mob.getRandom().nextIntBetweenInclusive(-range, range));
            if (this.mob.getNavigation() instanceof WaterBoundPathNavigation) {
                if (GoalUtils.isWater(this.mob, blockPos)) {
                    vec3 = Vec3.atBottomCenterOf(blockPos);
                    break;
                }
            } else {
                BlockPos blockPos1 = LandRandomPos.movePosUpOutOfSolid(this.mob, blockPos);
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
            return !this.mob.isStaying() && !this.mob.isCommanded() || this.mob.getTrueOwner() == null;
        }
    }
}
