package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.DraugrServant;
import com.github.L_Ender.cataclysm.client.animation.Draugar_Animation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

public class DraugrServantModel extends HierarchicalModel<DraugrServant> implements ArmedModel {
	private final ModelPart everything;
	private final ModelPart root;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart left_arm_r1;
	private final ModelPart left_arm_r2;
	private final ModelPart right_arm;
	private final ModelPart right_arm_r1;
	private final ModelPart right_arm_r2;
	private final ModelPart head;
	private final ModelPart maw;
	private final ModelPart body_r1;

	public DraugrServantModel(ModelPart root) {
		this.everything = root;
		this.root = this.everything.getChild("root");
		this.right_leg = this.root.getChild("right_leg");
		this.left_leg = this.root.getChild("left_leg");
		this.body = this.root.getChild("body");
		this.left_arm = this.body.getChild("left_arm");
		this.left_arm_r1 = this.left_arm.getChild("left_arm_r1");
		this.left_arm_r2 = this.left_arm.getChild("left_arm_r2");
		this.right_arm = this.body.getChild("right_arm");
		this.right_arm_r1 = this.right_arm.getChild("right_arm_r1");
		this.right_arm_r2 = this.right_arm.getChild("right_arm_r2");
		this.head = this.body.getChild("head");
		this.maw = this.head.getChild("maw");
		this.body_r1 = this.maw.getChild("body_r1");
	}

	@Override
	public void setupAnim(DraugrServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateHeadLookTarget(netHeadYaw, headPitch);
		this.animateWalk(Draugar_Animation.WALK, limbSwing, limbSwingAmount, 2.0F, 2.0F);
		this.animate(entity.getAnimationState("idle"), Draugar_Animation.IDLE, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("attack"), Draugar_Animation.ATTACK, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("attack2"), Draugar_Animation.ATTACK2, ageInTicks, 1.0F);

	}

	private void animateHeadLookTarget(float yRot, float xRot) {
		this.head.xRot = xRot * ((float) Math.PI / 180F);
		this.head.yRot = yRot * ((float) Math.PI / 180F);
	}

	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		root.translateAndRotate(poseStack);
		body.translateAndRotate(poseStack);
		if (arm == HumanoidArm.RIGHT) {
			right_arm.translateAndRotate(poseStack);
			poseStack.translate(0.0F, 0.0F, 0.0F);
		} else {
			left_arm.translateAndRotate(poseStack);
			poseStack.translate(0.0F, 0.0F, 0.0F);
		}
	}


	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}