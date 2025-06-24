package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.ClawdianServant;
import com.github.L_Ender.cataclysm.client.animation.Clawdian_Animation;
import com.github.L_Ender.cataclysm.client.animation.Clawdian_Skill_Animation;
import com.github.L_Ender.lionfishapi.server.animation.LegSolverQuadruped;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class ClawdianServantModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart mid_root;
    private final ModelPart lower_body;
    private final ModelPart pelvis;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart right_down_antenna;
    private final ModelPart left_down_antenna;
    private final ModelPart head_tail;
    private final ModelPart left_arm;
    private final ModelPart left_front_arm_rotator;
    private final ModelPart left_front_arm;
    private final ModelPart left_claw;
    private final ModelPart block;
    private final ModelPart right_arm;
    private final ModelPart right_front_arm_rotator;
    private final ModelPart right_front_arm;
    private final ModelPart right_claw;
    private final ModelPart right_hammer;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart legs;
    private final ModelPart left_f_leg_joint;
    private final ModelPart left_f_leg;
    private final ModelPart left_f_leg_solver;
    private final ModelPart left_f_fore_leg;
    private final ModelPart left_f_fore_leg_solver;
    private final ModelPart right_f_leg_joint;
    private final ModelPart right_f_leg;
    private final ModelPart right_f_leg_solver;
    private final ModelPart right_f_fore_leg;
    private final ModelPart right_f_fore_leg_solver;
    private final ModelPart right_b_leg_joint;
    private final ModelPart right_b_leg;
    private final ModelPart right_b_leg_solver;
    private final ModelPart right_b_fore_leg;
    private final ModelPart right_b_fore_leg_solver;
    private final ModelPart left_b_leg_joint;
    private final ModelPart left_b_leg;
    private final ModelPart left_b_leg_solver;
    private final ModelPart left_b_fore_leg;
    private final ModelPart left_b_fore_leg_solver;

    public ClawdianServantModel(ModelPart root) {
        this.root = root;
        this.everything = this.root.getChild("everything");
        this.mid_root = this.everything.getChild("mid_root");
        this.lower_body = this.mid_root.getChild("lower_body");
        this.pelvis = this.lower_body.getChild("pelvis");
        this.body = this.pelvis.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.right_down_antenna = this.head.getChild("right_down_antenna");
        this.left_down_antenna = this.head.getChild("left_down_antenna");
        this.head_tail = this.head.getChild("head_tail");
        this.left_arm = this.body.getChild("left_arm");
        this.left_front_arm_rotator = this.left_arm.getChild("left_front_arm_rotator");
        this.left_front_arm = this.left_front_arm_rotator.getChild("left_front_arm");
        this.left_claw = this.left_front_arm.getChild("left_claw");
        this.block = this.left_front_arm.getChild("block");
        this.right_arm = this.body.getChild("right_arm");
        this.right_front_arm_rotator = this.right_arm.getChild("right_front_arm_rotator");
        this.right_front_arm = this.right_front_arm_rotator.getChild("right_front_arm");
        this.right_claw = this.right_front_arm.getChild("right_claw");
        this.right_hammer = this.right_front_arm.getChild("right_hammer");
        this.tail1 = this.lower_body.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
        this.legs = this.lower_body.getChild("legs");
        this.left_f_leg_joint = this.legs.getChild("left_f_leg_joint");
        this.left_f_leg = this.left_f_leg_joint.getChild("left_f_leg");
        this.left_f_leg_solver = this.left_f_leg.getChild("left_f_leg_solver");
        this.left_f_fore_leg = this.left_f_leg_solver.getChild("left_f_fore_leg");
        this.left_f_fore_leg_solver = this.left_f_fore_leg.getChild("left_f_fore_leg_solver");
        this.right_f_leg_joint = this.legs.getChild("right_f_leg_joint");
        this.right_f_leg = this.right_f_leg_joint.getChild("right_f_leg");
        this.right_f_leg_solver = this.right_f_leg.getChild("right_f_leg_solver");
        this.right_f_fore_leg = this.right_f_leg_solver.getChild("right_f_fore_leg");
        this.right_f_fore_leg_solver = this.right_f_fore_leg.getChild("right_f_fore_leg_solver");
        this.right_b_leg_joint = this.legs.getChild("right_b_leg_joint");
        this.right_b_leg = this.right_b_leg_joint.getChild("right_b_leg");
        this.right_b_leg_solver = this.right_b_leg.getChild("right_b_leg_solver");
        this.right_b_fore_leg = this.right_b_leg_solver.getChild("right_b_fore_leg");
        this.right_b_fore_leg_solver = this.right_b_fore_leg.getChild("right_b_fore_leg_solver");
        this.left_b_leg_joint = this.legs.getChild("left_b_leg_joint");
        this.left_b_leg = this.left_b_leg_joint.getChild("left_b_leg");
        this.left_b_leg_solver = this.left_b_leg.getChild("left_b_leg_solver");
        this.left_b_fore_leg = this.left_b_leg_solver.getChild("left_b_fore_leg");
        this.left_b_fore_leg_solver = this.left_b_fore_leg.getChild("left_b_fore_leg_solver");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(netHeadYaw, headPitch);
        if (entity instanceof ClawdianServant servant) {
            if (servant.getAttackState() != 5 && servant.getAttackState() != 2 && servant.getAttackState() != 10) {
                this.animateWalk(Clawdian_Animation.WALK, limbSwing, limbSwingAmount, 2.0F, 1.5F);
            }
            this.animate(servant.getAnimationState("idle"), Clawdian_Animation.IDLE, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("vertical_swing"), Clawdian_Animation.VERTICAL_SWING, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("horizontal_swing"), Clawdian_Animation.HORIZONTAL_SWING, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("death"), Clawdian_Animation.DEATH, ageInTicks, 1.0F);

            this.animate(servant.getAnimationState("charge_ready"), Clawdian_Skill_Animation.CHARGE_READY, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("charge_loop"), Clawdian_Skill_Animation.CHARGE_LOOP, ageInTicks, 1.5F);
            this.animate(servant.getAnimationState("charge_end"), Clawdian_Skill_Animation.CHARGE_END, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("wave_stomp"), Clawdian_Skill_Animation.WAVE_STOMP, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("claw_punch"), Clawdian_Animation.CLAW_PUNCH, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("grab_and_throw"), Clawdian_Animation.GRAB_AND_THROW, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("backstep"), Clawdian_Animation.BACKSTEP, ageInTicks, 1.0F);
            float partialTick = Minecraft.getInstance().getFrameTime();

            articulateLegs(servant.legSolver, partialTick);
        }
    }

    public void translateToHand(PoseStack matrixStack) {
        this.root.translateAndRotate(matrixStack);
        this.everything.translateAndRotate(matrixStack);
        this.mid_root.translateAndRotate(matrixStack);
        this.lower_body.translateAndRotate(matrixStack);
        this.pelvis.translateAndRotate(matrixStack);
        this.body.translateAndRotate(matrixStack);
        this.left_arm.translateAndRotate(matrixStack);
        this.left_front_arm_rotator.translateAndRotate(matrixStack);
        this.left_front_arm.translateAndRotate(matrixStack);
        this.block.translateAndRotate(matrixStack);
    }

    private void articulateLegs(LegSolverQuadruped legs, float partialTick) {
        float heightBackLeft = legs.backLeft.getHeight(partialTick);
        float heightBackRight = legs.backRight.getHeight(partialTick);
        float heightFrontLeft = legs.frontLeft.getHeight(partialTick);
        float heightFrontRight = legs.frontRight.getHeight(partialTick);
        float max = Math.max(Math.max(heightBackLeft, heightBackRight), Math.max(heightFrontLeft, heightFrontRight)) * 0.8F;
        this.everything.y += max * 16;

        this.right_f_leg_joint.y += (heightFrontRight - max) * 3;
        this.right_f_leg_solver.zRot += (heightFrontRight - max) * Math.toRadians(-45F);
        this.right_b_leg_joint.y += (heightBackRight - max) * 3;
        this.right_b_leg_solver.zRot += (heightBackRight - max) * Math.toRadians(-45F);

        this.right_f_fore_leg_solver.zRot += (heightFrontRight - max) * Math.toRadians(45F);
        this.right_b_fore_leg_solver.zRot += (heightBackRight - max) * Math.toRadians(45F);

        this.left_f_leg_joint.y += (heightFrontLeft - max) * 3;
        this.left_f_leg_solver.zRot += (heightFrontLeft - max) * Math.toRadians(45F);
        this.left_b_leg_joint.y += (heightBackLeft - max) * 3;
        this.left_b_leg_solver.zRot += (heightBackLeft - max) * Math.toRadians(45F);

        this.left_f_fore_leg_solver.zRot += (heightFrontLeft - max) * Math.toRadians(-45F);
        this.left_b_fore_leg_solver.zRot += (heightBackLeft - max) * Math.toRadians(-45F);
    }


    private void animateHeadLookTarget(float yRot, float xRot) {
        this.neck.xRot += xRot * ((float) Math.PI / 180F) * 1/2;
        this.neck.yRot += yRot * ((float) Math.PI / 180F) * 1/2;
        this.head.xRot += xRot * ((float) Math.PI / 180F) * 1/2;
        this.head.yRot += yRot * ((float) Math.PI / 180F) * 1/2;
    }

    public ModelPart root() {
        return this.root;
    }
}
