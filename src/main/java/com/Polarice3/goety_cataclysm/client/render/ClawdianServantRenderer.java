package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.ClawdianServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.ClawdianServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.layer.LayerGenericGlowing;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public class ClawdianServantRenderer extends MobRenderer<ClawdianServant, ClawdianServantModel<ClawdianServant>> {
    private static final ResourceLocation KOBOLEDIATOR_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/shrimp.png");
    private static final ResourceLocation LAYER =new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/shrimp_glow.png");

    public ClawdianServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ClawdianServantModel<>(renderManagerIn.bakeLayer(CMModelLayers.CLAWDIAN_MODEL)), 1.8F);
        this.addLayer(new LayerGenericGlowing(this, LAYER));
        this.addLayer(new ClawdianHoldBlockLayer(this, renderManagerIn.getBlockRenderDispatcher()));
        this.addLayer(new ClawdianHoldEntityLayer(this, renderManagerIn.getEntityRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(ClawdianServant entity) {
        return KOBOLEDIATOR_TEXTURES;
    }

    @Override
    protected float getFlipDegrees(ClawdianServant entity) {
        return 0;
    }

    @Override
    protected void scale(ClawdianServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.1F, 1.1F, 1.1F);
    }

    static class ClawdianHoldBlockLayer extends RenderLayer<ClawdianServant, ClawdianServantModel<ClawdianServant>> {
        private final BlockRenderDispatcher blockRenderer;

        public ClawdianHoldBlockLayer(RenderLayerParent<ClawdianServant, ClawdianServantModel<ClawdianServant>> renderer, BlockRenderDispatcher blockRenderer) {
            super(renderer);
            this.blockRenderer = blockRenderer;
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ClawdianServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            BlockState blockstate = entity.getHoldBlock();
            if (blockstate != null) {
                int amount = 2;
                for (int i = 0; i < amount; i++) {
                    for (int l = 0; l < amount; l++) {
                        float xOffset = (i * 2 - 1) * 0.5F;
                        float zOffset = (l * 2 - 1) * 0.5F;

                        matrixStackIn.pushPose();
                        this.getParentModel().translateToHand(matrixStackIn);
                        matrixStackIn.translate(0.5F + xOffset, 1.0F + 0.08 * (i + l), -0.7F + zOffset);
                        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
                        this.blockRenderer.renderSingleBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                        matrixStackIn.popPose();
                    }
                }
            }
        }
    }

    static class ClawdianHoldEntityLayer extends RenderLayer<ClawdianServant, ClawdianServantModel<ClawdianServant>> {
        private final EntityRenderDispatcher entityRenderer;

        public ClawdianHoldEntityLayer(RenderLayerParent<ClawdianServant, ClawdianServantModel<ClawdianServant>> renderer, EntityRenderDispatcher entityRenderer) {
            super(renderer);
            this.entityRenderer = entityRenderer;
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ClawdianServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.isVehicle()) {
                for (Entity passenger : entity.getPassengers()) {
                    if (passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                        continue;
                    }
                    renderEntityInClaw(partialTicks, matrixStackIn, bufferIn, packedLightIn, passenger, this.entityRenderer);

                }
            }
        }

        public void renderEntityInClaw(float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, Entity entity, EntityRenderDispatcher entityRenderer) {
            Cataclysm.PROXY.releaseRenderingEntity(entity.getUUID());
            poseStack.pushPose();
            this.getParentModel().translateToHand(poseStack);
            poseStack.translate(0.0F, -0.25F , 0F);

            poseStack.scale(1, 1, 1);
            entityRenderer.render(entity, (double)0.0F, (double)0.0F, (double)0.0F, 0.0F, partialTick, poseStack, buffer, packedLight);
            poseStack.popPose();
            Cataclysm.PROXY.blockRenderingEntity(entity.getUUID());
        }
    }
}
