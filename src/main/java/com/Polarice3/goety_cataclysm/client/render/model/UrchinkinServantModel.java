package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.UrchinkinServant;
import com.github.L_Ender.cataclysm.client.animation.Urchinkin_Animation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class UrchinkinServantModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart mid_root;
    private final ModelPart legs;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public UrchinkinServantModel(ModelPart root) {
        this.root = root;
        this.everything = this.root.getChild("everything");
        this.mid_root = this.everything.getChild("mid_root");
        this.legs = this.mid_root.getChild("legs");
        this.body = this.legs.getChild("body");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_leg = this.legs.getChild("right_leg");
        this.left_leg = this.legs.getChild("left_leg");
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(netHeadYaw, headPitch);
        this.animateWalk(Urchinkin_Animation.WALK, limbSwing, limbSwingAmount, 1.0F, 4.0F);
        if (entity instanceof UrchinkinServant servant) {
            this.animate(servant.getAnimationState("idle"), Urchinkin_Animation.IDLE, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("roll"), Urchinkin_Animation.ROLL, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("attack"), Urchinkin_Animation.ATTACK, ageInTicks, 1.0F);
        }
    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        this.body.xRot = xRot * 0.017453292F;
        this.everything.yRot = yRot * 0.017453292F;
    }

    public ModelPart root() {
        return this.root;
    }
}
