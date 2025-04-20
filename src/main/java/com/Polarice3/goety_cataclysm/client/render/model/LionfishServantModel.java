package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.LionfishServant;
import com.github.L_Ender.lionfishapi.client.model.Animations.ModelAnimator;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedEntityModel;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedModelBox;
import com.github.L_Ender.lionfishapi.client.model.tools.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class LionfishServantModel extends AdvancedEntityModel<LionfishServant> {
    private final AdvancedModelBox root;
    private final AdvancedModelBox body;
    private final AdvancedModelBox r_fin;
    private final AdvancedModelBox right_long_fin;
    private final AdvancedModelBox left_long_fin;
    private final AdvancedModelBox l_fin;
    private final AdvancedModelBox tail;
    private final AdvancedModelBox tail2;
    private final AdvancedModelBox head;
    private final AdvancedModelBox upper_jaw;
    private final AdvancedModelBox jaw;
    private ModelAnimator animator;

    public LionfishServantModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-2.0F, -6.0F, -6.0F, 4.0F, 8.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(0, 20).addBox(0.0F, 2.0F, -5.0F, 0.0F, 6.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(12, 31).addBox(0.0F, -12.0F, -5.0F, 0.0F, 6.0F, 11.0F, 0.0F, false);
        this.r_fin = new AdvancedModelBox(this);
        this.r_fin.setRotationPoint(-2.0F, -1.0F, -4.0F);
        this.body.addChild(this.r_fin);
        this.setRotationAngle(this.r_fin, 0.0F, 0.4363F, 0.0F);
        this.r_fin.setTextureOffset(24, 31).addBox(-10.0F, -5.0F, 0.0F, 10.0F, 8.0F, 0.0F, 0.0F, true);
        this.right_long_fin = new AdvancedModelBox(this);
        this.right_long_fin.setRotationPoint(-2.0F, 2.0F, -5.0F);
        this.body.addChild(this.right_long_fin);
        this.setRotationAngle(this.right_long_fin, 0.0F, 0.0F, 0.0873F);
        this.right_long_fin.setTextureOffset(19, 8).addBox(0.0F, 0.0F, -1.0F, 0.0F, 10.0F, 12.0F, 0.0F, true);
        this.left_long_fin = new AdvancedModelBox(this);
        this.left_long_fin.setRotationPoint(2.0F, 2.0F, -5.0F);
        this.body.addChild(this.left_long_fin);
        this.setRotationAngle(this.left_long_fin, 0.0F, 0.0F, -0.0873F);
        this.left_long_fin.setTextureOffset(19, 8).addBox(0.0F, 0.0F, -1.0F, 0.0F, 10.0F, 12.0F, 0.0F, false);
        this.l_fin = new AdvancedModelBox(this);
        this.l_fin.setRotationPoint(2.0F, -1.0F, -4.0F);
        this.body.addChild(this.l_fin);
        this.setRotationAngle(this.l_fin, 0.0F, -0.4363F, 0.0F);
        this.l_fin.setTextureOffset(24, 31).addBox(0.0F, -5.0F, 0.0F, 10.0F, 8.0F, 0.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -3.0F, 5.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 20).addBox(0.0F, 2.0F, 0.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);
        this.tail.setTextureOffset(35, 40).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 5.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addBox(0.0F, -5.0F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(32, 0).addBox(0.0F, -5.0F, 0.0F, 0.0F, 9.0F, 7.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -1.0F, -6.0F);
        this.body.addChild(this.head);
        this.upper_jaw = new AdvancedModelBox(this);
        this.upper_jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.upper_jaw);
        this.upper_jaw.setTextureOffset(44, 13).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 4.0F, 4.0F, 0.0F, false);
        this.upper_jaw.setTextureOffset(40, 0).addBox(-1.5F, 1.0F, -4.0F, 3.0F, 0.0F, 4.0F, 0.0F, false);
        this.upper_jaw.setTextureOffset(0, 38).addBox(0.0F, -4.0F, -4.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);
        this.upper_jaw.setTextureOffset(12, 20).addBox(0.0F, -4.0F, -6.0F, 0.0F, 5.0F, 2.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 0.7F, -1.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(44, 22).addBox(-1.5F, -0.5F, -3.5F, 3.0F, 3.0F, 4.0F, 0.025F, false);
        this.jaw.setTextureOffset(20, 0).addBox(-1.5F, 2.5F, -3.5F, 3.0F, 0.0F, 4.0F, 0.0F, false);
        this.jaw.setTextureOffset(20, 5).addBox(-1.5F, -0.5F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    public void animate(LionfishServant entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(LionfishServant.LIONFISH_BITE);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.root, (float)Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.r_fin, 0.0F, (float)Math.toRadians(-22.5), 0.0F);
        this.animator.rotate(this.l_fin, 0.0F, (float)Math.toRadians(22.5), 0.0F);
        this.animator.rotate(this.tail, (float)Math.toRadians(-2.5), 0.0F, 0.0F);
        this.animator.rotate(this.upper_jaw, (float)Math.toRadians(-27.5), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float)Math.toRadians(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, (float)Math.toRadians(-22.5), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.root, (float)Math.toRadians(7.5), 0.0F, 0.0F);
        this.animator.rotate(this.r_fin, 0.0F, (float)Math.toRadians(35.0), 0.0F);
        this.animator.rotate(this.body, (float)Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.l_fin, 0.0F, (float)Math.toRadians(-35.0), 0.0F);
        this.animator.rotate(this.tail, (float)Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.upper_jaw, (float)Math.toRadians(-7.5), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float)Math.toRadians(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, (float)Math.toRadians(17.5), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
        this.animator.setStaticKeyframe(10);
    }

    public void setupAnim(LionfishServant entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float idleSpeed = 0.2F;
        float idleDegree = 0.3F;
        float swimSpeed = 0.55F;
        float swimDegree = 0.7F;
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[]{this.tail, this.tail2};
        float partialTick = ageInTicks - (float)entityIn.tickCount;
        float landProgress = entityIn.prevOnLandProgress + (entityIn.onLandProgress - entityIn.prevOnLandProgress) * partialTick;
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, (float)Math.toRadians(-90.0), 5.0F);
        this.chainSwing(tailBoxes, idleSpeed, idleDegree * 0.1F, 3.0, ageInTicks, 1.0F);
        this.swing(this.r_fin, idleSpeed * 0.5F, idleDegree * 0.4F, true, 0.0F, -0.12F, ageInTicks, 1.0F);
        this.swing(this.l_fin, idleSpeed * 0.5F, idleDegree * 0.4F, false, 0.0F, -0.12F, ageInTicks, 1.0F);
        this.walk(this.right_long_fin, idleSpeed * 0.5F, idleDegree * 0.2F, true, 0.0F, -0.06F, ageInTicks, 1.0F);
        this.walk(this.left_long_fin, idleSpeed * 0.5F, idleDegree * 0.2F, true, 0.0F, -0.06F, ageInTicks, 1.0F);
        this.swing(this.r_fin, swimSpeed * 0.5F, swimDegree * 0.4F, true, 0.0F, -0.28F, limbSwing, limbSwingAmount);
        this.swing(this.l_fin, swimSpeed * 0.5F, swimDegree * 0.4F, false, 0.0F, -0.28F, limbSwing, limbSwingAmount);
        this.walk(this.right_long_fin, swimSpeed * 0.5F, swimDegree * 0.2F, true, 0.0F, -0.14F, limbSwing, limbSwingAmount);
        this.walk(this.left_long_fin, swimSpeed * 0.5F, swimDegree * 0.2F, true, 0.0F, -0.14F, limbSwing, limbSwingAmount);
        this.chainSwing(tailBoxes, swimSpeed, swimDegree, -2.0, limbSwing, limbSwingAmount);
        this.body.rotateAngleX += headPitch * 0.017453292F;
        this.head.rotateAngleX -= headPitch * 0.5F * 0.017453292F;
    }

    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.tail2, this.l_fin, this.left_long_fin, this.right_long_fin, this.upper_jaw, this.jaw, this.head, this.r_fin);
    }

    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}
