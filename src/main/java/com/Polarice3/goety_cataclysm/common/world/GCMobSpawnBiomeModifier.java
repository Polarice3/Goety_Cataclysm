package com.Polarice3.goety_cataclysm.common.world;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Based of codes by @AlexModGuy
 */
public class GCMobSpawnBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(GoetyCataclysm.MOD_ID, "mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, GoetyCataclysm.MOD_ID);

    public GCMobSpawnBiomeModifier() {
    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            GCLevelRegistry.addBiomeSpawns(biome, builder);
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<GCMobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(GCMobSpawnBiomeModifier::new);
    }
}
