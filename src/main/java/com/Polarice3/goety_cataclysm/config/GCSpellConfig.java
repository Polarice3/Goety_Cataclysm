package com.Polarice3.goety_cataclysm.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class GCSpellConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalMinesCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalMinesDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalMinesCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> AbyssalMinesRadius;

    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalBeamCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalBeamDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalBeamCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> AbyssalBeamDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> AbyssalBeamHPDamage;
    public static final ForgeConfigSpec.ConfigValue<Boolean> AbyssalBeamGriefing;

    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalOrbsCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalOrbsDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> AbyssalOrbsCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> AbyssalOrbsDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> AbyssalOrbsRadius;

    public static final ForgeConfigSpec.ConfigValue<Integer> BlazingFireCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> BlazingFireDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> BlazingFireCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> BlazingFireRadius;

    public static final ForgeConfigSpec.ConfigValue<Integer> DesertCrushCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DesertCrushDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DesertCrushCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> DesertCrushDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> DraugrCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DraugrDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DraugrCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DraugrSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DraugrLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> EliteDraugrCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> EliteDraugrDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> EliteDraugrCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> EliteDraugrSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> EliteDraugrLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> RoyalDraugrCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoyalDraugrDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoyalDraugrCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoyalDraugrSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> RoyalDraugrLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> FlareBombCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> FlareBombDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> FlareBombCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> FlareBombDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> KoboletonCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> KoboletonDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> KoboletonCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> KoboletonSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> KoboletonLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> SandstormCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SandstormDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SandstormCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> SandstormDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> VoidRuneCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidRuneDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidRuneCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> VoidRuneDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> VoidVortexCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidVortexDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidVortexCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> VoidVortexRadius;

    public static final ForgeConfigSpec.ConfigValue<Integer> AptygangrLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> CoralssusLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> CoralGolemLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderGolemLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> IgnitedBerserkerLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> IgnitedRevenantLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> KobolediatorLimit;

    static {
        BUILDER.push("Spells");
            BUILDER.push("Abyssal Mines Spell");
            AbyssalMinesCost = BUILDER.comment("Abyssal Mines Spell Cost, Default: 16")
                    .defineInRange("abyssalMinesCost", 16, 0, Integer.MAX_VALUE);
            AbyssalMinesDuration = BUILDER.comment("Time to cast Abyssal Mines Spell, Default: 70")
                    .defineInRange("abyssalMinesTime", 70, 0, 72000);
            AbyssalMinesCoolDown = BUILDER.comment("Abyssal Mines Spell Cooldown, Default: 800")
                    .defineInRange("abyssalMinesCoolDown", 800, 0, Integer.MAX_VALUE);
            AbyssalMinesRadius = BUILDER.comment("Abyssal Mines default explosion radius, Default: 1.0")
                    .defineInRange("abyssalMinesRadius", 1.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Abyssal Beam Spell");
            AbyssalBeamCost = BUILDER.comment("Abyssal Beam Spell Cost, Default: 32")
                    .defineInRange("abyssalBeamCost", 32, 0, Integer.MAX_VALUE);
            AbyssalBeamDuration = BUILDER.comment("Time to cast Abyssal Beam Spell, Default: 80")
                    .defineInRange("abyssalBeamTime", 80, 0, 72000);
            AbyssalBeamCoolDown = BUILDER.comment("Abyssal Beam Spell Cooldown, Default: 400")
                    .defineInRange("abyssalBeamCoolDown", 400, 0, Integer.MAX_VALUE);
            AbyssalBeamDamage = BUILDER.comment("Abyssal Beam Spell Damage, Default: 10.0")
                    .defineInRange("abyssalBeamDamage", 10.0, 0, Double.MAX_VALUE);
            AbyssalBeamHPDamage = BUILDER.comment("Abyssal Beam Spell HP percentage Damage, Default: 0.1")
                    .defineInRange("abyssalBeamHPDamage", 0.1, 0, Double.MAX_VALUE);
            AbyssalBeamGriefing = BUILDER.comment("Whether Abyssal Beam Spell can potentially breaks blocks, Default: false")
                    .define("abyssalBeamGriefing", false);
            BUILDER.pop();
            BUILDER.push("Abyssal Orbs Spell");
            AbyssalOrbsCost = BUILDER.comment("Abyssal Orbs Spell Cost, Default: 16")
                    .defineInRange("abyssalOrbsCost", 16, 0, Integer.MAX_VALUE);
            AbyssalOrbsDuration = BUILDER.comment("Time to cast Abyssal Orbs Spell, Default: 90")
                    .defineInRange("abyssalOrbsTime", 90, 0, 72000);
            AbyssalOrbsCoolDown = BUILDER.comment("Abyssal Orbs Spell Cooldown, Default: 400")
                    .defineInRange("abyssalOrbsCoolDown", 400, 0, Integer.MAX_VALUE);
            AbyssalOrbsDamage = BUILDER.comment("Abyssal Orbs Spell Damage, Default: 4.0")
                    .defineInRange("abyssalOrbsDamage", 4.0, 0, Double.MAX_VALUE);
            AbyssalOrbsRadius = BUILDER.comment("Abyssal Orbs default explosion radius, Default: 1.0")
                    .defineInRange("abyssalOrbsRadius", 1.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Blazing Fire Spell");
            BlazingFireCost = BUILDER.comment("Blazing Fire Spell Cost, Default: 64")
                    .defineInRange("blazingFireCost", 64, 0, Integer.MAX_VALUE);
            BlazingFireDuration = BUILDER.comment("Time to cast Blazing Fire Spell, Default: 70")
                    .defineInRange("blazingFireTime", 70, 0, 72000);
            BlazingFireCoolDown = BUILDER.comment("Blazing Fire Spell Cooldown, Default: 300")
                    .defineInRange("blazingFireCoolDown", 300, 0, Integer.MAX_VALUE);
            BlazingFireRadius = BUILDER.comment("Blazing Fire's Fireballs default explosion radius, Default: 1.0")
                    .defineInRange("blazingFireRadius", 1.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Desert Crush Spell");
            DesertCrushCost = BUILDER.comment("Desert Crush Spell Cost, Default: 64")
                    .defineInRange("desertCrushCost", 64, 0, Integer.MAX_VALUE);
            DesertCrushDuration = BUILDER.comment("Time to cast Desert Crush Spell, Default: 70")
                    .defineInRange("desertCrushTime", 70, 0, 72000);
            DesertCrushCoolDown = BUILDER.comment("Desert Crush Spell Cooldown, Default: 160")
                    .defineInRange("desertCrushCoolDown", 160, 0, Integer.MAX_VALUE);
            DesertCrushDamage = BUILDER.comment("Desert Crush Spell Damage, Default: 18.0")
                    .defineInRange("desertCrushDamage", 18.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Cursed Grave Spell");
            DraugrCost = BUILDER.comment("Cursed Grave Spell Cost, Default: 24")
                    .defineInRange("draugrCost", 24, 0, Integer.MAX_VALUE);
            DraugrDuration = BUILDER.comment("Time to cast Cursed Grave Spell, Default: 60")
                    .defineInRange("draugrTime", 60, 0, 72000);
            DraugrCoolDown = BUILDER.comment("Cursed Grave Spell Cooldown, Default: 100")
                    .defineInRange("draugrCoolDown", 100, 0, Integer.MAX_VALUE);
            DraugrSummonDown = BUILDER.comment("Cursed Grave Spell Summon Down, Default: 300")
                    .defineInRange("draugrSummonDown", 300, 0, 72000);
            DraugrLimit = BUILDER.comment("Number of Draugr Servants that can a player can have, Default: 32")
                    .defineInRange("draugrLimit", 32, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Cursed Cairn Spell");
            EliteDraugrCost = BUILDER.comment("Cursed Cairn Spell Cost, Default: 32")
                    .defineInRange("eliteDraugrCost", 32, 0, Integer.MAX_VALUE);
            EliteDraugrDuration = BUILDER.comment("Time to cast Cursed Cairn Spell, Default: 60")
                    .defineInRange("eliteDraugrTime", 60, 0, 72000);
            EliteDraugrCoolDown = BUILDER.comment("Cursed Cairn Spell Cooldown, Default: 100")
                    .defineInRange("eliteDraugrCoolDown", 100, 0, Integer.MAX_VALUE);
            EliteDraugrSummonDown = BUILDER.comment("Cursed Cairn Spell Summon Down, Default: 300")
                    .defineInRange("eliteDraugrSummonDown", 300, 0, 72000);
            EliteDraugrLimit = BUILDER.comment("Number of Elite Draugr Servants that can a player can have, Default: 16")
                    .defineInRange("eliteDraugrLimit", 16, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Cursed Tomb Spell");
            RoyalDraugrCost = BUILDER.comment("Cursed Tomb Spell Cost, Default: 48")
                    .defineInRange("royalDraugrCost", 48, 0, Integer.MAX_VALUE);
            RoyalDraugrDuration = BUILDER.comment("Time to cast Cursed Tomb Spell, Default: 100")
                    .defineInRange("royalDraugrTime", 100, 0, 72000);
            RoyalDraugrCoolDown = BUILDER.comment("Cursed Tomb Spell Cooldown, Default: 1200")
                    .defineInRange("royalDraugrCoolDown", 1200, 0, Integer.MAX_VALUE);
            RoyalDraugrSummonDown = BUILDER.comment("Cursed Tomb Spell Summon Down, Default: 300")
                    .defineInRange("royalDraugrSummonDown", 300, 0, 72000);
            RoyalDraugrLimit = BUILDER.comment("Number of Royal Draugr Servants that can a player can have, Default: 16")
                    .defineInRange("royalDraugrLimit", 16, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Flare Bomb Spell");
            FlareBombCost = BUILDER.comment("Flare Bomb Spell Cost, Default: 32")
                    .defineInRange("flareBombCost", 32, 0, Integer.MAX_VALUE);
            FlareBombDuration = BUILDER.comment("Time to cast Flare Bomb Spell, Default: 60")
                    .defineInRange("flareBombTime", 60, 0, 72000);
            FlareBombCoolDown = BUILDER.comment("Flare Bomb Spell Cooldown, Default: 120")
                    .defineInRange("flareBombCoolDown", 120, 0, Integer.MAX_VALUE);
            FlareBombDamage = BUILDER.comment("Flare Bomb Spell Damage, Default: 7.0")
                    .defineInRange("flareBombDamage", 7.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Desert Raid Spell");
            KoboletonCost = BUILDER.comment("Desert Raid Spell Cost, Default: 16")
                    .defineInRange("koboletonCost", 16, 0, Integer.MAX_VALUE);
            KoboletonDuration = BUILDER.comment("Time to cast Desert Raid Spell, Default: 60")
                    .defineInRange("koboletonTime", 60, 0, 72000);
            KoboletonCoolDown = BUILDER.comment("Desert Raid Spell Cooldown, Default: 100")
                    .defineInRange("koboletonCoolDown", 100, 0, Integer.MAX_VALUE);
            KoboletonSummonDown = BUILDER.comment("Desert Raid Spell Summon Down, Default: 300")
                    .defineInRange("koboletonSummonDown", 300, 0, 72000);
            KoboletonLimit = BUILDER.comment("Number of Koboleton Servants that can a player can have, Default: 32")
                    .defineInRange("koboletonLimit", 32, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Sandstorm Spell");
            SandstormCost = BUILDER.comment("Sandstorm Spell Cost, Default: 32")
                    .defineInRange("sandstormCost", 32, 0, Integer.MAX_VALUE);
            SandstormDuration = BUILDER.comment("Time to cast Sandstorm Spell, Default: 55")
                    .defineInRange("sandstormTime", 55, 0, 72000);
            SandstormCoolDown = BUILDER.comment("Sandstorm Spell Cooldown, Default: 500")
                    .defineInRange("sandstormCoolDown", 500, 0, Integer.MAX_VALUE);
            SandstormDamage = BUILDER.comment("Sandstorm Spell Damage, Default: 5.0")
                    .defineInRange("sandstormDamage", 5.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Void Rune Spell");
            VoidRuneCost = BUILDER.comment("Void Rune Spell Cost, Default: 12")
                    .defineInRange("voidRuneCost", 12, 0, Integer.MAX_VALUE);
            VoidRuneDuration = BUILDER.comment("Time to cast Void Rune Spell, Default: 0")
                    .defineInRange("voidRuneTime", 0, 0, 72000);
            VoidRuneCoolDown = BUILDER.comment("Void Rune Spell Cooldown, Default: 160")
                    .defineInRange("voidRuneCoolDown", 160, 0, Integer.MAX_VALUE);
            VoidRuneDamage = BUILDER.comment("Void Rune Spell Damage, Default: 7.0")
                    .defineInRange("voidRuneDamage", 7.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Void Vortex Spell");
            VoidVortexCost = BUILDER.comment("Void Vortex Spell Cost, Default: 64")
                    .defineInRange("voidVortexCost", 64, 0, Integer.MAX_VALUE);
            VoidVortexDuration = BUILDER.comment("Time to cast Void Vortex Spell, Default: 50")
                    .defineInRange("voidVortexTime", 50, 0, 72000);
            VoidVortexCoolDown = BUILDER.comment("Void Vortex Spell Cooldown, Default: 280")
                    .defineInRange("voidVortexCoolDown", 280, 0, Integer.MAX_VALUE);
            VoidVortexRadius = BUILDER.comment("Void Vortex default explosion radius, Default: 2.0")
                    .defineInRange("voidVortexRadius", 2.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
        BUILDER.push("Servant Limits");
        AptygangrLimit = BUILDER.comment("Number of Aptrgangr Servants that an individual player can have in total, Default: 2")
                .defineInRange("aptygangrLimit", 2, 1, Integer.MAX_VALUE);
        CoralssusLimit = BUILDER.comment("Number of Coralssus Servants that an individual player can have in total, Default: 2")
                .defineInRange("coralssusLimit", 2, 1, Integer.MAX_VALUE);
        CoralGolemLimit = BUILDER.comment("Number of Coral Golem Servants that an individual player can have in total, Default: 4")
                .defineInRange("coralGolemLimit", 4, 1, Integer.MAX_VALUE);
        EnderGolemLimit = BUILDER.comment("Total number of Ender Golems an individual player can have, Default: 2")
                .defineInRange("enderGolemLimit", 2, 0, Integer.MAX_VALUE);
        IgnitedBerserkerLimit = BUILDER.comment("Total number of Ignited Berserkers an individual player can have, Default: 2")
                .defineInRange("ignitedBerserkerLimit", 2, 0, Integer.MAX_VALUE);
        IgnitedRevenantLimit = BUILDER.comment("Total number of Ignited Revenant an individual player can have, Default: 2")
                .defineInRange("ignitedRevenantLimit", 2, 0, Integer.MAX_VALUE);
        KobolediatorLimit = BUILDER.comment("Total number of Kobolediators an individual player can have, Default: 2")
                .defineInRange("kobolediatorLimit", 2, 0, Integer.MAX_VALUE);
        BUILDER.pop();
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
