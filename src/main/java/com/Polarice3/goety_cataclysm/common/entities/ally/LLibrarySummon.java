package com.Polarice3.goety_cataclysm.common.entities.ally;

import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.AnimationHandler;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class LLibrarySummon extends AnimationSummon implements IAnimatedEntity {
    public int animationTick;
    public Animation currentAnimation;

    public LLibrarySummon(EntityType<? extends AnimationSummon> entity, Level world) {
        super(entity, world);
    }

    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    protected void onDeathAIUpdate() {
    }

    protected void tickDeath() {
        if (this.getAnimation() != this.getDeathAnimation()) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, this.getDeathAnimation());
        }

        Animation death;
        if ((death = this.getDeathAnimation()) != null) {
            this.onDeathUpdate(death.getDuration() - 20);
        } else {
            this.onDeathUpdate(20);
        }

    }

    protected void onAnimationFinish(Animation animation) {
    }

    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION};
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    public Animation getAnimation() {
        return this.currentAnimation;
    }

    public void setAnimation(Animation animation) {
        if (animation == NO_ANIMATION) {
            this.onAnimationFinish(this.currentAnimation);
        }

        this.currentAnimation = animation;
        this.setAnimationTick(0);
    }

    @Nullable
    public Animation getDeathAnimation() {
        return null;
    }
}
