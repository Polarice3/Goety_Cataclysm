package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.AncientRemnantServant;
import com.github.L_Ender.cataclysm.client.animation.Ancient_Remnant_Animation;
import com.github.L_Ender.cataclysm.client.animation.Ancient_Remnant_Power_Animation;
import com.github.L_Ender.lionfishapi.server.animation.LegSolver;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class AncientRemnantServantModel extends HierarchicalModel<AncientRemnantServant> {
    private final ModelPart root;
    private final ModelPart roots;
    private final ModelPart mid_pivot;
    private final ModelPart pelvis;
    private final ModelPart left_long_bone;
    private final ModelPart right_long_bone;
    private final ModelPart spine_sail2;
    private final ModelPart left_bone;
    private final ModelPart right_bone;
    private final ModelPart left_big_bone;
    private final ModelPart right_big_bone;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart spine1;
    private final ModelPart spine2;
    private final ModelPart spine_sail1;
    private final ModelPart right_shoulder;
    private final ModelPart left_shoulder;
    private final ModelPart neck1;
    private final ModelPart neck2;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart crown;
    private final ModelPart under_crown;
    private final ModelPart right_crown1;
    private final ModelPart right_crown2;
    private final ModelPart left_crown;
    private final ModelPart left_crown2;
    private final ModelPart snake;
    private final ModelPart upper_crown;
    private final ModelPart desert_necklace;
    private final ModelPart chain1;
    private final ModelPart chain2;
    private final ModelPart chain3;
    private final ModelPart chain4;
    private final ModelPart chain5;
    private final ModelPart desert_eye;
    private final ModelPart eye;
    private final ModelPart left_arm;
    private final ModelPart left_front_arm;
    private final ModelPart left_hand;
    private final ModelPart left_finger3;
    private final ModelPart left_finger1;
    private final ModelPart left_finger2;
    private final ModelPart right_arm;
    private final ModelPart right_front_arm;
    private final ModelPart right_hand;
    private final ModelPart right_finger1;
    private final ModelPart right_finger2;
    private final ModelPart right_finger3;
    private final ModelPart spine_deco;
    private final ModelPart legs;
    private final ModelPart left_leg;
    private final ModelPart left_deco1;
    private final ModelPart left_front_leg;
    private final ModelPart left_ankel_joint;
    private final ModelPart left_mini_bone;
    private final ModelPart left_deco2;
    private final ModelPart left_deco3;
    private final ModelPart left_ankel;
    private final ModelPart left_foot;
    private final ModelPart left_toe;
    private final ModelPart left_toe2;
    private final ModelPart left_toe3;
    private final ModelPart right_leg;
    private final ModelPart right_deco1;
    private final ModelPart right_front_leg;
    private final ModelPart right_ankel_joint;
    private final ModelPart right_mini_bone;
    private final ModelPart right_deco2;
    private final ModelPart right_deco3;
    private final ModelPart right_ankel;
    private final ModelPart right_foot;
    private final ModelPart right_toe;
    private final ModelPart right_toe2;
    private final ModelPart right_toe3;

    public AncientRemnantServantModel(ModelPart root) {
        this.root = root;
        this.roots = this.root.getChild("roots");
        this.mid_pivot = this.roots.getChild("mid_pivot");
        this.pelvis = this.mid_pivot.getChild("pelvis");
        this.left_long_bone = this.pelvis.getChild("left_long_bone");
        this.right_long_bone = this.pelvis.getChild("right_long_bone");
        this.spine_sail2 = this.pelvis.getChild("spine_sail2");
        this.left_bone = this.pelvis.getChild("left_bone");
        this.right_bone = this.pelvis.getChild("right_bone");
        this.left_big_bone = this.pelvis.getChild("left_big_bone");
        this.right_big_bone = this.pelvis.getChild("right_big_bone");
        this.tail1 = this.pelvis.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
        this.tail3 = this.tail2.getChild("tail3");
        this.tail4 = this.tail3.getChild("tail4");
        this.spine1 = this.pelvis.getChild("spine1");
        this.spine2 = this.spine1.getChild("spine2");
        this.spine_sail1 = this.spine2.getChild("spine_sail1");
        this.right_shoulder = this.spine2.getChild("right_shoulder");
        this.left_shoulder = this.spine2.getChild("left_shoulder");
        this.neck1 = this.spine2.getChild("neck1");
        this.neck2 = this.neck1.getChild("neck2");
        this.head = this.neck2.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.crown = this.head.getChild("crown");
        this.under_crown = this.crown.getChild("under_crown");
        this.right_crown1 = this.under_crown.getChild("right_crown1");
        this.right_crown2 = this.right_crown1.getChild("right_crown2");
        this.left_crown = this.under_crown.getChild("left_crown");
        this.left_crown2 = this.left_crown.getChild("left_crown2");
        this.snake = this.crown.getChild("snake");
        this.upper_crown = this.crown.getChild("upper_crown");
        this.desert_necklace = this.neck2.getChild("desert_necklace");
        this.chain1 = this.desert_necklace.getChild("chain1");
        this.chain2 = this.chain1.getChild("chain2");
        this.chain3 = this.chain2.getChild("chain3");
        this.chain4 = this.chain3.getChild("chain4");
        this.chain5 = this.chain4.getChild("chain5");
        this.desert_eye = this.chain5.getChild("desert_eye");
        this.eye = this.desert_eye.getChild("eye");
        this.left_arm = this.spine2.getChild("left_arm");
        this.left_front_arm = this.left_arm.getChild("left_front_arm");
        this.left_hand = this.left_front_arm.getChild("left_hand");
        this.left_finger3 = this.left_hand.getChild("left_finger3");
        this.left_finger1 = this.left_hand.getChild("left_finger1");
        this.left_finger2 = this.left_hand.getChild("left_finger2");
        this.right_arm = this.spine2.getChild("right_arm");
        this.right_front_arm = this.right_arm.getChild("right_front_arm");
        this.right_hand = this.right_front_arm.getChild("right_hand");
        this.right_finger1 = this.right_hand.getChild("right_finger1");
        this.right_finger2 = this.right_hand.getChild("right_finger2");
        this.right_finger3 = this.right_hand.getChild("right_finger3");
        this.spine_deco = this.spine2.getChild("spine_deco");
        this.legs = this.mid_pivot.getChild("legs");
        this.left_leg = this.legs.getChild("left_leg");
        this.left_deco1 = this.left_leg.getChild("left_deco1");
        this.left_front_leg = this.left_leg.getChild("left_front_leg");
        this.left_ankel_joint = this.left_front_leg.getChild("left_ankel_joint");
        this.left_mini_bone = this.left_ankel_joint.getChild("left_mini_bone");
        this.left_deco2 = this.left_ankel_joint.getChild("left_deco2");
        this.left_deco3 = this.left_ankel_joint.getChild("left_deco3");
        this.left_ankel = this.left_ankel_joint.getChild("left_ankel");
        this.left_foot = this.left_ankel_joint.getChild("left_foot");
        this.left_toe = this.left_foot.getChild("left_toe");
        this.left_toe2 = this.left_foot.getChild("left_toe2");
        this.left_toe3 = this.left_foot.getChild("left_toe3");
        this.right_leg = this.legs.getChild("right_leg");
        this.right_deco1 = this.right_leg.getChild("right_deco1");
        this.right_front_leg = this.right_leg.getChild("right_front_leg");
        this.right_ankel_joint = this.right_front_leg.getChild("right_ankel_joint");
        this.right_mini_bone = this.right_ankel_joint.getChild("right_mini_bone");
        this.right_deco2 = this.right_ankel_joint.getChild("right_deco2");
        this.right_deco3 = this.right_ankel_joint.getChild("right_deco3");
        this.right_ankel = this.right_ankel_joint.getChild("right_ankel");
        this.right_foot = this.right_ankel_joint.getChild("right_foot");
        this.right_toe = this.right_foot.getChild("right_toe");
        this.right_toe2 = this.right_foot.getChild("right_toe2");
        this.right_toe3 = this.right_foot.getChild("right_toe3");
    }

    @Override
    public void setupAnim(AncientRemnantServant entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(netHeadYaw, headPitch);

        if(entityIn.getAttackState() != 10 && entityIn.getAttackState() != 11 && entityIn.getAttackState() != 12 && !entityIn.isSleep()) {
            this.animateWalk(Ancient_Remnant_Animation.WALK, limbSwing, limbSwingAmount, 1.0F, 4.0F);
        }

        this.animate(entityIn.getAnimationState("idle"), Ancient_Remnant_Animation.IDLE, ageInTicks, entityIn.getNecklace() ? 1.0f : 0.15F);
        this.animate(entityIn.getAnimationState("death"), Ancient_Remnant_Animation.DEATH, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("right_bite"), Ancient_Remnant_Animation.RIGHT_BITE, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("sandstorm_roar"), Ancient_Remnant_Animation.SAND_STORM_ROAR, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("phase_roar"), Ancient_Remnant_Animation.PHASE_ROAR, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("charge"), Ancient_Remnant_Animation.CHARGE, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("sleep"), Ancient_Remnant_Animation.SLEEP, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("awaken"), Ancient_Remnant_Animation.AWAKEN, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("left_double_stomp"), Ancient_Remnant_Power_Animation.DOUBLE_STOMP2, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("right_double_stomp"), Ancient_Remnant_Power_Animation.DOUBLE_STOMP1, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("ground_tail"), Ancient_Remnant_Power_Animation.GROUND_TAIL, ageInTicks, 1.0F);

        this.animate(entityIn.getAnimationState("tail_swing"), Ancient_Remnant_Power_Animation.TAIL_SWING, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("monolith"), Ancient_Remnant_Power_Animation.MONOLITH, ageInTicks, 1.0F);

        this.animate(entityIn.getAnimationState("right_stomp"), Ancient_Remnant_Animation.STOMP1, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("left_stomp"), Ancient_Remnant_Animation.STOMP2, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("charge_prepare"), Ancient_Remnant_Animation.CHARGE_PREPARE, ageInTicks, 1.0F);
        this.animate(entityIn.getAnimationState("charge_stun"), Ancient_Remnant_Animation.CHARGE_STUN, ageInTicks, 1.0F);
        float partialTick = Minecraft.getInstance().getFrameTime();

        if (!entityIn.isSleep()) {
            articulateLegs(entityIn.legSolver, partialTick);
        }

        desert_necklace.visible = entityIn.getNecklace();

    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        this.head.xRot += xRot * ((float) Math.PI / 180F);
        this.head.yRot += yRot * ((float) Math.PI / 180F);
    }

    private void articulateLegs(LegSolver legs, float partialTick) {
        float heightBackLeft = legs.legs[0].getHeight(partialTick);
        float heightBackRight = legs.legs[1].getHeight(partialTick);
        float max = (1F - smin(1F - heightBackLeft, 1F - heightBackRight, 0.1F)) * 0.8F;
        roots.y += max * 16;
        right_leg.y += (heightBackRight - max) * 16;
        left_leg.y += (heightBackLeft - max) * 16;
    }

    private static float smin(float a, float b, float k) {
        float h = Math.max(k - Math.abs(a - b), 0.0F) / k;
        return Math.min(a, b) - h * h * k * (1.0F / 4.0F);
    }

    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
