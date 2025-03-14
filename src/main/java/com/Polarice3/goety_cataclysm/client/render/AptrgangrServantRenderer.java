package com.Polarice3.goety_cataclysm.client.render;


import com.Polarice3.goety_cataclysm.client.render.model.AptrgangrServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.AptrgangrServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class AptrgangrServantRenderer extends MobRenderer<AptrgangrServant, AptrgangrServantModel> {
    private final RandomSource rnd = RandomSource.create();
    private static final ResourceLocation APTRGANGR_TEXTURES = new ResourceLocation(Cataclysm.MODID,"textures/entity/draugar/aptrgangr.png");

    public AptrgangrServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AptrgangrServantModel(renderManagerIn.bakeLayer(CMModelLayers.APTRGANGR_MODEL)), 1.25F);
        this.addLayer(new AptrgangrRiderLayer(this));
        this.addLayer(new Aptrgangr_Layer(this));

    }
    public Vec3 getRenderOffset(AptrgangrServant entityIn, float partialTicks) {
        if (entityIn.getAttackState() == 4) {
            double d0 = 0.01D;
            return new Vec3(this.rnd.nextGaussian() * d0, this.rnd.nextGaussian() * d0, this.rnd.nextGaussian() * d0);
        } else {
            return super.getRenderOffset(entityIn, partialTicks);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(AptrgangrServant entity) {
        return APTRGANGR_TEXTURES;
    }

    @Override
    protected float getFlipDegrees(AptrgangrServant entity) {
        return 0;
    }

    @Override
    protected void scale(AptrgangrServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.35F, 1.35F, 1.35F);
    }

    static class AptrgangrRiderLayer extends RenderLayer<AptrgangrServant, AptrgangrServantModel> {

        public AptrgangrRiderLayer(AptrgangrServantRenderer render) {
            super(render);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, AptrgangrServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float bodyYaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
            if (entity.isVehicle()) {
                Vec3 offset = new Vec3(0, 0.0F, 0F);
                Vec3 ridePos = getRiderPosition(offset);
                for (Entity passenger : entity.getPassengers()) {
                    if (passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                        continue;
                    }
                    poseStack.pushPose();
                    poseStack.translate(ridePos.x, ridePos.y - 0.65F + passenger.getBbHeight(), ridePos.z);
                    poseStack.mulPose(Axis.XN.rotationDegrees(180F));
                    poseStack.mulPose(Axis.YN.rotationDegrees(360 - bodyYaw));
                    Cataclysm.PROXY.releaseRenderingEntity(passenger.getUUID());
                    renderPassenger(passenger, 0, 0, 0, 0, partialTicks, poseStack, bufferIn, packedLightIn);
                    Cataclysm.PROXY.blockRenderingEntity(passenger.getUUID());
                    poseStack.popPose();
                }

            }
        }



        public Vec3 getRiderPosition(Vec3 offsetIn) {
            PoseStack translationStack = new PoseStack();
            translationStack.pushPose();
            this.getParentModel().translateToHand(translationStack);
            Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
            armOffsetVec.mul(translationStack.last().pose());
            Vec3 vec3 = new Vec3(armOffsetVec.x(), armOffsetVec.y(), armOffsetVec.z());
            translationStack.popPose();
            return vec3;
        }


        public static <E extends Entity> void renderPassenger(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
            EntityRenderer<? super E> render = null;
            EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
            try {
                render = manager.getRenderer(entityIn);

                if (render != null) {
                    try {
                        render.render(entityIn, yaw, partialTicks, matrixStack, bufferIn, packedLight);
                    } catch (Throwable throwable1) {
                        throw new ReportedException(CrashReport.forThrowable(throwable1, "Rendering entity in world"));
                    }
                }
            } catch (Throwable throwable3) {
                CrashReport crashreport = CrashReport.forThrowable(throwable3, "Rendering entity in world");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
                entityIn.fillCrashReportCategory(crashreportcategory);
                CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
                crashreportcategory1.setDetail("Assigned renderer", render);
                crashreportcategory1.setDetail("Rotation", Float.valueOf(yaw));
                crashreportcategory1.setDetail("Delta", Float.valueOf(partialTicks));
                throw new ReportedException(crashreport);
            }
        }

    }

    static class Aptrgangr_Layer extends RenderLayer<AptrgangrServant, AptrgangrServantModel> {
        private static final ResourceLocation LAYER = new ResourceLocation(Cataclysm.MODID,"textures/entity/draugar/aptrgangr_layer.png");

        public Aptrgangr_Layer(AptrgangrServantRenderer renderIn) {
            super(renderIn);
        }

        public ResourceLocation getLayerTextureLocation() {
            return LAYER;
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AptrgangrServant entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float f = 1.0F - entity.deathTime / (float) entity.deathTimer();
            RenderType eyes = CMRenderTypes.CMEyes(this.getLayerTextureLocation());
            VertexConsumer VertexConsumer = bufferIn.getBuffer(eyes);
            this.getParentModel().renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, f, f, f, 1.0F);

        }
    }
}

