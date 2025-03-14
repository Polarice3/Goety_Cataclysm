package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.common.entities.projectiles.AbyssMine;
import com.github.L_Ender.cataclysm.client.model.entity.Abyss_Mine_Model;
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
import org.joml.Quaternionf;

public class AbyssMineRenderer extends EntityRenderer<AbyssMine> {
    private static final ResourceLocation ABYSS_MINE_TEXTURE = new ResourceLocation("cataclysm", "textures/entity/leviathan/abyss_mine.png");
    private static final float SIN_45 = (float)Math.sin(0.7853981633974483);
    public Abyss_Mine_Model model = new Abyss_Mine_Model();

    public AbyssMineRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    protected int getBlockLightLevel(AbyssMine entity, BlockPos pos) {
        return 15;
    }

    public void render(AbyssMine p_114162_, float p_114163_, float p_114164_, PoseStack p_114165_, MultiBufferSource p_114166_, int p_114167_) {
        p_114165_.pushPose();
        float f1 = ((float)p_114162_.time + p_114164_) * 3.0F;
        float activateProgress = p_114162_.prevactivateProgress + (p_114162_.activateProgress - p_114162_.prevactivateProgress) * p_114164_;
        float d = activateProgress * 0.0875F;
        float e = activateProgress * 0.2F;
        VertexConsumer vertexconsumer = p_114166_.getBuffer(CMRenderTypes.getfullBright(ABYSS_MINE_TEXTURE));
        p_114165_.pushPose();
        p_114165_.scale(e, e, e);
        p_114165_.translate(0.0, -0.5, 0.0);
        int i = OverlayTexture.NO_OVERLAY;
        p_114165_.mulPose(Axis.YP.rotationDegrees(f1));
        p_114165_.translate(0.0, 0.75, 0.0);
        p_114165_.mulPose((new Quaternionf()).setAngleAxis(1.0471976F, SIN_45, 0.0F, SIN_45));
        this.model.glass.render(p_114165_, vertexconsumer, p_114167_, i);
        float f2 = 0.875F;
        p_114165_.scale(d, d, d);
        p_114165_.mulPose((new Quaternionf()).setAngleAxis(1.0471976F, SIN_45, 0.0F, SIN_45));
        p_114165_.mulPose(Axis.YP.rotationDegrees(f1));
        this.model.glass2.render(p_114165_, vertexconsumer, p_114167_, i);
        p_114165_.scale(d, d, d);
        p_114165_.mulPose((new Quaternionf()).setAngleAxis(1.0471976F, SIN_45, 0.0F, SIN_45));
        p_114165_.mulPose(Axis.YP.rotationDegrees(f1));
        this.model.root.render(p_114165_, vertexconsumer, p_114167_, i);
        p_114165_.popPose();
        p_114165_.popPose();
        super.render(p_114162_, p_114163_, p_114164_, p_114165_, p_114166_, p_114167_);
    }

    public ResourceLocation getTextureLocation(AbyssMine entity) {
        return ABYSS_MINE_TEXTURE;
    }
}
