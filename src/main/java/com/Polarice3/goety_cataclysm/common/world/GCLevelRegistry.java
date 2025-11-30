package com.Polarice3.goety_cataclysm.common.world;

import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.init.GCTags;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.common.world.ModifiableStructureInfo;

public class GCLevelRegistry {

    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!biome.containsTag(ModTags.Biomes.COMMON_BLACKLIST) && !biome.is(biomeResourceKey -> biomeResourceKey.registry().getNamespace().contains("alexscaves"))){
            if (biome.is(GCTags.Biomes.DRAUGR_NECROMANCER_SPAWN) && GCMobsConfig.DraugrNecromancerSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(GCEntityType.DRAUGR_NECROMANCER.get(), GCMobsConfig.DraugrNecromancerSpawnWeight.get(), GCMobsConfig.DraugrNecromancerSpawnMinCount.get(), GCMobsConfig.DraugrNecromancerSpawnMaxCount.get()));
            }
        }
    }

    public static boolean startName(ResourceKey<Biome> biomeResourceKey, String string){
        return biomeResourceKey.registry().getNamespace().startsWith(string);
    }

    public static boolean containsName(ResourceKey<Biome> biomeResourceKey, String string){
        return biomeResourceKey.registry().getNamespace().contains(string);
    }

    public static void addStructureSpawns(Holder<Structure> structure, ModifiableStructureInfo.StructureInfo.Builder builder) {
    }
}
