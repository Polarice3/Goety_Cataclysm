package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.ProwlerServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.factory.ProwlerServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;

public class ProwlerServantRenderer extends MobRenderer<ProwlerServant, ProwlerServantModel> {
    private final RandomSource rnd = RandomSource.create();
    private static final ResourceLocation PROWLER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_prowler.png");
    private static final ResourceLocation[] TEXTURE_PROGRESS = new ResourceLocation[4];

    public ProwlerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ProwlerServantModel(renderManagerIn.bakeLayer(CMModelLayers.PROWLER_MODEL)), 0.7F);
        this.addLayer(new ProwlerLayer(this));
        for(int i = 0; i < 4; i++){
            TEXTURE_PROGRESS[i] = new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_prowler_" + i + ".png");
        }
    }

    @Override
    protected float getFlipDegrees(ProwlerServant entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(ProwlerServant entity) {
        WalkAnimationState walkanimationstate = entity.walkAnimation;
        int f3 = (int) walkanimationstate.position(entity.tickCount);
        return getGrowingTexture(entity, (int) ((f3 * 0.5F) % 4));
    }

    public ResourceLocation getGrowingTexture(ProwlerServant entity, int age) {
        return TEXTURE_PROGRESS[Mth.clamp(age, 0, 4)];
    }

    public Vec3 getRenderOffset(ProwlerServant entityIn, float partialTicks) {
        if (entityIn.getAttackState() == 1) {
            double d0 = 0.05D;
            return new Vec3(this.rnd.nextGaussian() * d0, 0.0D, this.rnd.nextGaussian() * d0);
        } else {
            return super.getRenderOffset(entityIn, partialTicks);
        }
    }

    public static class ProwlerLayer extends RenderLayer<ProwlerServant, ProwlerServantModel> {
        private static final ResourceLocation PROWLER_LAYER_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_prowler_layer.png");

        private static final ResourceLocation[] TEXTURE_PROGRESS = new ResourceLocation[4];

        public ProwlerLayer(ProwlerServantRenderer renderIn) {
            super(renderIn);
            for(int i = 0; i < 4; i++){
                TEXTURE_PROGRESS[i] = new ResourceLocation(Cataclysm.MODID,"textures/entity/factory/the_prowler_layer_" + i + ".png");
            }
        }

        public ResourceLocation getTextureLocation(ProwlerServant entity) {
            WalkAnimationState walkanimationstate = entity.walkAnimation;
            int f3 = (int) walkanimationstate.position(entity.tickCount);
            return getGrowingTexture(entity, (int) ((f3 * 0.5F) % 4));
        }

        public ResourceLocation getGrowingTexture(ProwlerServant entity, int age) {
            return TEXTURE_PROGRESS[Mth.clamp(age, 0, 4)];
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ProwlerServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float f = 1.0F - entity.deathTime / (float) entity.deathTimer();
            RenderType eyes = CMRenderTypes.CMEyes(this.getTextureLocation(entity));
            VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, f, f, f, 1.0F);

        }
    }
}
