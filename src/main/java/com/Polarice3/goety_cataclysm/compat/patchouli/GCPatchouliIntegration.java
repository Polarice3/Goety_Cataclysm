package com.Polarice3.goety_cataclysm.compat.patchouli;

import com.Polarice3.Goety.compat.ICompatable;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.blocks.CataclysmBlocks;
import com.Polarice3.goety_cataclysm.common.blocks.GCBlocks;
import com.Polarice3.goety_cataclysm.common.blocks.GoetyBlocks;
import com.Polarice3.goety_cataclysm.init.GCTags;
import com.google.common.base.Suppliers;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.Supplier;

public class GCPatchouliIntegration implements ICompatable {
    public void setup(FMLCommonSetupEvent event) {
        PatchouliAPI.get().registerMultiblock(GoetyCataclysm.location("coral_golem"), CORAL_GOLEM.get());
        PatchouliAPI.get().registerMultiblock(GoetyCataclysm.location("coralssus"), CORALSSUS.get());
        PatchouliAPI.get().registerMultiblock(GoetyCataclysm.location("ender_golem"), ENDER_GOLEM.get());
        PatchouliAPI.get().registerMultiblock(GoetyCataclysm.location("netherite_monstrosity"), NETHERITE_MONSTROSITY.get());
    }

    public static final Supplier<IMultiblock> CORALSSUS = Suppliers.memoize(() -> {
        IStateMatcher amethystMold = PatchouliAPI.get().predicateMatcher(Blocks.AMETHYST_BLOCK,
                state -> state.is(Tags.Blocks.STORAGE_BLOCKS_AMETHYST));
        IStateMatcher coral = PatchouliAPI.get().predicateMatcher(Blocks.FIRE_CORAL_BLOCK,
                state -> state.is(GCTags.Blocks.CORALSSUS_CORALS));
        IStateMatcher stoneMold = PatchouliAPI.get().predicateMatcher(Blocks.STONE_BRICKS,
                state -> state.is(GCTags.Blocks.CORALSSUS_BRICK_MOLD));
        IStateMatcher pickle = PatchouliAPI.get().predicateMatcher(Blocks.SEA_PICKLE,
                state -> state.is(Blocks.SEA_PICKLE) && state.hasProperty(SeaPickleBlock.PICKLES) && state.getValue(SeaPickleBlock.PICKLES) > 1);
        return PatchouliAPI.get().makeMultiblock(
                new String[][] {
                        {
                                "_________",
                                "_________",
                                "_________",
                                "_________",
                                "____P____",
                                "_________",
                                "_________",
                                "_________",
                                "_________",
                        },
                        {
                                "___LLL___",
                                "__LSDSL__",
                                "_LDRRRDL_",
                                "LSRRRRRSL",
                                "LDRRRRRDL",
                                "LSRRRRRSL",
                                "_LDRRRDL_",
                                "__LSDSL__",
                                "___LLL___",
                        },
                        {
                                "_________",
                                "___LLL___",
                                "__LSDSL__",
                                "_LSSSSSL_",
                                "_LDS0SDL_",
                                "_LSSSSSL_",
                                "__LSDSL__",
                                "___LLL___",
                                "_________",
                        }
                },
                'L', Blocks.WATER,
                'P', pickle,
                'D', amethystMold,
                'R', coral,
                'S', stoneMold,
                '0', stoneMold
        );
    });

    public static final Supplier<IMultiblock> CORAL_GOLEM = Suppliers.memoize(() -> {
        IStateMatcher stoneMold = PatchouliAPI.get().predicateMatcher(Blocks.STONE_BRICKS,
                state -> state.is(GCTags.Blocks.CORALSSUS_BRICK_MOLD));
        IStateMatcher amethystMold = PatchouliAPI.get().predicateMatcher(Blocks.AMETHYST_BLOCK,
                state -> state.is(Tags.Blocks.STORAGE_BLOCKS_AMETHYST));
        IStateMatcher coral = PatchouliAPI.get().predicateMatcher(Blocks.FIRE_CORAL_BLOCK,
                state -> state.is(BlockTags.CORAL_BLOCKS));
        IStateMatcher copper = PatchouliAPI.get().predicateMatcher(Blocks.COPPER_BLOCK,
                state -> state.is(Tags.Blocks.STORAGE_BLOCKS_COPPER));
        return PatchouliAPI.get().makeMultiblock(
                new String[][] {
                        {
                                "_________",
                                "___HHH___",
                                "__HGGGH__",
                                "__HGJGH__",
                                "__HGGGH__",
                                "___HHH___",
                                "_________"
                        },
                        {
                                "_________",
                                "_________",
                                "___III___",
                                "___I0I___",
                                "___III___",
                                "_________",
                                "_________"
                        }
                },
                'H', stoneMold,
                'G', coral,
                'J', copper,
                'I', amethystMold,
                '0', amethystMold
        );
    });

    public static final Supplier<IMultiblock> ENDER_GOLEM = Suppliers.memoize(() -> {
        IStateMatcher voidMold = PatchouliAPI.get().predicateMatcher(GCBlocks.VOID_MOLD_BLOCK.get(),
                state -> state.is(GCBlocks.VOID_MOLD_BLOCK.get()));
        IStateMatcher voidStone = PatchouliAPI.get().predicateMatcher(CataclysmBlocks.VOID_STONE.get(),
                state -> state.is(CataclysmBlocks.VOID_STONE.get()));
        IStateMatcher obsidianMold = PatchouliAPI.get().predicateMatcher(Blocks.OBSIDIAN,
                state -> state.is(GCTags.Blocks.ENDER_GOLEM_OBSIDIAN_MOLD));
        return PatchouliAPI.get().makeMultiblock(
                new String[][] {
                        {
                                "_________",
                                "___SDS___",
                                "__DRRRD__",
                                "_SRRRRRS_",
                                "_DRRRRRD_",
                                "_SRRRRRS_",
                                "__DRRRD__",
                                "___SDS___",
                                "_________",
                        },
                        {
                                "_________",
                                "_________",
                                "___SDS___",
                                "__SSSSS__",
                                "__DS0SD__",
                                "__SSSSS__",
                                "___SDS___",
                                "_________",
                                "_________",
                        }
                },
                'D', voidMold,
                'R', voidStone,
                'S', obsidianMold,
                '0', obsidianMold
        );
    });

    public static final Supplier<IMultiblock> NETHERITE_MONSTROSITY = Suppliers.memoize(() -> {
        IStateMatcher gildedMold = PatchouliAPI.get().predicateMatcher(Blocks.GILDED_BLACKSTONE,
                state -> state.is(Blocks.GILDED_BLACKSTONE));
        IStateMatcher redstone = PatchouliAPI.get().predicateMatcher(Blocks.REDSTONE_BLOCK,
                state -> state.is(Tags.Blocks.STORAGE_BLOCKS_REDSTONE));
        IStateMatcher redstoneCore = PatchouliAPI.get().predicateMatcher(GoetyBlocks.REINFORCED_REDSTONE_BLOCK.get(),
                state -> state.is(GoetyBlocks.REINFORCED_REDSTONE_BLOCK.get()));
        IStateMatcher netherBricks = PatchouliAPI.get().predicateMatcher(Blocks.NETHER_BRICKS,
                state -> state.is(Blocks.NETHER_BRICKS));
        IStateMatcher netherite = PatchouliAPI.get().predicateMatcher(Blocks.NETHERITE_BLOCK,
                state -> state.is(Tags.Blocks.STORAGE_BLOCKS_NETHERITE));
        IStateMatcher stoneMold = PatchouliAPI.get().predicateMatcher(Blocks.BLACKSTONE,
                state -> state.getBlock().getDescriptionId().contains("blackstone") && !state.is(Blocks.GILDED_BLACKSTONE) && !(state.getBlock() instanceof SlabBlock) && !(state.getBlock() instanceof StairBlock) && !(state.getBlock() instanceof WallBlock));
        return PatchouliAPI.get().makeMultiblock(
                new String[][] {
                        {
                                "_____OOOOO_____",
                                "____OGSGSGO____",
                                "___OSLLLLLSO___",
                                "__OGLLNNNLLGO__",
                                "_OSLLNBBBNLLSO_",
                                "OGLLNBRRRBNLLGO",
                                "OSLNBRRRRRBNLSO",
                                "OGLNBRRCRRBNLGO",
                                "OSLNBRRRRRBNLSO",
                                "OGLLNBRRRBNLLGO",
                                "_OSLLNBBBNLLSO_",
                                "__OGLLNNNLLGO__",
                                "___OSLLLLLSO___",
                                "____OGSGSGO____",
                                "_____OOOOO_____"
                        },
                        {
                                "_______________",
                                "_____OOOOO_____",
                                "____OGSGSGO____",
                                "___OSSGSGSSO___",
                                "__OSSSSSSSSSO__",
                                "_OGSSSSSSSSSGO_",
                                "_OSGSSSGSSSGSO_",
                                "_OGSSSG0GSSSGO_",
                                "_OSGSSSGSSSGSO_",
                                "_OGSSSSSSSSSGO_",
                                "__OSSSSSSSSSO__",
                                "___OSSGSGSSO___",
                                "____OGSGSGO____",
                                "_____OOOOO_____",
                                "_______________"
                        }
                },
                'O', Blocks.SOUL_FIRE,
                'L', Blocks.LAVA,
                'N', netherite,
                'B', netherBricks,
                'G', gildedMold,
                'R', redstone,
                'C', redstoneCore,
                'S', stoneMold,
                '0', stoneMold
        );
    });

}
