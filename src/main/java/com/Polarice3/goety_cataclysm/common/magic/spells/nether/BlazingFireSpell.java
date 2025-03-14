package com.Polarice3.goety_cataclysm.common.magic.spells.nether;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.IgnisAbyssFireball;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.IgnisFireball;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
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

public class BlazingFireSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(GCSpellConfig.BlazingFireRadius.get());
    }

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.BlazingFireCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.BlazingFireDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.BlazingFireCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NETHER;
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
        float radius = (float) spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            radius += (float)WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F;
        }
        switch (worldIn.random.nextInt(5)) {
            case 0:
                if (this.rightStaff(staff)){
                    this.shootAbyssFireball(caster, new Vec3(-5.0, 3.0, 0.0), 109, potency, radius);
                    this.shootFireball(caster, new Vec3(5.0, 3.0, 0.0), 93, potency, radius);
                }
                this.shootFireball(caster, new Vec3(-2.0, 3.0, 0.0), 45, potency, radius);
                this.shootFireball(caster, new Vec3(0.0, 3.0, 0.0), 61, potency, radius);
                this.shootFireball(caster, new Vec3(2.0, 3.0, 0.0), 77, potency, radius);
                break;
            case 1:
                if (this.rightStaff(staff)){
                    this.shootFireball(caster, new Vec3(-5.0, 3.0, 0.0), 45, potency, radius);
                    this.shootFireball(caster, new Vec3(5.0, 3.0, 0.0), 93, potency, radius);
                }
                this.shootAbyssFireball(caster, new Vec3(-2.0, 3.0, 0.0), 109, potency, radius);
                this.shootFireball(caster, new Vec3(0.0, 3.0, 0.0), 61, potency, radius);
                this.shootFireball(caster, new Vec3(2.0, 3.0, 0.0), 77, potency, radius);
                break;
            case 2:
                if (this.rightStaff(staff)){
                    this.shootFireball(caster, new Vec3(-5.0, 3.0, 0.0), 45, potency, radius);
                    this.shootFireball(caster, new Vec3(5.0, 3.0, 0.0), 93, potency, radius);
                }
                this.shootFireball(caster, new Vec3(-2.0, 3.0, 0.0), 61, potency, radius);
                this.shootAbyssFireball(caster, new Vec3(0.0, 3.0, 0.0), 109, potency, radius);
                this.shootFireball(caster, new Vec3(2.0, 3.0, 0.0), 77, potency, radius);
                break;
            case 3:
                if (this.rightStaff(staff)){
                    this.shootFireball(caster, new Vec3(-5.0, 3.0, 0.0), 45, potency, radius);
                    this.shootFireball(caster, new Vec3(5.0, 3.0, 0.0), 93, potency, radius);
                }
                this.shootFireball(caster, new Vec3(-2.0, 3.0, 0.0), 61, potency, radius);
                this.shootFireball(caster, new Vec3(0.0, 3.0, 0.0), 77, potency, radius);
                this.shootAbyssFireball(caster, new Vec3(2.0, 3.0, 0.0), 109, potency, radius);
                break;
            case 4:
                if (this.rightStaff(staff)){
                    this.shootFireball(caster, new Vec3(-5.0, 3.0, 0.0), 45, potency, radius);
                    this.shootAbyssFireball(caster, new Vec3(5.0, 3.0, 0.0), 109, potency, radius);
                }
                this.shootFireball(caster, new Vec3(-2.0, 3.0, 0.0), 61, potency, radius);
                this.shootFireball(caster, new Vec3(0.0, 3.0, 0.0), 77, potency, radius);
                this.shootFireball(caster, new Vec3(2.0, 3.0, 0.0), 93, potency, radius);
        }
    }

    private void shootAbyssFireball(LivingEntity caster, Vec3 shotAt, int timer, int potency, float radius) {
        shotAt = shotAt.yRot(-caster.getYRot() * 0.017453292F);
        IgnisAbyssFireball shot = new IgnisAbyssFireball(caster.level(), caster, this.getTarget(caster));
        shot.setExtraDamage(potency);
        shot.setRadius(radius + 1.0F);
        shot.setPos(caster.getX() - (double)(caster.getBbWidth() + 1.0F) * 0.15 * (double) Mth.sin(caster.yBodyRot * 0.017453292F), caster.getY() + 1.0, caster.getZ() + (double)(caster.getBbWidth() + 1.0F) * 0.15 * (double)Mth.cos(caster.yBodyRot * 0.017453292F));
        double d0 = shotAt.x;
        double d1 = shotAt.y;
        double d2 = shotAt.z;
        float f = Mth.sqrt((float)(d0 * d0 + d2 * d2)) * 0.35F;
        shot.shoot(d0, d1 + (double)f, d2, 0.25F, 3.0F);
        shot.setUp(timer);
        caster.level().addFreshEntity(shot);
    }

    private void shootFireball(LivingEntity caster, Vec3 shotAt, int timer, int potency, float radius) {
        shotAt = shotAt.yRot(-caster.getYRot() * 0.017453292F);
        IgnisFireball shot = new IgnisFireball(caster.level(), caster, this.getTarget(caster));
        shot.setExtraDamage(potency);
        shot.setRadius(radius);
        shot.setPos(caster.getX() - (double)(caster.getBbWidth() + 1.0F) * 0.15 * (double)Mth.sin(caster.yBodyRot * 0.017453292F), caster.getY() + 1.0, caster.getZ() + (double)(caster.getBbWidth() + 1.0F) * 0.15 * (double)Mth.cos(caster.yBodyRot * 0.017453292F));
        double d0 = shotAt.x;
        double d1 = shotAt.y;
        double d2 = shotAt.z;
        float f = Mth.sqrt((float)(d0 * d0 + d2 * d2)) * 0.35F;
        shot.shoot(d0, d1 + (double)f, d2, 0.25F, 3.0F);
        shot.setUp(timer);
        if (MobUtil.healthIsHalved(caster)) {
            shot.setSoul(true);
        }

        caster.level().addFreshEntity(shot);
    }
}
