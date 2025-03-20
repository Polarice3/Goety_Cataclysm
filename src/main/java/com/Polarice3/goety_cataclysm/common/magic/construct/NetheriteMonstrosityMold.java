package com.Polarice3.goety_cataclysm.common.magic.construct;

import com.Polarice3.Goety.api.magic.IMold;
import com.Polarice3.Goety.common.research.ResearchList;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.goety_cataclysm.common.blocks.GoetyBlocks;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.NetheriteMonstrosityServant;
import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class NetheriteMonstrosityMold implements IMold {
    private static final List<BlockPos> BOTTOM_STONE_LOCATIONS = ImmutableList.of(
            new BlockPos(0, -1, 0),
            new BlockPos(0, -1, 2),
            new BlockPos(0, -1, 3),
            new BlockPos(0, -1, 4),
            new BlockPos(0, -1, -2),
            new BlockPos(0, -1, -3),
            new BlockPos(0, -1, -4),

            new BlockPos(1, -1, 1),
            new BlockPos(1, -1, -1),
            new BlockPos(1, -1, 2),
            new BlockPos(1, -1, -2),
            new BlockPos(1, -1, 3),
            new BlockPos(1, -1, -3),
            new BlockPos(1, -1, 5),
            new BlockPos(1, -1, -5),

            new BlockPos(-1, -1, 1),
            new BlockPos(-1, -1, -1),
            new BlockPos(-1, -1, 2),
            new BlockPos(-1, -1, -2),
            new BlockPos(-1, -1, 3),
            new BlockPos(-1, -1, -3),
            new BlockPos(-1, -1, 5),
            new BlockPos(-1, -1, -5),

            new BlockPos(2, -1, 0),
            new BlockPos(2, -1, 1),
            new BlockPos(2, -1, -1),
            new BlockPos(2, -1, 2),
            new BlockPos(2, -1, -2),
            new BlockPos(2, -1, 3),
            new BlockPos(2, -1, -3),
            new BlockPos(2, -1, 4),
            new BlockPos(2, -1, -4),

            new BlockPos(-2, -1, 0),
            new BlockPos(-2, -1, 1),
            new BlockPos(-2, -1, -1),
            new BlockPos(-2, -1, 2),
            new BlockPos(-2, -1, -2),
            new BlockPos(-2, -1, 3),
            new BlockPos(-2, -1, -3),
            new BlockPos(-2, -1, 4),
            new BlockPos(-2, -1, -4),

            new BlockPos(3, -1, 0),
            new BlockPos(3, -1, 1),
            new BlockPos(3, -1, -1),
            new BlockPos(3, -1, 2),
            new BlockPos(3, -1, -2),
            new BlockPos(3, -1, 3),
            new BlockPos(3, -1, -3),
            new BlockPos(3, -1, 4),
            new BlockPos(3, -1, -4),

            new BlockPos(-3, -1, 0),
            new BlockPos(-3, -1, 1),
            new BlockPos(-3, -1, -1),
            new BlockPos(-3, -1, 2),
            new BlockPos(-3, -1, -2),
            new BlockPos(-3, -1, 3),
            new BlockPos(-3, -1, -3),
            new BlockPos(-3, -1, 4),
            new BlockPos(-3, -1, -4),

            new BlockPos(4, -1, 0),
            new BlockPos(4, -1, 2),
            new BlockPos(4, -1, -2),
            new BlockPos(4, -1, 3),
            new BlockPos(4, -1, -3),

            new BlockPos(-4, -1, 0),
            new BlockPos(-4, -1, 2),
            new BlockPos(-4, -1, -2),
            new BlockPos(-4, -1, 3),
            new BlockPos(-4, -1, -3),

            new BlockPos(5, -1, 1),
            new BlockPos(5, -1, -1),

            new BlockPos(-5, -1, 1),
            new BlockPos(-5, -1, -1)
    );
    private static final List<BlockPos> ABOVE_STONE_LOCATIONS = ImmutableList.of(
            new BlockPos(1, 0, 6),
            new BlockPos(1, 0, -6),

            new BlockPos(3, 0, 5),
            new BlockPos(3, 0, -5),

            new BlockPos(-3, 0, 5),
            new BlockPos(-3, 0, -5),

            new BlockPos(5, 0, 3),
            new BlockPos(5, 0, -3),

            new BlockPos(-5, 0, 3),
            new BlockPos(-5, 0, -3),

            new BlockPos(6, 0, 1),
            new BlockPos(6, 0, -1),

            new BlockPos(-6, 0, 1),
            new BlockPos(-6, 0, -1)
    );
    private static final List<BlockPos> STONE_LOCATIONS = Stream.of(BOTTOM_STONE_LOCATIONS, ABOVE_STONE_LOCATIONS).flatMap(Collection::stream).toList();
    private static final List<BlockPos> BOTTOM_GILDED_LOCATIONS = ImmutableList.of(
            new BlockPos(0, -1, 1),
            new BlockPos(0, -1, -1),
            new BlockPos(0, -1, 5),
            new BlockPos(0, -1, -5),

            new BlockPos(1, -1, 0),
            new BlockPos(1, -1, 4),
            new BlockPos(1, -1, -4),

            new BlockPos(-1, -1, 0),
            new BlockPos(-1, -1, 4),
            new BlockPos(-1, -1, -4),

            new BlockPos(2, -1, 5),
            new BlockPos(2, -1, -5),

            new BlockPos(-2, -1, 5),
            new BlockPos(-2, -1, -5),

            new BlockPos(4, -1, 1),
            new BlockPos(4, -1, -1),

            new BlockPos(-4, -1, 1),
            new BlockPos(-4, -1, -1),

            new BlockPos(5, -1, 0),
            new BlockPos(5, -1, 2),
            new BlockPos(5, -1, -2),

            new BlockPos(-5, -1, 0),
            new BlockPos(-5, -1, 2),
            new BlockPos(-5, -1, -2)
    );
    private static final List<BlockPos> ABOVE_GILDED_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 6),
            new BlockPos(0, 0, -6),

            new BlockPos(2, 0, 6),
            new BlockPos(2, 0, -6),

            new BlockPos(-2, 0, 6),
            new BlockPos(-2, 0, -6),

            new BlockPos(4, 0, 4),
            new BlockPos(4, 0, -4),

            new BlockPos(-4, 0, 4),
            new BlockPos(-4, 0, -4),

            new BlockPos(6, 0, 0),
            new BlockPos(6, 0, 2),
            new BlockPos(6, 0, -2),

            new BlockPos(-6, 0, 0),
            new BlockPos(-6, 0, 2),
            new BlockPos(-6, 0, -2)
    );
    private static final List<BlockPos> GILDED_LOCATIONS = Stream.of(BOTTOM_GILDED_LOCATIONS, ABOVE_GILDED_LOCATIONS).flatMap(Collection::stream).toList();
    private static final List<BlockPos> BOTTOM_SOUL_FIRE_LOCATIONS = ImmutableList.of(
            new BlockPos(0, -1, 6),
            new BlockPos(0, -1, -6),

            new BlockPos(1, -1, 6),
            new BlockPos(1, -1, -6),

            new BlockPos(-1, -1, 6),
            new BlockPos(-1, -1, -6),

            new BlockPos(2, -1, 6),
            new BlockPos(2, -1, -6),

            new BlockPos(-2, -1, 6),
            new BlockPos(-2, -1, -6),

            new BlockPos(3, -1, 5),
            new BlockPos(3, -1, -5),

            new BlockPos(-3, -1, 5),
            new BlockPos(-3, -1, -5),

            new BlockPos(4, -1, 4),
            new BlockPos(4, -1, -4),

            new BlockPos(-4, -1, 4),
            new BlockPos(-4, -1, -4),

            new BlockPos(5, -1, 3),
            new BlockPos(5, -1, -3),

            new BlockPos(-5, -1, 3),
            new BlockPos(-5, -1, -3),

            new BlockPos(6, -1, 0),
            new BlockPos(6, -1, 1),
            new BlockPos(6, -1, -1),
            new BlockPos(6, -1, 2),
            new BlockPos(6, -1, -2),

            new BlockPos(-6, -1, 0),
            new BlockPos(-6, -1, 1),
            new BlockPos(-6, -1, -1),
            new BlockPos(-6, -1, 2),
            new BlockPos(-6, -1, -2)
    );
    private static final List<BlockPos> ABOVE_SOUL_FIRE_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 7),
            new BlockPos(0, 0, -7),

            new BlockPos(1, 0, 7),
            new BlockPos(1, 0, -7),

            new BlockPos(-1, 0, 7),
            new BlockPos(-1, 0, -7),

            new BlockPos(2, 0, 7),
            new BlockPos(2, 0, -7),

            new BlockPos(-2, 0, 7),
            new BlockPos(-2, 0, -7),

            new BlockPos(3, 0, 6),
            new BlockPos(3, 0, -6),

            new BlockPos(-3, 0, 6),
            new BlockPos(-3, 0, -6),

            new BlockPos(4, 0, 5),
            new BlockPos(4, 0, -5),

            new BlockPos(-4, 0, 5),
            new BlockPos(-4, 0, -5),

            new BlockPos(5, 0, 4),
            new BlockPos(5, 0, -4),

            new BlockPos(-5, 0, 4),
            new BlockPos(-5, 0, -4),

            new BlockPos(6, 0, 3),
            new BlockPos(6, 0, -3),

            new BlockPos(-6, 0, 3),
            new BlockPos(-6, 0, -3),

            new BlockPos(7, 0, 0),
            new BlockPos(7, 0, 1),
            new BlockPos(7, 0, -1),
            new BlockPos(7, 0, 2),
            new BlockPos(7, 0, -2),

            new BlockPos(-7, 0, 0),
            new BlockPos(-7, 0, 1),
            new BlockPos(-7, 0, -1),
            new BlockPos(-7, 0, 2),
            new BlockPos(-7, 0, -2)
    );
    private static final List<BlockPos> SOUL_FIRE_LOCATIONS = Stream.of(BOTTOM_SOUL_FIRE_LOCATIONS, ABOVE_SOUL_FIRE_LOCATIONS).flatMap(Collection::stream).toList();
    private static final List<BlockPos> REDSTONE_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, 2),
            new BlockPos(0, 0, -1),
            new BlockPos(0, 0, -2),

            new BlockPos(1, 0, 0),
            new BlockPos(2, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(-2, 0, 0),

            new BlockPos(1, 0, 1),
            new BlockPos(1, 0, -1),
            new BlockPos(-1, 0, 1),
            new BlockPos(-1, 0, -1),

            new BlockPos(1, 0, 2),
            new BlockPos(1, 0, -2),
            new BlockPos(-1, 0, 2),
            new BlockPos(-1, 0, -2),

            new BlockPos(2, 0, 1),
            new BlockPos(2, 0, -1),
            new BlockPos(-2, 0, 1),
            new BlockPos(-2, 0, -1)
    );
    private static final List<BlockPos> NETHER_BRICKS_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 3),
            new BlockPos(0, 0, -3),
            new BlockPos(3, 0, 0),
            new BlockPos(-3, 0, 0),

            new BlockPos(1, 0, 3),
            new BlockPos(1, 0, -3),
            new BlockPos(-1, 0, 3),
            new BlockPos(-1, 0, -3),

            new BlockPos(3, 0, 1),
            new BlockPos(3, 0, -1),
            new BlockPos(-3, 0, 1),
            new BlockPos(-3, 0, -1),

            new BlockPos(2, 0, 2),
            new BlockPos(2, 0, -2),
            new BlockPos(-2, 0, 2),
            new BlockPos(-2, 0, -2)
    );
    private static final List<BlockPos> NETHERITE_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 4),
            new BlockPos(0, 0, -4),
            new BlockPos(4, 0, 0),
            new BlockPos(-4, 0, 0),

            new BlockPos(1, 0, 4),
            new BlockPos(1, 0, -4),
            new BlockPos(4, 0, 1),
            new BlockPos(-4, 0, 1),

            new BlockPos(-1, 0, 4),
            new BlockPos(-1, 0, -4),
            new BlockPos(4, 0, -1),
            new BlockPos(-4, 0, -1),

            new BlockPos(2, 0, 3),
            new BlockPos(2, 0, -3),
            new BlockPos(3, 0, 2),
            new BlockPos(-3, 0, 2),

            new BlockPos(-2, 0, 3),
            new BlockPos(-2, 0, -3),
            new BlockPos(3, 0, -2),
            new BlockPos(-3, 0, -2)
    );
    private static final List<BlockPos> LAVA_LOCATIONS = ImmutableList.of(
            new BlockPos(0, 0, 5),
            new BlockPos(0, 0, -5),
            new BlockPos(5, 0, 0),
            new BlockPos(-5, 0, 0),

            new BlockPos(1, 0, 5),
            new BlockPos(1, 0, -5),
            new BlockPos(5, 0, 1),
            new BlockPos(-5, 0, 1),

            new BlockPos(-1, 0, 5),
            new BlockPos(-1, 0, -5),
            new BlockPos(5, 0, -1),
            new BlockPos(-5, 0, -1),

            new BlockPos(2, 0, 5),
            new BlockPos(2, 0, -5),
            new BlockPos(5, 0, 2),
            new BlockPos(-5, 0, 2),

            new BlockPos(-2, 0, 5),
            new BlockPos(-2, 0, -5),
            new BlockPos(5, 0, -2),
            new BlockPos(-5, 0, -2),

            new BlockPos(2, 0, 4),
            new BlockPos(2, 0, -4),
            new BlockPos(4, 0, 2),
            new BlockPos(-4, 0, 2),

            new BlockPos(-2, 0, 4),
            new BlockPos(-2, 0, -4),
            new BlockPos(4, 0, -2),
            new BlockPos(-4, 0, -2),

            new BlockPos(3, 0, 4),
            new BlockPos(3, 0, -4),
            new BlockPos(4, 0, 3),
            new BlockPos(-4, 0, 3),

            new BlockPos(-3, 0, 4),
            new BlockPos(-3, 0, -4),
            new BlockPos(4, 0, -3),
            new BlockPos(-4, 0, -3),

            new BlockPos(3, 0, 3),
            new BlockPos(3, 0, -3),
            new BlockPos(-3, 0, 3),
            new BlockPos(-3, 0, -3)
    );
    private static final BlockPos CORE = new BlockPos(0, 0, 0);


    private static List<BlockPos> checkStones(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : STONE_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            BlockState blockState = level.getBlockState(blockPos2);
            if (!blockState.getBlock().getDescriptionId().contains("blackstone") && !blockState.is(Blocks.GILDED_BLACKSTONE)){
                invalid.add(blockPos1);
            }
            if (!blockState.isCollisionShapeFullBlock(level, blockPos2)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkGilds(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : GILDED_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Blocks.GILDED_BLACKSTONE)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkSoulFire(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : SOUL_FIRE_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Blocks.SOUL_FIRE)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkRedstone(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : REDSTONE_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Tags.Blocks.STORAGE_BLOCKS_REDSTONE)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkNetherBricks(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : NETHER_BRICKS_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Blocks.NETHER_BRICKS)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkNetherite(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : NETHERITE_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getBlockState(blockPos2).is(Tags.Blocks.STORAGE_BLOCKS_NETHERITE)){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    private static List<BlockPos> checkLava(Level level, BlockPos blockPos){
        List<BlockPos> invalid = new ArrayList<>();
        for (BlockPos blockPos1 : LAVA_LOCATIONS){
            BlockPos blockPos2 = blockPos.offset(blockPos1);
            if (!level.getFluidState(blockPos2).is(FluidTags.LAVA)){
                invalid.add(blockPos1);
            }
            if (!level.getFluidState(blockPos2).isSource()){
                invalid.add(blockPos1);
            }
        }
        return invalid;
    }

    public static boolean checkBlocks(Level level, BlockPos blockPos){
        return checkStones(level, blockPos).isEmpty()
                && checkGilds(level, blockPos).isEmpty()
                && checkSoulFire(level, blockPos).isEmpty()
                && checkRedstone(level, blockPos).isEmpty()
                && checkNetherBricks(level, blockPos).isEmpty()
                && checkNetherite(level, blockPos).isEmpty()
                && checkLava(level, blockPos).isEmpty();
    }

    public boolean conditionsMet(Level worldIn, LivingEntity entityLiving) {
        int count = 0;
        int global = 0;
        if (worldIn instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof NetheriteMonstrosityServant servant) {
                    if (servant.getTrueOwner() == entityLiving && servant.isAlive()) {
                        ++count;
                    }
                    ++global;
                }
            }
        }
        return count < SpellConfig.RedstoneMonstrosityPlayerLimit.get() && global < SpellConfig.RedstoneMonstrosityGlobalLimit.get();
    }

    @Override
    public boolean spawnServant(Player player, ItemStack stack, Level level, BlockPos blockPos){
        if (!level.isClientSide) {
            if (level.getBlockState(blockPos).is(GoetyBlocks.REINFORCED_REDSTONE_BLOCK.get())) {
                if (checkBlocks(level, blockPos)) {
                    if (SEHelper.hasResearch(player, ResearchList.TERMINUS)) {
                        if (conditionsMet(level, player)) {
                            NetheriteMonstrosityServant monstrosity = GCEntityType.NETHERITE_MONSTROSITY.get().create(level);
                            if (monstrosity != null) {
                                monstrosity.setTrueOwner(player);
                                monstrosity.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(monstrosity.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                                monstrosity.moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D, (double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
                                if (level.addFreshEntity(monstrosity)) {
                                    removeBlocks(level, blockPos);
                                    stack.shrink(1);
                                    if (player instanceof ServerPlayer serverPlayer) {
                                        CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, monstrosity);
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
            for (BlockPos blockPos1 : SOUL_FIRE_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getBlockState(blockPos2).is(Blocks.SOUL_FIRE)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
            for (BlockPos blockPos1 : REDSTONE_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getBlockState(blockPos2).is(Tags.Blocks.STORAGE_BLOCKS_REDSTONE)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
            for (BlockPos blockPos1 : NETHER_BRICKS_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getBlockState(blockPos2).is(Blocks.NETHER_BRICKS)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
            for (BlockPos blockPos1 : NETHERITE_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getBlockState(blockPos2).is(Tags.Blocks.STORAGE_BLOCKS_NETHERITE)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
            for (BlockPos blockPos1 : LAVA_LOCATIONS) {
                BlockPos blockPos2 = blockPos.offset(blockPos1);
                if (level.getFluidState(blockPos2).is(FluidTags.LAVA)) {
                    level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                    level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
                }
            }
            BlockPos blockPos2 = blockPos.offset(CORE);
            if (level.getBlockState(blockPos2).is(GoetyBlocks.REINFORCED_REDSTONE_BLOCK.get())) {
                level.levelEvent(2001, blockPos2, Block.getId(level.getBlockState(blockPos2)));
                level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
            }
        }
    }
}
