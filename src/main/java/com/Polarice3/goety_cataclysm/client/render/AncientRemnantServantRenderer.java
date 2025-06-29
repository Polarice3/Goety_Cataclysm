package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.AncientRemnantServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.AncientRemnantServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AncientRemnantServantRenderer extends MobRenderer<AncientRemnantServant, AncientRemnantServantModel> {
    private static final ResourceLocation REMNANT_TEXTURES = new ResourceLocation(Cataclysm.MODID, "textures/entity/ancient_remnant/ancient_remnant.png");

    public AncientRemnantServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AncientRemnantServantModel(renderManagerIn.bakeLayer(CMModelLayers.ANCIENT_REMNANT_MODEL)), 1.5F);
        this.addLayer(new AncientRemnantLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AncientRemnantServant entity) {
        return REMNANT_TEXTURES;
    }

    @Override
    protected float getFlipDegrees(AncientRemnantServant entity) {
        return 0;
    }

    public static class AncientRemnantLayer extends RenderLayer<AncientRemnantServant, AncientRemnantServantModel> {
        private static final ResourceLocation LAYER_TEXTURES  = new ResourceLocation(Cataclysm.MODID, "textures/entity/ancient_remnant/ancient_remnant_layer.png");

        public AncientRemnantLayer(AncientRemnantServantRenderer renderIn) {
            super(renderIn);
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AncientRemnantServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getIsPower() && entity.isAlive()) {
                RenderType eyes = RenderType.eyes(LAYER_TEXTURES);
                VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
                this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
