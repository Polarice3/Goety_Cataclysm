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

    public static final RegistryObject<ServantSpawnEggItem> DEEPLING_SERVANT_SPAWN_EGG = ITEMS.register("deepling_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DEEPLING, 0x0b141c, 0xbaedf4, egg()));

    public static final RegistryObject<ServantSpawnEggItem> DEEPLING_BRUTE_SERVANT_SPAWN_EGG = ITEMS.register("deepling_brute_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DEEPLING_BRUTE, 0x0b141c, 0x6500ff, egg()));

    public static final RegistryObject<ServantSpawnEggItem> DEEPLING_ANGLER_SERVANT_SPAWN_EGG = ITEMS.register("deepling_angler_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DEEPLING_ANGLER, 0x0b141c, 0x98d8e2, egg()));

    public static final RegistryObject<ServantSpawnEggItem> DEEPLING_PRIEST_SERVANT_SPAWN_EGG = ITEMS.register("deepling_priest_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DEEPLING_PRIEST, 0x0b141c, 0x820540, egg()));

    public static final RegistryObject<ServantSpawnEggItem> DEEPLING_WARLOCK_SERVANT_SPAWN_EGG = ITEMS.register("deepling_warlock_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DEEPLING_WARLOCK, 0x0b141c, 0xd66a98, egg()));

    public static final RegistryObject<ServantSpawnEggItem> LIONFISH_SERVANT_SPAWN_EGG = ITEMS.register("lionfish_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.LIONFISH, 0x98d8e2, 0x0b141c, egg()));

    public static final RegistryObject<ServantSpawnEggItem> CORAL_GOLEM_SPAWN_EGG = ITEMS.register("coral_golem_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.CORAL_GOLEM, 0x4c7dc4, 0xabad9e, egg()));

    public static final RegistryObject<ServantSpawnEggItem> CORALSSUS_SPAWN_EGG = ITEMS.register("coralssus_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.CORALSSUS, 0x5f7cb4, 0xac2020, egg()));

    public static final RegistryObject<ServantSpawnEggItem> IGNITED_REVENANT_SPAWN_EGG = ITEMS.register("ignited_revenant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.IGNITED_REVENANT, 0x271c1d, 0xfffc94, egg()));

    public static final RegistryObject<ServantSpawnEggItem> IGNITED_BERSERKER_SPAWN_EGG = ITEMS.register("ignited_berserker_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.IGNITED_BERSERKER, 0x501c00, 0xffd73f, egg()));

    public static final RegistryObject<ServantSpawnEggItem> WATCHER_SERVANT_SPAWN_EGG = ITEMS.register("watcher_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.WATCHER_SERVANT, 0x3f444c, 0xe83b3b, egg()));

    public static final RegistryObject<ServantSpawnEggItem> PROWLER_SERVANT_SPAWN_EGG = ITEMS.register("prowler_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.THE_PROWLER, 0x181a1b, 0x682e22, egg()));

    public static final RegistryObject<ServantSpawnEggItem> KOBOLETON_SERVANT_SPAWN_EGG = ITEMS.register("koboleton_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.KOBOLETON_SERVANT, 0xb7b196, 0xe18103, egg()));

    public static final RegistryObject<ServantSpawnEggItem> KOBOLEDIATOR_SPAWN_EGG = ITEMS.register("kobolediator_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.KOBOLEDIATOR, 0xb98a3c, 0xffe69a, egg()));

    public static final RegistryObject<ServantSpawnEggItem> WADJET_SPAWN_EGG = ITEMS.register("wadjet_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.WADJET, 0x8c8873, 0xbfa05c, egg()));

    public static final RegistryObject<ServantSpawnEggItem> ANCIENT_REMNANT_SPAWN_EGG = ITEMS.register("ancient_remnant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.ANCIENT_REMNANT, 0x682e22, 0xffe69a, egg()));

    public static final RegistryObject<ServantSpawnEggItem> DRAUGR_SERVANT_SPAWN_EGG = ITEMS.register("draugr_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DRAUGR_SERVANT, 0x2e2a27, 0x56eccc, egg()));

    public static final RegistryObject<ServantSpawnEggItem> ROYAL_DRAUGR_SERVANT_SPAWN_EGG = ITEMS.register("royal_draugr_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.ROYAL_DRAUGR_SERVANT, 0x2e2a27, 0xffe69a, egg()));

    public static final RegistryObject<ServantSpawnEggItem> ELITE_DRAUGR_SERVANT_SPAWN_EGG = ITEMS.register("elite_draugr_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.ELITE_DRAUGR_SERVANT, 0x2e2a27, 0x383430, egg()));

    public static final RegistryObject<ServantSpawnEggItem> APTRGANGR_SPAWN_EGG = ITEMS.register("aptrgangr_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.APTRGANGR, 0x2f303c, 0x8bffe6, egg()));

    public static final RegistryObject<ServantSpawnEggItem> HIPPOCAMPTUS_SPAWN_EGG = ITEMS.register("hippocamptus_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.HIPPOCAMTUS, 0x504b77, 0xffe98e, egg()));

    public static final RegistryObject<ServantSpawnEggItem> CINDARIA_SERVANT_SPAWN_EGG = ITEMS.register("cindaria_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.CINDARIA_SERVANT, 0x7257e4, 0xb0fec0, egg()));

    public static final RegistryObject<ServantSpawnEggItem> CLAWDIAN_SPAWN_EGG = ITEMS.register("clawdian_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.CLAWDIAN, 0x383c61, 0xe3845a, egg()));

    public static final RegistryObject<ServantSpawnEggItem> URCHINKIN_SERVANT_SPAWN_EGG = ITEMS.register("urchinkin_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.URCHINKIN_SERVANT, 0x0f0220, 0xffffff, egg()));

    public static final RegistryObject<ServantSpawnEggItem> DROWNED_HOST_SERVANT_SPAWN_EGG = ITEMS.register("drowned_host_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.DROWNED_HOST_SERVANT, 0x182d37, 0xe7b275, egg()));

    public static final RegistryObject<ServantSpawnEggItem> SYMBIOCTO_SERVANT_SPAWN_EGG = ITEMS.register("symbiocto_servant_spawn_egg",
            () -> new ServantSpawnEggItem(GCEntityType.SYMBIOCTO_SERVANT, 0x462014, 0xf2b3a1, egg()));

    public static Item.Properties egg(){
        return new Item.Properties();
    }
}
