package com.Polarice3.goety_cataclysm.common.entities.ally;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.github.L_Ender.cataclysm.init.ModTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class IABossSummon extends InternalAnimationSummon{
    private int reducedDamageTicks;
    private float damageBucket = 0.0f;

    public IABossSummon(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, amount);
        } else{
            amount = Math.min(DamageCap(), amount);
        }

        double distSqr = calculateRange(source);

        if (distSqr != -1) {
            double limit = this.RangeLimit();
            double maxLimit = limit * 1.5;

            double limitSqr = limit * limit;
            double maxLimitSqr = maxLimit * maxLimit;

            if (distSqr >= maxLimitSqr) {
                return false;
            }

            if (distSqr > limitSqr) {
                double distance = Math.sqrt(distSqr);

                float multiplier = (float) ((maxLimit - distance) / (maxLimit - limit));

                amount *= multiplier;

                if (amount <= 0) return false;
            }
        }

        if (!source.is(ModTag.BYPASSES_HURT_TIME)) {

            float projectedBucket = damageBucket + amount;
            float limit = this.DamageCap();

            if (projectedBucket > limit) {
                float roomLeft = limit - damageBucket;

                if (roomLeft > 0) {
                    amount = roomLeft;
                    damageBucket = limit;
                } else {
                    amount = 0.1F;
                }
            } else {
                damageBucket += amount;
            }
        }
        boolean flag = super.hurt(source, amount);

        return flag;
    }


    public float DamageCap() {
        return Float.MAX_VALUE;
    }

    public float DpsCap() {
        return Float.MAX_VALUE;
    }

    public double RangeLimit() {
        return Double.MAX_VALUE;
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (!this.isNoAi()) {
                if (this.damageBucket > 0) {
                    this.damageBucket -= (this.DpsCap() / 20.0f);
                    if (this.damageBucket < 0) {
                        this.damageBucket = 0;
                    }
                }
            }
        }

    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return ModTag.EFFECTIVE_FOR_BOSSES_LOOKUP.contains(p_34192_.getEffect()) && super.canBeAffected(p_34192_);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }
}
