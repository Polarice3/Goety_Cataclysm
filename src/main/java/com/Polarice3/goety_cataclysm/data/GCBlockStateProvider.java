package com.Polarice3.goety_cataclysm.data;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GCBlockStateProvider extends BlockStateProvider {
    public GCBlockStateProvider(PackOutput gen, ExistingFileHelper exFileHelper) {
        super(gen, GoetyCataclysm.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(GCBlocks.VOID_MOLD_BLOCK.get());
    }

    public void simpleBlockWithItem(Block block) {
        simpleBlock(block, cubeAll(block));
        simpleBlockItem(block, cubeAll(block));
    }
}
