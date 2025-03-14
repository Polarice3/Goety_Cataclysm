package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.KobolediatorServant;
import com.github.L_Ender.cataclysm.client.animation.Kobolediator_Animation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class KobolediatorServantModel extends HierarchicalModel<KobolediatorServant> {
    private final ModelPart root;
    private final ModelPart head;

    public KobolediatorServantModel(ModelPart root) {
        this.root = root;
        ModelPart everything = this.root.getChild("everything");
        ModelPart mid_root = everything.getChild("mid_root");
        ModelPart pelvis = mid_root.getChild("pelvis");
        ModelPart lower_body = pelvis.getChild("lower_body");
        ModelPart body = lower_body.getChild("body");
        this.head = body.getChild("head");
    }

    @Override
    public void setupAnim(KobolediatorServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(netHeadYaw, headPitch);
        if(entity.getAttackState() != 6 && !entity.isSleep()) {
            this.animateWalk(Kobolediator_Animation.WALK, limbSwing, limbSwingAmount, 1.0F, 4.0F);
        }
        this.animate(entity.getAnimationState("idle"), Kobolediator_Animation.IDLE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("sleep"), Kobolediator_Animation.SLEEP, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("awake"), Kobolediator_Animation.AWAKE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("sword1"), Kobolediator_Animation.SWORD1, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("sword2"), Kobolediator_Animation.SWORD2, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("charge"), Kobolediator_Animation.CHARGE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("charge_prepare"), Kobolediator_Animation.CHARGE_PREPARE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("charge_end"), Kobolediator_Animation.CHARGE_END, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("death"), Kobolediator_Animation.DEATH, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("block"), Kobolediator_Animation.BLOCK, ageInTicks, 1.0F);
    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        this.head.xRot += xRot * ((float) Math.PI / 180F);
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
