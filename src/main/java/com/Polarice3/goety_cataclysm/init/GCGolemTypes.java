package com.Polarice3.goety_cataclysm.init;

import com.Polarice3.Goety.api.magic.GolemType;
import com.Polarice3.Goety.api.magic.IMold;
import com.Polarice3.goety_cataclysm.common.blocks.CataclysmBlocks;
import com.Polarice3.goety_cataclysm.common.magic.construct.CoralGolemMold;
import com.Polarice3.goety_cataclysm.common.magic.construct.CoralssusMold;
import com.Polarice3.goety_cataclysm.common.magic.construct.EnderGolemMold;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GCGolemTypes {

    public static final List<GolemType> NEW_GOLEM_TYPES = new ArrayList<>();

    public static void addGolems(){
        addGolem("ENDER_GOLEM", CataclysmBlocks.VOID_STONE.get()::defaultBlockState, new EnderGolemMold());
        addGolem("CORAL_GOLEM", Blocks.COPPER_BLOCK::defaultBlockState, new CoralGolemMold());
        addGolem("CORALSSUS_FIRE", Blocks.FIRE_CORAL_BLOCK::defaultBlockState, new CoralssusMold());
        addGolem("CORALSSUS_HORN", Blocks.HORN_CORAL_BLOCK::defaultBlockState, new CoralssusMold());
        addGolem("CORALSSUS_TUBE", Blocks.TUBE_CORAL_BLOCK::defaultBlockState, new CoralssusMold());
    }

    private static GolemType addGolem(String name, Supplier<BlockState> blockState, IMold mold) {
        GolemType golemType = GolemType.create(name, blockState, mold);
        NEW_GOLEM_TYPES.add(golemType);
        return golemType;
    }
}
