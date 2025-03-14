package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.FlareBombModel;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.FlareBomb;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class FlareBombRenderer extends EntityRenderer<FlareBomb> {
    private static final ResourceLocation OUTER_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/monstrosity/flare_bomb_outer.png");
    private static final ResourceLocation INNER_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/monstrosity/flare_bomb_inner.png");
    private final FlareBombModel model;

    public FlareBombRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.model = new FlareBombModel(renderManagerIn.bakeLayer(CMModelLayers.FLARE_BOMB_MODEL));
    }

    public void render(FlareBomb entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose((new Quaternionf()).setAngleAxis(entityYaw * 0.017453292F, 0.0F, -1.0F, 0.0F));
        VertexConsumer VertexConsumer = bufferIn.getBuffer(CMRenderTypes.CMEyes(this.getTextureLocation(entityIn)));
        this.model.setupAnim(entityIn, 0.0F, 0.0F, (float)entityIn.tickCount + partialTicks, 0.0F, 0.0F);
        this.model.renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer VertexConsumer2 = bufferIn.getBuffer(CMRenderTypes.CMEyes(OUTER_TEXTURES));
        this.model.renderToBuffer(matrixStackIn, VertexConsumer2, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.4F);
        matrixStackIn.popPose();
    }

    protected int getBlockLightLevel(FlareBomb entityIn, BlockPos pos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(FlareBomb entity) {
        return INNER_TEXTURES;
    }
}
