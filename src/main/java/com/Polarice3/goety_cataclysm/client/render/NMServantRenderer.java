package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.NMServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.NMPart;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.NetheriteMonstrosityServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class NMServantRenderer extends MobRenderer<NetheriteMonstrosityServant, NMServantModel> {
    private static final ResourceLocation NETHER_MONSTROSITY_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/monstrosity/netherite_monstrosity.png");

    public NMServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new NMServantModel(renderManagerIn.bakeLayer(CMModelLayers.NETHERITE_MONSTROSITY_MODEL)), 2.5F);
        this.addLayer(new Netherite_Monstrosity_Layer(this));
        this.addLayer(new Netherite_Monstrosity_Layer2(this));
        this.addLayer(new Netherite_Monstrosity_Flare(this));
    }
    @Override
    public ResourceLocation getTextureLocation(NetheriteMonstrosityServant entity) {
        return NETHER_MONSTROSITY_TEXTURES;
    }

    public boolean shouldRender(NetheriteMonstrosityServant livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            for(NMPart part : livingEntityIn.monstrosityParts){
                if(camera.isVisible(part.getBoundingBox())){
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    protected void scale(NetheriteMonstrosityServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
    }

    @Override
    protected float getFlipDegrees(NetheriteMonstrosityServant entity) {
        return 0;
    }

    static class Netherite_Monstrosity_Layer extends RenderLayer<NetheriteMonstrosityServant, NMServantModel> {
        private static final ResourceLocation NETHERITE_MONSTRISITY_LAYER_TEXTURES  = new ResourceLocation(Cataclysm.MODID,"textures/entity/monstrosity/netherite_monstrosity_layer.png");

        public Netherite_Monstrosity_Layer(NMServantRenderer renderIn) {
            super(renderIn);
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, NetheriteMonstrosityServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float f = 0.5F;
            f = (float) (f - Mth.clamp((float) entity.deathTime / entity.deathTimer(), 0, 0.5));
            RenderType eyes = CMRenderTypes.CMEyes(NETHERITE_MONSTRISITY_LAYER_TEXTURES);
            VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, f, f, f, f);
        }
    }

    static class Netherite_Monstrosity_Layer2 extends RenderLayer<NetheriteMonstrosityServant, NMServantModel> {
        private static final ResourceLocation NETHERITE_MONSTRISITY_LAYER_TEXTURES  = new ResourceLocation(Cataclysm.MODID,"textures/entity/monstrosity/netherite_monstrosity_layer2.png");

        public Netherite_Monstrosity_Layer2(NMServantRenderer renderIn) {
            super(renderIn);
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, NetheriteMonstrosityServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            RenderType eyes = CMRenderTypes.CMEyes(NETHERITE_MONSTRISITY_LAYER_TEXTURES);
            VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);

            float strength = 0.5F + Mth.clamp(((float) Math.cos((entity.LayerTicks + partialTicks) * 0.1F)) - 0.25F, -0.25F, 0.5F);

            if(!entity.getIsAwaken()){
                strength = 0F;
            }

            strength += Mth.lerp(partialTicks, entity.oLayerBrightness, entity.LayerBrightness) * 1 * Mth.PI;
            strength = Mth.clamp(strength, 0.25f, 1.0F);

            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, strength, strength, strength, 1.0F);
        }
    }

    static class Netherite_Monstrosity_Flare extends RenderLayer<NetheriteMonstrosityServant, NMServantModel> {
        private static final ResourceLocation NETHERITE_MONSTRISITY_OUTER = new ResourceLocation(Cataclysm.MODID,"textures/entity/monstrosity/netherite_monstrosity_flare_outer.png");

        public Netherite_Monstrosity_Flare(NMServantRenderer renderIn) {
            super(renderIn);
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, NetheriteMonstrosityServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            RenderType eyes2 = CMRenderTypes.CMEyes(NETHERITE_MONSTRISITY_OUTER);
            VertexConsumer VertexConsumer2 = bufferIn.getBuffer(eyes2);
            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer2, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.4F);
        }
    }
}
