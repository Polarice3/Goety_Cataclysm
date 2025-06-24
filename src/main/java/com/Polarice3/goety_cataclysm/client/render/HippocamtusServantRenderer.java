package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.HippocamtusServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.HippocamtusServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
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
import net.minecraft.util.FastColor;

public class HippocamtusServantRenderer extends MobRenderer<HippocamtusServant, HippocamtusServantModel<HippocamtusServant>> {
    private static final ResourceLocation KOBOLEDIATOR_TEXTURES = new ResourceLocation(Cataclysm.MODID, "textures/entity/sea/hippocamtus.png");

    public HippocamtusServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new HippocamtusServantModel<>(renderManagerIn.bakeLayer(CMModelLayers.HIPPOCAMTUS_MODEL)), 0.75F);
        this.addLayer(new HippocamtusLayer(this));
    }
    @Override
    public ResourceLocation getTextureLocation(HippocamtusServant entity) {
        return KOBOLEDIATOR_TEXTURES;
    }

    @Override
    protected float getFlipDegrees(HippocamtusServant entity) {
        return 0;
    }

    @Override
    protected void scale(HippocamtusServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }

    public class HippocamtusLayer extends RenderLayer<HippocamtusServant, HippocamtusServantModel<HippocamtusServant>> {
        private static final ResourceLocation PROWLER_LAYER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/hippocamtus_layer.png");

        public HippocamtusLayer(HippocamtusServantRenderer renderIn) {
            super(renderIn);
        }

        protected ResourceLocation getTextureLocation(HippocamtusServant entity) {
            return PROWLER_LAYER_TEXTURES;
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, HippocamtusServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getAttackState() == 4) {

                int f;
                if (entity.attackTicks < 7) {
                    f = entity.attackTicks * 1 / 12;
                } else if (entity.attackTicks <= 17) {
                    f = 1;
                } else {
                    f = Math.max(0, 255 - ((entity.attackTicks - 17) * 1 / (38 - 17)));
                }
                int i = FastColor.ARGB32.color(255, f, f, f);

                RenderType eyes = CMRenderTypes.CMEyes(this.getTextureLocation(entity));
                VertexConsumer vertexConsumer = bufferIn.getBuffer(eyes);

                this.getParentModel().renderToBuffer(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, f,f,f,1);
            }
        }
    }
}
