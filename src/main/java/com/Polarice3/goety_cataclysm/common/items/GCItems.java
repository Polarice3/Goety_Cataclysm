package com.Polarice3.goety_cataclysm.common.items;

import com.Polarice3.Goety.common.items.ItemBase;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.client.render.block.GCISTER;
import com.Polarice3.goety_cataclysm.common.items.block.EnderGolemSkullItem;
import com.Polarice3.goety_cataclysm.common.items.revive.*;
import com.Polarice3.goety_cataclysm.common.magic.spells.abyss.*;
import com.Polarice3.goety_cataclysm.common.magic.spells.geomancy.AmethystClusterSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.geomancy.DesertCrushSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.geomancy.EarthShakeSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.DraugrSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.EliteDraugrSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.KoboletonSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.RoyalDraugrSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.nether.AshenBreathSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.nether.ExtinctFlameSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.nether.FlareBombSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.storm.DeathLaserSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.storm.LightningSpearSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.storm.StormSerpentSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.storm.ThunderRageSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.void_spells.VoidRuneSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.void_spells.VoidVortexSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.wind.SandstormSpell;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class GCItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GoetyCataclysm.MOD_ID);

    public static void init(){
        GCItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Focus
    ///Abyss
    public static final RegistryObject<Item> ABYSSAL_MINE_FOCUS = ITEMS.register("abyssal_mine_focus", () -> new MagicFocus(new AbyssalMinesSpell()));
    public static final RegistryObject<Item> ABYSSAL_BEAM_FOCUS = ITEMS.register("abyssal_beam_focus", () -> new MagicFocus(new AbyssalBeamSpell()));
    public static final RegistryObject<Item> ABYSSAL_ORB_FOCUS = ITEMS.register("abyssal_orb_focus", () -> new MagicFocus(new AbyssalOrbsSpell()));
    public static final RegistryObject<Item> WATER_SPEAR_FOCUS = ITEMS.register("water_spear_focus", () -> new MagicFocus(new WaterSpearSpell()));
    public static final RegistryObject<Item> SUNKEN_SWELL_FOCUS = ITEMS.register("sunken_swell_focus", () -> new MagicFocus(new DeeplingSpell()));
    public static final RegistryObject<Item> SUNKEN_CURRENT_FOCUS = ITEMS.register("sunken_current_focus", () -> new MagicFocus(new DeeplingBruteSpell()));
    public static final RegistryObject<Item> SUNKEN_TRIBUNE_FOCUS = ITEMS.register("sunken_tribune_focus", () -> new MagicFocus(new DeeplingCasterSpell()));
    public static final RegistryObject<Item> KAKOURGOS_FOCUS = ITEMS.register("kakourgos_focus", () -> new MagicFocus(new UrchinkinSpell()));
    public static final RegistryObject<Item> POLEMISTIS_FOCUS = ITEMS.register("polemistis_focus", () -> new MagicFocus(new OctoHostSpell()));
    public static final RegistryObject<Item> KYRIA_FOCUS = ITEMS.register("kyria_focus", () -> new MagicFocus(new CindariaSpell()));

    ///Geomancy
    public static final RegistryObject<Item> AMETHYST_CLUSTER_FOCUS = ITEMS.register("amethyst_cluster_focus", () -> new MagicFocus(new AmethystClusterSpell()));
    public static final RegistryObject<Item> DESERT_CRUSH_FOCUS = ITEMS.register("desert_crush_focus", () -> new MagicFocus(new DesertCrushSpell()));
    public static final RegistryObject<Item> EARTH_SHAKE_FOCUS = ITEMS.register("earth_shake_focus", () -> new MagicFocus(new EarthShakeSpell()));

    ///Necromancy
    public static final RegistryObject<Item> DESERT_RAID_FOCUS = ITEMS.register("desert_raid_focus", () -> new MagicFocus(new KoboletonSpell()));
    public static final RegistryObject<Item> CURSED_GRAVE_FOCUS = ITEMS.register("cursed_grave_focus", () -> new MagicFocus(new DraugrSpell()));
    public static final RegistryObject<Item> CURSED_CAIRN_FOCUS = ITEMS.register("cursed_cairn_focus", () -> new MagicFocus(new EliteDraugrSpell()));
    public static final RegistryObject<Item> CURSED_TOMB_FOCUS = ITEMS.register("cursed_tomb_focus", () -> new MagicFocus(new RoyalDraugrSpell()));

    ///Nether
    public static final RegistryObject<Item> ASHEN_BREATH_FOCUS = ITEMS.register("ashen_breath_focus", () -> new MagicFocus(new AshenBreathSpell()));
    public static final RegistryObject<Item> EXTINCT_FLAME_FOCUS = ITEMS.register("extinct_flame_focus", () -> new MagicFocus(new ExtinctFlameSpell()));
    public static final RegistryObject<Item> FLARE_BOMB_FOCUS = ITEMS.register("flare_bomb_focus", () -> new MagicFocus(new FlareBombSpell()));

    ///Storm
    public static final RegistryObject<Item> DEATH_LASER_FOCUS = ITEMS.register("death_laser_focus", () -> new MagicFocus(new DeathLaserSpell()));
    public static final RegistryObject<Item> LIGHTNING_SPEAR_FOCUS = ITEMS.register("lightning_spear_focus", () -> new MagicFocus(new LightningSpearSpell()));
    public static final RegistryObject<Item> THUNDER_RAGE_FOCUS = ITEMS.register("thunder_rage_focus", () -> new MagicFocus(new ThunderRageSpell()));
    public static final RegistryObject<Item> STORM_SERPENT_FOCUS = ITEMS.register("storm_serpent_focus", () -> new MagicFocus(new StormSerpentSpell()));

    ///Void
    public static final RegistryObject<Item> VOID_RUNE_FOCUS = ITEMS.register("void_rune_focus", () -> new MagicFocus(new VoidRuneSpell()));
    public static final RegistryObject<Item> VOID_VORTEX_FOCUS = ITEMS.register("void_vortex_focus", () -> new MagicFocus(new VoidVortexSpell()));

    ///Wind
    public static final RegistryObject<Item> SANDSTORM_FOCUS = ITEMS.register("sandstorm_focus", () -> new MagicFocus(new SandstormSpell()));

    //Items
    public static final RegistryObject<Item> ESSENCE_OF_THE_ABYSS = ITEMS.register("abyss_essence", ItemBase::new);
    public static final RegistryObject<Item> IGNITED_HELM = ITEMS.register("ignited_helm", IgnitedHelm::new);
    public static final RegistryObject<Item> IGNITED_SOUL = ITEMS.register("ignited_soul", IgnitedSoul::new);
    public static final RegistryObject<Item> MECHANIZED_CORE = ITEMS.register("mechanized_core", MechanizedCore::new);
    public static final RegistryObject<Item> WARRIOR_SPIRIT = ITEMS.register("warrior_spirit", WarriorSpirit::new);
    public static final RegistryObject<Item> ARCANE_SPIRIT = ITEMS.register("arcane_spirit", ArcaneSpirit::new);
    public static final RegistryObject<Item> SOUL_JAR_DRAUGR = ITEMS.register("soul_jar_draugr", DraugrSoulJar::new);
    public static final RegistryObject<Item> REMNANT_SKULL = ITEMS.register("remnant_skull", GCRemnantSkull::new);
    public static final RegistryObject<Item> ENDER_GOLEM_SKULL_ITEM = ITEMS.register("ender_golem_skull",
            () -> new EnderGolemSkullItem((new Item.Properties()).rarity(Rarity.UNCOMMON).fireResistant()){
                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    consumer.accept(new IClientItemExtensions() {
                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return new GCISTER();
                        }
                    });
                }
            });

    public static boolean shouldSkipCreativeModTab(Item item) {
        return item == REMNANT_SKULL.get();
    }
}
