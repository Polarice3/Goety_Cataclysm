package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.UrchinkinServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.UrchinkinServant;
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

public class UrchinkinServantRenderer extends MobRenderer<UrchinkinServant, UrchinkinServantModel<UrchinkinServant>> {
    private static final ResourceLocation URCHIN_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/sea/urchinkin.png");
    private static final ResourceLocation MEAT_BOY = new ResourceLocation("cataclysm", "textures/entity/sea/meat_boy.png");

    public UrchinkinServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new UrchinkinServantModel<>(renderManagerIn.bakeLayer(CMModelLayers.URCHINKIN_MODEL)), 0.25F);
        this.addLayer(new UrchinkinLayer(this));
    }

    public ResourceLocation getTextureLocation(UrchinkinServant entity) {
        return entity.isMeatBoy() ? MEAT_BOY : URCHIN_TEXTURES;
    }

    public static class UrchinkinLayer extends RenderLayer<UrchinkinServant, UrchinkinServantModel<UrchinkinServant>> {
        private static final ResourceLocation URCHIN_LAYER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/urchinkin_layer.png");
        private static final ResourceLocation MEAT_BOY_LAYER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/meat_boy_layer.png");

        public UrchinkinLayer(UrchinkinServantRenderer renderIn) {
            super(renderIn);

        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, UrchinkinServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            RenderType eyes = RenderType.eyes(Texture(entity));
            VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY,1,1,1,1);;
        }

        public ResourceLocation Texture(UrchinkinServant entity) {
            if (entity.isMeatBoy()) {
                return MEAT_BOY_LAYER_TEXTURES;
            } else {
                return URCHIN_LAYER_TEXTURES;
            }
        }
    }
}
