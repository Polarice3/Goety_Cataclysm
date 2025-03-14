package com.Polarice3.goety_cataclysm.init;

import com.Polarice3.Goety.init.ModCreativeTab;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.common.items.GCSpawnEggs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class GCCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GoetyCataclysm.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(GoetyCataclysm.MOD_ID, () -> CreativeModeTab.builder()
            .withTabsBefore(ModCreativeTab.TAB.getId())
            .icon(() -> GCItems.ABYSSAL_BEAM_FOCUS.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.goety_cataclysm"))
            .displayItems((parameters, output) -> {
                GCItems.ITEMS.getEntries().forEach(i -> {
                    if (i.isPresent()) {
                        output.accept(i.get());
                    }
                });
                GCSpawnEggs.ITEMS.getEntries().forEach(i -> {
                    if (i.isPresent()) {
                        output.accept(i.get());
                    }
                });
            }).build());
}
