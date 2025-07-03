package com.Polarice3.goety_cataclysm.common.magic.spells.storm;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.init.ModParticle;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EMPSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return 32;
    }

    @Override
    public int defaultCastDuration() {
        return 30;
    }

    @Override
    public int defaultSpellCooldown() {
        return 200;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        double radius = spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        worldIn.sendParticles(ModParticle.EM_PULSE.get(), caster.getX(), caster.getY() + 0.5F, caster.getZ(), 1, 0.0, 0.0, 0.0, 0);
        ScreenShake_Entity.ScreenShake(worldIn, caster.position(), 20.0F, 0.01F, 0, 20);
        this.playSound(worldIn, caster, ModSounds.EMP_ACTIVATED.get(), 4.0F, worldIn.getRandom().nextFloat() * 0.2F + 1.0F);
        for (LivingEntity livingEntity : worldIn.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(5.0F + radius))) {
            if (!MobUtil.areAllies(caster, livingEntity)) {
                if (livingEntity.hurt(CMDamageTypes.getDamageSource(worldIn, CMDamageTypes.EMP), (float) ((3 + potency) + worldIn.getRandom().nextInt(3)))) {
                    if (this.rightStaff(staff)) {
                        float chance = 0.25F;
                        if (worldIn.isThundering() && worldIn.isRainingAt(livingEntity.blockPosition())){
                            chance += 0.25F;
                        }
                        if (worldIn.getRandom().nextFloat() <= chance){
                            livingEntity.addEffect(new MobEffectInstance(GoetyEffects.SPASMS.get(), MathHelper.secondsToTicks(5)));
                        }
                    }
                }
            }
        }
    }
}
