package com.Polarice3.goety_cataclysm.common.magic.spells.nether;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.EverChargeSpell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class AshenBreathSpell extends EverChargeSpell {
    private static final int ARC = 45;

    public SpellStat defaultStats() {
        return super.defaultStats().setRange(7);
    }

    public int defaultSoulCost() {
        return GCSpellConfig.AshenBreathCost.get();
    }

    public int defaultCastUp() {
        return GCSpellConfig.AshenBreathChargeUp.get();
    }

    public int shotsNumber() {
        return GCSpellConfig.AshenBreathDuration.get();
    }

    public int shotsNumber(LivingEntity caster, ItemStack staff) {
        int duration = 0;
        if (WandUtil.enchantedFocus(caster)) {
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }
        return this.shotsNumber() + MathHelper.secondsToTicks(duration);
    }

    public int defaultSpellCooldown() {
        return GCSpellConfig.AshenBreathCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NETHER;
    }

    public SoundEvent CastingSound() {
        return CataclysmSounds.REVENANT_BREATH.get();
    }

    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster, SpellStat spellStat) {
        if (caster instanceof Mob mob) {
            if (mob.getTarget() != null) {
                int range = spellStat.getRange();
                if (WandUtil.enchantedFocus(caster)) {
                    range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
                }

                return mob.hasLineOfSight(mob.getTarget()) && (double)mob.distanceTo(mob.getTarget()) <= (double)range + 4.0;
            }
        }

        return super.conditionsMet(worldIn, caster, spellStat);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
        }

        float yaw = (float) Math.toRadians(-caster.getYRot());
        float pitch = (float) Math.toRadians(-caster.getXRot());
        float spread = 0.25f;
        float speed = 0.56f;
        float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
        float yComp = (float) (Math.sin(pitch));
        float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
        double theta = (caster.getYRot()) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        double vec = 0.9D;

        for (int i = 0; i < 80; i++) {
            double xSpeed = speed * xComp + (spread * 1 * (caster.getRandom().nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
            double ySpeed = speed * yComp + (spread * 1 * (caster.getRandom().nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
            double zSpeed = speed * zComp + (spread * 1 * (caster.getRandom().nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
            worldIn.sendParticles(ParticleTypes.SMOKE, caster.getX() + vec * vecX, caster.getEyeY(), caster.getZ() + vec * vecZ, 0, xSpeed, ySpeed, zSpeed, 1.0F);
        }
        for (int i = 0; i < 2; i++) {
            double xSpeed = speed * xComp + (spread * 0.7 * (caster.getRandom().nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
            double ySpeed = speed * yComp + (spread * 0.7 * (caster.getRandom().nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
            double zSpeed = speed * zComp + (spread * 0.7 * (caster.getRandom().nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
            worldIn.sendParticles(ParticleTypes.FLAME, caster.getX() + vec * vecX, caster.getEyeY(), caster.getZ() + vec * vecZ, 0, xSpeed, ySpeed, zSpeed, 1.0F);
        }

        if (caster.tickCount > 2) {
            this.hitEntities(caster, range, potency);
        }
    }

    public void hitEntities(LivingEntity caster, int range, int potency) {
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(caster, range, range, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitYaw = (float) ((Math.atan2(entityHit.getZ() - caster.getZ(), entityHit.getX() - caster.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingYaw = caster.getYRot() % 360;
            if (entityHitYaw < 0) {
                entityHitYaw += 360;
            }
            if (entityAttackingYaw < 0) {
                entityAttackingYaw += 360;
            }
            float entityRelativeYaw = entityHitYaw - entityAttackingYaw;

            float xzDistance = (float) Math.sqrt((entityHit.getZ() - caster.getZ()) * (entityHit.getZ() - caster.getZ()) + (entityHit.getX() - caster.getX()) * (entityHit.getX() - caster.getX()));
            double hitY = entityHit.getY() + entityHit.getBbHeight() / 2.0;
            float entityHitPitch = (float) ((Math.atan2((hitY - caster.getEyeY()), xzDistance) * (180 / Math.PI)) % 360);
            float entityAttackingPitch = -caster.getXRot() % 360;
            if (entityHitPitch < 0) {
                entityHitPitch += 360;
            }
            if (entityAttackingPitch < 0) {
                entityAttackingPitch += 360;
            }
            float entityRelativePitch = entityHitPitch - entityAttackingPitch;

            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - caster.getZ()) * (entityHit.getZ() - caster.getZ()) + (entityHit.getX() - caster.getX()) * (entityHit.getX() - caster.getX()) + (hitY - caster.getEyeY()) * (hitY - caster.getEyeY()));
            int distance = caster.tickCount / 2 ;
            boolean inRange = entityHitDistance <= distance + 1.0F;
            boolean yawCheck = (entityRelativeYaw <= ARC / 2f && entityRelativeYaw >= -ARC / 2f) || (entityRelativeYaw >= 360 - ARC / 2f || entityRelativeYaw <= -360 + ARC / 2f);
            boolean pitchCheck = (entityRelativePitch <= ARC / 2f && entityRelativePitch >= -ARC / 2f) || (entityRelativePitch >= 360 - ARC / 2f || entityRelativePitch <= -360 + ARC / 2f);
            boolean CloseCheck = entityHitDistance <= 2;
            if (inRange && yawCheck && pitchCheck || CloseCheck) {
                if (caster.tickCount % 3 == 0) {
                    if (!MobUtil.areAllies(caster, entityHit)) {
                        float damage = (float) (GCSpellConfig.AshenBreathDamage.get() * WandUtil.damageMultiply());
                        boolean flag = entityHit.hurt(caster.damageSources().indirectMagic(caster, caster), damage + potency);
                        if (flag) {
                            MobEffectInstance effectinstance = new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false, true);
                            entityHit.addEffect(effectinstance);
                        }
                    }
                }
            }
        }
    }

    public List<LivingEntity> getEntityLivingBaseNearby(LivingEntity caster, double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(caster, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity caster, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return caster.level().getEntitiesOfClass(entityClass, caster.getBoundingBox().inflate(dX, dY, dZ), e -> e != caster && caster.distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= caster.getEyeY() + dY);
    }
}
