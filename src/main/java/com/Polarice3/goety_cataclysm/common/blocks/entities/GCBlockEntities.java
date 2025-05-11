package com.Polarice3.goety_cataclysm.common.blocks.entities;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GCBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GoetyCataclysm.MOD_ID);

    public static final RegistryObject<BlockEntityType<FabricatorBlockEntity>> FABRICATOR = BLOCK_ENTITY.register("fabricator",
            () -> BlockEntityType.Builder.of(FabricatorBlockEntity::new, GCBlocks.FABRICATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<EnderGolemSkullBlockEntity>> ENDER_GOLEM_SKULL = BLOCK_ENTITY.register("ender_golem_skull",
            () -> BlockEntityType.Builder.of(EnderGolemSkullBlockEntity::new, GCBlocks.ENDER_GOLEM_SKULL_BLOCK.get(), GCBlocks.WALL_ENDER_GOLEM_SKULL_BLOCK.get()).build(null));

}
