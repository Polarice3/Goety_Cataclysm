package com.Polarice3.goety_cataclysm.common.magic.spells.geomancy;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.projectile.Amethyst_Cluster_Projectile_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AmethystClusterSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return 0;
    }

    @Override
    public int defaultCastDuration() {
        return 50;
    }

    @Override
    public int defaultSpellCooldown() {
        return 240;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        float velocity = spellStat.getVelocity();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 10.0F;
        }
        int amount = 16;
        if (rightStaff(staff)) {
            amount = 32;
        }
        for (int i = 0; i < amount; i++) {
            float throwAngle = i * Mth.PI / 16F;

            double sx = caster.getX() + (Mth.cos(throwAngle) * 1);
            double sy = caster.getY() + (caster.getBbHeight() * 0.2D);
            double sz = caster.getZ() + (Mth.sin(throwAngle) * 1);

            double vx = Mth.cos(throwAngle);
            double vy = 0 + caster.getRandom().nextFloat() * 0.3F;
            double vz = Mth.sin(throwAngle);
            double v3 = Mth.sqrt((float) (vx * vx + vz * vz));
            Amethyst_Cluster_Projectile_Entity projectile = new Amethyst_Cluster_Projectile_Entity(ModEntities.AMETHYST_CLUSTER_PROJECTILE.get(), worldIn, caster, (float) CMConfig.AmethystClusterdamage + potency);

            projectile.moveTo(sx, sy, sz, i * 11.25F, caster.getXRot());
            float speed = 0.8F + velocity;
            projectile.shoot(vx, vy + v3 * 0.20000000298023224D, vz, speed, 1.0F);
            worldIn.addFreshEntity(projectile);
        }
    }
}
