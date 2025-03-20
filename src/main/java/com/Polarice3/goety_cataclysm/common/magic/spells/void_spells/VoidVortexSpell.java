package com.Polarice3.goety_cataclysm.common.magic.spells.void_spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.VoidVortex;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
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

public class VoidVortexSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(GCSpellConfig.VoidVortexRadius.get());
    }

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.VoidVortexCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.VoidVortexDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return GoetySounds.VOID_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.VoidVortexCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int range = spellStat.getRange();
        float radius = (float) spellStat.getRadius();
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

        double tx = vec3.x;
        double ty = vec3.y;
        double tz = vec3.z;
        double minY = Math.min(ty, caster.getY());
        double maxY = Math.max(ty, caster.getY()) + 1.0;
        float angle = (float)Mth.atan2(tz - caster.getZ(), tx - caster.getX());
        this.spawnVortex(caster, tx, tz, minY, maxY, angle, radius);
    }

    private void spawnVortex(LivingEntity caster, double x, double z, double minY, double maxY, float rotation, float radius) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = caster.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(caster.level(), blockpos1, Direction.UP)) {
                if (!caster.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = caster.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(caster.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            VoidVortex voidVortex = new VoidVortex(caster.level(), x, (double)blockpos.getY() + d0, z, rotation, caster, 300);
            voidVortex.setRadius(radius);
            caster.level().addFreshEntity(voidVortex);
        }

    }
}
