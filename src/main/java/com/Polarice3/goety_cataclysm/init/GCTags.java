package com.Polarice3.goety_cataclysm.init;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class GCTags {

    public static void init () {
        Blocks.init();
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
}
