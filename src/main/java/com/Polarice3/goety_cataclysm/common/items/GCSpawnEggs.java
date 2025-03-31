package com.Polarice3.goety_cataclysm.common.items;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GCSpawnEggs {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GoetyCataclysm.MOD_ID);

    public static void init(){
        GCSpawnEggs.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<ServantSpawnEggItem> ENDER_GOLEM_SPAWN_EGG = ITEMS.register("ender_golem_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.ENDER_GOLEM, 0x271e3d, 0x8308e4, egg()));

    public static final RegistryObject<ServantSpawnEggItem> NETHERITE_MONSTROSITY_SPAWN_EGG = ITEMS.register("netherite_monstrosity_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.NETHERITE_MONSTROSITY, 0x3b393b, 0xe1a61d, egg()));

    public static final RegistryObject<ServantSpawnEggItem> CORAL_GOLEM_SPAWN_EGG = ITEMS.register("coral_golem_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.CORAL_GOLEM, 0x4c7dc4, 0xabad9e, egg()));

    public static final RegistryObject<ServantSpawnEggItem> CORALSSUS_SPAWN_EGG = ITEMS.register("coralssus_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.CORALSSUS, 0x5f7cb4, 0xac2020, egg()));

    public static final RegistryObject<ServantSpawnEggItem> IGNITED_REVENANT_SPAWN_EGG = ITEMS.register("ignited_revenant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.IGNITED_REVENANT, 0x271c1d, 0xfffc94, egg()));

    public static final RegistryObject<ServantSpawnEggItem> IGNITED_BERSERKER_SPAWN_EGG = ITEMS.register("ignited_berserker_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.IGNITED_BERSERKER, 0x501c00, 0xffd73f, egg()));

    public static final RegistryObject<ServantSpawnEggItem> KOBOLETON_SERVANT_SPAWN_EGG = ITEMS.register("koboleton_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.KOBOLETON_SERVANT, 0xb7b196, 0xe18103, egg()));

    public static final RegistryObject<ServantSpawnEggItem> KOBOLEDIATOR_SPAWN_EGG = ITEMS.register("kobolediator_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.KOBOLEDIATOR, 0xb98a3c, 0xffe69a, egg()));

    public static final RegistryObject<ServantSpawnEggItem> WADJET_SPAWN_EGG = ITEMS.register("wadjet_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.WADJET, 0x8c8873, 0xbfa05c, egg()));

    public static final RegistryObject<ServantSpawnEggItem> DRAUGR_SERVANT_SPAWN_EGG = ITEMS.register("draugr_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DRAUGR_SERVANT, 0x2e2a27, 0x56eccc, egg()));

    public static final RegistryObject<ServantSpawnEggItem> ROYAL_DRAUGR_SERVANT_SPAWN_EGG = ITEMS.register("royal_draugr_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.ROYAL_DRAUGR_SERVANT, 0x2e2a27, 0xffe69a, egg()));

    public static final RegistryObject<ServantSpawnEggItem> ELITE_DRAUGR_SERVANT_SPAWN_EGG = ITEMS.register("elite_draugr_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.ELITE_DRAUGR_SERVANT, 0x2e2a27, 0x383430, egg()));

    public static final RegistryObject<ServantSpawnEggItem> APTRGANGR_SPAWN_EGG = ITEMS.register("aptrgangr_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.APTRGANGR, 0x2f303c, 0x8bffe6, egg()));

    public static Item.Properties egg(){
        return new Item.Properties();
    }
}
