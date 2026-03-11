package com.Polarice3.goety_cataclysm.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class NMHeadModel extends SkullModelBase {
	private final ModelPart head;

	public NMHeadModel(ModelPart root) {
		this.head = root.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition top = head.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 139).addBox(-14.0F, -25.0F, -20.0F, 28.0F, 31.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 9.0F));

		PartDefinition right_horn = top.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(246, 112).addBox(-7.25F, 0.5F, -6.5F, 20.0F, 13.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(253, 184).addBox(-7.25F, -14.5F, -6.5F, 8.0F, 15.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-26.75F, -23.5F, -8.5F));

		PartDefinition left_horn = top.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(246, 112).mirror().addBox(-12.75F, 0.5F, -6.5F, 20.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(169, 171).addBox(-0.75F, -2.5F, -6.5F, 8.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(26.75F, -23.5F, -8.5F));

		PartDefinition eyes = top.addOrReplaceChild("eyes", CubeListBuilder.create().texOffs(18, 6).addBox(-14.0F, -6.0F, -20.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(18, 6).addBox(10.25F, -6.0F, -20.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(13, 1).addBox(-2.5F, -9.0F, -20.5F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottom = head.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(209, 2).addBox(-14.0F, -6.0F, -20.0F, 27.0F, 16.0F, 21.0F, new CubeDeformation(0.0F))
				.texOffs(305, 8).addBox(-14.0F, 10.0F, -20.0F, 27.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(209, 40).addBox(-14.0F, 7.0F, -20.0F, 27.0F, 0.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -10.0F, 9.5F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	public void setupAnim(float p_225603_1_, float p_225603_2_, float p_225603_3_) {
		this.head.yRot = p_225603_2_ * ((float)Math.PI / 180F);
		this.head.xRot = p_225603_3_ * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}