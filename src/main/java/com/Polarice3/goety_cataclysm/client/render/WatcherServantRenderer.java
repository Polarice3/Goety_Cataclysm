package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.WatcherServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.factory.WatcherServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.render.layer.LayerGenericGlowing;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WatcherServantRenderer extends MobRenderer<WatcherServant, WatcherServantModel> {
    private static final ResourceLocation WATCHER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_watcher.png");
    private static final ResourceLocation WATCHER_LAYER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_watcher_layer.png");

    public WatcherServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new WatcherServantModel(), 0.7F);
        this.addLayer(new LayerGenericGlowing(this, WATCHER_LAYER_TEXTURES));
    }

    @Override
    public ResourceLocation getTextureLocation(WatcherServant entity) {
        return WATCHER_TEXTURES;
    }
}
