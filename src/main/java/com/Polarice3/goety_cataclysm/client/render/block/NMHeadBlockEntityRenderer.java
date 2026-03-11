package com.Polarice3.goety_cataclysm.client.render.block;

import com.Polarice3.goety_cataclysm.client.render.model.NMHeadModel;
import com.Polarice3.goety_cataclysm.common.blocks.NMHeadBlock;
import com.Polarice3.goety_cataclysm.common.blocks.WallNMHeadBlock;
import com.Polarice3.goety_cataclysm.common.blocks.entities.NMHeadBlockEntity;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class NMHeadBlockEntityRenderer implements BlockEntityRenderer<NMHeadBlockEntity> {
    private static final ResourceLocation ORIGINAL = new ResourceLocation(Cataclysm.MODID,"textures/entity/monstrosity/netherite_monstrosity.png");

    public NMHeadBlockEntityRenderer(BlockEntityRendererProvider.Context p_i226015_1_) {
    }

    public void render(NMHeadBlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        BlockState blockstate = pBlockEntity.getBlockState();
        boolean flag = blockstate.getBlock() instanceof WallNMHeadBlock;
        Direction direction = flag ? blockstate.getValue(WallNMHeadBlock.FACING) : null;
        float f1 = 22.5F * (float)(flag ? (2 + direction.get2DDataValue()) * 4 : blockstate.getValue(NMHeadBlock.ROTATION));
        renderSkull(direction, f1, pMatrixStack, pBuffer, pCombinedLight);
    }

    public static void renderSkull(@Nullable Direction p_228879_0_, float p_228879_1_, PoseStack p_228879_5_, MultiBufferSource p_228879_6_, int p_228879_7_) {
        NMHeadModel skullModel = new NMHeadModel(Minecraft.getInstance().getEntityModels().bakeLayer(GCBlockLayer.NM_HEAD));
        p_228879_5_.pushPose();
        if (p_228879_0_ == null) {
            p_228879_5_.translate(0.5D, 0.0D, 0.5D);
        } else {
            float f = 0.25F;
            p_228879_5_.translate((double)(0.5F - (float)p_228879_0_.getStepX() * f), f, (double)(0.5F - (float)p_228879_0_.getStepZ() * f));
        }

        p_228879_5_.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer ivertexbuilder = p_228879_6_.getBuffer(RenderType.entityCutoutNoCullZOffset(ORIGINAL));
        skullModel.setupAnim(0, p_228879_1_, 0.0F);
        skullModel.renderToBuffer(p_228879_5_, ivertexbuilder, p_228879_7_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_228879_5_.popPose();
    }

    public static void renderItemSkull(ItemStack stack, @Nullable Direction p_228879_0_, float p_228879_1_, PoseStack p_228879_5_, MultiBufferSource p_228879_6_, int p_228879_7_) {
        NMHeadModel skullModel = new NMHeadModel(Minecraft.getInstance().getEntityModels().bakeLayer(GCBlockLayer.NM_HEAD));
        p_228879_5_.pushPose();
        if (p_228879_0_ == null) {
            p_228879_5_.translate(0.5D, 0.0D, 0.5D);
        } else {
            float f = 0.25F;
            p_228879_5_.translate((double)(0.5F - (float)p_228879_0_.getStepX() * f), f, (double)(0.5F - (float)p_228879_0_.getStepZ() * f));
        }

        p_228879_5_.scale(-1.0F, -1.0F, 1.0F);
        p_228879_5_.scale(0.25F, 0.25F, 0.25F);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(p_228879_6_, RenderType.entityTranslucent(ORIGINAL), true, stack.hasFoil());
        skullModel.setupAnim(0, p_228879_1_, 0.0F);
        skullModel.renderToBuffer(p_228879_5_, vertexConsumer, p_228879_7_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_228879_5_.popPose();
    }
}
