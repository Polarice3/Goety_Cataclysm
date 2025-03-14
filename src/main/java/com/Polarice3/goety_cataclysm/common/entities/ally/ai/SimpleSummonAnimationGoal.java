package com.Polarice3.goety_cataclysm.common.entities.ally.ai;

import com.Polarice3.goety_cataclysm.common.entities.ally.LLibrarySummon;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;

import java.util.EnumSet;

public class SimpleSummonAnimationGoal<T extends LLibrarySummon & IAnimatedEntity> extends SummonAnimationGoal<T> {
    private final Animation animation;

    public SimpleSummonAnimationGoal(T entity, Animation animation) {
        super(entity);
        this.animation = animation;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    protected boolean test(Animation animation) {
        return animation == this.animation;
    }
}
