package com.Polarice3.goety_cataclysm.common.blocks.entities;

import com.Polarice3.Goety.common.blocks.entities.TrainingBlockEntity;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.factory.WatcherServant;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FabricatorBlockEntity extends TrainingBlockEntity {
    public int tickCount;
    private float activeRotation;

    public FabricatorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(GCBlockEntities.FABRICATOR.get(), p_155229_, p_155230_);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, TrainingBlockEntity blockEntity) {
        super.tick(level, blockPos, blockState, blockEntity);
        ++this.tickCount;
        ++this.activeRotation;
        if (blockEntity.isTraining()){
            if (blockEntity.trainTime != blockEntity.getMaxTrainTime()){
                if (level instanceof ServerLevel serverLevel) {
                    ServerParticleUtil.spawnRedstoneParticles(serverLevel, blockPos);
                }
                if (this.trainTime == 20){
                    level.playSound(null, blockPos, GoetySounds.BLAZING_CAGE_TRAIN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            } else {
                if (level instanceof ServerLevel serverLevel) {
                    ServerParticleUtil.spawnRedstoneParticles(serverLevel, blockPos);
                }
            }
        }
    }

    public float getActiveRotation(float p_205036_1_) {
        return (activeRotation + p_205036_1_) * -0.0375F;
    }

    @Override
    public void setVariant(ItemStack itemStack, Level level, BlockPos blockPos) {
        if (level instanceof ServerLevel) {
            if (this.getTrainMob() != GCEntityType.WATCHER_SERVANT.get()) {
                this.setEntityType(GCEntityType.WATCHER_SERVANT.get());
                this.markUpdated();
            }
        }
    }

    public void startTraining(int amount, ItemStack itemStack){
        super.startTraining(amount, itemStack);
        if (this.level != null) {
            this.level.playSound(null, this.getBlockPos(), GoetySounds.BLAZING_CAGE_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void playSpawnSound() {
        if (this.level != null) {
            this.level.playSound(null, this.getBlockPos(), GoetySounds.SUMMON_SPELL_FIERY.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public int maxTrainAmount() {
        return 5;
    }

    @Override
    public boolean summonLimit() {
        int count = 0;
        if (this.level instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof WatcherServant servant) {
                    if (this.getTrueOwner() != null && servant.getTrueOwner() == this.getTrueOwner() && servant.isAlive()) {
                        ++count;
                    }
                }
            }
        }
        return count >= GCSpellConfig.WatcherLimit.get();
    }

    @Override
    public boolean isFuel(ItemStack itemStack) {
        return itemStack.is(GCBlocks.MECHANIZED_IRON_BLOCK.get().asItem());
    }
}
