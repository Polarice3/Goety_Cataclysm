package com.Polarice3.goety_cataclysm.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class GCAttributesConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingAnglerHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingAnglerArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingAnglerDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingBruteHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingBruteArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingBruteDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingPriestHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingPriestArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingPriestDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingWarlockHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingWarlockArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> DeeplingWarlockDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> LionfishHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> LionfishArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> LionfishDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> CoralGolemHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> CoralGolemArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> CoralGolemDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> CoralssusHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> CoralssusArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> CoralssusDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> WatcherHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> WatcherArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> WatcherMeleeDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> WatcherRangeDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> ProwlerHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ProwlerArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> ProwlerMeleeDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> ProwlerMissileDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> KoboletonHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> KoboletonArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> KoboletonDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> KobolediatorHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> KobolediatorArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> KobolediatorDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> WadjetHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> WadjetArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> WadjetDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> IgnitedRevenantHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> IgnitedRevenantArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> IgnitedRevenantMeleeDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> IgnitedRevenantRangeDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> IgnitedBerserkerHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> IgnitedBerserkerArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> IgnitedBerserkerDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> DraugrHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> DraugrArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> DraugrDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> EliteDraugrHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> EliteDraugrArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> EliteDraugrMeleeDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> EliteDraugrRangeDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> RoyalDraugrHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> RoyalDraugrArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> RoyalDraugrDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> AptrgangrHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> AptrgangrArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> AptrgangrDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> AptrgangrAxeDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> EnderGolemHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> EnderGolemArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> EnderGolemDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> NetheriteMonstrosityHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> NetheriteMonstrosityArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> NetheriteMonstrosityDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> CindariaHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> CindariaArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> CindariaMeleeDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> CindariaRangeDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> ClawdianHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> ClawdianArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> ClawdianDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> HippocamtusHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> HippocamtusArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> HippocamtusDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> SymbioctoHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> SymbioctoArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> SymbioctoMeleeDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> SymbioctoRangeDamage;

    public static final ForgeConfigSpec.ConfigValue<Double> UrchinkinHealth;
    public static final ForgeConfigSpec.ConfigValue<Double> UrchinkinArmor;
    public static final ForgeConfigSpec.ConfigValue<Double> UrchinkinDamage;

    static {
        BUILDER.push("Attributes");
            BUILDER.push("Summoned Mobs");
                BUILDER.push("Deeplings");
                    BUILDER.push("Deepling Servant");
                    DeeplingHealth = BUILDER.comment("How much Max Health Deepling Servants have, Default: 26.0")
                            .defineInRange("deeplingHealth", 26.0, 1.0, Double.MAX_VALUE);
                    DeeplingArmor = BUILDER.comment("How much natural Armor Deepling Servants have, Default: 0.0")
                            .defineInRange("deeplingArmor", 0.0, 0.0, Double.MAX_VALUE);
                    DeeplingDamage = BUILDER.comment("How much damage Deepling Servants melee attack deals, Default: 4.0")
                            .defineInRange("deeplingDamage", 4.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Deepling Angler Servant");
                    DeeplingAnglerHealth = BUILDER.comment("How much Max Health Deepling Angler Servants have, Default: 30.0")
                            .defineInRange("deeplingAnglerHealth", 30.0, 1.0, Double.MAX_VALUE);
                    DeeplingAnglerArmor = BUILDER.comment("How much natural Armor Deepling Angler Servants have, Default: 0.0")
                            .defineInRange("deeplingAnglerArmor", 0.0, 0.0, Double.MAX_VALUE);
                    DeeplingAnglerDamage = BUILDER.comment("How much damage Deepling Angler Servants melee attack deals, Default: 4.0")
                            .defineInRange("deeplingAnglerDamage", 4.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Deepling Brute Servant");
                    DeeplingBruteHealth = BUILDER.comment("How much Max Health Deepling Brute Servants have, Default: 60.0")
                            .defineInRange("deeplingBruteHealth", 60.0, 1.0, Double.MAX_VALUE);
                    DeeplingBruteArmor = BUILDER.comment("How much natural Armor Deepling Brute Servants have, Default: 8.0")
                            .defineInRange("deeplingBruteArmor", 8.0, 0.0, Double.MAX_VALUE);
                    DeeplingBruteDamage = BUILDER.comment("How much damage Deepling Brute Servants melee attack deals, Default: 5.0")
                            .defineInRange("deeplingBruteDamage", 5.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Deepling Priest Servant");
                    DeeplingPriestHealth = BUILDER.comment("How much Max Health Deepling Priest Servants have, Default: 45.0")
                            .defineInRange("deeplingPriestHealth", 45.0, 1.0, Double.MAX_VALUE);
                    DeeplingPriestArmor = BUILDER.comment("How much natural Armor Deepling Priest Servants have, Default: 0.0")
                            .defineInRange("deeplingPriestArmor", 0.0, 0.0, Double.MAX_VALUE);
                    DeeplingPriestDamage = BUILDER.comment("How much damage Deepling Priest Servants melee attack deals, Default: 4.0")
                            .defineInRange("deeplingPriestDamage", 4.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Deepling Warlock Servant");
                    DeeplingWarlockHealth = BUILDER.comment("How much Max Health Deepling Warlock Servants have, Default: 45.0")
                            .defineInRange("deeplingWarlockHealth", 45.0, 1.0, Double.MAX_VALUE);
                    DeeplingWarlockArmor = BUILDER.comment("How much natural Armor Deepling Warlock Servants have, Default: 0.0")
                            .defineInRange("deeplingWarlockArmor", 0.0, 0.0, Double.MAX_VALUE);
                    DeeplingWarlockDamage = BUILDER.comment("How much damage Deepling Warlock Servants melee attack deals, Default: 4.0")
                            .defineInRange("deeplingWarlockDamage", 4.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Lionfish Servant");
                    LionfishHealth = BUILDER.comment("How much Max Health Lionfish Servants have, Default: 12.0")
                            .defineInRange("lionfishHealth", 12.0, 1.0, Double.MAX_VALUE);
                    LionfishArmor = BUILDER.comment("How much natural Armor Lionfish Servants have, Default: 0.0")
                            .defineInRange("lionfishArmor", 0.0, 0.0, Double.MAX_VALUE);
                    LionfishDamage = BUILDER.comment("How much damage Lionfish Servants melee attack deals, Default: 2.0")
                            .defineInRange("lionfishDamage", 2.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Coral Golem Servant");
                    CoralGolemHealth = BUILDER.comment("How much Max Health Coral Golem Servants have, Default: 110.0")
                            .defineInRange("coralGolemHealth", 110.0, 1.0, Double.MAX_VALUE);
                    CoralGolemArmor = BUILDER.comment("How much natural Armor Coral Golem Servants have, Default: 5.0")
                            .defineInRange("coralGolemArmor", 5.0, 0.0, Double.MAX_VALUE);
                    CoralGolemDamage = BUILDER.comment("How much damage Coral Golem Servants melee attack deals, Default: 11.0")
                            .defineInRange("coralGolemDamage", 11.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Coralssus Servant");
                    CoralssusHealth = BUILDER.comment("How much Max Health Coralssus Servants have, Default: 160.0")
                            .defineInRange("coralssusHealth", 160.0, 1.0, Double.MAX_VALUE);
                    CoralssusArmor = BUILDER.comment("How much natural Armor Coralssus Servants have, Default: 5.0")
                            .defineInRange("coralssusArmor", 5.0, 0.0, Double.MAX_VALUE);
                    CoralssusDamage = BUILDER.comment("How much damage Coralssus Servants melee attack deals, Default: 10.0")
                            .defineInRange("coralssusDamage", 10.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                BUILDER.pop();
                BUILDER.push("Factory");
                    BUILDER.push("Watcher Servant");
                    WatcherHealth = BUILDER.comment("How much Max Health Watcher Servants have, Default: 25.0")
                            .defineInRange("watcherHealth", 25.0, 1.0, Double.MAX_VALUE);
                    WatcherArmor = BUILDER.comment("How much natural Armor Watcher Servants have, Default: 5.0")
                            .defineInRange("watcherArmor", 5.0, 0.0, Double.MAX_VALUE);
                    WatcherMeleeDamage = BUILDER.comment("How much damage Watcher Servants melee attack deals, Default: 5.0")
                            .defineInRange("watcherMeleeDamage", 5.0, 1.0, Double.MAX_VALUE);
                    WatcherRangeDamage = BUILDER.comment("How much damage Watcher Servants range attack deals, Default: 5.0")
                            .defineInRange("watcherRangeDamage", 5.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Prowler Servant");
                    ProwlerHealth = BUILDER.comment("How much Max Health Prowler Servants have, Default: 160.0")
                            .defineInRange("prowlerHealth", 160.0, 1.0, Double.MAX_VALUE);
                    ProwlerArmor = BUILDER.comment("How much natural Armor Prowler Servants have, Default: 10.0")
                            .defineInRange("prowlerArmor", 10.0, 0.0, Double.MAX_VALUE);
                    ProwlerMeleeDamage = BUILDER.comment("How much damage Prowler Servants melee attack deals, Default: 14.0")
                            .defineInRange("prowlerMeleeDamage", 14.0, 1.0, Double.MAX_VALUE);
                    ProwlerMissileDamage = BUILDER.comment("How much damage Prowler Servants missiles deals, Default: 3.0")
                            .defineInRange("prowlerMissileDamage", 3.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                BUILDER.pop();
                BUILDER.push("Desert");
                    BUILDER.push("Koboleton Servant");
                    KoboletonHealth = BUILDER.comment("How much Max Health Koboleton Servants have, Default: 25.0")
                            .defineInRange("koboletonHealth", 25.0, 1.0, Double.MAX_VALUE);
                    KoboletonArmor = BUILDER.comment("How much natural Armor Koboleton Servants have, Default: 0.0")
                            .defineInRange("koboletonArmor", 0.0, 0.0, Double.MAX_VALUE);
                    KoboletonDamage = BUILDER.comment("How much damage Koboleton Servants melee attack deals, Default: 3.0")
                            .defineInRange("koboletonDamage", 3.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Kobolediator Servant");
                    KobolediatorHealth = BUILDER.comment("How much Max Health Kobolediator Servants have, Default: 180.0")
                            .defineInRange("kobolediatorHealth", 180.0, 1.0, Double.MAX_VALUE);
                    KobolediatorArmor = BUILDER.comment("How much natural Armor Kobolediator Servants have, Default: 10.0")
                            .defineInRange("kobolediatorArmor", 10.0, 0.0, Double.MAX_VALUE);
                    KobolediatorDamage = BUILDER.comment("How much damage Kobolediator Servants melee attack deals, Default: 14.0")
                            .defineInRange("kobolediatorDamage", 14.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Wadjet Servant");
                    WadjetHealth = BUILDER.comment("How much Max Health Wadjet Servants have, Default: 150.0")
                            .defineInRange("wadjetHealth", 150.0, 1.0, Double.MAX_VALUE);
                    WadjetArmor = BUILDER.comment("How much natural Armor Wadjet Servants have, Default: 5.0")
                            .defineInRange("wadjetArmor", 5.0, 0.0, Double.MAX_VALUE);
                    WadjetDamage = BUILDER.comment("How much damage Wadjet Servants melee attack deals, Default: 11.0")
                            .defineInRange("wadjetDamage", 11.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                BUILDER.pop();
                BUILDER.push("Ignited");
                    BUILDER.push("Ignited Revenant Servant");
                    IgnitedRevenantHealth = BUILDER.comment("How much Max Health Ignited Revenant Servants have, Default: 80.0")
                            .defineInRange("ignitedRevenantHealth", 80.0, 1.0, Double.MAX_VALUE);
                    IgnitedRevenantArmor = BUILDER.comment("How much natural Armor Ignited Revenant Servants have, Default: 12.0")
                            .defineInRange("ignitedRevenantArmor", 12.0, 0.0, Double.MAX_VALUE);
                    IgnitedRevenantMeleeDamage = BUILDER.comment("How much damage Ignited Revenant Servants melee attack deals, Default: 6.0")
                            .defineInRange("ignitedRevenantMeleeDamage", 6.0, 1.0, Double.MAX_VALUE);
                    IgnitedRevenantRangeDamage = BUILDER.comment("How much damage Ignited Revenant Servants range attack deals, Default: 5.0")
                            .defineInRange("ignitedRevenantRangeDamage", 5.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Ignited Berserker Servant");
                    IgnitedBerserkerHealth = BUILDER.comment("How much Max Health Ignited Berserker Servants have, Default: 65.0")
                            .defineInRange("ignitedBerserkerHealth", 65.0, 1.0, Double.MAX_VALUE);
                    IgnitedBerserkerArmor = BUILDER.comment("How much natural Armor Ignited Berserker Servants have, Default: 8.0")
                            .defineInRange("ignitedBerserkerArmor", 8.0, 0.0, Double.MAX_VALUE);
                    IgnitedBerserkerDamage = BUILDER.comment("How much damage Ignited Berserker Servants melee attack deals, Default: 7.5")
                            .defineInRange("ignitedBerserkerDamage", 7.5, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                BUILDER.pop();
                BUILDER.push("Draugr");
                    BUILDER.push("Draugr Servant");
                    DraugrHealth = BUILDER.comment("How much Max Health Draugr Servants have, Default: 28.0")
                            .defineInRange("draugrHealth", 28.0, 1.0, Double.MAX_VALUE);
                    DraugrArmor = BUILDER.comment("How much natural Armor Draugr Servants have, Default: 3.0")
                            .defineInRange("draugrArmor", 3.0, 0.0, Double.MAX_VALUE);
                    DraugrDamage = BUILDER.comment("How much damage Draugr Servants melee attack deals, Default: 4.0")
                            .defineInRange("draugrDamage", 4.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Elite Draugr Servant");
                    EliteDraugrHealth = BUILDER.comment("How much Max Health Elite Draugr Servants have, Default: 32.0")
                            .defineInRange("eliteDraugrHealth", 32.0, 1.0, Double.MAX_VALUE);
                    EliteDraugrArmor = BUILDER.comment("How much natural Elite Armor Draugr Servants have, Default: 3.0")
                            .defineInRange("eliteDraugrArmor", 3.0, 0.0, Double.MAX_VALUE);
                    EliteDraugrMeleeDamage = BUILDER.comment("How much damage Elite Draugr Servants melee attack deals, Default: 5.0")
                            .defineInRange("eliteDraugrMeleeDamage", 5.0, 1.0, Double.MAX_VALUE);
                    EliteDraugrRangeDamage = BUILDER.comment("How much extra damage Elite Draugr Servants range attack deals, Default: 0.0")
                            .defineInRange("eliteDraugrRangeDamage", 0.0, 0.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Royal Draugr Servant");
                    RoyalDraugrHealth = BUILDER.comment("How much Max Health Royal Draugr Servants have, Default: 30.0")
                            .defineInRange("royalDraugrHealth", 30.0, 1.0, Double.MAX_VALUE);
                    RoyalDraugrArmor = BUILDER.comment("How much natural Armor Royal Draugr Servants have, Default: 5.0")
                            .defineInRange("royalDraugrArmor", 5.0, 0.0, Double.MAX_VALUE);
                    RoyalDraugrDamage = BUILDER.comment("How much damage Royal Draugr Servants melee attack deals, Default: 5.0")
                            .defineInRange("royalDraugrDamage", 5.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Aptrgangr Servant");
                    AptrgangrHealth = BUILDER.comment("How much Max Health Aptrgangr Servants have, Default: 160.0")
                            .defineInRange("aptrgangrHealth", 160.0, 1.0, Double.MAX_VALUE);
                    AptrgangrArmor = BUILDER.comment("How much natural Armor Aptrgangr Servants have, Default: 10.0")
                            .defineInRange("aptrgangrArmor", 10.0, 0.0, Double.MAX_VALUE);
                    AptrgangrDamage = BUILDER.comment("How much damage Aptrgangr Servants melee attack deals, Default: 18.0")
                            .defineInRange("aptrgangrDamage", 18.0, 1.0, Double.MAX_VALUE);
                    AptrgangrAxeDamage = BUILDER.comment("How much damage Aptrgangr Servants Axe Blade attack deals, Default: 8.0")
                            .defineInRange("aptrgangrAxeDamage", 8.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                BUILDER.pop();
                BUILDER.push("Acropolis");
                    BUILDER.push("Cindaria Servant");
                    CindariaHealth = BUILDER.comment("How much Max Health Cindaria Servants have, Default: 60.0")
                            .defineInRange("cindariaHealth", 60.0, 1.0, Double.MAX_VALUE);
                    CindariaArmor = BUILDER.comment("How much natural Armor Cindaria Servants have, Default: 0.0")
                            .defineInRange("cindariaArmor", 0.0, 0.0, Double.MAX_VALUE);
                    CindariaMeleeDamage = BUILDER.comment("How much damage Cindaria Servants melee attack deals, Default: 7.0")
                            .defineInRange("cindariaMeleeDamage", 7.0, 1.0, Double.MAX_VALUE);
                    CindariaRangeDamage = BUILDER.comment("How much damage Cindaria Servants water spears deals, Default: 8.0")
                            .defineInRange("cindariaRangeDamage", 8.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Clawdian Servant");
                    ClawdianHealth = BUILDER.comment("How much Max Health Clawdian Servants have, Default: 225.0")
                            .defineInRange("clawdianHealth", 225.0, 1.0, Double.MAX_VALUE);
                    ClawdianArmor = BUILDER.comment("How much natural Armor Clawdian Servants have, Default: 12.0")
                            .defineInRange("clawdianArmor", 12.0, 0.0, Double.MAX_VALUE);
                    ClawdianDamage = BUILDER.comment("How much damage Clawdian Servants melee attack deals, Default: 16.0")
                            .defineInRange("clawdianDamage", 16.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Hippocamtus Servant");
                    HippocamtusHealth = BUILDER.comment("How much Max Health Hippocamtus Servants have, Default: 85.0")
                            .defineInRange("hippocamtusHealth", 85.0, 1.0, Double.MAX_VALUE);
                    HippocamtusArmor = BUILDER.comment("How much natural Armor Hippocamtus Servants have, Default: 15.0")
                            .defineInRange("hippocamtusArmor", 15.0, 0.0, Double.MAX_VALUE);
                    HippocamtusDamage = BUILDER.comment("How much damage Hippocamtus Servants melee attack deals, Default: 10.0")
                            .defineInRange("hippocamtusDamage", 10.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Symbiocto Servant");
                    SymbioctoHealth = BUILDER.comment("How much Max Health Symbiocto Servants have, Default: 16.0")
                            .defineInRange("symbioctoHealth", 16.0, 1.0, Double.MAX_VALUE);
                    SymbioctoArmor = BUILDER.comment("How much natural Armor Symbiocto Servants have, Default: 0.0")
                            .defineInRange("symbioctoArmor", 0.0, 0.0, Double.MAX_VALUE);
                    SymbioctoMeleeDamage = BUILDER.comment("How much damage Symbiocto Servants melee attack deals, Default: 3.0")
                            .defineInRange("symbioctoMeleeDamage", 3.0, 1.0, Double.MAX_VALUE);
                    SymbioctoRangeDamage = BUILDER.comment("How much damage Symbiocto Servants range attack deals, Default: 3.0")
                            .defineInRange("symbioctoRangeDamage", 3.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                    BUILDER.push("Urchinkin Servant");
                    UrchinkinHealth = BUILDER.comment("How much Max Health Urchinkin Servants have, Default: 12.0")
                            .defineInRange("urchinkinHealth", 12.0, 1.0, Double.MAX_VALUE);
                    UrchinkinArmor = BUILDER.comment("How much natural Armor Urchinkin Servants have, Default: 0.0")
                            .defineInRange("urchinkinArmor", 0.0, 0.0, Double.MAX_VALUE);
                    UrchinkinDamage = BUILDER.comment("How much damage Urchinkin Servants melee attack deals, Default: 3.0")
                            .defineInRange("urchinkinDamage", 3.0, 1.0, Double.MAX_VALUE);
                    BUILDER.pop();
                BUILDER.pop();
                BUILDER.push("Ender Golem Servant");
                EnderGolemHealth = BUILDER.comment("How much Max Health Ender Golem Servants have, Default: 150.0")
                        .defineInRange("enderGolemHealth", 150.0, 1.0, Double.MAX_VALUE);
                EnderGolemArmor = BUILDER.comment("How much natural Armor Ender Golem Servants have, Default: 12.0")
                        .defineInRange("enderGolemArmor", 12.0, 0.0, Double.MAX_VALUE);
                EnderGolemDamage = BUILDER.comment("How much damage Ender Golem Servants melee attack deals, Default: 10.0")
                        .defineInRange("enderGolemDamage", 10.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
                BUILDER.push("Netherite Monstrosity Servant");
                NetheriteMonstrosityHealth = BUILDER.comment("How much Max Health Netherite Monstrosity Servants have, Default: 600.0")
                        .defineInRange("netheriteMonstrosityHealth", 600.0, 1.0, Double.MAX_VALUE);
                NetheriteMonstrosityArmor = BUILDER.comment("How much natural Armor Netherite Monstrosity Servants have, Default: 12.0")
                        .defineInRange("netheriteMonstrosityArmor", 12.0, 0.0, Double.MAX_VALUE);
                NetheriteMonstrosityDamage = BUILDER.comment("How much damage Netherite Monstrosity Servants melee attack deals, Default: 25.0")
                        .defineInRange("netheriteMonstrosityDamage", 25.0, 1.0, Double.MAX_VALUE);
                BUILDER.pop();
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
