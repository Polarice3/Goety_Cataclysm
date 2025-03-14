package com.Polarice3.goety_cataclysm.common.magic.construct;

import com.Polarice3.Goety.api.magic.IMold;
import com.Polarice3.Goety.common.research.ResearchList;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.CoralssusServant;
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
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CoralssusMold implements IMold {
    private static final List<BlockPos> BOTTOM_STONE_LOCATIONS = ImmutableList.of(
            new BlockPos(0, -1, 0),

            new BlockPos(0, -1, 1),
            new BlockPos(0, -1, -1),

            new BlockPos(1, -1, 0),
            new BlockPos(-1, -1, 0),

            new BlockPos(1, -1, 1),
            new BlockPos(1, -1, -1),

            new BlockPos(-1, -1, 1),
            new BlockPos(-1, -1, -1),

            new BlockPos(2, -1, 1),
            new BlockPos(2, -1, -1),

            new BlockPos(-2, -1, 1),
            new BlockPos(-2, -1, -1),

            new BlockPos(1, -1, 2),
            new BlockPos(-1, -1, 2),

            new BlockPos(1, -1, -2),
            new BlockPos(-1, -1, -2)
    );
    private static final List<BlockPos> ABOVE_STONE_LOCATIONS = ImmutableList.of(
            new BlockPos(1, 0, 3),
            new BlockPos(-1, 0, 3),

            new BlockPos(1, 0, -3),
            new BlockPos(-1, 0, -3),

            new BlockPos(3, 0, 1),
            new BlockPos(3, 0, -1),

            new BlockPos(-3, 0, 1),
            new BlockPos(-3, 0, -1)
    );
    private static final List<BlockPos> STONE_LOCATIONS = Stream.of(BOTTOM_STONE_LOCATIONS, ABOVE_STONE_LOCATIONS).flatMap(Collection::stream).toList();
    private static final List<BlockPos> BOTTOM_AMETHYST_LOCATIONS = ImmutableList.of(
            new BlockPos(2, -1, 0),

            new BlockPos(-2, -1, 0),

            new BlockPos(0, -1, 2),

            new BlockPos(0, -1, -2)
    );
    private static final List<BlockPos> ABOVE_AMETHYST_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 3),

            new BlockPos(0, 0, -3),

            new BlockPos(3, 0, 0),

            new BlockPos(-3, 0, 0),

            new BlockPos(-2, 0, 2),
            new BlockPos(2, 0, 2),
            new BlockPos(2, 0, -2),
            new BlockPos(-2, 0, -2)
    );
    private static final List<BlockPos> AMETHYST_LOCATIONS = Stream.of(BOTTOM_AMETHYST_LOCATIONS, ABOVE_AMETHYST_LOCATIONS).flatMap(Collection::stream).toList();
    private static final List<BlockPos> WATER_LOCATIONS = ImmutableList.of(
            new BlockPos(3, -1, 1),
            new BlockPos(3, -1, 0),
            new BlockPos(3, -1, -1),

            new BlockPos(-3, -1, 1),
            new BlockPos(-3, -1, 0),
            new BlockPos(-3, -1, -1),

            new BlockPos(1, -1, 3),
            new BlockPos(0, -1, 3),
            new BlockPos(-1, -1, 3),

            new BlockPos(1, -1, -3),
            new BlockPos(0, -1, -3),
            new BlockPos(-1, -1, -3),

            new BlockPos(-2, -1, 2),
            new BlockPos(2, -1, 2),
            new BlockPos(2, -1, -2),
            new BlockPos(-2, -1, -2),

            new BlockPos(4, 0, 1),
            new BlockPos(4, 0, 0),
            new BlockPos(4, 0, -1),

            new BlockPos(1, 0, 4),
            new BlockPos(0, 0, 4),
            new BlockPos(-1, 0, 4),

            new BlockPos(-4, 0, 1),
            new BlockPos(-4, 0, 0),
            new BlockPos(-4, 0, -1),

            new BlockPos(1, 0, -4),
            new BlockPos(0, 0, -4),
            new BlockPos(-1, 0, -4),

            new BlockPos(3, 0, 2),
            new BlockPos(3, 0, -2),

            new BlockPos(-3, 0, 2),
            new BlockPos(-3, 0, -2),

            new BlockPos(2, 0, 3),
            new BlockPos(2, 0, -3),

            new BlockPos(-2, 0, 3),
            new BlockPos(-2, 0, -3)
    );
    private static final List<BlockPos> CORAL_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, -1),

            new BlockPos(1, 0, 1),
            new BlockPos(-1, 0, 1),

            new BlockPos(1, 0, -1),
            new BlockPos(-1, 0, -1),

            new BlockPos(0, 0, 2),
            new BlockPos(1, 0, 2),
            new BlockPos(-1, 0, 2),

            new BlockPos(0, 0, -2),
            new BlockPos(1, 0, -2),
            new BlockPos(-1, 0, -2),

            new BlockPos(2, 0, 0),
            new BlockPos(2, 0, 1),
            new BlockPos(2, 0, -1),

            new BlockPos(-2, 0, 0),
            new BlockPos(-2, 0, 1),
            new BlockPos(-2, 0, -1)
    );
    private static final BlockPos PICKLE = new BlockPos(0, 1, 0);

    private static List<BlockPos> checkStones(Level level, BlockPos blockPos){
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
            if (!level.getBlockState(blockPos2).is(Tags.Blocks.STORAGE_BLOCKS_AMETHYST)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkWater(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : WATER_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Blocks.WATER)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static boolean hasPickle(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos.offset(PICKLE));
        return blockState.is(Blocks.SEA_PICKLE)
                && blockState.hasProperty(SeaPickleBlock.PICKLES)
                && blockState.getValue(SeaPickleBlock.PICKLES) > 1;
    }

    private static List<BlockPos> checkFireCorals(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : CORAL_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            BlockState blockState = level.getBlockState(blockPos2);
            if (!blockState.is(Blocks.FIRE_CORAL_BLOCK)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkHornCorals(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : CORAL_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            BlockState blockState = level.getBlockState(blockPos2);
            if (!blockState.is(Blocks.HORN_CORAL_BLOCK)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkTubeCorals(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : CORAL_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            BlockState blockState = level.getBlockState(blockPos2);
            if (!blockState.is(Blocks.TUBE_CORAL_BLOCK)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    public static boolean checkCoralBlock(Level level, BlockPos blockPos, Block coral){
        if (coral == Blocks.FIRE_CORAL_BLOCK) {
            return checkFireCorals(level, blockPos).isEmpty();
        } else if (coral == Blocks.HORN_CORAL_BLOCK) {
            return checkHornCorals(level, blockPos).isEmpty();
        } else if (coral == Blocks.TUBE_CORAL_BLOCK) {
            return checkTubeCorals(level, blockPos).isEmpty();
        } else {
            return false;
        }
    }

    public static boolean checkBlocks(Level level, BlockPos blockPos, Block coral){
        return checkStones(level, blockPos).isEmpty()
                && checkAmethyst(level, blockPos).isEmpty()
                && checkCoralBlock(level, blockPos, coral)
                && hasPickle(level, blockPos)
                && checkWater(level, blockPos).isEmpty();
    }

    public static boolean conditionsMet(Level worldIn, LivingEntity entityLiving) {
        int count = 0;
        if (worldIn instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof CoralssusServant servant) {
                    if (servant.getTrueOwner() == entityLiving && servant.isAlive()) {
                        ++count;
                    }
                }
            }
        }
        return count < GCSpellConfig.CoralssusLimit.get();
    }

    @Override
    public boolean spawnServant(Player player, ItemStack stack, Level level, BlockPos blockPos){
        if (!level.isClientSide) {
            if (level.getBlockState(blockPos).is(BlockTags.CORAL_BLOCKS)) {
                Block block = level.getBlockState(blockPos).getBlock();
                if (checkBlocks(level, blockPos, level.getBlockState(blockPos).getBlock())) {
                    if (SEHelper.hasResearch(player, ResearchList.FLORAL)) {
                        if (conditionsMet(level, player)) {
                            CoralssusServant coralssusServant = GCEntityType.CORALSSUS.get().create(level);
                            if (coralssusServant != null) {
                                coralssusServant.setTrueOwner(player);
                                coralssusServant.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(coralssusServant.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                                coralssusServant.moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D, (double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
                                if (block == Blocks.FIRE_CORAL_BLOCK){
                                    coralssusServant.setVariant(CoralssusServant.Variant.FIRE);
                                } else if (block == Blocks.HORN_CORAL_BLOCK){
                                    coralssusServant.setVariant(CoralssusServant.Variant.HORN);
                                } else if (block == Blocks.TUBE_CORAL_BLOCK){
                                    coralssusServant.setVariant(CoralssusServant.Variant.TUBE);
                                }
                                if (level.addFreshEntity(coralssusServant)) {
                                    removeBlocks(level, blockPos);
                                    stack.shrink(1);
                                    if (player instanceof ServerPlayer serverPlayer) {
                                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, coralssusServant);
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
            for (int i = -4; i < 4; ++i){
                for (int j = -4; j < 4; ++j){
                    for (int k = -4; k < 4; ++k){
                        BlockPos blockPos2 = blockPos.offset(i, j, k);
                        if (level.getBlockState(blockPos2).is(Blocks.WATER)) {
                            level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                            level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
            for (BlockPos blockPos1 : CORAL_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                BlockState blockState = level.getBlockState(blockPos2);
                if (blockState.is(Blocks.FIRE_CORAL_BLOCK) || blockState.is(Blocks.HORN_CORAL_BLOCK) || blockState.is(Blocks.TUBE_CORAL_BLOCK)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
            if (level.getBlockState(blockPos.offset(PICKLE)).is(Blocks.SEA_PICKLE)){
                level.levelEvent(2001, blockPos.offset(PICKLE), Block.getId(level.getBlockState(blockPos.offset(PICKLE))));
                level.setBlockAndUpdate(blockPos.offset(PICKLE), Blocks.AIR.defaultBlockState());
            }
        }
    }
}
