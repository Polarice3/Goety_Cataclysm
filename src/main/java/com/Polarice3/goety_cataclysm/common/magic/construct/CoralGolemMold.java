package com.Polarice3.goety_cataclysm.common.magic.construct;

import com.Polarice3.Goety.api.magic.IMold;
import com.Polarice3.Goety.common.research.ResearchList;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.CoralGolemServant;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GCTags;
import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class CoralGolemMold implements IMold {
    private static final List<BlockPos> STONE_LOCATIONS = ImmutableList.of(
            new BlockPos(2, 0, 0),
            new BlockPos(2, 0, 1),
            new BlockPos(2, 0, -1),

            new BlockPos(-2, 0, 0),
            new BlockPos(-2, 0, 1),
            new BlockPos(-2, 0, -1),

            new BlockPos(0, 0, 2),
            new BlockPos(1, 0, 2),
            new BlockPos(-1, 0, 2),

            new BlockPos(0, 0, -2),
            new BlockPos(1, 0, -2),
            new BlockPos(-1, 0, -2)
    );
    private static final List<BlockPos> AMETHYST_LOCATIONS = ImmutableList.of(
            new BlockPos(0, -1, 0),

            new BlockPos(0, -1, 1),
            new BlockPos(0, -1, -1),

            new BlockPos(1, -1, 0),
            new BlockPos(-1, -1, 0),

            new BlockPos(1, -1, 1),
            new BlockPos(1, -1, -1),

            new BlockPos(-1, -1, 1),
            new BlockPos(-1, -1, -1)
    );
    private static final List<BlockPos> CORAL_LOCATIONS = ImmutableList.of(
            new BlockPos(1, 0, 1),
            new BlockPos(1, 0, -1),
            new BlockPos(-1, 0, 1),
            new BlockPos(-1, 0, -1),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1),
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, 0)
    );
    private static final BlockPos COPPER = new BlockPos(0, 0, 0);

    private static List<BlockPos> checkStone(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : STONE_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            BlockState blockState = level.getBlockState(blockPos2);
            if (!blockState.is(GCTags.Blocks.CORALSSUS_BRICK_MOLD)){
                invalid.add(blockPos1);
            }
            if (!blockState.isCollisionShapeFullBlock(level, blockPos2)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkAmethyst(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : AMETHYST_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Blocks.AMETHYST_BLOCK)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkCorals(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : CORAL_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(BlockTags.CORAL_BLOCKS)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    public static boolean checkBlocks(Level level, BlockPos blockPos){
        return checkStone(level, blockPos).isEmpty()
                && checkAmethyst(level, blockPos).isEmpty()
                && checkCorals(level, blockPos).isEmpty();
    }

    public static boolean conditionsMet(Level worldIn, LivingEntity entityLiving) {
        int count = 0;
        if (worldIn instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof CoralGolemServant servant) {
                    if (servant.getTrueOwner() == entityLiving && servant.isAlive()) {
                        ++count;
                    }
                }
            }
        }
        return count < GCSpellConfig.CoralGolemLimit.get();
    }

    @Override
    public boolean spawnServant(Player player, ItemStack stack, Level level, BlockPos blockPos) {
        if (!level.isClientSide) {
            if (level.getBlockState(blockPos).is(Tags.Blocks.STORAGE_BLOCKS_COPPER)) {
                if (checkBlocks(level, blockPos)) {
                    if (SEHelper.hasResearch(player, ResearchList.FLORAL)) {
                        if (conditionsMet(level, player)) {
                            CoralGolemServant servant = GCEntityType.CORAL_GOLEM.get().create(level);
                            if (servant != null) {
                                servant.setTrueOwner(player);
                                servant.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(servant.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                                servant.moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D, (double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
                                if (level.addFreshEntity(servant)) {
                                    removeBlocks(level, blockPos);
                                    stack.shrink(1);
                                    if (player instanceof ServerPlayer serverPlayer) {
                                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, servant);
                                    }
                                    return true;
                                }
                            }
                        } else {
                            player.displayClientMessage(Component.translatable("info.goety.summon.limit"), true);
                        }
                    } else {
                        player.displayClientMessage(Component.translatable("info.goety.research.fail"), true);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("info.goety.block.fail"), true);
                }
            }
        }
        return false;
    }

    public static void removeBlocks(Level level, BlockPos blockPos){
        if (!level.isClientSide) {
            for (BlockPos blockPos1 : CORAL_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getBlockState(blockPos2).is(BlockTags.CORAL_BLOCKS)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
            BlockPos blockPos2 = blockPos.offset(COPPER);
            if (level.getBlockState(blockPos2).is(Blocks.COPPER_BLOCK)) {
                level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
            }
        }
    }
}
