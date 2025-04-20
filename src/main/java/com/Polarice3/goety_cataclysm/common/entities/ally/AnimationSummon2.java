package com.Polarice3.goety_cataclysm.common.entities.ally;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class AnimationSummon2 extends Summoned implements IAnimatedEntity {
    public int animationTick;
    public Animation currentAnimation;

    public AnimationSummon2(EntityType entity, Level world) {
        super(entity, world);
    }

    public List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return this.level().getEntitiesOfClass(entityClass, this.getBoundingBox().inflate(dX, dY, dZ), (e) -> {
            return e != this && (double)this.distanceTo(e) <= r + (double)(e.getBbWidth() / 2.0F) && e.getY() <= this.getY() + dY;
        });
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

    public DamageSource getMobAttack(){
        return this.getTrueOwner() != null ? ModDamageSource.summonAttack(this, this.getTrueOwner()) : this.damageSources().mobAttack(this);
    }
}
