package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.common.entities.projectiles.IgnisFireball;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.entity.Ignis_Fireball_Model;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IgnisFireballRenderer extends EntityRenderer<IgnisFireball> {
	private static final ResourceLocation IGNIS_FIRE_BALL = new ResourceLocation(Cataclysm.MODID,"textures/entity/ignis_fireball.png");
	private static final ResourceLocation IGNIS_FIRE_BALL_SOUL = new ResourceLocation(Cataclysm.MODID,"textures/entity/ignis_fireball_soul.png");
	public Ignis_Fireball_Model model;

	public IgnisFireballRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.model = new Ignis_Fireball_Model();
	}
	
	@Override
	protected int getBlockLightLevel(IgnisFireball entity, BlockPos pos) {
		return 15;
	}

	@Override
	public void render(IgnisFireball entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		float f = rotLerp(entityIn.yRotO, entityIn.getYRot(), partialTicks);
		float f1 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
		float f2 = (float) entityIn.tickCount + partialTicks;
		matrixStackIn.translate(0.0D, (double) 0.3F, 0.0D);
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
		matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
		matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
		this.model.setupAnim(entityIn, 0.0F, 0.0F, 0.0F, f, f1);
		VertexConsumer VertexConsumer = bufferIn.getBuffer(this.model.renderType(getTextureLocation(entityIn)));
		this.model.renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStackIn.popPose();

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(IgnisFireball entity) {
		return entity.isSoul() ? IGNIS_FIRE_BALL_SOUL : IGNIS_FIRE_BALL;
	}

	
	/**
	 * A helper method to do some Math Magic
	 */
	private float rotLerp(float prevRotation, float rotation, float partialTicks) {
		float f;
		for(f = rotation - prevRotation; f < -180.0F; f += 360.0F) {
			;
		}

		while(f >= 180.0F) {
			f -= 360.0F;
		}

		return prevRotation + partialTicks * f;
	}
}