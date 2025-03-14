package com.Polarice3.goety_cataclysm.common.magic.spells.abyss;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.AbyssMine;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AbyssalMinesSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(GCSpellConfig.AbyssalMinesRadius.get());
    }

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.AbyssalMinesCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.AbyssalMinesDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.LEVIATHAN_IDLE.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.AbyssalMinesCoolDown.get();
    }

    public SpellType getSpellType() {
        return SpellType.ABYSS;
    }

    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int range = spellStat.getRange();
        double radius = spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.0F;
        }

        HitResult hitResult = this.rayTrace(worldIn, caster, range, 3.0F);
        Vec3 vec3 = hitResult.getLocation();
        LivingEntity target = this.getTarget(caster);
        if (target != null){
            vec3 = target.position();
        }
        float f = (float) Mth.atan2(vec3.z - caster.getZ(), vec3.x - caster.getX());
        int amount = this.rightStaff(staff) ? 35 : 17;
        for(int l = 0; l < amount; ++l) {
            int j = (int)(2.0F * (float)l);
            double randomNearbyX = vec3.x + caster.getRandom().nextGaussian() * 12.0;
            double randomNearbyY = vec3.y + caster.getRandom().nextGaussian() * 8.0;
            double randomNearbyZ = vec3.z + caster.getRandom().nextGaussian() * 12.0;
            if (!worldIn.isFluidAtPosition(BlockPos.containing(randomNearbyX, randomNearbyY, randomNearbyZ), (fluidState) -> fluidState.is(FluidTags.WATER))){
                randomNearbyY = BlockFinder.moveDownToGround(caster) + 1.0D;
            }
            this.spawnMines(caster, randomNearbyX, randomNearbyY, randomNearbyZ, f, j, radius);
        }
        worldIn.playSound(null, caster, ModSounds.LEVIATHAN_STUN_ROAR.get(), this.getSoundSource(), 2.0F, 0.8F);
    }

    private void spawnMines(LivingEntity caster, double x, double y, double z, float rotation, int delay, double radius) {
        AbyssMine mine = new AbyssMine(caster.level(), x, y, z, rotation, delay, caster);
        mine.setRadius((float) radius);
        if (mine.level().noCollision(mine)) {
            caster.level().addFreshEntity(mine);
        }

    }
}
