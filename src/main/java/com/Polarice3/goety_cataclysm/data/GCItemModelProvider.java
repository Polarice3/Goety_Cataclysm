package com.Polarice3.goety_cataclysm.data;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class GCItemModelProvider extends ItemModelProvider {
    public GCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, GoetyCataclysm.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Item item : ForgeRegistries.ITEMS) {
            if (ForgeRegistries.ITEMS.getKey(item) != null) {
                ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
                if (resourceLocation != null) {
                    if (item instanceof SpawnEggItem && resourceLocation.getNamespace().equals(GoetyCataclysm.MOD_ID)) {
                        getBuilder(resourceLocation.getPath())
                                .parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
                    }
                }
            }
        }
    }
}
