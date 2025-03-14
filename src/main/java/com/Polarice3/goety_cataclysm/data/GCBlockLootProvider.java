package com.Polarice3.goety_cataclysm.data;

import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import com.Polarice3.goety_cataclysm.common.blocks.GoetyBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Based on @klikli-dev's Block Loot Generator
 */
public class GCBlockLootProvider extends BlockLootSubProvider {

    public GCBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        this.generate();
        this.map.forEach(consumer::accept);
    }

    @Override
    protected void generate() {
        Collection<Block> blocks = new ArrayList<>();
        GCBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block ->
        {
            GoetyBlocks.BlockLootSetting setting = GCBlocks.BLOCK_LOOT.get(ForgeRegistries.BLOCKS.getKey(block));
            if (setting.lootTableType == GoetyBlocks.LootTableType.DROP){
                blocks.add(block);
            }
        });
        for (Block block : blocks){
            this.dropSelf(block);
        }
    }
}
