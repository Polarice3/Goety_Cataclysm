package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.AbyssBlastPortalModel;
import com.Polarice3.goety_cataclysm.common.entities.util.AbyssBlastPortal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class AbyssBlastPortalRenderer extends EntityRenderer<AbyssBlastPortal> {
    private static final ResourceLocation PORTAL = new ResourceLocation("cataclysm", "textures/entity/leviathan/portal/abyss_blast_portal.png");
    public AbyssBlastPortalModel model = new AbyssBlastPortalModel();

    public AbyssBlastPortalRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    protected int getBlockLightLevel(AbyssBlastPortal entity, BlockPos pos) {
        return 15;
    }

    public void render(AbyssBlastPortal entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        float activateProgress = entityIn.prevactivateProgress + (entityIn.activateProgress - entityIn.prevactivateProgress) * partialTicks;
        float d = activateProgress * 0.15F;
        matrixStackIn.scale(-d, -d, d);
        matrixStackIn.translate(0.0F, -1.5F, 0.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F - entityIn.getYRot()));
        VertexConsumer vertexconsumer = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
        this.model.setupAnim(entityIn, 0.0F, 0.0F, (float)entityIn.tickCount + partialTicks, 0.0F, 0.0F);
        this.model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(AbyssBlastPortal entity) {
        return PORTAL;
    }
}
