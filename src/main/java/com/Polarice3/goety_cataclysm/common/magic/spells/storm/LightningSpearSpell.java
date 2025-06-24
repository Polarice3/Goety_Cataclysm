package com.Polarice3.goety_cataclysm.common.magic.spells.storm;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import com.github.L_Ender.cataclysm.entity.projectile.Lightning_Spear_Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LightningSpearSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.LightningSpearCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.LightningSpearDuration.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.LightningSpearCoolDown.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound(LivingEntity caster) {
        return GoetySounds.ZAP.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.VELOCITY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        float velocity = spellStat.getVelocity();
        double radius = spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 10.0F;
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        Vec3 vector3d = caster.getLookAngle();
        float multiply = SpellConfig.SpellDamageMultiplier.get();
        float damage = (float) (GCSpellConfig.LightningSpearDamage.get() * multiply);
        float areaDamage = (float) (GCSpellConfig.LightningSpearAreaDamage.get() * multiply);
        float yRot = (float)(Mth.atan2(vector3d.z, vector3d.x) * (180F / Math.PI)) + 90.0F;
        float xRot = (float)(-(Mth.atan2(vector3d.y, Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z)) * (180F / Math.PI)));
        if (rightStaff(staff)) {
            for (int i = 0; i < 2; ++i) {
                float f = Mth.cos(caster.yBodyRot * ((float)Math.PI / 180F));
                float f1 = Mth.sin(caster.yBodyRot * ((float)Math.PI / 180F));
                double dis = 2;
                double firstAngleOffset = (2 - 1) / 2.0 * dis;
                double math = -0.5 - firstAngleOffset + (i * dis);
                double d0 = caster.getX() + f * math;
                double d1 = caster.getY() + caster.getBbHeight() * 0.7F;
                double d2 = caster.getZ() + f1 * math;

                Lightning_Spear_Entity lightning = new Lightning_Spear_Entity(caster, vector3d, worldIn, damage + potency);
                lightning.accelerationPower = 0.15D + velocity;
                lightning.setYRot(yRot);
                lightning.setXRot(xRot);
                lightning.setPosRaw(d0,
                        caster.getEyeY() - 0.2,
                        d2);
                lightning.setAreaDamage(areaDamage + potency);
                lightning.setHpDamage(GCSpellConfig.LightningSpearHPDamage.get().floatValue());
                lightning.setAreaRadius((float) radius);
                worldIn.addFreshEntity(lightning);
            }
        } else {
            Lightning_Spear_Entity lightningSpear = new Lightning_Spear_Entity(caster, vector3d, worldIn, damage + potency);
            lightningSpear.setYRot(yRot);
            lightningSpear.setXRot(xRot);
            lightningSpear.setPosRaw(caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2);
            lightningSpear.accelerationPower = 0.2D + velocity;
            lightningSpear.setAreaDamage(areaDamage + potency);
            lightningSpear.setAreaRadius((float) radius);
            worldIn.addFreshEntity(lightningSpear);
        }
        this.playSound(worldIn, caster, GoetySounds.SHOCK_CAST.get());
    }
}
