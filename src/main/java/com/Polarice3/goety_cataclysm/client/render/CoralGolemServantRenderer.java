package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.CoralGolemServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.CoralGolemServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CoralGolemServantRenderer extends MobRenderer<CoralGolemServant, CoralGolemServantModel> {
    private static final ResourceLocation CORALSSUS_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/deepling/coral_golem.png");

    public CoralGolemServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CoralGolemServantModel(), 1.5F);
    }

    public ResourceLocation getTextureLocation(CoralGolemServant entity) {
        return CORALSSUS_TEXTURES;
    }

    protected void scale(CoralGolemServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.75F, 1.75F, 1.75F);
    }
}
