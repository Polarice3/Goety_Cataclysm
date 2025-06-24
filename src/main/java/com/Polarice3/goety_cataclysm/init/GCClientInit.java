package com.Polarice3.goety_cataclysm.init;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.client.render.*;
import com.Polarice3.goety_cataclysm.client.render.block.EnderGolemSkullBlockEntityRenderer;
import com.Polarice3.goety_cataclysm.client.render.block.FabricatorRenderer;
import com.Polarice3.goety_cataclysm.common.blocks.entities.GCBlockEntities;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GoetyCataclysm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GCClientInit {

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(GCBlockEntities.FABRICATOR.get(), FabricatorRenderer::new);
        event.registerBlockEntityRenderer(GCBlockEntities.ENDER_GOLEM_SKULL.get(), EnderGolemSkullBlockEntityRenderer::new);
        event.registerEntityRenderer(GCEntityType.ENDER_GOLEM.get(), EnderGolemServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.NETHERITE_MONSTROSITY.get(), NMServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.DEEPLING.get(), DeeplingServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.DEEPLING_BRUTE.get(), DeeplingBruteServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.DEEPLING_ANGLER.get(), DeeplingAnglerServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.DEEPLING_PRIEST.get(), DeeplingPriestServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.DEEPLING_WARLOCK.get(), DeeplingWarlockServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.LIONFISH.get(), LionfishServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.CORAL_GOLEM.get(), CoralGolemServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.CORALSSUS.get(), CoralssusServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.IGNITED_REVENANT.get(), IgnitedRevenantServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.IGNITED_BERSERKER.get(), IgnitedBerserkerServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.WATCHER_SERVANT.get(), WatcherServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.THE_PROWLER.get(), ProwlerServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.KOBOLETON_SERVANT.get(), KoboletonServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.KOBOLEDIATOR.get(), KobolediatorServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.WADJET.get(), WadjetServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.DRAUGR_SERVANT.get(), DraugrServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.ROYAL_DRAUGR_SERVANT.get(), RoyalDraugrServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.ELITE_DRAUGR_SERVANT.get(), EliteDraugrServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.APTRGANGR.get(), AptrgangrServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.HIPPOCAMTUS.get(), HippocamtusServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.CINDARIA_SERVANT.get(), CindariaServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.CLAWDIAN.get(), ClawdianServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.URCHINKIN_SERVANT.get(), UrchinkinServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.DROWNED_HOST_SERVANT.get(), DrownedHostServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.SYMBIOCTO_SERVANT.get(), SymbioctoServantRenderer::new);
        event.registerEntityRenderer(GCEntityType.IGNIS_FIREBALL.get(), IgnisFireballRenderer::new);
        event.registerEntityRenderer(GCEntityType.IGNIS_ABYSS_FIREBALL.get(), IgnisAbyssFireballRenderer::new);
        event.registerEntityRenderer(GCEntityType.FLARE_BOMB.get(), FlareBombRenderer::new);
        event.registerEntityRenderer(GCEntityType.ABYSS_MINE.get(), AbyssMineRenderer::new);
        event.registerEntityRenderer(GCEntityType.ABYSS_BLAST_PORTAL.get(), AbyssBlastPortalRenderer::new);
        event.registerEntityRenderer(GCEntityType.PORTAL_ABYSS_BLAST.get(), PortalAbyssBlastRenderer::new);
        event.registerEntityRenderer(GCEntityType.ABYSS_ORB.get(), AbyssOrbRenderer::new);
        event.registerEntityRenderer(GCEntityType.ABYSS_MARK.get(), AbyssMarkRenderer::new);
        event.registerEntityRenderer(GCEntityType.VOID_VORTEX.get(), VoidVortexRenderer::new);
        event.registerEntityRenderer(GCEntityType.STORM_SERPENT.get(), StormSerpentRenderer::new);
        event.registerEntityRenderer(GCEntityType.DEATH_LASER_BEAM.get(), DeathLaserBeamRenderer::new);
        event.registerEntityRenderer(GCEntityType.LASER_BEAM.get(), LaserBeamRenderer::new);
        event.registerEntityRenderer(GCEntityType.WITHER_HOMING_MISSILE.get(), WitherHomingMissileRenderer::new);
        event.registerEntityRenderer(GCEntityType.SANDSTORM.get(), SandstormRenderer::new);
        event.registerEntityRenderer(GCEntityType.OCTO_INK.get(), OctoInkRenderer::new);
    }
}
