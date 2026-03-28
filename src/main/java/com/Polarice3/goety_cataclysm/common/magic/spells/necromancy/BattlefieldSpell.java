package com.Polarice3.goety_cataclysm.common.magic.spells.necromancy;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.client.particles.GatherTrailParticle;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.server.SStaffParticlePacket;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.projectile.Phantom_Halberd_Entity;
import com.github.L_Ender.cataclysm.init.ModParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BattlefieldSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(0.0D);
    }

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.BattlefieldCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return 60;
    }

    @Override
    public int castDuration(LivingEntity caster, ItemStack staff) {
        return 60;
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.BattlefieldCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NECROMANCY;
    }

    @Nullable
    @Override
    public SoundEvent CastingSound(LivingEntity caster) {
        return CataclysmSounds.PHANTOM_SPEAR.get();
    }

    @Override
    public void useParticle(Level worldIn, LivingEntity caster, ItemStack stack) {
        if (worldIn instanceof ServerLevel serverLevel) {
            int range = 1;
            int color = 0x75ffe2;
            if (caster instanceof Player player) {
                for (int i = 0; i < (caster.getRandom().nextFloat() < 0.1F ? 3 : 1); i++) {
                    ModNetwork.sentToTrackingEntityAndPlayer(player, new SStaffParticlePacket(player.getId(), stack.getItem() instanceof IWand wand ? wand.getWandVisualHeight(serverLevel, player, stack) : 0.8F, range, color, caster.getUsedItemHand() == InteractionHand.OFF_HAND));
                }
            } else {
                ServerParticleUtil.gatheringParticles(new GatherTrailParticle.Option(new ColorUtil(color), caster.position().add(0, 2, 0)), caster, serverLevel, range);
            }
        }
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void useSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int castTime, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int radius = (int) spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }

        int initialCount = 44;
        int howFar = 9;
        if (this.rightStaff(staff)) {
            howFar = 14;
        }
        howFar += radius;
        int finalCount = initialCount + howFar;
        if (this.isShifting(caster)) {
            if (castTime == (finalCount - 2)) {
                this.StrikeWindmillHalberd(worldIn, caster, 6, 10, 1.0, 0.75, 0.2, 1, potency);
                this.StrikeWindmillHalberd(worldIn, caster, 4, 10, 1.0, 1.2, 0.15, 1, potency);
            }
        } else {
            for (int i = initialCount; i <= finalCount; i += 2) {
                if (castTime == i) {
                    int d = i - (initialCount - 2);
                    this.radagonskill(worldIn, caster, 0.7F, d, 1.0F, 2, potency);
                }
            }
        }
    }

    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
        if (castTime >= 37 && caster instanceof Player player) {
            if (!focus.isEmpty()) {
                SEHelper.addCooldown(player, focus.getItem(), this.spellCooldown(caster));
                SEHelper.sendSEUpdatePacket(player);
            }
        }
    }

    private void StrikeWindmillHalberd(ServerLevel level, LivingEntity caster, int numberOfBranches, int particlesPerBranch, double initialRadius, double radiusIncrement, double curveFactor, int delay, int potency) {
        float angleIncrement = (float)(6.283185307179586 / (double)numberOfBranches);

        for(int branch = 0; branch < numberOfBranches; ++branch) {
            float baseAngle = angleIncrement * (float)branch;

            for(int i = 0; i < particlesPerBranch; ++i) {
                double currentRadius = initialRadius + (double)i * radiusIncrement;
                float currentAngle = (float)((double)baseAngle + (double)((float)i * angleIncrement) / initialRadius + (double)((float)((double)i * curveFactor)));
                double xOffset = currentRadius * Math.cos((double)currentAngle);
                double zOffset = currentRadius * Math.sin((double)currentAngle);
                double spawnX = caster.getX() + xOffset;
                double spawnY = caster.getY() + 0.3;
                double spawnZ = caster.getZ() + zOffset;
                int d3 = delay * (i + 1);
                this.spawnHalberd(level, caster, spawnX, spawnZ, caster.getY() - 5.0, caster.getY() + 3.0, currentAngle, d3, potency);
                double deltaX = level.getRandom().nextGaussian() * 0.007;
                double deltaY = level.getRandom().nextGaussian() * 0.007;
                double deltaZ = level.getRandom().nextGaussian() * 0.007;
                level.sendParticles(ModParticle.PHANTOM_WING_FLAME.get(), spawnX, spawnY, spawnZ, 0, deltaX, deltaY, deltaZ, 1.0F);
            }
        }

    }

    private void radagonskill(ServerLevel level, LivingEntity caster, float spreadarc, int distance, float vec, int delay, int potency) {
        double perpFacing = caster.yHeadRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        double spread = Math.PI * spreadarc;
        int arcLen = Mth.ceil(distance * spread);

        for(int i = 0; i < arcLen; ++i) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = caster.getX() + vx * distance + vec * Math.cos((caster.yHeadRot + 90) * Math.PI / 180);
            double pz = caster.getZ() + vz * distance + vec * Math.sin((caster.yHeadRot + 90) * Math.PI / 180);
            int hitX = Mth.floor(px);
            int hitZ = Mth.floor(pz);
            this.spawnHalberd(level, caster, hitX + 0.5D, hitZ + 0.5D, caster.getY() -5, caster.getY() + 3, (float) theta, delay, potency);
            this.radagonparticle(level, caster, hitX + 0.5D, hitZ + 0.5D, caster.getY() -5, caster.getY() + 3);
        }

    }

    private void radagonparticle(ServerLevel level, LivingEntity caster, double x, double z, double minY, double maxY) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(level, blockpos1, Direction.UP)) {
                if (!level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(level, blockpos);
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
            for(int i1 = 0; i1 < 4; ++i1) {
                double DeltaMovementX = level.getRandom().nextGaussian() * 0.007;
                double DeltaMovementY = level.getRandom().nextGaussian() * 0.007;
                double DeltaMovementZ = level.getRandom().nextGaussian() * 0.007;
                float angle = 0.017453292F * caster.yHeadRot + (float)i1;
                double extraX = (double)(0.35F * Mth.sin((float)(Math.PI + (double)angle)));
                double extraY = 0.30000001192092896;
                double extraZ = (double)(0.35F * Mth.cos(angle));
                level.sendParticles(ModParticle.PHANTOM_WING_FLAME.get(), x + extraX, (double)blockpos.getY() + d0 + extraY, z + extraZ, 0, DeltaMovementX, DeltaMovementY, DeltaMovementZ, 1.0F);
            }
        }

    }

    private void StrikeHalberd(ServerLevel level, LivingEntity caster, int rune, float close, float radius, double range, int delay, int potency) {
        float angle2 = 0.017453292F * caster.yHeadRot;

        for(int k = 0; k < rune; ++k) {
            float f2 = angle2 + (float)k * 3.1415927F * 2.0F / close + 6.2831855F / radius;
            this.spawnHalberd(level, caster, caster.getX() + (double)Mth.cos(f2) * range, caster.getZ() + (double)Mth.sin(f2) * range, caster.getY() - 5.0, caster.getY() + 3.0, f2, delay, potency);
            for(int i1 = 0; i1 < 6 + level.getRandom().nextInt(2); ++i1) {
                double DeltaMovementX = level.getRandom().nextGaussian() * 0.007;
                double DeltaMovementY = level.getRandom().nextGaussian() * 0.007;
                double DeltaMovementZ = level.getRandom().nextGaussian() * 0.007;
                float angle = 0.017453292F * caster.yHeadRot + (float)i1;
                double extraX = (double)(0.5F * Mth.sin((float)(Math.PI + (double)angle)));
                double extraY = 0.30000001192092896;
                double extraZ = (double)(0.5F * Mth.cos(angle));
                level.sendParticles(ModParticle.PHANTOM_WING_FLAME.get(), caster.getX() + (double)Mth.cos(f2) * range + extraX, caster.getY() + extraY, caster.getZ() + (double)Mth.sin(f2) * range + extraZ, 0, DeltaMovementX, DeltaMovementY, DeltaMovementZ, 1.0F);
            }
        }
    }

    private void spawnHalberd(ServerLevel level, LivingEntity caster, double x, double z, double minY, double maxY, float rotation, int delay, int potency) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(level, blockpos1, Direction.UP)) {
                if (!level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(level, blockpos);
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
            level.addFreshEntity(new Phantom_Halberd_Entity(level, x, (double)blockpos.getY() + d0, z, rotation, delay, caster, GCSpellConfig.BattlefieldDamage.get().floatValue() + potency));
        }

    }
}
