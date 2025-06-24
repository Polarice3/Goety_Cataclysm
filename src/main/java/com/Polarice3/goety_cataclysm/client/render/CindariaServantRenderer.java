package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.CindariaServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.CindariaServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class CindariaServantRenderer extends MobRenderer<CindariaServant, CindariaServantModel<CindariaServant>> {
    private static final ResourceLocation CINDARIA_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/cindaria_armor.png");

    public CindariaServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CindariaServantModel<>(renderManagerIn.bakeLayer(CMModelLayers.CINDARIA_MODEL)), 0.5F);
        this.addLayer(new CindariaLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(CindariaServant entity) {
        return CINDARIA_TEXTURES;
    }

    @Override
    protected float getFlipDegrees(CindariaServant entity) {
        return 0;
    }

    @Override
    protected void scale(CindariaServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
    }

    public class CindariaLayer extends RenderLayer<CindariaServant, CindariaServantModel<CindariaServant>> {
        private static final ResourceLocation LAYER_TEXTURES  = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/cindaria_body.png");

        public CindariaLayer(CindariaServantRenderer renderIn) {
            super(renderIn);
        }

        public ResourceLocation getLayerTextureLocation() {
            return LAYER_TEXTURES;
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, CindariaServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!entity.isInvisible()) {
                RenderType ghost = CMRenderTypes.jelly(this.getLayerTextureLocation());
                VertexConsumer VertexConsumer = bufferIn.getBuffer(ghost);
                float alpha = 0.65F;
                boolean hurt = Math.max(entity.hurtTime, entity.deathTime) > 0;
                float c = hurt ? 0.5F : 1.0F;
                this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F),1,c,c, alpha);
            }
        }
    }
}
