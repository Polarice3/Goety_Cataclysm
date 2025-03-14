package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.common.entities.projectiles.VoidVortex;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class VoidVortexRenderer extends EntityRenderer<VoidVortex> {
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("cataclysm", "textures/entity/void_vortex/void_vortex_idle1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("cataclysm", "textures/entity/void_vortex/void_vortex_idle2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("cataclysm", "textures/entity/void_vortex/void_vortex_idle3.png");
    private static final ResourceLocation TEXTURE_4 = new ResourceLocation("cataclysm", "textures/entity/void_vortex/void_vortex_idle4.png");
    private static final ResourceLocation[] TEXTURE_PROGRESS = new ResourceLocation[4];

    public VoidVortexRenderer(EntityRendererProvider.Context mgr) {
        super(mgr);

        for(int i = 0; i < 4; ++i) {
            TEXTURE_PROGRESS[i] = new ResourceLocation("cataclysm", "textures/entity/void_vortex/void_vortex_grow_" + i + ".png");
        }

    }

    public void render(VoidVortex entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.001, 0.0);
        ResourceLocation tex;
        if (entityIn.getLifespan() < 16) {
            tex = this.getGrowingTexture((int)((float)entityIn.getLifespan() * 0.5F % 20.0F));
        } else if (entityIn.tickCount < 16) {
            tex = this.getGrowingTexture((int)((float)entityIn.tickCount * 0.5F % 20.0F));
        } else {
            tex = this.getIdleTexture(entityIn.tickCount % 9);
        }

        matrixStackIn.scale(3.0F, 3.0F, 3.0F);
        this.renderArc(matrixStackIn, bufferIn, tex);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private void renderArc(PoseStack matrixStackIn, MultiBufferSource bufferIn, ResourceLocation res) {
        matrixStackIn.pushPose();
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(CMRenderTypes.getfullBright(res));
        PoseStack.Pose lvt_19_1_ = matrixStackIn.last();
        Matrix4f lvt_20_1_ = lvt_19_1_.pose();
        Matrix3f lvt_21_1_ = lvt_19_1_.normal();
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, -1, 0, -1, 0.0F, 0.0F, 1, 0, 1, 240);
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, -1, 0, 1, 0.0F, 1.0F, 1, 0, 1, 240);
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, 1, 0, 1, 1.0F, 1.0F, 1, 0, 1, 240);
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, 1, 0, -1, 1.0F, 0.0F, 1, 0, 1, 240);
        matrixStackIn.popPose();
    }

    public ResourceLocation getTextureLocation(VoidVortex entity) {
        return TEXTURE_1;
    }

    public void drawVertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, VertexConsumer p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_) {
        p_229039_3_.vertex(p_229039_1_, (float)p_229039_4_, (float)p_229039_5_, (float)p_229039_6_).color(255, 255, 255, 255).uv(p_229039_7_, p_229039_8_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229039_12_).normal(p_229039_2_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }

    public ResourceLocation getIdleTexture(int age) {
        if (age < 3) {
            return TEXTURE_1;
        } else if (age < 6) {
            return TEXTURE_2;
        } else {
            return age < 10 ? TEXTURE_3 : TEXTURE_4;
        }
    }

    public ResourceLocation getGrowingTexture(int age) {
        return TEXTURE_PROGRESS[Mth.clamp(age / 2, 0, 3)];
    }
}
