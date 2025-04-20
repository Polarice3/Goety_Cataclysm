package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.LionfishServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.LionfishServant;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LionfishServantRenderer extends MobRenderer<LionfishServant, LionfishServantModel> {
    private static final ResourceLocation LIONFISH_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/deepling/lionfish.png");

    public LionfishServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new LionfishServantModel(), 0.4F);
        this.addLayer(new LionfishLayer(this));
    }

    public ResourceLocation getTextureLocation(LionfishServant entity) {
        return LIONFISH_TEXTURES;
    }

    public static class LionfishLayer extends RenderLayer<LionfishServant, LionfishServantModel> {
        private static final ResourceLocation LION_LAYER_TEXTURES = new ResourceLocation("cataclysm:textures/entity/deepling/lionfish_layer.png");

        public LionfishLayer(LionfishServantRenderer renderIn) {
            super(renderIn);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LionfishServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            RenderType eyes = CMRenderTypes.CMEyes(LION_LAYER_TEXTURES);
            VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
            float strength = 0.5F + Mth.clamp((float)Math.cos((double)(((float)entity.LayerTicks + partialTicks) * 0.1F)) - 0.5F, -0.5F, 0.5F);
            strength += Mth.lerp(partialTicks, entity.oLayerBrightness, entity.LayerBrightness) * 1.0F * 3.1415927F;
            strength = Mth.clamp(strength, 0.1F, 1.0F);
            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, strength, strength, strength, 1.0F);
        }
    }
}
