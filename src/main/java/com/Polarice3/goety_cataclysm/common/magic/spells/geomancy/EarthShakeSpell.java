package com.Polarice3.goety_cataclysm.common.magic.spells.geomancy;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.EarthQuake_Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EarthShakeSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setVelocity(0.25F);
    }

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.EarthShakeCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.EarthShakeDuration.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.EarthShakeCoolDown.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound(LivingEntity caster) {
        return GoetySounds.RUMBLE.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
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
            potency += WandUtil.getPotencyLevel(caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 3.0F;
        }
        int quakeCount = 16;
        if (this.rightStaff(staff)) {
            quakeCount = 22 + worldIn.getRandom().nextInt(8);
            velocity += 0.2F;
        }
        float angle = 360.0F / quakeCount;
        for (int i = 0; i < quakeCount; i++) {
            EarthQuake_Entity peq = new EarthQuake_Entity(worldIn, caster);
            float damage = (float) (GCSpellConfig.EarthShakeDamage.get() * WandUtil.damageMultiply());
            peq.setDamage(damage + potency);
            peq.shootFromRotation(caster, 0, angle * i, 0.0F, velocity, 0.0F);
            peq.setPos(caster.getX(), caster.getY(), caster.getZ());
            worldIn.addFreshEntity(peq);
        }
        ScreenShake_Entity.ScreenShake(worldIn, caster.position(), 15.0F, 0.1F, 0, 20);
        this.playSound(worldIn, caster, SoundEvents.GENERIC_EXPLODE, 1.0F, 1.0F + worldIn.getRandom().nextFloat() * 0.1F);
    }
}
