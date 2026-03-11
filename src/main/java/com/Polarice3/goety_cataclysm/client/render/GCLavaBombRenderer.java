package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.common.entities.projectiles.GCLavaBomb;
import com.github.L_Ender.cataclysm.client.model.entity.Lava_Bomb_Model;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GCLavaBombRenderer extends EntityRenderer<GCLavaBomb> {
    private static final ResourceLocation FIRE_BOMB_TEXTURES = new ResourceLocation("cataclysm", "textures/entity/fire_bomb.png");
    private final Lava_Bomb_Model model = new Lava_Bomb_Model();

    public GCLavaBombRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(GCLavaBomb entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.25, 0.0);
        float scale = entityIn.getGround() ? 0.0F : 1.0F;
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 180.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        VertexConsumer VertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entityIn)));
        this.model.renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }

    protected int getBlockLightLevel(GCLavaBomb entityIn, BlockPos pos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(GCLavaBomb entity) {
        return FIRE_BOMB_TEXTURES;
    }
}
