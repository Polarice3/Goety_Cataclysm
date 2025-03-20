package com.Polarice3.goety_cataclysm.common.magic.spells.void_spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import com.github.L_Ender.cataclysm.entity.projectile.Void_Rune_Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VoidRuneSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.VoidRuneCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return GCSpellConfig.VoidRuneDuration.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return GoetySounds.VOID_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.VoidRuneCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
        }

        if (!this.rightStaff(staff)){
            int standingOnY = Mth.floor(caster.getY()) - 1;
            double headY = caster.getY() + 1.0D;
            float yawRadians = (float) (Math.toRadians(90 + caster.getYRot()));
            if (this.isShifting(caster)) {
                for (int i = 0; i < 5; i++) {
                    float mulPosedYaw = yawRadians + (float) i * (float) Math.PI * 0.4F;
                    this.spawnFangs(caster.getX() + (double) Mth.cos(mulPosedYaw) * 1.5D, headY, caster.getZ() + (double) Mth.sin(mulPosedYaw) * 1.5D, standingOnY, mulPosedYaw, 0, caster, potency);
                }
                for (int k = 0; k < 8; k++) {
                    float mulPosedYaw = yawRadians + (float) k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
                    this.spawnFangs(caster.getX() + (double) Mth.cos(mulPosedYaw) * 2.5D, headY, caster.getZ() + (double) Mth.sin(mulPosedYaw) * 2.5D, standingOnY, mulPosedYaw, 3, caster, potency);
                }
            } else {
                for (int l = 0; l < 10; l++) {
                    double d2 = 1.25D * (double) (l + 1);
                    this.spawnFangs(caster.getX() + (double) Mth.cos(yawRadians) * d2, headY, caster.getZ() + (double) Mth.sin(yawRadians) * d2, standingOnY, yawRadians, l, caster, potency);
                }
            }
        } else {
            HitResult hitResult = this.rayTrace(worldIn, caster, range, 3.0F);
            Vec3 vec3 = hitResult.getLocation();

            double d0 = Math.min(vec3.y, caster.getY());
            double d1 = Math.max(vec3.y, caster.getY()) + 1.0;
            float f = (float)Mth.atan2(vec3.z - caster.getZ(), vec3.x - caster.getX());

            LivingEntity target = this.getTarget(caster);
            if (target != null){
                d0 = Math.min(target.getY(), caster.getY());
                d1 = Math.max(target.getY(), caster.getY()) + 1.0;
                f = (float)Mth.atan2(target.getZ() - caster.getZ(), target.getX() - caster.getX());
            }
            float f2 = Mth.cos(caster.getYRot() * 0.017453292F) * 2.0F;
            float f3 = Mth.sin(caster.getYRot() * 0.017453292F) * 2.0F;

            for (int k = 0; k < 10; ++k) {
                double d2 = 1.5 * (double)(k + 1);
                int j = (int)(1.25F * (float)k);
                this.spawnFangs(caster.getX() + (double)f2 + (double)Mth.cos(f) * d2, caster.getZ() + (double)f3 + (double)Mth.sin(f) * d2, d0, d1, f, j, caster, potency);
                this.spawnFangs(caster.getX() - (double)f2 + (double)Mth.cos(f) * d2, caster.getZ() - (double)f3 + (double)Mth.sin(f) * d2, d0, d1, f, j, caster, potency);
            }

            for (int k = 0; k < 6; ++k) {
                float f4 = f + (float)k * 3.1415927F * 2.0F / 6.0F + 0.83775806F;
                this.spawnFangs(caster.getX() + (double)Mth.cos(f4) * 2.5, caster.getZ() + (double)Mth.sin(f4) * 2.5, d0, d1, f2, 5, caster, potency);
            }

            for (int k = 0; k < 8; ++k) {
                this.spawnFangs(caster.getX() + caster.getRandom().nextGaussian() * 4.5, caster.getZ() + caster.getRandom().nextGaussian() * 4.5, d0, d1, f3, 15, caster, potency);
            }
        }
    }

    private void spawnFangs(double x, double y, double z, int lowestYCheck, float yRot, int warmupDelayTicks, LivingEntity caster, int potency) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
        boolean flag = false;
        double d0 = 0.0D;

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
        } while (blockpos.getY() >= lowestYCheck);

        if (flag) {
            caster.level().addFreshEntity(new Void_Rune_Entity(caster.level(), x, (double) blockpos.getY() + d0, z, yRot, warmupDelayTicks, (GCSpellConfig.VoidRuneDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get()) + potency, caster));
        }
    }

    private void spawnFangs(double x, double z, double minY, double y, float rotation, int delay, LivingEntity caster, int potency) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
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
            caster.level().addFreshEntity(new Void_Rune_Entity(caster.level(), x, (double)blockpos.getY() + d0, z, rotation, delay, (GCSpellConfig.VoidRuneDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get()) + potency, caster));
        }

    }
}
