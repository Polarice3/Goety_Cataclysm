package com.Polarice3.goety_cataclysm.client.render.layer;

import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.AbstractDeeplingServant;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AbstractDeeplingServantLayer<T extends AbstractDeeplingServant> extends RenderLayer<T, EntityModel<T>> {
    private final ResourceLocation texture;
    private final RenderType renderType;

    public AbstractDeeplingServantLayer(RenderLayerParent<T, EntityModel<T>> renderer, ResourceLocation texture) {
        super(renderer);
        this.texture = texture;
        this.renderType = CMRenderTypes.CMEyes(texture);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer VertexConsumer = bufferIn.getBuffer(this.renderType);
        float strength = 0.5F + Mth.clamp((float)Math.cos((double)(((float)entitylivingbaseIn.LayerTicks + partialTicks) * 0.1F)) - 0.5F, -0.5F, 0.5F);
        strength += Mth.lerp(partialTicks, entitylivingbaseIn.oLayerBrightness, entitylivingbaseIn.LayerBrightness) * 1.0F * 3.1415927F;
        strength = Mth.clamp(strength, 0.1F, 1.0F);
        this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, strength, strength, strength, 1.0F);
    }
}
