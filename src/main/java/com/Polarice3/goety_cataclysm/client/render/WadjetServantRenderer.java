package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.WadjetServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.WadjetServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class WadjetServantRenderer extends MobRenderer<WadjetServant, WadjetServantModel> {
    private static final ResourceLocation LOCATION = new ResourceLocation("cataclysm", "textures/entity/koboleton/wadjet.png");

    public WadjetServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new WadjetServantModel(), 0.75F);
        this.addLayer(new WadjetServantLayer(this));
    }

    public ResourceLocation getTextureLocation(WadjetServant entity) {
        return LOCATION;
    }

    protected float getFlipDegrees(WadjetServant entity) {
        return 0.0F;
    }

    protected void scale(WadjetServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }

    public static class WadjetServantLayer extends RenderLayer<WadjetServant, WadjetServantModel> {
        private static final ResourceLocation LAYER_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/koboleton/wadjet_layer.png");

        public WadjetServantLayer(WadjetServantRenderer renderIn) {
            super(renderIn);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, WadjetServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getAttackState() != 1 && entity.isAlive()) {
                RenderType eyes = RenderType.eyes(LAYER_TEXTURES);
                VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
                this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }

        }
    }
}
