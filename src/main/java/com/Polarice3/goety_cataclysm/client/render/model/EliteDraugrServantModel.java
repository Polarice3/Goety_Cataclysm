package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.EliteDraugrServant;
import com.github.L_Ender.cataclysm.client.animation.Elite_Draugr_Animation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

public class EliteDraugrServantModel extends HierarchicalModel<EliteDraugrServant> implements ArmedModel {
	private final ModelPart everything;
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart front_cloth1;
	private final ModelPart front_cloth2;
	private final ModelPart back_cloth1;
	private final ModelPart back_cloth2;
	private final ModelPart waist;
	private final ModelPart chest;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart maw;
	private final ModelPart l_arm;
	private final ModelPart cube_r1;
	private final ModelPart l_arm2;
	private final ModelPart r_arm;
	private final ModelPart cube_r2;
	private final ModelPart r_arm2;
	private final ModelPart right_leg;
	private final ModelPart left_leg;


	public EliteDraugrServantModel(ModelPart root) {
		this.everything = root;
		this.root = this.everything.getChild("root");
		this.body = this.root.getChild("body");
		this.front_cloth1 = this.body.getChild("front_cloth1");
		this.front_cloth2 = this.front_cloth1.getChild("front_cloth2");
		this.back_cloth1 = this.body.getChild("back_cloth1");
		this.back_cloth2 = this.back_cloth1.getChild("back_cloth2");
		this.waist = this.body.getChild("waist");
		this.chest = this.waist.getChild("chest");
		this.neck = this.chest.getChild("neck");
		this.head = this.neck.getChild("head");
		this.maw = this.head.getChild("maw");
		this.l_arm = this.chest.getChild("l_arm");
		this.cube_r1 = this.l_arm.getChild("cube_r1");
		this.l_arm2 = this.l_arm.getChild("l_arm2");
		this.r_arm = this.chest.getChild("r_arm");
		this.cube_r2 = this.r_arm.getChild("cube_r2");
		this.r_arm2 = this.r_arm.getChild("r_arm2");
		this.right_leg = this.root.getChild("right_leg");
		this.left_leg = this.root.getChild("left_leg");

	}

	@Override
	public void setupAnim(EliteDraugrServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateHeadLookTarget(netHeadYaw, headPitch);

		this.animateWalk(Elite_Draugr_Animation.WALK, limbSwing, limbSwingAmount, 2.0F, 2.0F);
		this.animate(entity.getAnimationState("idle"), Elite_Draugr_Animation.IDLE, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("re_load"), Elite_Draugr_Animation.RE_LOAD, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("shoot"), Elite_Draugr_Animation.SHOOT, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("shoot2"), Elite_Draugr_Animation.SHOOT2, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("swing"), Elite_Draugr_Animation.SWING, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("attack"), Elite_Draugr_Animation.ATTACK, ageInTicks, 1.5F);
		this.animate(entity.getAnimationState("attack2"), Elite_Draugr_Animation.ATTACK2, ageInTicks, 1.5F);
	}

	private void animateHeadLookTarget(float yRot, float xRot) {
		this.head.xRot = xRot * ((float) Math.PI / 180F);
		this.head.yRot = yRot * ((float) Math.PI / 180F);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		root.translateAndRotate(poseStack);
		body.translateAndRotate(poseStack);
		waist.translateAndRotate(poseStack);
		chest.translateAndRotate(poseStack);
		if (arm == HumanoidArm.RIGHT) {
			r_arm.translateAndRotate(poseStack);
			r_arm2.translateAndRotate(poseStack);
			poseStack.translate(0.0F, 0.0F, 0.0F);
		} else {
			l_arm.translateAndRotate(poseStack);
			l_arm2.translateAndRotate(poseStack);
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