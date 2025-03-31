package com.Polarice3.goety_cataclysm.data;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GCRegisteryDataProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder());

    public GCRegisteryDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", GoetyCataclysm.MOD_ID));
    }
}
