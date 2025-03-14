package com.Polarice3.goety_cataclysm.common.magic.construct;

import com.Polarice3.Goety.api.magic.IMold;
import com.Polarice3.Goety.common.research.ResearchList;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.goety_cataclysm.common.blocks.CataclysmBlocks;
import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.EnderGolemServant;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GCTags;
import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class EnderGolemMold implements IMold {
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
    private static final List<BlockPos> BOTTOM_DIAMOND_LOCATIONS = ImmutableList.of(
            new BlockPos(2, -1, 0),

            new BlockPos(-2, -1, 0),

            new BlockPos(0, -1, 2),

            new BlockPos(0, -1, -2)
    );
    private static final List<BlockPos> ABOVE_DIAMOND_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 3),

            new BlockPos(0, 0, -3),

            new BlockPos(3, 0, 0),

            new BlockPos(-3, 0, 0),

            new BlockPos(-2, 0, 2),
            new BlockPos(2, 0, 2),
            new BlockPos(2, 0, -2),
            new BlockPos(-2, 0, -2)
    );
    private static final List<BlockPos> DIAMOND_LOCATIONS = Stream.of(BOTTOM_DIAMOND_LOCATIONS, ABOVE_DIAMOND_LOCATIONS).flatMap(Collection::stream).toList();
    private static final List<BlockPos> LAVA_LOCATIONS = ImmutableList.of(
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
    private static final List<BlockPos> VOID_STONE_LOCATIONS = ImmutableList.of(
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

    private static List<BlockPos> checkStones(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : STONE_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            BlockState blockState = level.getBlockState(blockPos2);
            if (!blockState.is(GCTags.Blocks.ENDER_GOLEM_OBSIDIAN_MOLD)){
                invalid.add(blockPos1);
            }
            if (!blockState.isCollisionShapeFullBlock(level, blockPos2)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkDiamonds(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : DIAMOND_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(GCBlocks.VOID_MOLD_BLOCK.get())){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkLava(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : LAVA_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Blocks.LAVA)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkVoidStones(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : VOID_STONE_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(CataclysmBlocks.VOID_STONE.get())){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    public static boolean checkBlocks(Level level, BlockPos blockPos){
        return checkStones(level, blockPos).isEmpty() && checkDiamonds(level, blockPos).isEmpty()
                && checkVoidStones(level, blockPos).isEmpty();
    }

    public static boolean conditionsMet(Level worldIn, LivingEntity entityLiving) {
        int count = 0;
        if (worldIn instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof EnderGolemServant servant) {
                    if (servant.getTrueOwner() == entityLiving && servant.isAlive()) {
                        ++count;
                    }
                }
            }
        }
        return count < GCSpellConfig.EnderGolemLimit.get();
    }

    @Override
    public boolean spawnServant(Player player, ItemStack stack, Level level, BlockPos blockPos){
        if (!level.isClientSide) {
            if (level.getBlockState(blockPos).is(CataclysmBlocks.VOID_STONE.get())) {
                if (checkBlocks(level, blockPos)) {
                    if (SEHelper.hasResearch(player, ResearchList.WARRED)) {
                        if (conditionsMet(level, player)) {
                            EnderGolemServant enderGolemServant = GCEntityType.ENDER_GOLEM.get().create(level);
                            if (enderGolemServant != null) {
                                enderGolemServant.setTrueOwner(player);
                                enderGolemServant.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(enderGolemServant.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                                enderGolemServant.moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D, (double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
                                if (level.addFreshEntity(enderGolemServant)) {
                                    removeBlocks(level, blockPos);
                                    stack.shrink(1);
                                    if (player instanceof ServerPlayer serverPlayer) {
                                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, enderGolemServant);
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
            /*for (BlockPos blockPos1 : LAVA_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getBlockState(blockPos2).is(Blocks.LAVA)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }*/
            for (BlockPos blockPos1 : VOID_STONE_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getBlockState(blockPos2).is(CataclysmBlocks.VOID_STONE.get())) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }
}
