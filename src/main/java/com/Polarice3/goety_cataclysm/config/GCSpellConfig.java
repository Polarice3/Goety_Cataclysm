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

    public static final ForgeConfigSpec.ConfigValue<Integer> AmethystClusterCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> AmethystClusterDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> AmethystClusterCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> AmethystClusterDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> AshenBreathCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> AshenBreathChargeUp;
    public static final ForgeConfigSpec.ConfigValue<Integer> AshenBreathDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> AshenBreathCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> AshenBreathDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> ExtinctFlameCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> ExtinctFlameDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> ExtinctFlameCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> ExtinctFlameRadius;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ExtinctFlameGriefing;

    public static final ForgeConfigSpec.ConfigValue<Integer> CindariaCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> CindariaDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> CindariaCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> CindariaSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> CindariaLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> DeathLaserCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeathLaserDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeathLaserCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> DeathLaserDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> DeathLaserHPDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> DesertCrushCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DesertCrushDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DesertCrushCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> DesertCrushDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingBruteCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingBruteDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingBruteCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingBruteSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingBruteLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingCasterCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingCasterDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingCasterCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingCasterSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> DeeplingCasterLimit;

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

    public static final ForgeConfigSpec.ConfigValue<Integer> LightningSpearCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> LightningSpearDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> LightningSpearCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> LightningSpearDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> LightningSpearAreaDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> LightningSpearHPDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> OctoHostCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> OctoHostDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> OctoHostCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> OctoHostSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> OctoHostLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> SandstormCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> SandstormDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> SandstormCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> SandstormDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> StormSerpentCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> StormSerpentCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> StormSerpentDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> ThunderRageCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> ThunderRageCoolDown;

    public static final ForgeConfigSpec.ConfigValue<Integer> UrchinkinCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> UrchinkinDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> UrchinkinCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> UrchinkinSummonDown;
    public static final ForgeConfigSpec.ConfigValue<Integer> UrchinkinLimit;

    public static final ForgeConfigSpec.ConfigValue<Integer> VoidRuneCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidRuneDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidRuneCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> VoidRuneDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> VoidVortexCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidVortexDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> VoidVortexCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> VoidVortexRadius;

    public static final ForgeConfigSpec.ConfigValue<Integer> WaterSpearCost;
    public static final ForgeConfigSpec.ConfigValue<Integer> WaterSpearDuration;
    public static final ForgeConfigSpec.ConfigValue<Integer> WaterSpearCoolDown;
    public static final ForgeConfigSpec.ConfigValue<Double> WaterSpearDamage;

    public static final ForgeConfigSpec.ConfigValue<Integer> AncientRemnantLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> AptygangrLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> ClawdianLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> CoralssusLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> CoralGolemLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> EnderGolemLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> HippocamtusLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> IgnitedBerserkerLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> IgnitedRevenantLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> WatcherLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> ProwlerLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> KobolediatorLimit;
    public static final ForgeConfigSpec.ConfigValue<Integer> WadjetLimit;

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
            BUILDER.push("Amethyst Cluster Spell");
            AmethystClusterCost = BUILDER.comment("Amethyst Cluster Spell Cost, Default: 32")
                    .defineInRange("amethystClusterCost", 32, 0, Integer.MAX_VALUE);
            AmethystClusterDuration = BUILDER.comment("Time to cast Amethyst Cluster Spell, Default: 50")
                    .defineInRange("amethystClusterDuration", 50, 0, 72000);
            AmethystClusterCoolDown = BUILDER.comment("Amethyst Cluster Spell Cooldown, Default: 240")
                    .defineInRange("amethystClusterCoolDown", 240, 0, Integer.MAX_VALUE);
            AmethystClusterDamage = BUILDER.comment("Amethyst Cluster Spell Damage, Default: 12.0")
                    .defineInRange("amethystClusterDamage", 12.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Ashen Breath Spell");
            AshenBreathCost = BUILDER.comment("Ashen Breath Spell Cost, Default: 16")
                    .defineInRange("ashenBreathCost", 16, 0, Integer.MAX_VALUE);
            AshenBreathChargeUp = BUILDER.comment("How many ticks the Ashen Breath Spell must charge before casting, Default: 0")
                    .defineInRange("ashenBreathChargeUp", 0, 0, Integer.MAX_VALUE);
            AshenBreathDuration = BUILDER.comment("How long the Ashen Breath Spell can be casted, setting it to 0 will allow the spell to be cast indefinitely, Default: 25")
                    .defineInRange("ashenBreathTime", 25, 0, 72000);
            AshenBreathCoolDown = BUILDER.comment("Ashen Breath Spell Cooldown, Default: 200")
                    .defineInRange("ashenBreathCoolDown", 200, 0, Integer.MAX_VALUE);
            AshenBreathDamage = BUILDER.comment("Ashen Breath Spell Damage, Default: 4.0")
                    .defineInRange("ashenBreathDamage", 4.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Extinct Flame Spell");
            ExtinctFlameCost = BUILDER.comment("Extinct Flame Spell Cost, Default: 64")
                    .defineInRange("extinctFlameCost", 64, 0, Integer.MAX_VALUE);
            ExtinctFlameDuration = BUILDER.comment("Time to cast Extinct Flame Spell, Default: 70")
                    .defineInRange("extinctFlameTime", 70, 0, 72000);
            ExtinctFlameCoolDown = BUILDER.comment("Extinct Flame Spell Cooldown, Default: 300")
                    .defineInRange("extinctFlameCoolDown", 300, 0, Integer.MAX_VALUE);
            ExtinctFlameRadius = BUILDER.comment("Extinct Flame's Fireballs default explosion radius, Default: 1.0")
                    .defineInRange("extinctFlameRadius", 1.0, 0, Double.MAX_VALUE);
            ExtinctFlameGriefing = BUILDER.comment("Whether Extinct Flame Spell can set fires, Default: false")
                    .define("extinctFlameGriefing", false);
            BUILDER.pop();
            BUILDER.push("Kyría Spell");
            CindariaCost = BUILDER.comment("Kyría Spell Cost, Default: 64")
                    .defineInRange("cindariaCost", 64, 0, Integer.MAX_VALUE);
            CindariaDuration = BUILDER.comment("Time to cast Kyría Spell, Default: 20")
                    .defineInRange("cindariaTime", 20, 0, 72000);
            CindariaCoolDown = BUILDER.comment("Kyría Spell Cooldown, Default: 900")
                    .defineInRange("cindariaCoolDown", 900, 0, Integer.MAX_VALUE);
            CindariaSummonDown = BUILDER.comment("Kyría Spell Summon Down, Default: 150")
                    .defineInRange("cindariaSummonDown", 150, 0, 72000);
            CindariaLimit = BUILDER.comment("Number of Cindaria Servants that can a player can have, Default: 8")
                    .defineInRange("cindariaLimit", 8, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Death Laser Spell");
            DeathLaserCost = BUILDER.comment("Death Laser Spell Cost, Default: 128")
                    .defineInRange("deathLaserCost", 128, 0, Integer.MAX_VALUE);
            DeathLaserDuration = BUILDER.comment("Time to cast Death Laser Spell, Default: 40")
                    .defineInRange("deathLaserDuration", 40, 0, 72000);
            DeathLaserCoolDown = BUILDER.comment("Death Laser Spell Cooldown, Default: 200")
                    .defineInRange("deathLaserCoolDown", 200, 0, Integer.MAX_VALUE);
            DeathLaserDamage = BUILDER.comment("How much base damage Death Laser deals, Default: 5.0")
                    .defineInRange("deathLaserDamage", 5.0, 1.0, Double.MAX_VALUE);
            DeathLaserHPDamage = BUILDER.comment("How much Hp percentage damage Death Laser deals, Default: 5.0")
                    .defineInRange("deathLaserHPDamage", 5.0, 1.0, Double.MAX_VALUE);
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
            BUILDER.push("Sunken Swell Spell");
            DeeplingCost = BUILDER.comment("Sunken Swell Spell Cost, Default: 24")
                    .defineInRange("deeplingCost", 24, 0, Integer.MAX_VALUE);
            DeeplingDuration = BUILDER.comment("Time to cast Sunken Swell Spell, Default: 60")
                    .defineInRange("deeplingTime", 60, 0, 72000);
            DeeplingCoolDown = BUILDER.comment("Sunken Swell Spell Cooldown, Default: 100")
                    .defineInRange("deeplingCoolDown", 100, 0, Integer.MAX_VALUE);
            DeeplingSummonDown = BUILDER.comment("Sunken Swell Spell Summon Down, Default: 300")
                    .defineInRange("deeplingSummonDown", 300, 0, 72000);
            DeeplingLimit = BUILDER.comment("Number of Deepling Servants that can a player can have, Default: 32")
                    .defineInRange("deeplingLimit", 32, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Sunken Current Spell");
            DeeplingBruteCost = BUILDER.comment("Sunken Current Spell Cost, Default: 32")
                    .defineInRange("deeplingBruteCost", 32, 0, Integer.MAX_VALUE);
            DeeplingBruteDuration = BUILDER.comment("Time to cast Sunken Current Spell, Default: 20")
                    .defineInRange("deeplingBruteTime", 20, 0, 72000);
            DeeplingBruteCoolDown = BUILDER.comment("Sunken Current Spell Cooldown, Default: 600")
                    .defineInRange("deeplingBruteCoolDown", 600, 0, Integer.MAX_VALUE);
            DeeplingBruteSummonDown = BUILDER.comment("Sunken Current Spell Summon Down, Default: 150")
                    .defineInRange("deeplingBruteSummonDown", 150, 0, 72000);
            DeeplingBruteLimit = BUILDER.comment("Number of Deepling Brute Servants that can a player can have, Default: 8")
                    .defineInRange("deeplingBruteLimit", 8, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Sunken Tribune Spell");
            DeeplingCasterCost = BUILDER.comment("Sunken Tribune Spell Cost, Default: 24")
                    .defineInRange("deeplingCasterCost", 24, 0, Integer.MAX_VALUE);
            DeeplingCasterDuration = BUILDER.comment("Time to cast Sunken Tribune Spell, Default: 100")
                    .defineInRange("deeplingCasterTime", 100, 0, 72000);
            DeeplingCasterCoolDown = BUILDER.comment("Sunken Tribune Spell Cooldown, Default: 1200")
                    .defineInRange("deeplingCasterCoolDown", 1200, 0, Integer.MAX_VALUE);
            DeeplingCasterSummonDown = BUILDER.comment("Sunken Tribune Spell Summon Down, Default: 300")
                    .defineInRange("deeplingCasterSummonDown", 300, 0, 72000);
            DeeplingCasterLimit = BUILDER.comment("Number of Deepling Priest/Warlock Servants that can a player can have, Default: 4")
                    .defineInRange("deeplingCasterLimit", 4, 1, Integer.MAX_VALUE);
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
            BUILDER.push("Lightning Spear Spell");
            LightningSpearCost = BUILDER.comment("Lightning Spear Spell Cost, Default: 64")
                    .defineInRange("lightningSpearCost", 64, 0, Integer.MAX_VALUE);
            LightningSpearDuration = BUILDER.comment("Time to cast Lightning Spear Spell, Default: 27")
                    .defineInRange("lightningSpearTime", 27, 0, 72000);
            LightningSpearCoolDown = BUILDER.comment("Lightning Spear Spell Cooldown, Default: 80")
                    .defineInRange("lightningSpearCoolDown", 80, 0, Integer.MAX_VALUE);
            LightningSpearDamage = BUILDER.comment("Lightning Spear Spell Damage, Default: 14.0")
                    .defineInRange("lightningSpearDamage", 14.0, 0, Double.MAX_VALUE);
            LightningSpearAreaDamage = BUILDER.comment("How much damage Lightning Spear's storm cloud deals, Default: 4.0")
                    .defineInRange("lightningSpearAreaDamage", 4.0, 0, Double.MAX_VALUE);
            LightningSpearHPDamage = BUILDER.comment("How much Hp percentage damage Storm Staff fired Lightning Spear's storm cloud deals, Default: 0.04")
                    .defineInRange("lightningSpearHPDamage", 0.04, 0.0, 1.0);
            BUILDER.pop();
            BUILDER.push("Polemistís Spell");
            OctoHostCost = BUILDER.comment("Polemistís Spell Cost, Default: 32")
                    .defineInRange("octoHostCost", 32, 0, Integer.MAX_VALUE);
            OctoHostDuration = BUILDER.comment("Time to cast Polemistís Spell, Default: 60")
                    .defineInRange("octoHostTime", 60, 0, 72000);
            OctoHostCoolDown = BUILDER.comment("Polemistís Spell Cooldown, Default: 100")
                    .defineInRange("octoHostCoolDown", 100, 0, Integer.MAX_VALUE);
            OctoHostSummonDown = BUILDER.comment("Polemistís Spell Summon Down, Default: 300")
                    .defineInRange("octoHostSummonDown", 300, 0, 72000);
            OctoHostLimit = BUILDER.comment("Number of Symbiocto Servants that can a player can have, Default: 32")
                    .defineInRange("octoHostLimit", 16, 1, Integer.MAX_VALUE);
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
            BUILDER.push("Storm Serpent Spell");
            StormSerpentCost = BUILDER.comment("Storm Serpent Spell Cost, Default: 128")
                    .defineInRange("stormSerpentCost", 128, 0, Integer.MAX_VALUE);
            StormSerpentCoolDown = BUILDER.comment("Storm Serpent Spell Cooldown, Default: 350")
                    .defineInRange("stormSerpentCoolDown", 350, 0, Integer.MAX_VALUE);
            StormSerpentDamage = BUILDER.comment("Storm Serpent Spell Damage, Default: 16.0")
                    .defineInRange("stormSerpentDamage", 16.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Thunder Rage Spell");
            ThunderRageCost = BUILDER.comment("Thunder Rage Spell Cost, Default: 64")
                    .defineInRange("thunderRageCost", 64, 0, Integer.MAX_VALUE);
            ThunderRageCoolDown = BUILDER.comment("Thunder Rage Spell Cooldown, Default: 350")
                    .defineInRange("thunderRageCoolDown", 350, 0, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Kakoúrgos Spell");
            UrchinkinCost = BUILDER.comment("Kakoúrgos Spell Cost, Default: 8")
                    .defineInRange("urchinkinCost", 8, 0, Integer.MAX_VALUE);
            UrchinkinDuration = BUILDER.comment("Time to cast Kakoúrgos Spell, Default: 60")
                    .defineInRange("urchinkinTime", 60, 0, 72000);
            UrchinkinCoolDown = BUILDER.comment("Kakoúrgos Spell Cooldown, Default: 100")
                    .defineInRange("urchinkinCoolDown", 100, 0, Integer.MAX_VALUE);
            UrchinkinSummonDown = BUILDER.comment("Kakoúrgos Spell Summon Down, Default: 120")
                    .defineInRange("urchinkinSummonDown", 120, 0, 72000);
            UrchinkinLimit = BUILDER.comment("Number of Urchinkin Servants that can a player can have, Default: 32")
                    .defineInRange("urchinkinLimit", 32, 1, Integer.MAX_VALUE);
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
            BUILDER.push("Water Spear Spell");
            WaterSpearCost = BUILDER.comment("Water Spear Spell Cost, Default: 12")
                    .defineInRange("waterSpearCost", 12, 0, Integer.MAX_VALUE);
            WaterSpearDuration = BUILDER.comment("Time to cast Water Spear Spell, Default: 0")
                    .defineInRange("waterSpearTime", 0, 0, 72000);
            WaterSpearCoolDown = BUILDER.comment("Water Spear Spell Cooldown, Default: 50")
                    .defineInRange("waterSpearCoolDown", 50, 0, Integer.MAX_VALUE);
            WaterSpearDamage = BUILDER.comment("Water Spear Spell Damage, Default: 8.0")
                    .defineInRange("waterSpearDamage", 8.0, 0, Double.MAX_VALUE);
            BUILDER.pop();
        BUILDER.push("Servant Limits");
        AncientRemnantLimit = BUILDER.comment("Number of Ancient Remnants that an individual player can have, Default: 1")
                .defineInRange("ancientRemnantLimit", 1, 0, Integer.MAX_VALUE);
        AptygangrLimit = BUILDER.comment("Number of Aptrgangr Servants that an individual player can have in total, Default: 2")
                .defineInRange("aptygangrLimit", 2, 1, Integer.MAX_VALUE);
        ClawdianLimit = BUILDER.comment("Total number of Clawdian Servants an individual player can have, Default: 2")
                .defineInRange("clawdianLimit", 2, 0, Integer.MAX_VALUE);
        CoralssusLimit = BUILDER.comment("Number of Coralssus Servants that an individual player can have in total, Default: 2")
                .defineInRange("coralssusLimit", 2, 1, Integer.MAX_VALUE);
        CoralGolemLimit = BUILDER.comment("Number of Coral Golem Servants that an individual player can have in total, Default: 4")
                .defineInRange("coralGolemLimit", 4, 1, Integer.MAX_VALUE);
        EnderGolemLimit = BUILDER.comment("Total number of Ender Golems an individual player can have, Default: 2")
                .defineInRange("enderGolemLimit", 2, 0, Integer.MAX_VALUE);
        HippocamtusLimit = BUILDER.comment("Total number of Hippocamtus Servants an individual player can have, Default: 4")
                .defineInRange("hippocamtusLimit", 4, 0, Integer.MAX_VALUE);
        IgnitedBerserkerLimit = BUILDER.comment("Total number of Ignited Berserkers an individual player can have, Default: 2")
                .defineInRange("ignitedBerserkerLimit", 2, 0, Integer.MAX_VALUE);
        IgnitedRevenantLimit = BUILDER.comment("Total number of Ignited Revenant an individual player can have, Default: 2")
                .defineInRange("ignitedRevenantLimit", 2, 0, Integer.MAX_VALUE);
        WatcherLimit = BUILDER.comment("Total number of Watcher Servants an individual player can have, Default: 16")
                .defineInRange("watcherLimit", 16, 0, Integer.MAX_VALUE);
        ProwlerLimit = BUILDER.comment("Total number of Prowlers an individual player can have, Default: 2")
                .defineInRange("prowlerLimit", 2, 0, Integer.MAX_VALUE);
        KobolediatorLimit = BUILDER.comment("Total number of Kobolediators an individual player can have, Default: 2")
                .defineInRange("kobolediatorLimit", 2, 0, Integer.MAX_VALUE);
        WadjetLimit = BUILDER.comment("Total number of Wadjets an individual player can have, Default: 2")
                .defineInRange("wadjetLimit", 2, 0, Integer.MAX_VALUE);
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
