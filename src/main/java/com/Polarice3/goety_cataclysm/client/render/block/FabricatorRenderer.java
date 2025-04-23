package com.Polarice3.goety_cataclysm.client.render.block;

import com.Polarice3.Goety.client.render.block.ModBlockLayer;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.blocks.entities.FabricatorBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FabricatorRenderer implements BlockEntityRenderer<FabricatorBlockEntity> {
    public static final ResourceLocation GLASS_CAGE_TEXTURE = GoetyCataclysm.location("textures/entity/fabricator/cage.png");
    public static final ResourceLocation CORE_TEXTURE = GoetyCataclysm.location("textures/entity/fabricator/core.png");
    private static final RenderType GLASS_CAGE_RENDER = RenderType.entityCutoutNoCull(GLASS_CAGE_TEXTURE);
    private static final RenderType CORE_RENDER = RenderType.entityCutoutNoCull(CORE_TEXTURE);
    private final ModelPart cage;
    private final ModelPart core;

    public FabricatorRenderer(BlockEntityRendererProvider.Context p_i226009_1_) {
        ModelPart modelpart = p_i226009_1_.bakeLayer(ModBlockLayer.ARCA);
        this.cage = modelpart.getChild("cage");
        this.core = modelpart.getChild("core");
    }

    public void render(FabricatorBlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        float f = (float)pBlockEntity.tickCount + pPartialTicks;
        float f1 = pBlockEntity.getActiveRotation(pPartialTicks) * (180F / (float)Math.PI);
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.5D, 0.4F, 0.5D);
        Vector3f vector3f = (new Vector3f(0.5F, 1.0F, 0.5F)).normalize();
        vector3f.normalize();
        pMatrixStack.mulPose((new Quaternionf()).rotationAxis(f1 * ((float)Math.PI / 180F), vector3f));
        VertexConsumer ivertexbuilder = pBuffer.getBuffer(GLASS_CAGE_RENDER);
        this.cage.render(pMatrixStack, ivertexbuilder, pCombinedLight, pCombinedOverlay);
        pMatrixStack.popPose();
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.5D, 0.4F, 0.5D);
        pMatrixStack.scale(0.5F, 0.5F, 0.5F);
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(3 * f));
        VertexConsumer ivertexbuilder2 = pBuffer.getBuffer(CORE_RENDER);
        this.core.render(pMatrixStack, ivertexbuilder2, pCombinedLight, pCombinedOverlay);
        pMatrixStack.popPose();
    }

}
