package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.DraugrServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.DraugrServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.layer.LayerGenericGlowing;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class DraugrServantRenderer extends MobRenderer<DraugrServant, DraugrServantModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cataclysm.MODID,"textures/entity/draugar/draugr.png");
    private static final ResourceLocation LAYER = new ResourceLocation(Cataclysm.MODID,"textures/entity/draugar/draugr_layer.png");

    public DraugrServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DraugrServantModel(renderManagerIn.bakeLayer(CMModelLayers.DRAUGR_MODEL)), 0.5F);
        this.addLayer(new LayerGenericGlowing(this, LAYER));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    protected void scale(DraugrServant p_114907_, PoseStack p_114908_, float p_114909_) {
        float f = 1.0F;
        p_114908_.scale(f, f, f);
        super.scale(p_114907_, p_114908_, p_114909_);
    }

    public ResourceLocation getTextureLocation(DraugrServant entity) {
        return TEXTURE;
    }
}


