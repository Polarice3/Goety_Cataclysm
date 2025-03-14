package com.Polarice3.goety_cataclysm.common.magic.spells.nether;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.FlareBomb;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.init.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FlareBombSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setVelocity(1.0F);
    }

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.FlareBombCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.FlareBombDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.MONSTROSITYGROWL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.FlareBombCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NETHER;
    }

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
            velocity += (float)WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 3.0F;
        }
        int i1 = this.rightStaff(staff) ? 5 : 1;
        for(int i = 0; i < i1; ++i) {
            FlareBomb lava = new FlareBomb(GCEntityType.FLARE_BOMB.get(), worldIn, caster);
            lava.setExtraDamage(potency);
            lava.setPosRaw(caster.getX(), caster.getY(0.65), caster.getZ());
            lava.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, velocity, (float)(1 + i * 8));
            worldIn.addFreshEntity(lava);
        }

        worldIn.playSound(null, caster, ModSounds.MONSTROSITYSHOOT.get(), this.getSoundSource(), 3.0F, 0.75F);
    }
}
