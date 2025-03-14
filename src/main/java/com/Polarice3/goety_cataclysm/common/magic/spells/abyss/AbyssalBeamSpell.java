package com.Polarice3.goety_cataclysm.common.magic.spells.abyss;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.util.AbyssMark;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AbyssalBeamSpell extends Spell {
    public AbyssalBeamSpell() {
    }

    public int defaultSoulCost() {
        return GCSpellConfig.AbyssalBeamCost.get();
    }

    public int defaultCastDuration() {
        return GCSpellConfig.AbyssalBeamDuration.get() + 10;
    }

    public int castDuration(LivingEntity caster) {
        return this.defaultCastDuration();
    }

    public SoundEvent CastingSound() {
        return ModSounds.TRIDENT_STORM_PRE.get();
    }

    public int defaultSpellCooldown() {
        return GCSpellConfig.AbyssalBeamCoolDown.get();
    }

    public SpellType getSpellType() {
        return SpellType.ABYSS;
    }

    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    public void startSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
        }

        HitResult rayTrace = this.rayTrace(worldIn, caster, range, 3.0);
        Vec3 location = rayTrace.getLocation();
        LivingEntity target = this.getTarget(caster, range);
        if (target != null) {
            location = caster.position();
        }

        AbyssMark abyssMark = new AbyssMark(worldIn, location, GCSpellConfig.AbyssalBeamDuration.get(), (GCSpellConfig.AbyssalBeamDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get()) + potency, GCSpellConfig.AbyssalBeamHPDamage.get().floatValue(), caster.getUUID(), target, this.rightStaff(staff), potency);
        worldIn.addFreshEntity(abyssMark);
    }

}
