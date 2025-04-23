package com.Polarice3.goety_cataclysm.common.entities;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
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
import com.Polarice3.goety_cataclysm.common.entities.projectiles.*;
import com.Polarice3.goety_cataclysm.common.entities.util.AbyssBlastPortal;
import com.Polarice3.goety_cataclysm.common.entities.util.AbyssMark;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GCEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GoetyCataclysm.MOD_ID);

    public static final RegistryObject<EntityType<EnderGolemServant>> ENDER_GOLEM = register("ender_golem",
            EntityType.Builder.of(EnderGolemServant::new, MobCategory.MONSTER)
                    .sized(2.5F, 3.5F)
                    .fireImmune());

    public static final RegistryObject<EntityType<NetheriteMonstrosityServant>> NETHERITE_MONSTROSITY = register("netherite_monstrosity",
            EntityType.Builder.of(NetheriteMonstrosityServant::new, MobCategory.MONSTER)
                    .sized(3.0F, 5.75F)
                    .fireImmune()
                    .clientTrackingRange(4));

    public static final RegistryObject<EntityType<DeeplingServant>> DEEPLING = register("deepling_servant",
            EntityType.Builder.of(DeeplingServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.3F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<DeeplingBruteServant>> DEEPLING_BRUTE = register("deepling_brute_servant",
            EntityType.Builder.of(DeeplingBruteServant::new, MobCategory.MONSTER)
                    .sized(0.7F, 2.6F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<DeeplingAnglerServant>> DEEPLING_ANGLER = register("deepling_angler_servant",
            EntityType.Builder.of(DeeplingAnglerServant::new, MobCategory.MONSTER)
                    .sized(0.65F, 2.45F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<DeeplingPriestServant>> DEEPLING_PRIEST = register("deepling_priest_servant",
            EntityType.Builder.of(DeeplingPriestServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.3F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<DeeplingWarlockServant>> DEEPLING_WARLOCK = register("deepling_warlock_servant",
            EntityType.Builder.of(DeeplingWarlockServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.3F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<LionfishServant>> LIONFISH = register("lionfish",
            EntityType.Builder.of(LionfishServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 0.55F)
                    .clientTrackingRange(6));

    public static final RegistryObject<EntityType<CoralGolemServant>> CORAL_GOLEM = register("coral_golem",
            EntityType.Builder.of(CoralGolemServant::new, MobCategory.MONSTER)
                    .sized(2.5F, 2.7F)
                    .clientTrackingRange(10));

    public static final RegistryObject<EntityType<CoralssusServant>> CORALSSUS = register("coralssus",
            EntityType.Builder.of(CoralssusServant::new, MobCategory.MONSTER)
                    .sized(2.75F, 2.85F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<IgnitedRevenantServant>> IGNITED_REVENANT = register("ignited_revenant",
            EntityType.Builder.of(IgnitedRevenantServant::new, MobCategory.MONSTER)
                    .sized(1.6F, 2.8F)
                    .fireImmune());

    public static final RegistryObject<EntityType<IgnitedBerserkerServant>> IGNITED_BERSERKER = register("ignited_berserker",
            EntityType.Builder.of(IgnitedBerserkerServant::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.4F)
                    .fireImmune());

    public static final RegistryObject<EntityType<WatcherServant>> WATCHER_SERVANT = register("watcher_servant",
            EntityType.Builder.of(WatcherServant::new, MobCategory.MONSTER)
                    .sized(0.85F, 0.85F)
                    .fireImmune());

    public static final RegistryObject<EntityType<ProwlerServant>> THE_PROWLER = register("the_prowler",
            EntityType.Builder.of(ProwlerServant::new, MobCategory.MONSTER)
                    .sized(2.5F, 2.75F)
                    .fireImmune()
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<KoboletonServant>> KOBOLETON_SERVANT = register("koboleton_servant",
            EntityType.Builder.of(KoboletonServant::new, MobCategory.MONSTER)
                    .sized(0.85F, 1.6F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<KobolediatorServant>> KOBOLEDIATOR = register("kobolediator",
            EntityType.Builder.of(KobolediatorServant::new, MobCategory.MONSTER)
                    .sized(2.4F, 4.4F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<WadjetServant>> WADJET = register("wadjet",
            EntityType.Builder.of(WadjetServant::new, MobCategory.MONSTER)
                    .sized(0.85F, 3.4F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<DraugrServant>> DRAUGR_SERVANT = register("draugr_servant",
            EntityType.Builder.of(DraugrServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10));

    public static final RegistryObject<EntityType<RoyalDraugrServant>> ROYAL_DRAUGR_SERVANT = register("royal_draugr_servant",
            EntityType.Builder.of(RoyalDraugrServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10));

    public static final RegistryObject<EntityType<EliteDraugrServant>> ELITE_DRAUGR_SERVANT = register("elite_draugr_servant",
            EntityType.Builder.of(EliteDraugrServant::new, MobCategory.MONSTER)
                    .sized(0.8F, 2.6F)
                    .clientTrackingRange(10));

    public static final RegistryObject<EntityType<AptrgangrServant>> APTRGANGR = register("aptrgangr",
            EntityType.Builder.of(AptrgangrServant::new, MobCategory.MONSTER)
                    .sized(2.4F, 4.0F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<IgnisFireball>> IGNIS_FIREBALL = register("ignis_fireball",
            EntityType.Builder.<IgnisFireball>of(IgnisFireball::new, MobCategory.MISC)
                    .sized(0.6F, 0.6F)
                    .setUpdateInterval(1)
                    .setTrackingRange(20)
                    .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<IgnisAbyssFireball>> IGNIS_ABYSS_FIREBALL = register("ignis_abyss_fireball",
            EntityType.Builder.<IgnisAbyssFireball>of(IgnisAbyssFireball::new, MobCategory.MISC)
                    .sized(0.6F, 0.6F)
                    .setUpdateInterval(1)
                    .setTrackingRange(20)
                    .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<FlareBomb>> FLARE_BOMB = register("flare_bomb",
            EntityType.Builder.<FlareBomb>of(FlareBomb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .fireImmune()
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(20));

    public static final RegistryObject<EntityType<AbyssMine>> ABYSS_MINE = register("abyss_mine",
            EntityType.Builder.<AbyssMine>of(AbyssMine::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .fireImmune()
                    .clientTrackingRange(6)
                    .updateInterval(2));

    public static final RegistryObject<EntityType<PortalAbyssBlast>> PORTAL_ABYSS_BLAST = register("portal_abyss_blast",
            EntityType.Builder.<PortalAbyssBlast>of(PortalAbyssBlast::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .fireImmune());

    public static final RegistryObject<EntityType<AbyssBlastPortal>> ABYSS_BLAST_PORTAL = register("abyss_blast_portal",
            EntityType.Builder.<AbyssBlastPortal>of(AbyssBlastPortal::new, MobCategory.MISC)
                    .sized(4.0F, 0.5F)
                    .fireImmune()
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static final RegistryObject<EntityType<AbyssOrb>> ABYSS_ORB = register("abyss_orb",
            EntityType.Builder.<AbyssOrb>of(AbyssOrb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .fireImmune()
                    .setUpdateInterval(1)
                    .setTrackingRange(20)
                    .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<AbyssMark>> ABYSS_MARK = register("abyss_mark",
            EntityType.Builder.<AbyssMark>of(AbyssMark::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .fireImmune()
                    .setUpdateInterval(1)
                    .setTrackingRange(20)
                    .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<VoidVortex>> VOID_VORTEX = register("void_vortex",
            EntityType.Builder.<VoidVortex>of(VoidVortex::new, MobCategory.MISC)
                    .sized(2.5F, 0.5F)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE));

    public static final RegistryObject<EntityType<DeathLaserBeam>> DEATH_LASER_BEAM = register("death_laser_beam",
            EntityType.Builder.<DeathLaserBeam>of(DeathLaserBeam::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .fireImmune());

    public static final RegistryObject<EntityType<LaserBeamProjectile>> LASER_BEAM = register("laser_beam",
            EntityType.Builder.<LaserBeamProjectile>of(LaserBeamProjectile::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .fireImmune()
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<WitherHomingMissile>> WITHER_HOMING_MISSILE = register("wither_homing_missile",
            EntityType.Builder.<WitherHomingMissile>of(WitherHomingMissile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .setUpdateInterval(1)
                    .setTrackingRange(20)
                    .setShouldReceiveVelocityUpdates(true));

    public static final RegistryObject<EntityType<Sandstorm>> SANDSTORM = register("sandstorm",
            EntityType.Builder.<Sandstorm>of(Sandstorm::new, MobCategory.MISC)
                    .sized(2.5F, 4.5F)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String p_20635_, EntityType.Builder<T> p_20636_) {
        return ENTITY_TYPE.register(p_20635_, () -> p_20636_.build(GoetyCataclysm.location(p_20635_).toString()));
    }
}
