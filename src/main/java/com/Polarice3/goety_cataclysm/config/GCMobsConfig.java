package com.Polarice3.goety_cataclysm.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class GCMobsConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingAnglerCatchTime;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BlazeIgnitedHelm;

    public static final ForgeConfigSpec.ConfigValue<Boolean> IgnitedRevenantHelm;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IgnitedBerserkerSoul;
    public static final ForgeConfigSpec.ConfigValue<Boolean> KobolediatorSpirit;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NetheriteMonstrosityGriefing;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ProwlerCore;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WadjetSpirit;

    static {
        BUILDER.push("Servants");
            BUILDER.push("Abyss Servants");
            DeeplingAnglerCatchTime = BUILDER.comment("How long until a Deepling Angler Servant will catch another Lionfish if they don't have one already attached, Default: 12000")
                    .defineInRange("deeplingAnglerCatchTime", 12000, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Nether Servants");
            IgnitedRevenantHelm = BUILDER.comment("Whether owned Ignited Revenants drop Ignited Helms, Default: true")
                    .define("ignitedRevenantHelm", true);
            IgnitedBerserkerSoul = BUILDER.comment("Whether owned Ignited Berserkers drop Ignited Souls, Default: true")
                    .define("ignitedBerserkerSoul", true);
            BlazeIgnitedHelm = BUILDER.comment("Whether Ignited Helms works on Blazes instead of just Wildfires, Default: false")
                    .define("blazeIgnitedHelm", false);
            NetheriteMonstrosityGriefing = BUILDER.comment("Whether Netherite Monstrosity, owned by players, breaks blocks if mob griefing is enabled, Default: false")
                    .define("netheriteMonstrosityGriefing", false);
            BUILDER.pop();
        KobolediatorSpirit = BUILDER.comment("Whether owned Kobolediators drop Warrior Spirits, Default: true")
                .define("kobolediatorSpirit", true);
        ProwlerCore = BUILDER.comment("Whether owned Prowlers drop Mechanized Cores, Default: true")
                .define("prowlerCore", true);
        WadjetSpirit = BUILDER.comment("Whether owned Wadjets drop Arcane Spirits, Default: true")
                .define("wadjetSpirit", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        file.load();
        config.setConfig(file);
    }

}
