package com.Polarice3.goety_cataclysm.common.entities.ally;

import com.github.L_Ender.cataclysm.init.ModTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class LLibraryBossSummon extends LLibrarySummon{
    private int reducedDamageTicks;

    public LLibraryBossSummon(EntityType<? extends AnimationSummon> entity, Level world) {
        super(entity, world);
    }

    public boolean hurt(DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, damage);
        } else {
            damage = Math.min(this.DamageCap(), damage);
            if (this.ReducedDamage(source) && this.reducedDamageTicks > 0) {
                float reductionFactor = 1.0F - (float)this.reducedDamageTicks / (float)this.DamageTime();
                damage *= reductionFactor;
            }

            boolean flag = super.hurt(source, damage);
            if (this.ReducedDamage(source) && flag) {
                this.reducedDamageTicks = this.DamageTime();
            }

            return flag;
        }
    }

    public boolean ReducedDamage(DamageSource damageSource) {
        return !damageSource.is(ModTag.BYPASSES_HURT_TIME) && this.DamageTime() > 0;
    }

    public float DamageCap() {
        return Float.MAX_VALUE;
    }

    public int DamageTime() {
        return 0;
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.reducedDamageTicks > 0) {
                --this.reducedDamageTicks;
            }
        }

    }

    protected void onDeathAIUpdate() {
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return ModTag.EFFECTIVE_FOR_BOSSES_LOOKUP.contains(p_34192_.getEffect()) && super.canBeAffected(p_34192_);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }
}
