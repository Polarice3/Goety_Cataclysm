package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.Goety.client.render.animation.NecromancerAnimations;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.goety_cataclysm.common.entities.neutral.AbstractDraugrNecromancer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class DraugrNecromancerModel<T extends AbstractDraugrNecromancer> extends HierarchicalModel<T> {
	public final ModelPart root;
	private final ModelPart skeleton;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart hat;
	private final ModelPart horns;
	private final ModelPart right_arm;
	private final ModelPart staff;
	private final ModelPart handle;
	private final ModelPart group;
	private final ModelPart staffhead;
	private final ModelPart right_pauldron;
	private final ModelPart left_arm;
	private final ModelPart leftItem;
	private final ModelPart left_pauldron;
	private final ModelPart pants;
	private final ModelPart middle;
	private final ModelPart cape;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public DraugrNecromancerModel(ModelPart root) {
		this.root = root;
		this.skeleton = root.getChild("skeleton");
		this.body = this.skeleton.getChild("body");
		this.head = this.body.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.hat = this.head.getChild("hat");
		this.horns = this.hat.getChild("horns");
		this.right_arm = this.body.getChild("right_arm");
		this.staff = this.right_arm.getChild("staff");
		this.handle = this.staff.getChild("handle");
		this.group = this.staff.getChild("group");
		this.staffhead = this.staff.getChild("staffhead");
		this.right_pauldron = this.right_arm.getChild("right_pauldron");
		this.left_arm = this.body.getChild("left_arm");
		this.leftItem = this.left_arm.getChild("leftItem");
		this.left_pauldron = this.left_arm.getChild("left_pauldron");
		this.pants = this.body.getChild("pants");
		this.middle = this.pants.getChild("middle");
		this.cape = this.body.getChild("cape");
		this.rightLeg = this.skeleton.getChild("right_leg");
		this.leftLeg = this.skeleton.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition skeleton = partdefinition.addOrReplaceChild("skeleton", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = skeleton.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 112).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 71).addBox(-3.0F, -2.5F, -2.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -1.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.75F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition horns = hat.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(28, 92).addBox(-9.0F, -39.0F, 0.0F, 18.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 112).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-5.0F, -10.0F, 0.0F, -1.3963F, 0.2618F, 0.0F));

		PartDefinition staff = right_arm.addOrReplaceChild("staff", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 10.0F, 5.5F, 1.4399F, 0.0F, 0.0F));

		PartDefinition handle = staff.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(60, 39).addBox(0.5F, -16.0F, -19.0F, 1.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 7.0F, 19.0F));

		PartDefinition group = staff.addOrReplaceChild("group", CubeListBuilder.create().texOffs(56, 47).addBox(0.5F, -19.0F, -21.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 47).addBox(2.5F, -19.0F, -19.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 47).addBox(-1.5F, -19.0F, -19.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 47).addBox(0.5F, -19.0F, -17.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 56).addBox(0.5F, -17.0F, -21.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(48, 62).addBox(-1.5F, -17.0F, -19.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 7.0F, 19.0F));

		PartDefinition staffhead = staff.addOrReplaceChild("staffhead", CubeListBuilder.create().texOffs(48, 50).addBox(-0.5F, -20.0F, -20.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 7.0F, 19.0F));

		PartDefinition right_pauldron = right_arm.addOrReplaceChild("right_pauldron", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -4.0F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.0472F, -0.0873F, -0.2618F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(24, 112).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(5.0F, -10.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition leftItem = left_arm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 1.0F));

		PartDefinition left_pauldron = left_arm.addOrReplaceChild("left_pauldron", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-1.0F, -4.0F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition pants = body.addOrReplaceChild("pants", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition middle = pants.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(40, 36).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(24, 64).addBox(-8.0F, 0.0F, -2.0F, 16.0F, 24.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 1.0F));

		PartDefinition right_leg = skeleton.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 112).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-2.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition left_leg = skeleton.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(24, 112).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(2.0F, -12.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		return LayerDefinition.create(meshdefinition, 64, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (!entity.isDeadOrDying()){
			if (entity.cantDo > 0){
				this.head.zRot = 0.3F * Mth.sin(0.45F * ageInTicks);
				this.head.xRot = 0.4F;
			} else {
				this.animateHeadLookTarget(netHeadYaw, headPitch);
			}
		}
		this.animate(entity.idleAnimationState, NecromancerAnimations.ALERT, ageInTicks);
		if (this.riding){
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		} else {
			if (!entity.isIdleOrNoAnimation()){
				this.animateWalk(entity, limbSwing, limbSwingAmount);
			} else {
				this.animateWalk(NecromancerAnimations.WALK, limbSwing, limbSwingAmount, 2.5F, 20.0F);
			}
		}
		this.animate(entity.attackAnimationState, NecromancerAnimations.ATTACK, ageInTicks, entity.getAttackSpeed());
		this.animate(entity.summonAnimationState, NecromancerAnimations.SUMMON, ageInTicks);
		this.animate(entity.spellAnimationState, NecromancerAnimations.SPELL, ageInTicks);
	}

	private void animateWalk(T entity, float limbSwing, float limbSwingAmount){
		float f = 1.0F;
		if (entity.getFallFlyingTicks() > 4) {
			f = (float)entity.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}
		if (f < 1.0F) {
			f = 1.0F;
		}
		this.cape.xRot = MathHelper.modelDegrees(10.0F) + Mth.abs(Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount / f);
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
	}

	private void animateHeadLookTarget(float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}