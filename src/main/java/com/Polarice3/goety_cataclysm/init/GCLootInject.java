package com.Polarice3.goety_cataclysm.init;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GoetyCataclysm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GCLootInject {

    @SubscribeEvent
    public static void InjectLootTables(LootTableLoadEvent evt) {
        String name = evt.getName().toString();
        if (name.equals("cataclysm:entities/the_leviathan")) {
            evt.getTable().addPool(getInjectPool("entities/the_leviathan"));
        }
    }

    private static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool().add(getInjectEntry(entryName)).name("goety_cataclysm_inject_pool").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        return LootTableReference.lootTableReference(GoetyCataclysm.location("inject/" + name));
    }
}
