package com.Polarice3.goety_cataclysm.common.blocks;

import com.Polarice3.goety_cataclysm.common.blocks.entities.EnderGolemSkullBlockEntity;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.EnderGolemServant;
import com.Polarice3.goety_cataclysm.common.items.block.EnderGolemSkullItem;
import com.Polarice3.goety_cataclysm.common.magic.construct.EnderGolemMold;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class EnderGolemSkullBlock extends BaseEntityBlock {
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    @Nullable
    private BlockPattern enderGolemBase;
    @Nullable
    private BlockPattern enderGolemFull;

    public EnderGolemSkullBlock() {
        super(Properties.of()
                .strength(1.0F)
                .instrument(NoteBlockInstrument.CUSTOM_HEAD)
                .pushReaction(PushReaction.DESTROY)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack itemStack = new ItemStack(this);
        if (player.isCrouching()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof EnderGolemSkullBlockEntity) {
                this.setOwner(itemStack, tileEntity);
                this.setModCustomName(itemStack, tileEntity);
            }
        }
        return itemStack;
    }

    public void setOwner(ItemStack itemStack, BlockEntity tileEntity){
        if (tileEntity instanceof EnderGolemSkullBlockEntity blockEntity){
            EnderGolemSkullItem.setOwner(blockEntity.getPlayer(), itemStack);
        }
    }

    public void setModCustomName(ItemStack itemStack, BlockEntity tileEntity){
        if (tileEntity instanceof EnderGolemSkullBlockEntity blockEntity){
            if (blockEntity.getCustomName() != null && !blockEntity.getCustomName().isEmpty()) {
                EnderGolemSkullItem.setCustomName(blockEntity.getCustomName(), itemStack);
            }
        }
    }

    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
        ItemStack itemStack = new ItemStack(this);
        if (pTe instanceof EnderGolemSkullBlockEntity blockEntity) {
            this.setOwner(itemStack, blockEntity);
            this.setModCustomName(itemStack, blockEntity);
        }
        popResource(pLevel, pPos, itemStack);
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        boolean flag = false;
        if (this.canSpawnGolem(pLevel, pPos)){
            if (EnderGolemMold.conditionsMet(pLevel, pPlacer)) {
                this.trySpawnGolem(pPlacer, pLevel, pPos, pStack);
            } else {
                if (pPlacer instanceof Player player) {
                    player.displayClientMessage(Component.translatable("info.goety.summon.limit"), true);
                }
                flag = true;
            }
        } else {
            flag = true;
        }
        if (flag) {
            BlockEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof EnderGolemSkullBlockEntity blockEntity) {
                blockEntity.setOwnerId(EnderGolemSkullItem.getOwnerID(pStack));
                if (EnderGolemSkullItem.getCustomName(pStack) != null) {
                    blockEntity.setCustomName(EnderGolemSkullItem.getCustomName(pStack));
                }
            }
        }
    }

    public VoxelShape getShape(BlockState p_56331_, BlockGetter p_56332_, BlockPos p_56333_, CollisionContext p_56334_) {
        return SHAPE;
    }

    public VoxelShape getOcclusionShape(BlockState p_56336_, BlockGetter p_56337_, BlockPos p_56338_) {
        return Shapes.empty();
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_56321_) {
        return this.defaultBlockState().setValue(ROTATION, Integer.valueOf(Mth.floor((double)(p_56321_.getRotation() * 16.0F / 360.0F) + 0.5D) & 15));
    }

    public BlockState rotate(BlockState p_56326_, Rotation p_56327_) {
        return p_56326_.setValue(ROTATION, Integer.valueOf(p_56327_.rotate(p_56326_.getValue(ROTATION), 16)));
    }

    public BlockState mirror(BlockState p_56323_, Mirror p_56324_) {
        return p_56323_.setValue(ROTATION, Integer.valueOf(p_56324_.mirror(p_56323_.getValue(ROTATION), 16)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ROTATION);
    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    private void trySpawnGolem(LivingEntity living, Level p_51379_, BlockPos p_51380_, ItemStack itemStack) {
        BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = this.getOrCreateEnderGolemFull().find(p_51379_, p_51380_);
        if (blockpattern$blockpatternmatch != null) {
            for(int j = 0; j < this.getOrCreateEnderGolemFull().getWidth(); ++j) {
                for(int k = 0; k < this.getOrCreateEnderGolemFull().getHeight(); ++k) {
                    BlockInWorld blockinworld2 = blockpattern$blockpatternmatch.getBlock(j, k, 0);
                    p_51379_.setBlock(blockinworld2.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    p_51379_.levelEvent(2001, blockinworld2.getPos(), Block.getId(blockinworld2.getState()));
                }
            }

            BlockPos blockpos = blockpattern$blockpatternmatch.getBlock(1, 2, 0).getPos();
            EnderGolemServant redstoneGolem = GCEntityType.ENDER_GOLEM.get().create(p_51379_);
            if (redstoneGolem != null) {
                if (EnderGolemSkullItem.getOwnerID(itemStack) != null){
                    redstoneGolem.setOwnerId(EnderGolemSkullItem.getOwnerID(itemStack));
                } else if (living != null){
                    redstoneGolem.setTrueOwner(living);
                }
                String string = EnderGolemSkullItem.getCustomName(itemStack);
                if (string != null){
                    redstoneGolem.setCustomName(Component.literal(string));
                }
                redstoneGolem.moveTo((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.05D, (double) blockpos.getZ() + 0.5D, 0.0F, 0.0F);
                if (p_51379_ instanceof ServerLevel serverLevel) {
                    redstoneGolem.finalizeSpawn(serverLevel, p_51379_.getCurrentDifficultyAt(p_51380_), MobSpawnType.MOB_SUMMONED, null, null);
                }
                p_51379_.addFreshEntity(redstoneGolem);
            }

            for(int i1 = 0; i1 < this.getOrCreateEnderGolemFull().getWidth(); ++i1) {
                for(int j1 = 0; j1 < this.getOrCreateEnderGolemFull().getHeight(); ++j1) {
                    BlockInWorld blockinworld1 = blockpattern$blockpatternmatch.getBlock(i1, j1, 0);
                    p_51379_.blockUpdated(blockinworld1.getPos(), Blocks.AIR);
                }
            }
        }
    }

    public boolean canSpawnGolem(LevelReader p_51382_, BlockPos p_51383_) {
        return this.getOrCreateEnderGolemBase().find(p_51382_, p_51383_) != null;
    }

    private BlockPattern getOrCreateEnderGolemBase() {
        if (this.enderGolemBase == null) {
            this.enderGolemBase = BlockPatternBuilder.start()
                    .aisle("~~ ~~", "#####", "~###~", "~~D~~")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(CataclysmBlocks.VOID_STONE.get())))
                    .where('D', BlockInWorld.hasState(BlockStatePredicate.forBlock(GCBlocks.VOID_MOLD_BLOCK.get())))
                    .where('~', (p_284869_) -> {
                        return p_284869_.getState().isAir();
                    }).build();
        }

        return this.enderGolemBase;
    }

    private BlockPattern getOrCreateEnderGolemFull() {
        if (this.enderGolemFull == null) {
            this.enderGolemFull = BlockPatternBuilder.start()
                    .aisle("~~^~~", "#####", "~###~", "~~D~~")
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(GCBlocks.ENDER_GOLEM_SKULL_BLOCK.get())
                            .or(BlockStatePredicate.forBlock(GCBlocks.WALL_ENDER_GOLEM_SKULL_BLOCK.get()))))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(CataclysmBlocks.VOID_STONE.get())))
                    .where('D', BlockInWorld.hasState(BlockStatePredicate.forBlock(GCBlocks.VOID_MOLD_BLOCK.get())))
                    .where('~', (p_284869_) -> {
                        return p_284869_.getState().isAir();
                    }).build();
        }

        return this.enderGolemFull;
    }

    public BlockEntity newBlockEntity(BlockPos p_151996_, BlockState p_151997_) {
        return new EnderGolemSkullBlockEntity(p_151996_, p_151997_);
    }
}
