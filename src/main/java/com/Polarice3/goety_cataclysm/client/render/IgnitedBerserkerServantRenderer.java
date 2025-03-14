package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.IgnitedBerserkerServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited.IgnitedBerserkerServant;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IgnitedBerserkerServantRenderer extends MobRenderer<IgnitedBerserkerServant, IgnitedBerserkerServantModel<IgnitedBerserkerServant>> {
    private static final ResourceLocation BERSERKER_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/ignited_berserker.png");
    private static final ResourceLocation BERSERKER_LAYER_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/ignited_berserker_layer.png");

    public IgnitedBerserkerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new IgnitedBerserkerServantModel<>(renderManagerIn.bakeLayer(CMModelLayers.IGNITED_BERSERKER_MODEL)), 0.5F);
        this.addLayer(new Ignited_Berserker_GlowLayer(this));
    }

    public ResourceLocation getTextureLocation(IgnitedBerserkerServant entity) {
        return BERSERKER_TEXTURES;
    }

    protected void scale(IgnitedBerserkerServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.05F, 1.05F, 1.05F);
    }

    static class Ignited_Berserker_GlowLayer extends RenderLayer<IgnitedBerserkerServant, IgnitedBerserkerServantModel<IgnitedBerserkerServant>> {
        public Ignited_Berserker_GlowLayer(IgnitedBerserkerServantRenderer p_i50928_1_) {
            super(p_i50928_1_);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, IgnitedBerserkerServant entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(CMRenderTypes.getFlickering(BERSERKER_LAYER_TEXTURES, 0.0F));
            float alpha = 0.5F + (Mth.cos(ageInTicks * 0.2F) + 1.0F) * 0.2F;
            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, 240, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }
}
