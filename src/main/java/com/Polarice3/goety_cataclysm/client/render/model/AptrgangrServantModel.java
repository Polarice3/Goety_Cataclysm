package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.AptrgangrServant;
import com.github.L_Ender.cataclysm.client.animation.Aptrgangr_Animation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class AptrgangrServantModel extends HierarchicalModel<AptrgangrServant> {
	private final ModelPart root;
	private final ModelPart roots;
	private final ModelPart l_leg;
	private final ModelPart l_leg_armor;
	private final ModelPart left_leg_r1;
	private final ModelPart left_leg_r2;
	private final ModelPart r_leg;
	private final ModelPart r_leg_armor;
	private final ModelPart right_leg_r1;
	private final ModelPart right_leg_r2;
	private final ModelPart body;
	private final ModelPart chest;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart helmet;
	private final ModelPart head_r1;
	private final ModelPart head_r2;
	private final ModelPart head_r3;
	private final ModelPart head_r4;
	private final ModelPart head_r5;
	private final ModelPart head_r6;
	private final ModelPart jaw;
	private final ModelPart head_r7;
	private final ModelPart chestplate;
	private final ModelPart body_r1;
	private final ModelPart body_r2;
	private final ModelPart body_r3;
	private final ModelPart body_r4;
	private final ModelPart body_r5;
	private final ModelPart body_r6;
	private final ModelPart body_r7;
	private final ModelPart l_arm;
	private final ModelPart l_arm_armor;
	private final ModelPart right_arm_r1;
	private final ModelPart right_arm_r2;
	private final ModelPart right_arm_r3;
	private final ModelPart right_arm_r4;
	private final ModelPart right_arm_r5;
	private final ModelPart right_arm_r6;
	private final ModelPart arrow;
	private final ModelPart arrow2;
	private final ModelPart left_arm2;
	private final ModelPart l_arm_cloth;
	private final ModelPart hold;
	private final ModelPart r_arm;
	private final ModelPart right_arm2;
	private final ModelPart r_arm_cloth;
	private final ModelPart axe;
	private final ModelPart cube_r1;
	private final ModelPart cube_r2;
	private final ModelPart axe_head;
	private final ModelPart cube_r3;
	private final ModelPart cube_r4;
	private final ModelPart cube_r5;
	private final ModelPart cube_r6;
	private final ModelPart cube_r7;
	private final ModelPart cube_r8;
	private final ModelPart emblem3;
	private final ModelPart right_arm_r7;
	private final ModelPart emblem4;
	private final ModelPart r_arm_armor;
	private final ModelPart left_arm_r1;
	private final ModelPart left_arm_r2;
	private final ModelPart left_arm_r3;
	private final ModelPart left_arm_r4;
	private final ModelPart left_arm_r5;
	private final ModelPart left_arm_r6;
	private final ModelPart belt;
	private final ModelPart body_r8;
	private final ModelPart emblem2;
	private final ModelPart emblem;
	private final ModelPart cloth2;
	private final ModelPart cloth;

	public AptrgangrServantModel(ModelPart root) {
		this.root = root;
		this.roots = this.root.getChild("roots");
		this.l_leg = this.roots.getChild("l_leg");
		this.l_leg_armor = this.l_leg.getChild("l_leg_armor");
		this.left_leg_r1 = this.l_leg_armor.getChild("left_leg_r1");
		this.left_leg_r2 = this.l_leg_armor.getChild("left_leg_r2");
		this.r_leg = this.roots.getChild("r_leg");
		this.r_leg_armor = this.r_leg.getChild("r_leg_armor");
		this.right_leg_r1 = this.r_leg_armor.getChild("right_leg_r1");
		this.right_leg_r2 = this.r_leg_armor.getChild("right_leg_r2");
		this.body = this.roots.getChild("body");
		this.chest = this.body.getChild("chest");
		this.neck = this.chest.getChild("neck");
		this.head = this.neck.getChild("head");
		this.helmet = this.head.getChild("helmet");
		this.head_r1 = this.helmet.getChild("head_r1");
		this.head_r2 = this.helmet.getChild("head_r2");
		this.head_r3 = this.helmet.getChild("head_r3");
		this.head_r4 = this.helmet.getChild("head_r4");
		this.head_r5 = this.helmet.getChild("head_r5");
		this.head_r6 = this.helmet.getChild("head_r6");
		this.jaw = this.head.getChild("jaw");
		this.head_r7 = this.jaw.getChild("head_r7");
		this.chestplate = this.chest.getChild("chestplate");
		this.body_r1 = this.chestplate.getChild("body_r1");
		this.body_r2 = this.chestplate.getChild("body_r2");
		this.body_r3 = this.chestplate.getChild("body_r3");
		this.body_r4 = this.chestplate.getChild("body_r4");
		this.body_r5 = this.chestplate.getChild("body_r5");
		this.body_r6 = this.chestplate.getChild("body_r6");
		this.body_r7 = this.chestplate.getChild("body_r7");
		this.l_arm = this.chest.getChild("l_arm");
		this.l_arm_armor = this.l_arm.getChild("l_arm_armor");
		this.right_arm_r1 = this.l_arm_armor.getChild("right_arm_r1");
		this.right_arm_r2 = this.l_arm_armor.getChild("right_arm_r2");
		this.right_arm_r3 = this.l_arm_armor.getChild("right_arm_r3");
		this.right_arm_r4 = this.l_arm_armor.getChild("right_arm_r4");
		this.right_arm_r5 = this.l_arm_armor.getChild("right_arm_r5");
		this.right_arm_r6 = this.l_arm_armor.getChild("right_arm_r6");
		this.arrow = this.l_arm_armor.getChild("arrow");
		this.arrow2 = this.l_arm_armor.getChild("arrow2");
		this.left_arm2 = this.l_arm.getChild("left_arm2");
		this.l_arm_cloth = this.left_arm2.getChild("l_arm_cloth");
		this.hold = this.l_arm_cloth.getChild("hold");
		this.r_arm = this.chest.getChild("r_arm");
		this.right_arm2 = this.r_arm.getChild("right_arm2");
		this.r_arm_cloth = this.right_arm2.getChild("r_arm_cloth");
		this.axe = this.right_arm2.getChild("axe");
		this.cube_r1 = this.axe.getChild("cube_r1");
		this.cube_r2 = this.axe.getChild("cube_r2");
		this.axe_head = this.axe.getChild("axe_head");
		this.cube_r3 = this.axe_head.getChild("cube_r3");
		this.cube_r4 = this.axe_head.getChild("cube_r4");
		this.cube_r5 = this.axe_head.getChild("cube_r5");
		this.cube_r6 = this.axe_head.getChild("cube_r6");
		this.cube_r7 = this.axe_head.getChild("cube_r7");
		this.cube_r8 = this.axe_head.getChild("cube_r8");
		this.emblem3 = this.axe_head.getChild("emblem3");
		this.right_arm_r7 = this.emblem3.getChild("right_arm_r7");
		this.emblem4 = this.axe_head.getChild("emblem4");
		this.r_arm_armor = this.r_arm.getChild("r_arm_armor");
		this.left_arm_r1 = this.r_arm_armor.getChild("left_arm_r1");
		this.left_arm_r2 = this.r_arm_armor.getChild("left_arm_r2");
		this.left_arm_r3 = this.r_arm_armor.getChild("left_arm_r3");
		this.left_arm_r4 = this.r_arm_armor.getChild("left_arm_r4");
		this.left_arm_r5 = this.r_arm_armor.getChild("left_arm_r5");
		this.left_arm_r6 = this.r_arm_armor.getChild("left_arm_r6");
		this.belt = this.body.getChild("belt");
		this.body_r8 = this.belt.getChild("body_r8");
		this.emblem2 = this.belt.getChild("emblem2");
		this.emblem = this.belt.getChild("emblem");
		this.cloth2 = this.belt.getChild("cloth2");
		this.cloth = this.belt.getChild("cloth");
	}

	@Override
	public void setupAnim(AptrgangrServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateHeadLookTarget(netHeadYaw, headPitch);
		if(entity.getAttackState() != 4) {
			this.animateWalk(Aptrgangr_Animation.WALK, limbSwing, limbSwingAmount, 2.5F, 4.0F);
		}
		this.animate(entity.getAnimationState("idle"), Aptrgangr_Animation.IDLE, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("swing_right"), Aptrgangr_Animation.SWING_RIGHT, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("smash"), Aptrgangr_Animation.SMASH, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("charge_start"), Aptrgangr_Animation.RUSH_START, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("charge"), Aptrgangr_Animation.RUSHING, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("charge_end"), Aptrgangr_Animation.RUSH_END, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("charge_hit"), Aptrgangr_Animation.RUSH_HIT, ageInTicks, 1.0F);
		this.animate(entity.getAnimationState("death"), Aptrgangr_Animation.DEATH, ageInTicks, 1.0F);
	}



	private void animateHeadLookTarget(float yRot, float xRot) {
		this.head.xRot += xRot * ((float) Math.PI / 180F);
		this.head.yRot += yRot * ((float) Math.PI / 180F);
	}

	public void translateToHand(PoseStack matrixStack) {
		this.root.translateAndRotate(matrixStack);
		this.roots.translateAndRotate(matrixStack);
		this.body.translateAndRotate(matrixStack);
		this.chest.translateAndRotate(matrixStack);
		this.l_arm.translateAndRotate(matrixStack);
		this.left_arm2.translateAndRotate(matrixStack);
		this.l_arm_cloth.translateAndRotate(matrixStack);
		this.hold.translateAndRotate(matrixStack);
	}



	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}