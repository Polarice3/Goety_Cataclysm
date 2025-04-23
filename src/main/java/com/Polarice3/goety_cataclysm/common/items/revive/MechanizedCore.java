package com.Polarice3.goety_cataclysm.common.items.revive;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.items.revive.ReviveServantItem;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.factory.ProwlerServant;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MechanizedCore extends ReviveServantItem {

    public MechanizedCore() {
        super(new Properties()
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .fireResistant()
                .stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        Direction direction = context.getClickedFace();
        if (!level.isClientSide) {
            if (player != null) {
                if (blockState.is(GCBlocks.MECHANIZED_IRON_BLOCK.get())) {
                    if (this.isCube(level, direction, blockPos)) {
                        if (RitualRequirements.canSummon(level, player, GCEntityType.THE_PROWLER.get())) {
                            Entity entity;
                            if (getSummon(stack, level) != null) {
                                entity = getSummon(stack, level);
                            } else {
                                entity = new ProwlerServant(GCEntityType.THE_PROWLER.get(), level);
                                IOwned owned = (IOwned) entity;
                                owned.setTrueOwner(player);
                            }
                            if (entity instanceof ProwlerServant servant && servant.getTrueOwner() == player) {
                                servant.setHealth(servant.getMaxHealth());
                                BlockPos blockPos1 = blockPos.relative(direction.getOpposite());
                                Vec3 vec3 = Vec3.atBottomCenterOf(blockPos1.below());
                                servant.setPos(vec3);
                                servant.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
                                if (level.addFreshEntity(servant)) {
                                    this.removeCube(level, direction, blockPos);
                                    servant.spawnAnim();
                                    if (level instanceof ServerLevel serverLevel) {
                                        for (int i = 0; i < 8; ++i) {
                                            ServerParticleUtil.addParticlesAroundSelf(serverLevel, ModParticleTypes.BIG_ELECTRIC.get(), servant);
                                            ServerParticleUtil.addParticlesAroundSelf(serverLevel, ParticleTypes.SMOKE, servant);
                                        }
                                    }
                                    servant.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.5F);
                                    player.swing(context.getHand());
                                    player.getCooldowns().addCooldown(this, MathHelper.secondsToTicks(30));
                                    stack.shrink(1);
                                    return InteractionResult.CONSUME;
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.useOn(context);
    }

    public boolean isCube(Level level, Direction direction, BlockPos blockPos){
        List<BlockPos> blockPosList = new ArrayList<>();
        for (BlockPos blockPos1 : cube(direction, blockPos, 1, 1, 1)){
            if (!level.getBlockState(blockPos1).is(GCBlocks.MECHANIZED_IRON_BLOCK.get())){
                blockPosList.add(blockPos1);
            }
        }
        for (BlockPos blockPos1 : cube(direction, blockPos.relative(direction.getOpposite()), 1, 1, 1)){
            if (!level.getBlockState(blockPos1).is(GCBlocks.MECHANIZED_IRON_BLOCK.get())){
                blockPosList.add(blockPos1);
            }
        }
        for (BlockPos blockPos1 : cube(direction, blockPos.relative(direction.getOpposite()).relative(direction.getOpposite()), 1, 1, 1)){
            if (!level.getBlockState(blockPos1).is(GCBlocks.MECHANIZED_IRON_BLOCK.get())){
                blockPosList.add(blockPos1);
            }
        }
        return blockPosList.isEmpty();
    }

    public void removeCube(Level level, Direction direction, BlockPos blockPos){
        for (BlockPos blockPos1 : cube(direction, blockPos, 1, 1, 1)){
            if (level.getBlockState(blockPos1).is(GCBlocks.MECHANIZED_IRON_BLOCK.get())){
                level.levelEvent(2001, blockPos1, Block.getId(level.getBlockState(blockPos1)));
                level.setBlockAndUpdate(blockPos1, Blocks.AIR.defaultBlockState());
            }
        }
        for (BlockPos blockPos1 : cube(direction, blockPos.relative(direction.getOpposite()), 1, 1, 1)){
            if (level.getBlockState(blockPos1).is(GCBlocks.MECHANIZED_IRON_BLOCK.get())){
                level.levelEvent(2001, blockPos1, Block.getId(level.getBlockState(blockPos1)));
                level.setBlockAndUpdate(blockPos1, Blocks.AIR.defaultBlockState());
            }
        }
        for (BlockPos blockPos1 : cube(direction, blockPos.relative(direction.getOpposite()).relative(direction.getOpposite()), 1, 1, 1)){
            if (level.getBlockState(blockPos1).is(GCBlocks.MECHANIZED_IRON_BLOCK.get())){
                level.levelEvent(2001, blockPos1, Block.getId(level.getBlockState(blockPos1)));
                level.setBlockAndUpdate(blockPos1, Blocks.AIR.defaultBlockState());
            }
        }
    }

    public static Iterable<BlockPos> cube(Direction direction, BlockPos blockPos, int x, int y, int z){
        boolean hasX = direction.getStepX() == 0;
        boolean hasY = direction.getStepY() == 0;
        boolean hasZ = direction.getStepZ() == 0;
        Vec3i start = new Vec3i(hasX ? -x : 0, hasY ? -y : 0, hasZ ? -z : 0);
        Vec3i end = new Vec3i(hasX ? x : 0, hasY ? (y * 2) - 1 : 0, hasZ ? z : 0);
        return BlockPos.betweenClosed(
                blockPos.offset(start),
                blockPos.offset(end));
    }
}
