package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.DeeplingPriestServant;
import com.github.L_Ender.lionfishapi.client.model.Animations.ModelAnimator;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedEntityModel;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedModelBox;
import com.github.L_Ender.lionfishapi.client.model.tools.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class DeeplingPriestServantModel extends AdvancedEntityModel<DeeplingPriestServant> {
    public final AdvancedModelBox root;
    public final AdvancedModelBox left_leg;
    public final AdvancedModelBox right_leg;
    public final AdvancedModelBox body;
    public final AdvancedModelBox body_coral1;
    public final AdvancedModelBox body_coral2;
    public final AdvancedModelBox head;
    public final AdvancedModelBox head2;
    public final AdvancedModelBox head_coral;
    public final AdvancedModelBox fin;
    public final AdvancedModelBox light;
    public final AdvancedModelBox r_fin;
    public final AdvancedModelBox l_fin;
    public final AdvancedModelBox headwear;
    public final AdvancedModelBox right_arm;
    public final AdvancedModelBox left_arm;
    private ModelAnimator animator;

    public DeeplingPriestServantModel() {
        texWidth = 128;
        texHeight = 128;


        root = new AdvancedModelBox(this);
        root.setRotationPoint(0.0F, 24.0F, 0.0F);


        left_leg = new AdvancedModelBox(this);
        left_leg.setRotationPoint(2.0F, -20.0F, 0.0F);
        root.addChild(left_leg);
        left_leg.setTextureOffset(40, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);

        right_leg = new AdvancedModelBox(this);
        right_leg.setRotationPoint(-2.0F, -20.0F, 0.0F);
        root.addChild(right_leg);
        right_leg.setTextureOffset(44, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);

        body = new AdvancedModelBox(this);
        body.setRotationPoint(0.0F, -20.0F, 0.0F);
        root.addChild(body);
        body.setTextureOffset(0, 32).addBox(-5.0F, -11.0F, -2.0F, 10.0F, 11.0F, 4.0F, 0.0F, false);
        body.setTextureOffset(96, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 27.0F, 4.0F, 0.3F, false);
        body.setTextureOffset(92, 43).addBox(-6.5F, -11.0F, -2.5F, 13.0F, 17.0F, 5.0F, 0.0F, false);
        body.setTextureOffset(52, 29).addBox(0.0F, -11.0F, 2.0F, 0.0F, 11.0F, 4.0F, 0.0F, false);

        body_coral1 = new AdvancedModelBox(this);
        body_coral1.setRotationPoint(-6.3F, 3.6F, -2.3F);
        body.addChild(body_coral1);
        setRotationAngle(body_coral1, 0.0F, -0.1745F, 0.0F);
        body_coral1.setTextureOffset(89, 7).addBox(-5.0F, -3.5F, 0.0F, 5.0F, 7.0F, 0.0F, 0.0F, false);

        body_coral2 = new AdvancedModelBox(this);
        body_coral2.setRotationPoint(6.3F, -3.5F, -2.3F);
        body.addChild(body_coral2);
        setRotationAngle(body_coral2, 0.0F, 0.48F, 0.0F);
        body_coral2.setTextureOffset(89, 0).addBox(0.0F, -3.6F, 0.0F, 5.0F, 7.0F, 0.0F, 0.0F, false);

        head = new AdvancedModelBox(this);
        head.setRotationPoint(0.0F, -11.0F, 0.0F);
        body.addChild(head);


        head2 = new AdvancedModelBox(this);
        head2.setRotationPoint(0.0F, -3.0F, 0.0F);
        head.addChild(head2);
        head2.setTextureOffset(0, 16).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, 0.0F, false);
        head2.setTextureOffset(80, 14).addBox(-6.0F, -9.0F, 0.0F, 7.0F, 5.0F, 0.0F, 0.0F, false);
        head2.setTextureOffset(34, 87).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, -0.1F, false);

        head_coral = new AdvancedModelBox(this);
        head_coral.setRotationPoint(6.0F, 0.0F, -4.0F);
        head2.addChild(head_coral);
        setRotationAngle(head_coral, 0.0F, 0.2618F, 0.0F);
        head_coral.setTextureOffset(80, 19).addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, false);

        fin = new AdvancedModelBox(this);
        fin.setRotationPoint(0.0F, -4.0F, 0.0F);
        head2.addChild(fin);
        fin.setTextureOffset(28, 47).addBox(0.0F, -6.0F, -10.0F, 0.0F, 6.0F, 11.0F, 0.0F, false);

        light = new AdvancedModelBox(this);
        light.setRotationPoint(0.0F, 1.0F, -8.5F);
        fin.addChild(light);
        light.setTextureOffset(44, 54).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        r_fin = new AdvancedModelBox(this);
        r_fin.setRotationPoint(-6.0F, 0.0F, 0.0F);
        head2.addChild(r_fin);
        r_fin.setTextureOffset(44, 44).addBox(-6.0F, -4.0F, 0.0F, 6.0F, 8.0F, 0.0F, 0.0F, false);

        l_fin = new AdvancedModelBox(this);
        l_fin.setRotationPoint(6.0F, 0.0F, 0.0F);
        head2.addChild(l_fin);
        l_fin.setTextureOffset(0, 47).addBox(0.0F, -4.0F, 0.0F, 6.0F, 8.0F, 0.0F, 0.0F, false);

        headwear = new AdvancedModelBox(this);
        headwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(headwear);
        headwear.setTextureOffset(0, 0).addBox(-6.0F, -7.0F, -4.0F, 12.0F, 8.0F, 8.0F, -0.5F, false);
        headwear.setTextureOffset(26, 71).addBox(-6.0F, -7.0F, -4.0F, 12.0F, 8.0F, 8.0F, -0.6F, false);

        right_arm = new AdvancedModelBox(this);
        right_arm.setRotationPoint(-6.0F, -10.0F, 0.0F);
        body.addChild(right_arm);
        right_arm.setTextureOffset(36, 32).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, 0.0F, false);
        right_arm.setTextureOffset(52, 58).addBox(-6.0F, 2.0F, 0.0F, 5.0F, 12.0F, 0.0F, 0.0F, false);

        left_arm = new AdvancedModelBox(this);
        left_arm.setRotationPoint(6.0F, -10.0F, 0.0F);
        body.addChild(left_arm);
        left_arm.setTextureOffset(28, 32).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, 0.0F, false);
        left_arm.setTextureOffset(52, 58).addBox(1.0F, 2.0F, 0.0F, 5.0F, 12.0F, 0.0F, 0.0F, true);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(root, left_leg, right_leg, body, headwear, head, head2, r_fin, l_fin, right_arm, left_arm, fin, light);
    }

    public void animate(DeeplingPriestServant entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        animator.update(entity);
        animator.setAnimation(DeeplingPriestServant.DEEPLING_MELEE);

        if(entity.isLeftHanded()){
            animator.startKeyframe(4);
            animator.rotate(right_arm, (float) Math.toRadians(12.5F), 0, (float) Math.toRadians(10F));
            animator.rotate(left_arm, 0, 0, (float) Math.toRadians(-75f));
            animator.rotate(body, (float) Math.toRadians(-12.5F), (float) Math.toRadians(-10F), (float) Math.toRadians(12.5F));
            animator.endKeyframe();

            animator.startKeyframe(2);
            animator.rotate(right_arm, (float) Math.toRadians(15F), 0, (float) Math.toRadians(10F));
            animator.rotate(left_arm, (float) Math.toRadians(-107.5f), (float) Math.toRadians(12.5f), (float) Math.toRadians(-77.5f));
            animator.rotate(body, (float) Math.toRadians(30f), (float) Math.toRadians(30F), (float) Math.toRadians(7.5F));
            animator.endKeyframe();

            animator.resetKeyframe(14);
        }else {
            animator.startKeyframe(4);
            animator.rotate(left_arm, (float) Math.toRadians(12.5F), 0, (float) Math.toRadians(-10F));
            animator.rotate(right_arm, 0, 0, (float) Math.toRadians(75f));
            animator.rotate(body, (float) Math.toRadians(-12.5F), (float) Math.toRadians(10F), (float) Math.toRadians(-12.5F));
            animator.endKeyframe();

            animator.startKeyframe(2);
            animator.rotate(left_arm, (float) Math.toRadians(15F), 0, (float) Math.toRadians(-10F));
            animator.rotate(right_arm, (float) Math.toRadians(-107.5f), (float) Math.toRadians(-12.5f), (float) Math.toRadians(77.5f));
            animator.rotate(body, (float) Math.toRadians(30f), (float) Math.toRadians(-30F), (float) Math.toRadians(-7.5F));
            animator.endKeyframe();

            animator.resetKeyframe(14);
        }

        animator.setAnimation(DeeplingPriestServant.DEEPLING_BLIND);

        animator.startKeyframe(5);
        animator.rotate(body, (float) Math.toRadians(37.5F),0,0);
        animator.rotate(head, (float) Math.toRadians(35F),0,0);
        animator.rotate(right_arm, (float) Math.toRadians(-35F),(float) Math.toRadians(12.5F),(float) Math.toRadians(-17.5F));
        animator.rotate(left_arm, (float) Math.toRadians(-35F),(float) Math.toRadians(-12.5F),(float) Math.toRadians(17.5F));
        animator.rotate(fin, (float) Math.toRadians(12.5F),0,0);
        animator.endKeyframe();

        animator.setStaticKeyframe(10);

        animator.startKeyframe(4);
        animator.rotate(body, (float) Math.toRadians(-30F),0,0);
        animator.rotate(head, (float) Math.toRadians(-20F),0,0);
        animator.move(head2, 0,-7,0);
        animator.rotate(right_arm, (float) Math.toRadians(-160F),(float) Math.toRadians(30F),(float) Math.toRadians(-50F));
        animator.rotate(left_arm, (float) Math.toRadians(-160F),(float) Math.toRadians(-30F),(float) Math.toRadians(50F));
        animator.rotate(fin, (float) Math.toRadians(-22.5F),0,0);
        animator.endKeyframe();

        animator.setStaticKeyframe(28);

        animator.resetKeyframe(10);


    }

    @Override
    public void setupAnim(DeeplingPriestServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.faceTarget(netHeadYaw, headPitch, 1, head);

        float partialTick = Minecraft.getInstance().getFrameTime();
        float swim = entity.getSwimAmount(partialTick);
        float walkSpeed = 1F;
        float walkDegree = 1F;
        float swimSpeed = 0.25F;
        float swimDegree = 0.5F;
        float swimAmount = limbSwingAmount * swim;

        this.walk(left_leg, walkSpeed, walkDegree * 1.2F, true, 0F, 0F, limbSwing, limbSwingAmount);
        this.walk(right_leg, walkSpeed, walkDegree * 1.2F, false, 0F, 0F, limbSwing, limbSwingAmount);
        this.walk(left_arm, walkSpeed, walkDegree * 1.2F, false, 0F, 0F, limbSwing, limbSwingAmount);
        this.walk(right_arm, walkSpeed, walkDegree * 1.2F, true, 0F, 0F, limbSwing, limbSwingAmount);

        progressRotationPrev(left_arm,swim,0, 0, (float)Math.toRadians(-37.5F), 10f);
        progressRotationPrev(right_arm,swim,0, 0, (float)Math.toRadians(37.5F), 10f);
        progressRotationPrev(root, swim, (float) Math.toRadians(80), 0, 0, 1F);
        progressRotationPrev(head, swim, (float) Math.toRadians(-70), 0, 0, 1F);
        progressPositionPrev(root, swim, 0, -5, 18, 1F);

        this.flap(root, swimSpeed, swimDegree, true, 0F, 0F, limbSwing, swimAmount);
        this.swing(head, swimSpeed, swimDegree * 1, false, 0.5F, 0F, limbSwing, swimAmount);

        this.flap(left_arm, swimSpeed, swimDegree * 2.75F, true, -0.5F, 1.5F, limbSwing, swimAmount);
        this.swing(left_arm, swimSpeed, swimDegree, true, -1.5F, 0, limbSwing, swimAmount);
        this.walk(left_arm, swimSpeed, swimDegree, true, -2F, -0.2F, limbSwing, swimAmount);
        this.flap(right_arm, swimSpeed, swimDegree * 2.75F, false, -0.5F, 1.5F, limbSwing, swimAmount);
        this.swing(right_arm, swimSpeed, swimDegree, false, -1.5F, 0, limbSwing, swimAmount);
        this.walk(right_arm, swimSpeed, swimDegree, false, -4.5F, -0.2F, limbSwing, swimAmount);

        this.walk(right_leg, swimSpeed * 1.5F, swimDegree, true, 2F, 0.0F, limbSwing, swimAmount);
        this.walk(left_leg, swimSpeed * 1.5F, swimDegree, false, 2F, 0.0F, limbSwing, swimAmount);
        if (this.riding) {
            this.root.rotationPointY += 13;
            this.right_arm.rotateAngleX += (-(float)Math.PI / 5F);
            this.left_arm.rotateAngleX += (-(float)Math.PI / 5F);
            this.right_leg.rotateAngleX = -1.4137167F;
            this.right_leg.rotateAngleY = ((float)Math.PI / 10F);
            this.right_leg.rotateAngleZ = 0.07853982F;
            this.left_leg.rotateAngleX = -1.4137167F;
            this.left_leg.rotateAngleY = (-(float)Math.PI / 10F);
            this.left_leg.rotateAngleZ = -0.07853982F;
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}
