package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.layer.AbstractDeeplingServantLayer;
import com.Polarice3.goety_cataclysm.client.render.model.DeeplingServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.DeeplingServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DeeplingServantRenderer extends MobRenderer<DeeplingServant, DeeplingServantModel> {
    private static final ResourceLocation SSAPBUG_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/deepling/deepling_1.png");
    private static final ResourceLocation DEEPLING_LAYER_TEXTURES  = new ResourceLocation(Cataclysm.MODID,"textures/entity/deepling/deepling_layer.png");

    public DeeplingServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DeeplingServantModel(), 0.7F);
        this.addLayer(new AbstractDeeplingServantLayer(this, DEEPLING_LAYER_TEXTURES));
        this.addLayer(new DeeplingItemLayer(this, renderManagerIn.getItemInHandRenderer()));

    }
    @Override
    public ResourceLocation getTextureLocation(DeeplingServant entity) {
        return SSAPBUG_TEXTURES;
    }

    public static class DeeplingItemLayer extends RenderLayer<DeeplingServant, DeeplingServantModel> {
        private final ItemInHandRenderer itemInHandRenderer;

        public DeeplingItemLayer(RenderLayerParent p_234846_, ItemInHandRenderer p_234847_) {
            super(p_234846_);
            this.itemInHandRenderer = p_234847_;
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DeeplingServant entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemstack = entitylivingbaseIn.getItemBySlot(EquipmentSlot.MAINHAND);
            matrixStackIn.pushPose();
            boolean left = entitylivingbaseIn.isLeftHanded();
            matrixStackIn.pushPose();
            this.translateToHand(matrixStackIn, left);
            matrixStackIn.translate(0.0F, 1.4225F, -0.1F);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            matrixStackIn.scale(1.0F, 1.0F, 1.0F);
            ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
            renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
            matrixStackIn.popPose();
        }

        protected void translateToHand(PoseStack matrixStack, boolean left) {
            this.getParentModel().root.translateAndRotate(matrixStack);
            this.getParentModel().body.translateAndRotate(matrixStack);
            if (left) {
                this.getParentModel().left_arm.translateAndRotate(matrixStack);
            } else {
                this.getParentModel().right_arm.translateAndRotate(matrixStack);
            }

        }
    }
}
