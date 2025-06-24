package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.CindariaServant;
import com.github.L_Ender.cataclysm.client.animation.Cindaria_Animation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class CindariaServantModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart everything;

    private final ModelPart lowerBody;
    private final ModelPart upperBody;
    private final ModelPart head;
    private final ModelPart head2;
    private final ModelPart rightHeadArm;
    private final ModelPart rightHeadArm2;
    private final ModelPart rightHeadArm3;
    private final ModelPart leftHeadArm;
    private final ModelPart leftHeadArm2;
    private final ModelPart leftHeadArm3;
    private final ModelPart rightArm;
    private final ModelPart rightArm2;
    private final ModelPart staff;
    private final ModelPart bone;
    private final ModelPart leftArm;
    private final ModelPart leftArm2;
    private final ModelPart skirt;
    private final ModelPart skirt2;
    private final ModelPart rightLeg;
    private final ModelPart rightLeg2;
    private final ModelPart leftLeg;
    private final ModelPart leftLeg2;

    public CindariaServantModel(ModelPart root) {
        this.root = root;
        this.everything = this.root.getChild("everything");
        this.lowerBody = this.everything.getChild("lowerBody");
        this.upperBody = this.lowerBody.getChild("upperBody");
        this.head = this.upperBody.getChild("head");
        this.head2 = this.head.getChild("head2");
        this.rightHeadArm = this.head2.getChild("rightHeadArm");
        this.rightHeadArm2 = this.rightHeadArm.getChild("rightHeadArm2");
        this.rightHeadArm3 = this.rightHeadArm2.getChild("rightHeadArm3");
        this.leftHeadArm = this.head2.getChild("leftHeadArm");
        this.leftHeadArm2 = this.leftHeadArm.getChild("leftHeadArm2");
        this.leftHeadArm3 = this.leftHeadArm2.getChild("leftHeadArm3");
        this.rightArm = this.upperBody.getChild("rightArm");
        this.rightArm2 = this.rightArm.getChild("rightArm2");
        this.staff = this.rightArm2.getChild("staff");
        this.bone = this.staff.getChild("bone");
        this.leftArm = this.upperBody.getChild("leftArm");
        this.leftArm2 = this.leftArm.getChild("leftArm2");
        this.skirt = this.lowerBody.getChild("skirt");
        this.skirt2 = this.lowerBody.getChild("skirt2");
        this.rightLeg = this.everything.getChild("rightLeg");
        this.rightLeg2 = this.rightLeg.getChild("rightLeg2");
        this.leftLeg = this.everything.getChild("leftLeg");
        this.leftLeg2 = this.leftLeg.getChild("leftLeg2");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(netHeadYaw, headPitch);
        this.animateWalk(Cindaria_Animation.WALK, limbSwing, limbSwingAmount, 1.0F, 1.5F);
        if (entity instanceof CindariaServant servant) {
            this.animate(servant.getAnimationState("idle"), Cindaria_Animation.IDLE, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("magic1"), Cindaria_Animation.MAGIC, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("melee"), Cindaria_Animation.MELEE, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("death"), Cindaria_Animation.DEATH, ageInTicks, 1.0F);
        }
    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        this.head.xRot = xRot * ((float) Math.PI / 180F);
        this.head.yRot = yRot * ((float) Math.PI / 180F);
    }

    public ModelPart root() {
        return this.root;
    }
}
