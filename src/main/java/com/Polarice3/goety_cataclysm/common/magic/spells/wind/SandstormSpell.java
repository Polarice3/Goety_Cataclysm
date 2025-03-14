package com.Polarice3.goety_cataclysm.common.magic.spells.wind;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.Sandstorm;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.init.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SandstormSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.SandstormCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.SandstormDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.REMNANT_ROAR.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.SandstormCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }
        int i = this.rightStaff(staff) ? 4 : 1;
        for(int l = 0; l < i; ++l) {
            float angle = (float)l * 3.1415927F / 1.5F;
            double sx = caster.getX() + (double)(Mth.cos(angle) * 8.0F);
            double sy = caster.getY();
            double sz = caster.getZ() + (double)(Mth.sin(angle) * 8.0F);
            Sandstorm sandstorm = new Sandstorm(worldIn, sx, sy, sz, 300 * (duration + 1), angle, caster.getUUID());
            sandstorm.setExtraDamage(potency);
            worldIn.addFreshEntity(sandstorm);
        }
    }
}
