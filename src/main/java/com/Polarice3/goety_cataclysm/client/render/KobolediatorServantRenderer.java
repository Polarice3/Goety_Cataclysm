package com.Polarice3.goety_cataclysm.client.render;


import com.Polarice3.goety_cataclysm.client.render.model.KobolediatorServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.KobolediatorServant;
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

public class KobolediatorServantRenderer extends MobRenderer<KobolediatorServant, KobolediatorServantModel> {
    private static final ResourceLocation KOBOLEDIATOR_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/koboleton/kobolediator.png");

    public KobolediatorServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new KobolediatorServantModel(renderManagerIn.bakeLayer(CMModelLayers.KOBOLEDIATOR_MODEL)), 1.25F);
        this.addLayer(new Kobolediator_Layer(this));

    }
    @Override
    public ResourceLocation getTextureLocation(KobolediatorServant entity) {
        return KOBOLEDIATOR_TEXTURES;
    }

    @Override
    protected float getFlipDegrees(KobolediatorServant entity) {
        return 0;
    }

    @Override
    protected void scale(KobolediatorServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }

    static class Kobolediator_Layer extends RenderLayer<KobolediatorServant, KobolediatorServantModel> {
        private static final ResourceLocation LAYER_TEXTURES = new ResourceLocation("cataclysm:textures/entity/koboleton/kobolediator_layer.png");

        public Kobolediator_Layer(KobolediatorServantRenderer renderIn) {
            super(renderIn);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, KobolediatorServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getAttackState() != 1 && entity.isAlive()) {
                RenderType eyes = RenderType.eyes(LAYER_TEXTURES);
                VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
                this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }

        }
    }
}

