package com.Polarice3.goety_cataclysm.common.items;

import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.magic.spells.abyss.AbyssalBeamSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.abyss.AbyssalMinesSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.abyss.AbyssalOrbsSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.geomancy.DesertCrushSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.DraugrSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.EliteDraugrSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.KoboletonSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.necromancy.RoyalDraugrSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.nether.BlazingFireSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.nether.FlareBombSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.void_spells.VoidRuneSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.void_spells.VoidVortexSpell;
import com.Polarice3.goety_cataclysm.common.magic.spells.wind.SandstormSpell;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

    ///Geomancy
    public static final RegistryObject<Item> DESERT_CRUSH_FOCUS = ITEMS.register("desert_crush_focus", () -> new MagicFocus(new DesertCrushSpell()));

    ///Necromancy
    public static final RegistryObject<Item> DESERT_RAID_FOCUS = ITEMS.register("desert_raid_focus", () -> new MagicFocus(new KoboletonSpell()));
    public static final RegistryObject<Item> CURSED_GRAVE_FOCUS = ITEMS.register("cursed_grave_focus", () -> new MagicFocus(new DraugrSpell()));
    public static final RegistryObject<Item> CURSED_CAIRN_FOCUS = ITEMS.register("cursed_cairn_focus", () -> new MagicFocus(new EliteDraugrSpell()));
    public static final RegistryObject<Item> CURSED_TOMB_FOCUS = ITEMS.register("cursed_tomb_focus", () -> new MagicFocus(new RoyalDraugrSpell()));

    ///Nether
    public static final RegistryObject<Item> EXTINCT_FLAME_FOCUS = ITEMS.register("extinct_flame_focus", () -> new MagicFocus(new BlazingFireSpell()));
    public static final RegistryObject<Item> FLARE_BOMB_FOCUS = ITEMS.register("flare_bomb_focus", () -> new MagicFocus(new FlareBombSpell()));

    ///Void
    public static final RegistryObject<Item> VOID_RUNE_FOCUS = ITEMS.register("void_rune_focus", () -> new MagicFocus(new VoidRuneSpell()));
    public static final RegistryObject<Item> VOID_VORTEX_FOCUS = ITEMS.register("void_vortex_focus", () -> new MagicFocus(new VoidVortexSpell()));

    ///Wind
    public static final RegistryObject<Item> SANDSTORM_FOCUS = ITEMS.register("sandstorm_focus", () -> new MagicFocus(new SandstormSpell()));
}
