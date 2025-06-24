package com.Polarice3.goety_cataclysm.common.magic.spells.abyss;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import com.github.L_Ender.cataclysm.entity.projectile.Water_Spear_Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WaterSpearSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.WaterSpearCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.WaterSpearDuration.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.WaterSpearCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ABYSS;
    }

    @Nullable
    @Override
    public SoundEvent CastingSound(LivingEntity caster) {
        return GoetySounds.ABYSS_PREPARE_SPELL.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.VELOCITY.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        float velocity = spellStat.getVelocity();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 10.0F;
        }
        Vec3 vector3d = caster.getLookAngle();
        float damage = (float) (GCSpellConfig.WaterSpearDamage.get() * SpellConfig.SpellDamageMultiplier.get());

        float yRot = (float)(Mth.atan2(vector3d.z, vector3d.x) * (180F / Math.PI)) + 90.0F;
        float xRot = (float)(-(Mth.atan2(vector3d.y, Math.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z)) * (180F / Math.PI)));
        if (rightStaff(staff)) {
            float f = Mth.cos(caster.yBodyRot * ((float)Math.PI / 180F));
            float f1 = Mth.sin(caster.yBodyRot * ((float)Math.PI / 180F));
            float math = -0.5F;
            int spearAmount = 5;
            double offsetangle = Math.toRadians(15);
            double d0 = caster.getX() + f * math;
            double d1 = caster.getY() + caster.getBbHeight() * 0.7F;
            double d2 = caster.getZ() + f1 * math;

            for (int i = 0; i <= (spearAmount - 1); ++i) {
                double angle = (i - ((spearAmount - 1) / 2.0)) * offsetangle;
                double x = vector3d.x * Math.cos(angle) + vector3d.z * Math.sin(angle);
                double z = -vector3d.x * Math.sin(angle) + vector3d.z * Math.cos(angle);

                Vec3 vec3 = new Vec3(x, vector3d.y, z).normalize();

                Water_Spear_Entity water = new Water_Spear_Entity(caster, vec3, worldIn, damage + potency);
                water.accelerationPower = 0.2D;
                water.setYRot(yRot);
                water.setXRot(xRot);
                water.setPosRaw(caster.getX() + (vector3d.x / 2) * math,
                        caster.getEyeY() - 0.2,
                        caster.getZ() + (vector3d.z / 2) * math);
                water.accelerationPower += velocity;
                water.setTotalBounces(8 + potency);
                worldIn.addFreshEntity(water);
            }
        } else {
            Water_Spear_Entity waterSpear = new Water_Spear_Entity(caster, vector3d, worldIn, damage + potency);
            waterSpear.setYRot(yRot);
            waterSpear.setXRot(xRot);
            waterSpear.setPosRaw(caster.getX() + vector3d.x / 2,
                    caster.getEyeY() - 0.2,
                    caster.getZ() + vector3d.z / 2);
            waterSpear.accelerationPower += velocity;
            waterSpear.setTotalBounces(10 + potency);
            worldIn.addFreshEntity(waterSpear);
        }
        this.playSound(worldIn, caster, SoundEvents.GENERIC_SPLASH, 1.0F, 1.0F);
    }
}
