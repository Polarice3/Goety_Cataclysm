package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.SymbioctoServant;
import com.github.L_Ender.cataclysm.client.animation.Symbiocto_Animation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SymbioctoServantModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart octo_head;
    private final ModelPart back_tentacle1;
    private final ModelPart face_tentacle2;
    private final ModelPart face_tentacle1;
    private final ModelPart face_tentacle3;
    private final ModelPart teeth2;
    private final ModelPart teeth;
    private final ModelPart teeth3;
    private final ModelPart teeth4;
    private final ModelPart left_tentacle1;
    private final ModelPart right_tentacle1;
    private final ModelPart left_tentacle2;
    private final ModelPart right_tentacle2;
    private final ModelPart left_tentacle3;
    private final ModelPart right_tentacle3;
    private final ModelPart back_tentacle2;
    private final ModelPart back_tentacle3;

    public SymbioctoServantModel(ModelPart root) {
        this.root = root;
        this.everything = this.root.getChild("everything");
        this.octo_head = this.everything.getChild("octo_head");
        this.back_tentacle1 = this.octo_head.getChild("back_tentacle1");
        this.face_tentacle2 = this.octo_head.getChild("face_tentacle2");
        this.face_tentacle1 = this.octo_head.getChild("face_tentacle1");
        this.face_tentacle3 = this.octo_head.getChild("face_tentacle3");
        this.teeth2 = this.octo_head.getChild("teeth2");
        this.teeth = this.octo_head.getChild("teeth");
        this.teeth3 = this.octo_head.getChild("teeth3");
        this.teeth4 = this.octo_head.getChild("teeth4");
        this.left_tentacle1 = this.octo_head.getChild("left_tentacle1");
        this.right_tentacle1 = this.octo_head.getChild("right_tentacle1");
        this.left_tentacle2 = this.octo_head.getChild("left_tentacle2");
        this.right_tentacle2 = this.octo_head.getChild("right_tentacle2");
        this.left_tentacle3 = this.octo_head.getChild("left_tentacle3");
        this.right_tentacle3 = this.octo_head.getChild("right_tentacle3");
        this.back_tentacle2 = this.octo_head.getChild("back_tentacle2");
        this.back_tentacle3 = this.octo_head.getChild("back_tentacle3");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(netHeadYaw, headPitch);
        this.animateWalk(Symbiocto_Animation.WALK, limbSwing, limbSwingAmount, 2.0F, 4.0F);
        if (entity instanceof SymbioctoServant servant) {
            this.animate(servant.getAnimationState("idle"), Symbiocto_Animation.IDLE, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("attack"), Symbiocto_Animation.ATTACK, ageInTicks, 1.0F);
            this.animate(servant.getAnimationState("spit"), Symbiocto_Animation.INK, ageInTicks, 1.0F);
        }
        if (this.riding) {
            this.octo_head.z += 1;
            this.octo_head.xRot -= (float) Math.toRadians(22.5F);
            this.right_tentacle1.zRot -= (float) Math.toRadians(52.5F);
            this.left_tentacle1.zRot += (float) Math.toRadians(52.5F);
            this.right_tentacle2.zRot -= (float) Math.toRadians(20F);
            this.left_tentacle2.zRot += (float) Math.toRadians(20F);
            this.right_tentacle3.zRot -= (float) Math.toRadians(7.5F);
            this.left_tentacle3.zRot += (float) Math.toRadians(7.5F);

            if (!(entity.getVehicle() instanceof Player)) {
                this.face_tentacle1.xRot += (float) Math.toRadians(105);
                this.face_tentacle2.xRot += (float) Math.toRadians(105);
                this.face_tentacle3.xRot += (float) Math.toRadians(105);
            }

            this.back_tentacle1.xRot -= (float) Math.toRadians(75);
            this.back_tentacle2.xRot -= (float) Math.toRadians(75);
            this.back_tentacle3.xRot -= (float) Math.toRadians(75);
        }
    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        this.everything.yRot = yRot * ((float) Math.PI / 180F);
    }

    public ModelPart root() {
        return this.root;
    }
}
