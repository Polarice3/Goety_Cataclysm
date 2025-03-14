package com.Polarice3.goety_cataclysm.common.magic.spells.geomancy;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.entity.projectile.Ancient_Desert_Stele_Entity;
import com.github.L_Ender.cataclysm.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DesertCrushSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.DesertCrushCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.DesertCrushDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.WADJET_AMBIENT.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.DesertCrushCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
        }

        HitResult hitResult = this.rayTrace(worldIn, caster, range, 3.0F);
        Vec3 vec3 = hitResult.getLocation();
        LivingEntity target = this.getTarget(caster);
        if (target != null){
            vec3 = target.position();
        }

        double d1 = vec3.y;
        float f = (float) Mth.atan2(vec3.z - caster.getZ(), vec3.x - caster.getX());

        for(int k = 0; k < 8; ++k) {
            float f6 = f + (float)k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
            this.spawnSpikeLine(caster, caster.getX() + (double)Mth.cos(f6) * 4.5, caster.getZ() + (double)Mth.sin(f6) * 4.5, d1, f6, 3, potency);
        }

        for(int k = 0; k < 13; ++k) {
            float f6 = f + (float)k * 3.1415927F * 2.0F / 13.0F + 0.62831855F;
            this.spawnSpikeLine(caster, caster.getX() + (double)Mth.cos(f6) * 6.5, caster.getZ() + (double)Mth.sin(f6) * 6.5, d1, f6, 10, potency);
        }

        for(int k = 0; k < 16; ++k) {
            float f6 = f + (float)k * 3.1415927F * 2.0F / 16.0F + 0.31415927F;
            this.spawnSpikeLine(caster, caster.getX() + (double)Mth.cos(f6) * 8.5, caster.getZ() + (double)Mth.sin(f6) * 8.5, d1, f6, 15, potency);
        }

        if (this.rightStaff(staff)){
            for(int k = 0; k < 19; ++k) {
                float f6 = f + (float)k * 3.1415927F * 2.0F / 19.0F + 0.15707964F;
                this.spawnSpikeLine(caster, caster.getX() + (double)Mth.cos(f6) * 10.5, caster.getZ() + (double)Mth.sin(f6) * 10.5, d1, f6, 20, potency);
            }

            for(int k = 0; k < 24; ++k) {
                float f6 = f + (float)k * 3.1415927F * 2.0F / 24.0F + 0.07853982F;
                this.spawnSpikeLine(caster, caster.getX() + (double)Mth.cos(f6) * 12.5, caster.getZ() + (double)Mth.sin(f6) * 12.5, d1, f6, 30, potency);
            }
        }
    }

    private void spawnSpikeLine(LivingEntity caster, double posX, double posZ, double posY, float rotation, int delay, int potency) {
        BlockPos blockpos = BlockPos.containing(posX, posY, posZ);
        double d0 = 0.0;

        do {
            BlockPos blockpos1 = blockpos.above();
            BlockState blockstate = caster.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(caster.level(), blockpos1, Direction.DOWN)) {
                if (!caster.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = caster.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(caster.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }
                break;
            }

            blockpos = blockpos.above();
        } while(blockpos.getY() < Math.min(caster.level().getMaxBuildHeight(), caster.getBlockY() + 12));

        caster.level().addFreshEntity(new Ancient_Desert_Stele_Entity(caster.level(), posX, (double)blockpos.getY() + d0 - 3.0, posZ, rotation, delay, (GCSpellConfig.DesertCrushDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get()) + potency, caster));
    }
}
