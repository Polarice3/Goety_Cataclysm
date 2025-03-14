package com.Polarice3.goety_cataclysm.common.entities.ally.ai;

import com.Polarice3.goety_cataclysm.common.entities.ally.LLibrarySummon;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class SummonAnimationGoal<T extends LLibrarySummon & IAnimatedEntity> extends Goal {
    protected final T entity;

    protected SummonAnimationGoal(T entity) {
        this(entity, true);
    }

    protected SummonAnimationGoal(T entity, boolean interruptsAI) {
        this.entity = entity;
        if (interruptsAI) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

    }

    public boolean canUse() {
        return this.test(this.entity.getAnimation());
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    protected abstract boolean test(Animation var1);
}
