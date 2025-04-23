package com.Polarice3.goety_cataclysm;

import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import com.Polarice3.goety_cataclysm.common.blocks.entities.GCBlockEntities;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.*;
import com.Polarice3.goety_cataclysm.common.entities.ally.factory.ProwlerServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.factory.WatcherServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.CoralGolemServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.CoralssusServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.EnderGolemServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.NetheriteMonstrosityServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.KobolediatorServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.KoboletonServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.WadjetServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.AptrgangrServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.DraugrServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.EliteDraugrServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.RoyalDraugrServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited.IgnitedBerserkerServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited.IgnitedRevenantServant;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.common.items.GCSpawnEggs;
import com.Polarice3.goety_cataclysm.compat.GCModCompat;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GCCreativeTab;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.minecraftforge.fml.loading.LogMarkers.CORE;

@Mod(GoetyCataclysm.MOD_ID)
public class GoetyCataclysm {
    public static final String MOD_ID = "goety_cataclysm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public GoetyCataclysm() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GCBlockEntities.BLOCK_ENTITY.register(modEventBus);
        GCEntityType.ENTITY_TYPE.register(modEventBus);
        GCCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupEntityAttributeCreation);

        getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve("goety_cataclysm"), "goety_cataclysm");

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GCSpellConfig.SPEC, "goety_cataclysm/goety_cataclysm-spells.toml");
        GCSpellConfig.loadConfig(GCSpellConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("goety_cataclysm/goety_cataclysm-spells.toml").toString());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GCMobsConfig.SPEC, "goety_cataclysm/goety_cataclysm-mobs.toml");
        GCMobsConfig.loadConfig(GCMobsConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("goety_cataclysm/goety_cataclysm-mobs.toml").toString());

        MinecraftForge.EVENT_BUS.register(this);
        GCItems.init();
        GCBlocks.init();
        GCSpawnEggs.init();
    }

    public static Path getOrCreateDirectory(Path dirPath, String dirLabel) {
        if (!Files.isDirectory(dirPath.getParent())) {
            getOrCreateDirectory(dirPath.getParent(), "parent of "+dirLabel);
        }
        if (!Files.isDirectory(dirPath))
        {
            LOGGER.debug(CORE, "Making {} directory : {}", dirLabel, dirPath);
            try {
                Files.createDirectory(dirPath);
            } catch (IOException e) {
                if (e instanceof FileAlreadyExistsException) {
                    LOGGER.error(CORE, "Failed to create {} directory - there is a file in the way", dirLabel);
                } else {
                    LOGGER.error(CORE, "Problem with creating {} directory (Permissions?)", dirLabel, e);
                }
                throw new RuntimeException("Problem creating directory", e);
            }
            LOGGER.debug(CORE, "Created {} directory : {}", dirLabel, dirPath);
        } else {
            LOGGER.debug(CORE, "Found existing {} directory : {}", dirLabel, dirPath);
        }
        return dirPath;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        GCModCompat.setup(event);
    }

    private void setupEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(GCEntityType.ENDER_GOLEM.get(), EnderGolemServant.setCustomAttributes().build());
        event.put(GCEntityType.NETHERITE_MONSTROSITY.get(), NetheriteMonstrosityServant.setCustomAttributes().build());
        event.put(GCEntityType.DEEPLING.get(), DeeplingServant.setCustomAttributes().build());
        event.put(GCEntityType.DEEPLING_BRUTE.get(), DeeplingBruteServant.setCustomAttributes().build());
        event.put(GCEntityType.DEEPLING_ANGLER.get(), DeeplingAnglerServant.setCustomAttributes().build());
        event.put(GCEntityType.DEEPLING_PRIEST.get(), DeeplingPriestServant.setCustomAttributes().build());
        event.put(GCEntityType.DEEPLING_WARLOCK.get(), DeeplingWarlockServant.setCustomAttributes().build());
        event.put(GCEntityType.LIONFISH.get(), LionfishServant.setCustomAttributes().build());
        event.put(GCEntityType.CORAL_GOLEM.get(), CoralGolemServant.setCustomAttributes().build());
        event.put(GCEntityType.CORALSSUS.get(), CoralssusServant.setCustomAttributes().build());
        event.put(GCEntityType.IGNITED_REVENANT.get(), IgnitedRevenantServant.setCustomAttributes().build());
        event.put(GCEntityType.IGNITED_BERSERKER.get(), IgnitedBerserkerServant.setCustomAttributes().build());
        event.put(GCEntityType.WATCHER_SERVANT.get(), WatcherServant.setCustomAttributes().build());
        event.put(GCEntityType.THE_PROWLER.get(), ProwlerServant.setCustomAttributes().build());
        event.put(GCEntityType.KOBOLETON_SERVANT.get(), KoboletonServant.setCustomAttributes().build());
        event.put(GCEntityType.KOBOLEDIATOR.get(), KobolediatorServant.setCustomAttributes().build());
        event.put(GCEntityType.WADJET.get(), WadjetServant.setCustomAttributes().build());
        event.put(GCEntityType.DRAUGR_SERVANT.get(), DraugrServant.setCustomAttributes().build());
        event.put(GCEntityType.ROYAL_DRAUGR_SERVANT.get(), RoyalDraugrServant.setCustomAttributes().build());
        event.put(GCEntityType.ELITE_DRAUGR_SERVANT.get(), EliteDraugrServant.setCustomAttributes().build());
        event.put(GCEntityType.APTRGANGR.get(), AptrgangrServant.setCustomAttributes().build());
    }
}
