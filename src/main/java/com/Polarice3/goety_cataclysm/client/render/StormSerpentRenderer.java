package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.StormSerpentModel;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.StormSerpent;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class StormSerpentRenderer extends EntityRenderer<StormSerpent> {
    private static final ResourceLocation SNAKE = new ResourceLocation(Cataclysm.MODID,"textures/entity/scylla/storm_serpent.png");
    private final StormSerpentModel model ;

    public StormSerpentRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.model = new StormSerpentModel(renderManagerIn.bakeLayer(CMModelLayers.STORM_SERPENT_MODEL));
    }

    public void render(StormSerpent entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-90.0F));
        matrixStackIn.translate(0.0D, 1.0F, 0.0D);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        float f = Mth.rotLerp(partialTicks, entityIn.yRotO, entityIn.getYRot());
        float f1 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
        model.setupAnim(entityIn, 0, 0, entityIn.tickCount + partialTicks, f, f1);
        float alpha = 0.8F;
        VertexConsumer vertexConsumer = bufferIn.getBuffer(CMRenderTypes.getGhost(this.getTextureLocation(entityIn)));
        this.model.renderToBuffer(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY,alpha,1,1,1);

        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);


    }

    protected int getBlockLightLevel(StormSerpent entityIn, BlockPos pos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(StormSerpent entity) {
        return SNAKE;
    }
}
