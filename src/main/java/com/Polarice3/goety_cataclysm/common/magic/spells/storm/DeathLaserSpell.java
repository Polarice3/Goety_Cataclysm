package com.Polarice3.goety_cataclysm.common.magic.spells.storm;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.DeathLaserBeam;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class DeathLaserSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.DeathLaserCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.DeathLaserDuration.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.DeathLaserCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    public SoundEvent CastingSound() {
        return CataclysmSounds.HARBINGER_DEATHLASER_PREPARE.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        float damage = GCSpellConfig.DeathLaserDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }

        DeathLaserBeam DeathBeam = new DeathLaserBeam(GCEntityType.DEATH_LASER_BEAM.get(), worldIn, caster, caster.getX(), caster.getEyeY() - 0.2D, caster.getZ(), (float) ((caster.yHeadRot + 90) * Math.PI / 180), (float) (-caster.getXRot() * Math.PI / 180), 28 + MathHelper.secondsToTicks(duration), damage + potency, GCSpellConfig.DeathLaserHPDamage.get().floatValue());
        DeathBeam.setImmediate(true);
        worldIn.addFreshEntity(DeathBeam);
        this.playSound(worldIn, caster, CataclysmSounds.DEATH_LASER.get());
        ScreenShake_Entity.ScreenShake(worldIn, caster.position(), 20, 0.2f, 0, 10);
    }
}
