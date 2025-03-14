package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited.IgnitedBerserkerServant;
import com.github.L_Ender.cataclysm.client.animation.Ignited_Berserker_Animation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class IgnitedBerserkerServantModel<T extends IgnitedBerserkerServant> extends HierarchicalModel<T> {
	private final ModelPart root;
    private final ModelPart head;
	private final ModelPart edges;

	public IgnitedBerserkerServantModel(ModelPart root) {
		this.root = root;
        ModelPart everything = root.getChild("everything");
        ModelPart mid_root = everything.getChild("mid_root");
        ModelPart rod = mid_root.getChild("rod");
        ModelPart body = rod.getChild("body");
		this.head = body.getChild("head");
		this.edges = body.getChild("edges");
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateHeadLookTarget(netHeadYaw, headPitch);
		if(entity.getAttackState() == 0) {
			this.animateWalk(Ignited_Berserker_Animation.WALK, limbSwing, limbSwingAmount, 1.0F, 2.0F);
			this.edges.yRot -= ageInTicks * 0.1F;
		}
		this.animate(entity.getAnimationState("idle"), Ignited_Berserker_Animation.IDLE, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("x_slash"), Ignited_Berserker_Animation.X_SLASH, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("mixer_start"), Ignited_Berserker_Animation.MIXER_START, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("mixer_idle"), Ignited_Berserker_Animation.MIXER_IDLE, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("mixer_finish"), Ignited_Berserker_Animation.MIXER_FINISH, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("sword_dance_left"), Ignited_Berserker_Animation.SWORD_DANCE_LEFT, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("sword_dance_right"), Ignited_Berserker_Animation.SWORD_DANCE_RIGHT, ageInTicks, 1.0F);

	}

	private void animateHeadLookTarget(float yRot, float xRot) {
		this.head.xRot = xRot * ((float) Math.PI / 180F);
		this.head.yRot = yRot * ((float) Math.PI / 180F);
	}

	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}