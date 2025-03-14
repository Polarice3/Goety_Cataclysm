package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.common.entities.projectiles.PortalAbyssBlast;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class PortalAbyssBlastRenderer extends EntityRenderer<PortalAbyssBlast> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("cataclysm", "textures/entity/abyss_laser_beam.png");
    private static final float TEXTURE_WIDTH = 256.0F;
    private static final float TEXTURE_HEIGHT = 32.0F;
    private static final float START_RADIUS = 2.0F;
    private static final float END_RADIUS = 2.0F;
    private static final float BEAM_RADIUS = 2.0F;
    private boolean clearerView = false;

    public PortalAbyssBlastRenderer(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    public ResourceLocation getTextureLocation(PortalAbyssBlast entity) {
        return TEXTURE;
    }

    public boolean shouldRender(PortalAbyssBlast solarBeam, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void render(PortalAbyssBlast solarBeam, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        this.clearerView = solarBeam.caster instanceof Player && Minecraft.getInstance().player == solarBeam.caster && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        double collidePosX = solarBeam.prevCollidePosX + (solarBeam.collidePosX - solarBeam.prevCollidePosX) * (double)delta;
        double collidePosY = solarBeam.prevCollidePosY + (solarBeam.collidePosY - solarBeam.prevCollidePosY) * (double)delta;
        double collidePosZ = solarBeam.prevCollidePosZ + (solarBeam.collidePosZ - solarBeam.prevCollidePosZ) * (double)delta;
        double posX = solarBeam.xo + (solarBeam.getX() - solarBeam.xo) * (double)delta;
        double posY = solarBeam.yo + (solarBeam.getY() - solarBeam.yo) * (double)delta;
        double posZ = solarBeam.zo + (solarBeam.getZ() - solarBeam.zo) * (double)delta;
        float yaw = solarBeam.prevYaw + (solarBeam.renderYaw - solarBeam.prevYaw) * delta;
        float pitch = solarBeam.prevPitch + (solarBeam.renderPitch - solarBeam.prevPitch) * delta;
        float length = (float)Math.sqrt(Math.pow(collidePosX - posX, 2.0) + Math.pow(collidePosY - posY, 2.0) + Math.pow(collidePosZ - posZ, 2.0));
        int frame = Mth.floor(((float)(solarBeam.appear.getTimer() - 1) + delta) * 2.0F);
        if (frame < 0) {
            frame = 6;
        }

        VertexConsumer ivertexbuilder = bufferIn.getBuffer(CMRenderTypes.getGlowingEffect(this.getTextureLocation(solarBeam)));
        this.renderBeam(length, 57.295776F * yaw, 57.295776F * pitch, frame, matrixStackIn, ivertexbuilder, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        this.renderEnd(frame, solarBeam.blockSide, matrixStackIn, ivertexbuilder, packedLightIn);
        matrixStackIn.popPose();
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0.0F + 0.0625F * (float)frame;
        float minV = 0.0F;
        float maxU = minU + 0.0625F;
        float maxV = minV + 0.5F;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        this.drawVertex(matrix4f, matrix3f, builder, -2.0F, -2.0F, 0.0F, minU, minV, 1.0F, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, -2.0F, 2.0F, 0.0F, minU, maxV, 1.0F, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, 2.0F, 2.0F, 0.0F, maxU, maxV, 1.0F, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, 2.0F, -2.0F, 0.0F, maxU, minV, 1.0F, packedLightIn);
    }

    private void renderEnd(int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        this.renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
        if (side != null) {
            matrixStackIn.pushPose();
            Quaternionf sideQuat = side.getRotation();
            sideQuat.mul((new Quaternionf()).rotationX(1.5707964F));
            matrixStackIn.mulPose(sideQuat);
            matrixStackIn.translate(0.0F, 0.0F, -0.01F);
            this.renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
            matrixStackIn.popPose();
        }
    }

    private void drawBeam(float length, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0.0F;
        float minV = 0.5F + 0.03125F * (float)frame;
        float maxU = minU + 0.078125F;
        float maxV = minV + 0.03125F;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        float offset = this.clearerView ? -1.0F : 0.0F;
        this.drawVertex(matrix4f, matrix3f, builder, -2.0F, offset, 0.0F, minU, minV, 1.0F, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, -2.0F, length, 0.0F, minU, maxV, 1.0F, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, 2.0F, length, 0.0F, maxU, maxV, 1.0F, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, 2.0F, offset, 0.0F, maxU, minV, 1.0F, packedLightIn);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose((new Quaternionf()).rotationX(1.5707964F));
        matrixStackIn.mulPose((new Quaternionf()).rotationZ((yaw - 90.0F) * 0.017453292F));
        matrixStackIn.mulPose((new Quaternionf()).rotationX(-pitch * 0.017453292F));
        matrixStackIn.pushPose();
        if (!this.clearerView) {
            matrixStackIn.mulPose((new Quaternionf()).rotationY(Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 90.0F));
        }

        this.drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
        if (!this.clearerView) {
            matrixStackIn.pushPose();
            matrixStackIn.mulPose((new Quaternionf()).rotationY((-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 90.0F) * 0.017453292F));
            this.drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
            matrixStackIn.popPose();
        }

        matrixStackIn.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1.0F, 1.0F, 1.0F, 1.0F * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
