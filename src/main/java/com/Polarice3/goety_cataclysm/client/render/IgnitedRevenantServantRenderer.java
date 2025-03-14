package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.IgnitedRevenantServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited.IgnitedRevenantServant;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class IgnitedRevenantServantRenderer extends MobRenderer<IgnitedRevenantServant, IgnitedRevenantServantModel> {
    private static final ResourceLocation IGNITED_REVENANT_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/revenant_body.png");
    private static final ResourceLocation IGNITED_REVENANT_LAYER_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/revenant_layer.png");
    private final RandomSource randomSource = RandomSource.create();

    public IgnitedRevenantServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new IgnitedRevenantServantModel(), 0.5F);
        this.addLayer(new Ignited_Revenant_GlowLayer(this));
        this.addLayer(new Revenant_Layer(this));
    }

    public ResourceLocation getTextureLocation(IgnitedRevenantServant entity) {
        return IGNITED_REVENANT_TEXTURES;
    }

    protected void scale(IgnitedRevenantServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.1F, 1.1F, 1.1F);
    }

    public Vec3 getRenderOffset(IgnitedRevenantServant entityIn, float partialTicks) {
        if (entityIn.getAnimation() == IgnitedRevenantServant.ASH_BREATH_ATTACK && entityIn.getAnimationTick() >= 28 && entityIn.getAnimationTick() <= 43) {
            double d0 = 0.02;
            return new Vec3(this.randomSource.nextGaussian() * d0, 0.0, this.randomSource.nextGaussian() * d0);
        } else {
            return super.getRenderOffset(entityIn, partialTicks);
        }
    }

    static class Ignited_Revenant_GlowLayer extends RenderLayer<IgnitedRevenantServant, IgnitedRevenantServantModel> {
        public Ignited_Revenant_GlowLayer(IgnitedRevenantServantRenderer p_i50928_1_) {
            super(p_i50928_1_);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, IgnitedRevenantServant entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(CMRenderTypes.getFlickering(IGNITED_REVENANT_LAYER_TEXTURES, 0.0F));
            float alpha = 0.5F + (Mth.cos(ageInTicks * 0.2F) + 1.0F) * 0.2F;
            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, 240, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }

    static class Revenant_Layer extends RenderLayer<IgnitedRevenantServant, IgnitedRevenantServantModel> {
        private final IgnitedRevenantServantModel model = new IgnitedRevenantServantModel();
        private static final ResourceLocation REVENANT_SHIELD = new ResourceLocation("cataclysm", "textures/entity/revenant_shield.png");

        public Revenant_Layer(IgnitedRevenantServantRenderer renderIgnis) {
            super(renderIgnis);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, IgnitedRevenantServant revenant, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.setupAnim(revenant, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer lvt_13_1_ = bufferIn.getBuffer(RenderType.entityCutoutNoCull(REVENANT_SHIELD));
            this.model.renderToBuffer(matrixStackIn, lvt_13_1_, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
