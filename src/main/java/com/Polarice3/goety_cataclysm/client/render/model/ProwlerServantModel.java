package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.factory.ProwlerServant;
import com.github.L_Ender.cataclysm.client.animation.Prowler_Animation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ProwlerServantModel extends HierarchicalModel<ProwlerServant> {
    private final ModelPart root;
    private final ModelPart upperbody;
    private final ModelPart saw;

    public ProwlerServantModel(ModelPart root) {
        this.root = root;
        ModelPart roots = this.root.getChild("roots");
        this.upperbody = roots.getChild("upperbody");
        ModelPart right_arm = this.upperbody.getChild("right_arm");
        ModelPart right_arm_joint = right_arm.getChild("right_arm_joint");
        ModelPart right_arm2 = right_arm_joint.getChild("right_arm2");
        ModelPart right_joint = right_arm2.getChild("right_joint");
        ModelPart chainsaw = right_joint.getChild("chainsaw");
        this.saw = chainsaw.getChild("saw");
    }

    @Override
    public void setupAnim(ProwlerServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.upperbody.yRot += netHeadYaw * 0.6F * Mth.DEG_TO_RAD;
        float sawspeed = entity.getAttackState() == 3 ? 0F : 0.5f;
        this.animate(entity.getAnimationState("death"), Prowler_Animation.DEATH, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("idle"), Prowler_Animation.IDLE, ageInTicks, 1.0f);
        this.animate(entity.getAnimationState("spin"), Prowler_Animation.SPIN, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("melee"), Prowler_Animation.MELEE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("strong_attack"), Prowler_Animation.STRONG_ATTACK, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("stun"), Prowler_Animation.STUN, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("laser"), Prowler_Animation.LASER, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("pierce"), Prowler_Animation.PIERCE, ageInTicks, 1.0F);
        saw.xRot -= ageInTicks * sawspeed;
    }

    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
