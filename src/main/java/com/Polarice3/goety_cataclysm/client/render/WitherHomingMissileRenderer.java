package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.WitherHomingMissileModel;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.WitherHomingMissile;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WitherHomingMissileRenderer extends EntityRenderer<WitherHomingMissile> {
    private static final ResourceLocation WITHER_MISSILE = new ResourceLocation(Cataclysm.MODID,"textures/entity/harbinger/wither_homing_missile.png");

    public WitherHomingMissileModel model;

    public WitherHomingMissileRenderer(EntityRendererProvider.Context manager) {
        super(manager);
        this.model = new WitherHomingMissileModel();
    }

    @Override
    protected int getBlockLightLevel(WitherHomingMissile entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(WitherHomingMissile entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(-1.5F, -1.5F, 1.5F);
        matrixStackIn.translate(0F, 0.04F, 0F);
        float f = Mth.rotLerp(partialTicks, entityIn.yRotO, entityIn.getYRot());
        float f1 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
        VertexConsumer vertexconsumer = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
        this.model.setupAnim(entityIn, 0.0F, 0.0F, 0.0F, f, f1);
        this.model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(WitherHomingMissile entity) {
        return WITHER_MISSILE;
    }
}
