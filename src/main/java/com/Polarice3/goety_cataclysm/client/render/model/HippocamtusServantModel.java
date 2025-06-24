package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.HippocamtusServant;
import com.github.L_Ender.cataclysm.client.animation.Hippocamtus_Animation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class HippocamtusServantModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart body;
    private final ModelPart chest;
    private final ModelPart l_arm;
    private final ModelPart l_arm2;
    private final ModelPart r_arm;
    private final ModelPart r_arm2;
    private final ModelPart spear;
    private final ModelPart head;

    public HippocamtusServantModel(ModelPart root) {
        this.root = root;
        this.everything = this.root.getChild("everything");
        this.tail = this.everything.getChild("tail");
        this.tail2 = this.tail.getChild("tail2");
        this.tail3 = this.tail2.getChild("tail3");
        this.tail4 = this.tail3.getChild("tail4");
        this.tail5 = this.tail4.getChild("tail5");
        this.body = this.everything.getChild("body");
        this.chest = this.body.getChild("chest");
        this.l_arm = this.chest.getChild("l_arm");
        this.l_arm2 = this.l_arm.getChild("l_arm2");
        this.r_arm = this.chest.getChild("r_arm");
        this.r_arm2 = this.r_arm.getChild("r_arm2");
        this.spear = this.r_arm2.getChild("spear");
        this.head = this.chest.getChild("head");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(netHeadYaw, headPitch);

        this.animateWalk(Hippocamtus_Animation.WALK, limbSwing, limbSwingAmount, 1.0F, 1.5F);
        if (entity instanceof HippocamtusServant servant) {
            this.animate(servant.getAnimationState("idle"), Hippocamtus_Animation.IDLE, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("stab"), Hippocamtus_Animation.STAB, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("swing1"), Hippocamtus_Animation.SWING, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("swing2"), Hippocamtus_Animation.SWING2, ageInTicks, 1.0F);

            this.animate(servant.getAnimationState("guard"), Hippocamtus_Animation.GUARD, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("guardcounter"), Hippocamtus_Animation.GUARD_COUNTER, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("parry"), Hippocamtus_Animation.PARRYING, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("death"), Hippocamtus_Animation.DEATH, ageInTicks, 1.0F);
        }
    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        this.head.xRot += xRot * ((float) Math.PI / 180F);
        this.head.yRot += yRot * ((float) Math.PI / 180F);
    }

    public ModelPart root() {
        return this.root;
    }
}
