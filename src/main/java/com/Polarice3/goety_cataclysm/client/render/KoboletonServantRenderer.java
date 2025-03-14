package com.Polarice3.goety_cataclysm.client.render;


import com.Polarice3.goety_cataclysm.client.render.model.KoboletonServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.KoboletonServant;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class KoboletonServantRenderer extends MobRenderer<KoboletonServant, KoboletonServantModel> {
    private static final ResourceLocation KOBOLETON_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/koboleton/koboleton.png");
    private static final ResourceLocation KOBOLETON_LAYER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/koboleton/koboleton_layer.png");

    public KoboletonServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new KoboletonServantModel(), 0.5F);
        this.addLayer(new LayerKoboletonItem(this, renderManagerIn.getItemInHandRenderer()));
        this.addLayer(new LayerGenericGlowing(this, KOBOLETON_LAYER_TEXTURES));

    }
    @Override
    public ResourceLocation getTextureLocation(KoboletonServant entity) {
        return KOBOLETON_TEXTURES;
    }

    @Override
    protected void scale(KoboletonServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }

    static class LayerKoboletonItem extends RenderLayer<KoboletonServant, KoboletonServantModel> {
        private final ItemInHandRenderer itemInHandRenderer;

        public LayerKoboletonItem(RenderLayerParent p_234846_, ItemInHandRenderer p_234847_) {
            super(p_234846_);
            this.itemInHandRenderer = p_234847_;
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, KoboletonServant entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemstack = entitylivingbaseIn.getItemBySlot(EquipmentSlot.MAINHAND);
            matrixStackIn.pushPose();
            boolean left = entitylivingbaseIn.isLeftHanded();
            matrixStackIn.pushPose();
            translateToHand(matrixStackIn, left);
            matrixStackIn.translate(0.0F, -0.1F, -0.1F);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-190F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180F));
            matrixStackIn.scale(1.0F, 1.0F, 1.0F);
            ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
            renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
            matrixStackIn.popPose();
        }

        protected void translateToHand(PoseStack matrixStack, boolean left) {
            this.getParentModel().root.translateAndRotate(matrixStack);
            this.getParentModel().pelvis.translateAndRotate(matrixStack);
            this.getParentModel().lower_body.translateAndRotate(matrixStack);
            this.getParentModel().body.translateAndRotate(matrixStack);
            if(left){
                this.getParentModel().left_arm.translateAndRotate(matrixStack);
                this.getParentModel().left_weapon.translateAndRotate(matrixStack);
            }else{
                this.getParentModel().right_arm.translateAndRotate(matrixStack);
                this.getParentModel().right_weapon.translateAndRotate(matrixStack);
            }
        }

    }

    static class LayerGenericGlowing extends RenderLayer<KoboletonServant, KoboletonServantModel> {
        private final ResourceLocation texture;
        private final RenderType renderType;

        public LayerGenericGlowing(RenderLayerParent<KoboletonServant, KoboletonServantModel> renderer, ResourceLocation texture) {
            super(renderer);
            this.texture = texture;
            this.renderType = RenderType.eyes(texture);
        }

        public boolean shouldCombineTextures() {
            return true;
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, KoboletonServant entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.renderType);
            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}

