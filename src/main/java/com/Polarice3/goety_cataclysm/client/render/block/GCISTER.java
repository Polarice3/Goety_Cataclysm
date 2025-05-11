package com.Polarice3.goety_cataclysm.client.render.block;

import com.Polarice3.goety_cataclysm.common.blocks.EnderGolemSkullBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class GCISTER extends BlockEntityWithoutLevelRenderer {

    public GCISTER() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pCamera, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pLight, int pOverlay) {
        Item item = pStack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof EnderGolemSkullBlock){
                if(pCamera == ItemDisplayContext.GUI) {
                    pMatrixStack.pushPose();
                    pMatrixStack.translate(0.5F, 0.5F, 0.5F);
                    pMatrixStack.mulPose(Axis.XP.rotationDegrees(30));
                    pMatrixStack.mulPose(Axis.YN.rotationDegrees(-45));
                    pMatrixStack.translate(-0.5F, -0.5F, -0.5F);
                    pMatrixStack.translate(0.0F, 0.25F, 0.0F);
                    EnderGolemSkullBlockEntityRenderer.renderItemSkull(pStack, null, 180.0F, pMatrixStack, pBuffer, pLight);
                    pMatrixStack.popPose();
                } else {
                    EnderGolemSkullBlockEntityRenderer.renderItemSkull(pStack, null, 180.0F, pMatrixStack, pBuffer, pLight);
                }
            }
        }

    }
}
