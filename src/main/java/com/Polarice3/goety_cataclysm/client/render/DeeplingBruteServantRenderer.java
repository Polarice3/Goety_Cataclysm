package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.layer.AbstractDeeplingServantLayer;
import com.Polarice3.goety_cataclysm.client.render.model.DeeplingBruteServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.DeeplingBruteServant;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DeeplingBruteServantRenderer extends MobRenderer<DeeplingBruteServant, DeeplingBruteServantModel> {
    private static final ResourceLocation SSAPBUG_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/deepling/deepling_brute.png");
    private static final ResourceLocation DEEPLING_LAYER_TEXTURES  = new ResourceLocation(Cataclysm.MODID,"textures/entity/deepling/deepling_brute_layer.png");

    public DeeplingBruteServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DeeplingBruteServantModel(), 0.7F);
        this.addLayer(new AbstractDeeplingServantLayer(this, DEEPLING_LAYER_TEXTURES));
        this.addLayer(new BruteItemLayer(this, renderManagerIn.getItemInHandRenderer()));

    }
    @Override
    public ResourceLocation getTextureLocation(DeeplingBruteServant entity) {
        return SSAPBUG_TEXTURES;
    }

    @Override
    protected void scale(DeeplingBruteServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.125F, 1.125F, 1.125F);
    }
    @Override
    protected void setupRotations(DeeplingBruteServant p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {
        if (this.isShaking(p_115317_)) {
            p_115320_ += (float)(Math.cos((double)p_115317_.tickCount * 3.25D) * Math.PI * (double)0.4F);
        }

        if (!p_115317_.hasPose(Pose.SLEEPING)) {
            p_115318_.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
        }

        if (p_115317_.deathTime > 0) {
            float f = ((float)p_115317_.deathTime + p_115321_ - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            p_115318_.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(p_115317_)));
        } else if (p_115317_.isAutoSpinAttack()|| p_115317_.getSpinAttack()) {
            p_115318_.mulPose(Axis.XP.rotationDegrees(-90.0F - p_115317_.getXRot()));
            p_115318_.mulPose(Axis.YP.rotationDegrees(((float)p_115317_.tickCount + p_115321_) * -75.0F));
        } else if (p_115317_.hasPose(Pose.SLEEPING)) {
            Direction direction = p_115317_.getBedOrientation();
            float f1 = direction != null ? sleepDirectionToRotation(direction) : p_115320_;
            p_115318_.mulPose(Axis.YP.rotationDegrees(f1));
            p_115318_.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(p_115317_)));
            p_115318_.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (isEntityUpsideDown(p_115317_)) {
            p_115318_.translate(0.0F, p_115317_.getBbHeight() + 0.1F, 0.0F);
            p_115318_.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }

    }

    private static float sleepDirectionToRotation(Direction p_115329_) {
        return switch (p_115329_) {
            case SOUTH -> 90.0F;
            case NORTH -> 270.0F;
            case EAST -> 180.0F;
            default -> 0.0F;
        };
    }

    public static class BruteItemLayer extends RenderLayer<DeeplingBruteServant, DeeplingBruteServantModel> {
        private final ItemInHandRenderer itemInHandRenderer;

        public BruteItemLayer(RenderLayerParent p_234846_, ItemInHandRenderer p_234847_) {
            super(p_234846_);
            this.itemInHandRenderer = p_234847_;
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DeeplingBruteServant entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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
