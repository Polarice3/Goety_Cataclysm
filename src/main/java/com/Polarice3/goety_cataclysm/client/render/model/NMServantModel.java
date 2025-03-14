package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.golem.NetheriteMonstrosityServant;
import com.github.L_Ender.cataclysm.client.animation.Netherite_Monstrosity_Animation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class NMServantModel extends HierarchicalModel<NetheriteMonstrosityServant> {
    private final ModelPart root;
    private final ModelPart head;

    public NMServantModel(ModelPart root) {
        this.root = root;
        ModelPart roots = this.root.getChild("roots");
        ModelPart lowerbody = roots.getChild("lowerbody");
        ModelPart upperbody = lowerbody.getChild("upperbody");
        this.head = upperbody.getChild("head");
    }

    @Override
    public void setupAnim(NetheriteMonstrosityServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateHeadLookTarget(netHeadYaw, headPitch);

        if(!(entity.getAttackState() == 8 && entity.attackTicks > 19 && entity.attackTicks < 49)) {
            this.animateWalk(Netherite_Monstrosity_Animation.WALK, limbSwing, limbSwingAmount, 2.0F, 2.0F);
        }

        this.animate(entity.getAnimationState("idle"), Netherite_Monstrosity_Animation.IDLE, ageInTicks, 1.0F);

        this.animate(entity.getAnimationState("smash"), Netherite_Monstrosity_Animation.SMASH, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("sleep"), Netherite_Monstrosity_Animation.SLEEP, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("awake"), Netherite_Monstrosity_Animation.AWAKE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("phase_two"), Netherite_Monstrosity_Animation.PHASE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("death"), Netherite_Monstrosity_Animation.DEATH, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("fire"), Netherite_Monstrosity_Animation.FIRE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("drain"), Netherite_Monstrosity_Animation.DRAIN, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("shoulder_check"), Netherite_Monstrosity_Animation.SHOULDER_CHECK, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("overpower"), Netherite_Monstrosity_Animation.OVERPOWER, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("flare_shot"), Netherite_Monstrosity_Animation.FLARE_SHOT, ageInTicks, 1.0F);
    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        this.head.xRot += xRot * ((float) Math.PI / 180F);
        this.head.yRot += yRot * ((float) Math.PI / 180F);
    }

    public ModelPart root() {
        return this.root;
    }
}
