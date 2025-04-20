package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.layer.AbstractDeeplingServantLayer;
import com.Polarice3.goety_cataclysm.client.render.model.DeeplingPriestServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.DeeplingPriestServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class DeeplingPriestServantRenderer extends MobRenderer<DeeplingPriestServant, DeeplingPriestServantModel> {
    private static final ResourceLocation DEEPLING_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/deepling/deepling_priest.png");
    private static final ResourceLocation DEEPLING_LAYER_TEXTURES  = new ResourceLocation(Cataclysm.MODID,"textures/entity/deepling/deepling_priest_layer.png");

    public DeeplingPriestServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DeeplingPriestServantModel(), 0.7F);
        this.addLayer(new AbstractDeeplingServantLayer(this,DEEPLING_LAYER_TEXTURES));
        this.addLayer(new PriestItemLayer(this, renderManagerIn.getItemInHandRenderer()));
        this.addLayer(new PriestLightLayer(this));

    }
    @Override
    public ResourceLocation getTextureLocation(DeeplingPriestServant entity) {
        return DEEPLING_TEXTURES;
    }

    public static class PriestItemLayer extends RenderLayer<DeeplingPriestServant, DeeplingPriestServantModel> {
        private final ItemInHandRenderer itemInHandRenderer;

        public PriestItemLayer(RenderLayerParent p_234846_, ItemInHandRenderer p_234847_) {
            super(p_234846_);
            this.itemInHandRenderer = p_234847_;
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DeeplingPriestServant entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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

    public static class PriestLightLayer extends RenderLayer<DeeplingPriestServant, DeeplingPriestServantModel> {
        private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);

        public PriestLightLayer(RenderLayerParent p_234846_) {
            super(p_234846_);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DeeplingPriestServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            matrixStackIn.pushPose();
            if (entity.getAnimation() == DeeplingPriestServant.DEEPLING_BLIND && entity.getAnimationTick() > 18 && entity.getAnimationTick() < 47) {
                float f5 = ((float)entity.getAnimationTick() + partialTicks) / 144.0F;
                float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
                RandomSource randomsource = RandomSource.create(432L);
                VertexConsumer vertexconsumer2 = bufferIn.getBuffer(RenderType.lightning());
                matrixStackIn.pushPose();
                this.translateToLight(matrixStackIn);

                for(int i = 0; (float)i < 4.0F; ++i) {
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F + f5 * 90.0F));
                    float f3 = 2.75F;
                    float f4 = 2.75F;
                    Matrix4f matrix4f = matrixStackIn.last().pose();
                    int j = (int)(255.0F * (1.0F - f7));
                    vertex01(vertexconsumer2, matrix4f, j);
                    vertex2(vertexconsumer2, matrix4f, f3, f4);
                    vertex3(vertexconsumer2, matrix4f, f3, f4);
                    vertex01(vertexconsumer2, matrix4f, j);
                    vertex3(vertexconsumer2, matrix4f, f3, f4);
                    vertex4(vertexconsumer2, matrix4f, f3, f4);
                    vertex01(vertexconsumer2, matrix4f, j);
                    vertex4(vertexconsumer2, matrix4f, f3, f4);
                    vertex2(vertexconsumer2, matrix4f, f3, f4);
                }

                matrixStackIn.popPose();
            }

            matrixStackIn.popPose();
        }

        private static void vertex01(VertexConsumer p_114220_, Matrix4f p_114221_, int p_114222_) {
            p_114220_.vertex(p_114221_, 0.0F, 0.0F, 0.0F).color(51, 255, 255, p_114222_).endVertex();
        }

        private static void vertex2(VertexConsumer p_114215_, Matrix4f p_114216_, float p_114217_, float p_114218_) {
            p_114215_.vertex(p_114216_, -HALF_SQRT_3 * p_114218_, p_114217_, -0.5F * p_114218_).color(51, 255, 255, 0).endVertex();
        }

        private static void vertex3(VertexConsumer p_114224_, Matrix4f p_114225_, float p_114226_, float p_114227_) {
            p_114224_.vertex(p_114225_, HALF_SQRT_3 * p_114227_, p_114226_, -0.5F * p_114227_).color(51, 255, 255, 0).endVertex();
        }

        private static void vertex4(VertexConsumer p_114229_, Matrix4f p_114230_, float p_114231_, float p_114232_) {
            p_114229_.vertex(p_114230_, 0.0F, p_114231_, 1.0F * p_114232_).color(51, 255, 255, 0).endVertex();
        }

        private void translateToLight(PoseStack matrixStack) {
            this.getParentModel().root.translateAndRotate(matrixStack);
            this.getParentModel().body.translateAndRotate(matrixStack);
            this.getParentModel().head.translateAndRotate(matrixStack);
            this.getParentModel().head2.translateAndRotate(matrixStack);
            this.getParentModel().fin.translateAndRotate(matrixStack);
            this.getParentModel().light.translateAndRotate(matrixStack);
        }
    }
}
