package com.Polarice3.goety_cataclysm.common.blocks;

import com.Polarice3.Goety.common.items.block.BlockItemBase;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GCBlocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GoetyCataclysm.MOD_ID);
    public static final Map<ResourceLocation, GoetyBlocks.BlockLootSetting> BLOCK_LOOT = new HashMap<>();

    public static void init(){
        GCBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> MECHANIZED_IRON_BLOCK = register("mechanized_iron_block", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> VOID_MOLD_BLOCK = register("void_mold_block", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.CRYING_OBSIDIAN)));

    public static <T extends Block> RegistryObject<T> register(final String string, final Supplier<? extends T> sup){
        return register(string, sup, true);
    }

    public static <T extends Block> RegistryObject<T> register(final String string, final Supplier<? extends T> sup, boolean blockItemDefault){
        return register(string, sup, blockItemDefault, GoetyBlocks.LootTableType.DROP);
    }

    public static <T extends Block> RegistryObject<T> register(final String string, final Supplier<? extends T> sup, boolean blockItemDefault, GoetyBlocks.LootTableType lootTableType) {
        RegistryObject<T> block = BLOCKS.register(string, sup);
        BLOCK_LOOT.put(block.getId(), new GoetyBlocks.BlockLootSetting(blockItemDefault, lootTableType));
        if (blockItemDefault) {
            GCItems.ITEMS.register(string,
                    () -> new BlockItemBase(block.get()));
        }
        return block;
    }
}
