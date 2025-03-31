package com.Polarice3.goety_cataclysm.data;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GCEntityTypeTagsProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
    public GCEntityTypeTagsProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, Registries.ENTITY_TYPE, p_256572_, (p_256665_) -> p_256665_.builtInRegistryHolder().key(), GoetyCataclysm.MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(GCEntityType.NETHERITE_MONSTROSITY.get())
                .add(GCEntityType.KOBOLETON_SERVANT.get())
                .add(GCEntityType.KOBOLEDIATOR.get())
                .add(GCEntityType.WADJET.get())
                .add(GCEntityType.ENDER_GOLEM.get())
                .add(GCEntityType.CORALSSUS.get())
                .add(GCEntityType.CORAL_GOLEM.get())
                .add(GCEntityType.IGNITED_REVENANT.get())
                .add(GCEntityType.IGNITED_BERSERKER.get())
                .add(GCEntityType.APTRGANGR.get());
        this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(GCEntityType.NETHERITE_MONSTROSITY.get())
                .add(GCEntityType.ENDER_GOLEM.get())
                .add(GCEntityType.DRAUGR_SERVANT.get())
                .add(GCEntityType.ELITE_DRAUGR_SERVANT.get())
                .add(GCEntityType.ROYAL_DRAUGR_SERVANT.get())
                .add(GCEntityType.APTRGANGR.get());
        this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                .add(GCEntityType.NETHERITE_MONSTROSITY.get())
                .add(GCEntityType.ENDER_GOLEM.get())
                .add(GCEntityType.DRAUGR_SERVANT.get())
                .add(GCEntityType.ELITE_DRAUGR_SERVANT.get())
                .add(GCEntityType.ROYAL_DRAUGR_SERVANT.get())
                .add(GCEntityType.APTRGANGR.get());
        this.tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(GCEntityType.ABYSS_ORB.get())
                .add(GCEntityType.IGNIS_FIREBALL.get())
                .add(GCEntityType.IGNIS_ABYSS_FIREBALL.get());
    }
}
