package com.Polarice3.goety_cataclysm.common.magic.spells.abyss;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.AbyssOrb;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AbyssalOrbsSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(GCSpellConfig.AbyssalOrbsRadius.get());
    }

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.AbyssalOrbsCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.AbyssalOrbsDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return CataclysmSounds.LEVIATHAN_IDLE.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.AbyssalOrbsCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ABYSS;
    }

    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        float radius = (float) spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F;
        }

        for(int i = 0; i < 3; ++i) {
            Vec3 motion = (new Vec3(0.5, -1.25, 0.5)).yRot(-((float)(120 * i)) * 0.017453292F);
            this.shootAbyssOrb(caster, motion.x, motion.y, motion.z, potency, radius);
        }

        for(int i = 0; i < 6; ++i) {
            Vec3 motion = (new Vec3(1.0, -0.75, 1.0)).yRot(-((float)(60 * i)) * 0.017453292F);
            this.shootAbyssOrb(caster, motion.x, motion.y, motion.z, potency, radius);
        }

        if (this.rightStaff(staff)){
            for(int i = 0; i < 6; ++i) {
                Vec3 motion = (new Vec3(1.0, 0.0, 1.0)).yRot(-((float)(60 * i)) * 0.017453292F);
                this.shootAbyssOrb(caster, motion.x, motion.y, motion.z, potency, radius);
            }

            for(int i = 0; i < 6; ++i) {
                Vec3 motion = (new Vec3(1.0, 0.75, 1.0)).yRot(-((float)(60 * i)) * 0.017453292F);
                this.shootAbyssOrb(caster, motion.x, motion.y, motion.z, potency, radius);
            }

            for(int i = 0; i < 3; ++i) {
                Vec3 motion = (new Vec3(0.5, 1.25, 0.5)).yRot(-((float)(120 * i)) * 0.017453292F);
                this.shootAbyssOrb(caster, motion.x, motion.y, motion.z, potency, radius);
            }
        }
        worldIn.playSound(null, caster, CataclysmSounds.LEVIATHAN_STUN_ROAR.get(), this.getSoundSource(), 2.0F, 0.8F);
    }

    public void shootAbyssOrb(LivingEntity caster, double xMotion, double yMotion, double zMotion, int potency, float radius) {
        AbyssOrb fireball;
        if (this.getTarget(caster) != null) {
            fireball = new AbyssOrb(caster, xMotion, yMotion, zMotion, caster.level(), GCSpellConfig.AbyssalOrbsDamage.get().floatValue() + potency, this.getTarget(caster));
        } else {
            fireball = new AbyssOrb(caster, xMotion, yMotion, zMotion, caster.level(), GCSpellConfig.AbyssalOrbsDamage.get().floatValue() + potency, null);
        }
        fireball.setRadius(radius);
        fireball.setPos(fireball.getX(), caster.getEyeY(), fireball.getZ());
        fireball.setUp(40);
        if (!caster.level().isClientSide) {
            caster.level().addFreshEntity(fireball);
        }

    }
}
