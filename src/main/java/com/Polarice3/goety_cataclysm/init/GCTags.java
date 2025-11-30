package com.Polarice3.goety_cataclysm.init;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class GCTags {

    public static void init () {
        Blocks.init();
        Biomes.init();
    }

    public static class Blocks {

        private static void init(){}

        public static final TagKey<Block> CORALSSUS_BRICK_MOLD = tag("coralssus_brick_mold");
        public static final TagKey<Block> CORALSSUS_CORALS = tag("coralssus_corals");
        public static final TagKey<Block> ENDER_GOLEM_OBSIDIAN_MOLD = tag("ender_golem_obsidian_mold");

        private static TagKey<Block> tag(String name)
        {
            return BlockTags.create(GoetyCataclysm.location(name));
        }
    }

    public static class Biomes {

        private static void init(){}

        public static final TagKey<Biome> DRAUGR_NECROMANCER_SPAWN = tag("mob_spawn/draugr_necromancer");
        public static final TagKey<Biome> DRAUGR_NECROMANCER_EXCLUDE_SPAWN = tag("mob_spawn/draugr_necromancer_exclude");

        private static TagKey<Biome> tag(String name) {
            return create(GoetyCataclysm.location(name));
        }

        private static TagKey<Biome> create(ResourceLocation p_215874_) {
            return TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), p_215874_);
        }
    }
}
