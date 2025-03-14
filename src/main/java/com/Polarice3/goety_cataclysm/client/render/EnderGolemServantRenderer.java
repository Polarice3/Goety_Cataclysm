package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.EnderGolemServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.EnderGolemServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EnderGolemServantRenderer extends MobRenderer<EnderGolemServant, EnderGolemServantModel> {
    private static final ResourceLocation ENDER_GOLEM_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/ender_golem.png");

    public EnderGolemServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new EnderGolemServantModel(), 1.5F);
        this.addLayer(new EnderGolemLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EnderGolemServant entity) {
        return ENDER_GOLEM_TEXTURES;
    }

    protected void scale(EnderGolemServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }

    protected float getFlipDegrees(EnderGolemServant entity) {
        return 0.0F;
    }

    public static class EnderGolemLayer extends RenderLayer<EnderGolemServant, EnderGolemServantModel> {
        private static final ResourceLocation ENDER_GOLEM_LAYER_TEXTURES = new ResourceLocation("cataclysm:textures/entity/ender_golem_layer.png");

        public EnderGolemLayer(EnderGolemServantRenderer renderIn) {
            super(renderIn);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EnderGolemServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.deathTime <= 45) {
                float f = 1.0F - entity.deactivateProgress / 30.0F;
                RenderType eyes = RenderType.eyes(ENDER_GOLEM_LAYER_TEXTURES);
                VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
                this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, f, f, f, f);
            }

        }
    }
}
