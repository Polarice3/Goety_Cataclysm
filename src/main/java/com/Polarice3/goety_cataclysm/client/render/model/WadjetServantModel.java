package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.WadjetServant;
import com.github.L_Ender.cataclysm.client.animation.Wadjet_Animation;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedEntityModel;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedModelBox;
import com.github.L_Ender.lionfishapi.client.model.tools.BasicModelPart;
import com.github.L_Ender.lionfishapi.client.model.tools.DynamicChain;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class WadjetServantModel extends AdvancedEntityModel<WadjetServant> {
    private final AdvancedModelBox everything;
    private final AdvancedModelBox mid_root;
    private final AdvancedModelBox upper_body1;
    private final AdvancedModelBox pelvis;
    private final AdvancedModelBox upper_body2;
    private final AdvancedModelBox body;
    private final AdvancedModelBox neck1;
    private final AdvancedModelBox neck2;
    private final AdvancedModelBox face;
    private final AdvancedModelBox head;
    private final AdvancedModelBox cube_r1;
    private final AdvancedModelBox cube_r2;
    private final AdvancedModelBox cube_r3;
    private final AdvancedModelBox cube_r4;
    private final AdvancedModelBox jaw;
    private final AdvancedModelBox right_arm;
    private final AdvancedModelBox right_fore_arm;
    private final AdvancedModelBox right_finger3;
    private final AdvancedModelBox right_finger2;
    private final AdvancedModelBox right_finger1;
    private final AdvancedModelBox right_finger4;
    private final AdvancedModelBox wand;
    private final AdvancedModelBox cube_r5;
    private final AdvancedModelBox left_arm;
    private final AdvancedModelBox left_fore_arm;
    private final AdvancedModelBox left_finger3;
    private final AdvancedModelBox left_finger2;
    private final AdvancedModelBox left_finger1;
    private final AdvancedModelBox left_finger4;
    private final AdvancedModelBox tail1;
    private final AdvancedModelBox tail2;
    private final AdvancedModelBox tail3;
    private final AdvancedModelBox tail4;
    private final AdvancedModelBox tail5;
    private final AdvancedModelBox tailend;
    private DynamicChain tail;
    public AdvancedModelBox[] tailOriginal;
    public AdvancedModelBox[] tailDynamic;

    public WadjetServantModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.everything = new AdvancedModelBox(this, "everything");
        this.everything.setRotationPoint(0.0F, 18.1769F, -2.6276F);
        this.mid_root = new AdvancedModelBox(this, "mid_root");
        this.mid_root.setRotationPoint(0.0F, 5.8231F, 2.6276F);
        this.everything.addChild(this.mid_root);
        this.upper_body1 = new AdvancedModelBox(this, "upper_body1");
        this.upper_body1.setRotationPoint(0.0F, -4.8231F, -0.6276F);
        this.mid_root.addChild(this.upper_body1);
        this.setRotationAngle(this.upper_body1, -0.2618F, 0.0F, 0.0F);
        this.upper_body1.setTextureOffset(0, 63).addBox(-5.5F, -17.8375F, -3.68F, 11.0F, 20.0F, 6.0F, 0.0F, false);
        this.pelvis = new AdvancedModelBox(this, "pelvis");
        this.pelvis.setRotationPoint(-0.0798F, -17.8375F, 2.02F);
        this.upper_body1.addChild(this.pelvis);
        this.setRotationAngle(this.pelvis, 0.5716F, 0.0F, 0.0F);
        this.pelvis.setTextureOffset(0, 47).addBox(-4.5076F, -3.0225F, -5.0839F, 9.0F, 4.0F, 6.0F, 0.0F, false);
        this.upper_body2 = new AdvancedModelBox(this, "upper_body2");
        this.upper_body2.setRotationPoint(-0.0076F, -2.9878F, 0.5324F);
        this.pelvis.addChild(this.upper_body2);
        this.setRotationAngle(this.upper_body2, -0.1814F, 0.0F, 0.0F);
        this.upper_body2.setTextureOffset(79, 63).addBox(-8.5403F, -15.7808F, -5.6395F, 17.0F, 7.2F, 8.0F, 0.0F, false);
        this.upper_body2.setTextureOffset(37, 0).addBox(-3.5403F, -13.7808F, -3.6395F, 7.0F, 14.2F, 4.0F, 0.0F, false);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0492F, -6.6808F, 0.7605F);
        this.upper_body2.addChild(this.body);
        this.setRotationAngle(this.body, 0.0429F, 0.0F, 0.0F);
        this.neck1 = new AdvancedModelBox(this, "neck1");
        this.neck1.setRotationPoint(0.0F, -8.5F, 0.0F);
        this.body.addChild(this.neck1);
        this.setRotationAngle(this.neck1, -0.2593F, 0.0F, 0.0F);
        this.neck1.setTextureOffset(112, 79).addBox(-4.3316F, -7.2976F, 0.0584F, 9.0F, 8.0F, 0.0F, 0.0F, false);
        this.neck1.setTextureOffset(0, 0).addBox(-2.3316F, -7.3252F, -3.9267F, 5.0F, 8.0F, 4.0F, 0.0F, false);
        this.neck2 = new AdvancedModelBox(this, "neck2");
        this.neck2.setRotationPoint(0.3579F, -7.4995F, 1.0809F);
        this.neck1.addChild(this.neck2);
        this.setRotationAngle(this.neck2, 0.7854F, 0.0F, 0.0F);
        this.neck2.setTextureOffset(38, 63).addBox(-2.1895F, -8.4892F, -4.7357F, 4.0F, 8.0F, 4.0F, -0.1F, false);
        this.neck2.setTextureOffset(31, 26).addBox(-7.1895F, -7.5444F, -0.8563F, 14.0F, 9.0F, 0.0F, 0.0F, false);
        this.face = new AdvancedModelBox(this, "face");
        this.face.setRotationPoint(-0.0895F, -6.8719F, -1.2524F);
        this.neck2.addChild(this.face);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.face.addChild(this.head);
        this.setRotationAngle(this.head, -0.4363F, 0.0F, 0.0F);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(-1.6F, 8.0116F, -1.3235F);
        this.head.addChild(this.cube_r1);
        this.setRotationAngle(this.cube_r1, 0.1745F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(103, 0).addBox(-1.0F, -9.9F, -3.0F, 5.0F, 3.0F, 6.0F, 0.0F, false);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(1.4F, 1.0116F, -6.3235F);
        this.head.addChild(this.cube_r2);
        this.setRotationAngle(this.cube_r2, 0.7195F, 0.4166F, 0.2315F);
        this.cube_r2.setTextureOffset(31, 47).addBox(-2.2863F, -1.9404F, -0.9425F, 3.0F, 3.0F, 8.0F, 0.0F, false);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(-1.6F, 1.0116F, -6.3235F);
        this.head.addChild(this.cube_r3);
        this.setRotationAngle(this.cube_r3, 0.7195F, -0.4166F, -0.2315F);
        this.cube_r3.setTextureOffset(99, 99).addBox(-0.7137F, -1.9404F, -0.9425F, 3.0F, 3.0F, 8.0F, 0.0F, false);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(-1.1F, 8.0116F, -1.3235F);
        this.head.addChild(this.cube_r4);
        this.setRotationAngle(this.cube_r4, 0.3491F, 0.0F, 0.0F);
        this.cube_r4.setTextureOffset(103, 20).addBox(-1.0F, -10.0F, -4.8F, 4.0F, 3.0F, 5.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setRotationPoint(0.5895F, 0.9632F, -2.4564F);
        this.head.addChild(this.jaw);
        this.setRotationAngle(this.jaw, 0.3927F, 0.0F, 0.0F);
        this.jaw.setTextureOffset(103, 10).addBox(-2.1895F, -1.0797F, -6.0886F, 3.0F, 2.0F, 7.0F, 0.0F, false);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-5.3F, -5.8F, -2.4F);
        this.body.addChild(this.right_arm);
        this.setRotationAngle(this.right_arm, 0.0F, 0.5672F, -1.2654F);
        this.right_arm.setTextureOffset(65, 25).addBox(-9.9464F, -0.9857F, -1.7571F, 11.0F, 4.0F, 4.0F, 0.0F, false);
        this.right_fore_arm = new AdvancedModelBox(this, "right_fore_arm");
        this.right_fore_arm.setRotationPoint(-9.9464F, -0.5213F, 0.1616F);
        this.right_arm.addChild(this.right_fore_arm);
        this.setRotationAngle(this.right_fore_arm, 0.0F, -0.6545F, 0.0F);
        this.right_fore_arm.setTextureOffset(0, 90).addBox(-11.0F, -0.4395F, -1.9186F, 11.0F, 3.0F, 4.0F, 0.0F, false);
        this.right_fore_arm.setTextureOffset(65, 0).addBox(-12.0F, -0.9395F, -2.4186F, 11.0F, 2.0F, 5.0F, 0.0F, false);
        this.right_finger3 = new AdvancedModelBox(this, "right_finger3");
        this.right_finger3.setRotationPoint(-11.0F, 1.1F, -2.3F);
        this.right_fore_arm.addChild(this.right_finger3);
        this.right_finger3.setTextureOffset(0, 35).addBox(-6.0211F, -0.936F, 0.3767F, 6.0F, 2.0F, 0.0F, 0.0F, false);
        this.right_finger2 = new AdvancedModelBox(this, "right_finger2");
        this.right_finger2.setRotationPoint(-11.0F, 1.1F, -0.3F);
        this.right_fore_arm.addChild(this.right_finger2);
        this.right_finger2.setTextureOffset(31, 36).addBox(-6.0211F, -0.936F, 0.3767F, 6.0F, 2.0F, 0.0F, 0.0F, false);
        this.right_finger1 = new AdvancedModelBox(this, "right_finger1");
        this.right_finger1.setRotationPoint(-11.0F, 1.1F, 1.7F);
        this.right_fore_arm.addChild(this.right_finger1);
        this.right_finger1.setTextureOffset(0, 38).addBox(-6.0211F, -0.936F, 0.3767F, 6.0F, 2.0F, 0.0F, 0.0F, false);
        this.right_finger4 = new AdvancedModelBox(this, "right_finger4");
        this.right_finger4.setRotationPoint(-10.0F, 2.5F, -2.7F);
        this.right_fore_arm.addChild(this.right_finger4);
        this.right_finger4.setTextureOffset(0, 16).addBox(-5.0211F, 0.164F, -0.7233F, 6.0F, 0.0F, 2.0F, 0.0F, false);
        this.wand = new AdvancedModelBox(this, "wand");
        this.wand.setRotationPoint(-13.0F, 1.0F, 0.0F);
        this.right_fore_arm.addChild(this.wand);
        this.wand.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -25.0F, 2.0F, 2.0F, 60.0F, 0.0F, false);
        this.wand.setTextureOffset(65, 0).addBox(0.0F, -7.0F, -45.0F, 0.0F, 16.0F, 37.0F, 0.0F, false);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(13.0F, -1.0F, 0.0F);
        this.wand.addChild(this.cube_r5);
        this.setRotationAngle(this.cube_r5, 0.0F, 0.0F, -1.5708F);
        this.cube_r5.setTextureOffset(0, 63).addBox(-2.0F, -20.0F, -45.0F, 0.0F, 16.0F, 37.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(5.121F, -5.8F, -2.4F);
        this.body.addChild(this.left_arm);
        this.setRotationAngle(this.left_arm, 0.0F, -0.2618F, 1.2654F);
        this.left_arm.setTextureOffset(65, 16).addBox(-1.0905F, -0.9857F, -1.8408F, 11.0F, 4.0F, 4.0F, 0.0F, false);
        this.left_fore_arm = new AdvancedModelBox(this, "left_fore_arm");
        this.left_fore_arm.setRotationPoint(9.9095F, -0.5213F, 0.0778F);
        this.left_arm.addChild(this.left_fore_arm);
        this.setRotationAngle(this.left_fore_arm, 0.0F, 0.6545F, 0.0F);
        this.left_fore_arm.setTextureOffset(38, 90).addBox(0.0F, -0.4395F, -1.9186F, 11.0F, 3.0F, 4.0F, 0.0F, false);
        this.left_fore_arm.setTextureOffset(65, 8).addBox(1.0F, -0.9395F, -2.4186F, 11.0F, 2.0F, 5.0F, 0.0F, false);
        this.left_finger3 = new AdvancedModelBox(this, "left_finger3");
        this.left_finger3.setRotationPoint(11.0F, 1.1F, -2.3F);
        this.left_fore_arm.addChild(this.left_finger3);
        this.left_finger3.setTextureOffset(0, 32).addBox(0.0211F, -0.936F, 0.3767F, 6.0F, 2.0F, 0.0F, 0.0F, false);
        this.left_finger2 = new AdvancedModelBox(this, "left_finger2");
        this.left_finger2.setRotationPoint(11.0F, 1.1F, -0.3F);
        this.left_fore_arm.addChild(this.left_finger2);
        this.left_finger2.setTextureOffset(0, 29).addBox(0.0211F, -0.936F, 0.3767F, 6.0F, 2.0F, 0.0F, 0.0F, false);
        this.left_finger1 = new AdvancedModelBox(this, "left_finger1");
        this.left_finger1.setRotationPoint(11.0F, 1.1F, 1.7F);
        this.left_fore_arm.addChild(this.left_finger1);
        this.left_finger1.setTextureOffset(0, 26).addBox(0.0211F, -0.936F, 0.3767F, 6.0F, 2.0F, 0.0F, 0.0F, false);
        this.left_finger4 = new AdvancedModelBox(this, "left_finger4");
        this.left_finger4.setRotationPoint(10.0F, 2.5F, -2.7F);
        this.left_fore_arm.addChild(this.left_finger4);
        this.left_finger4.setTextureOffset(0, 13).addBox(-0.9789F, 0.164F, -0.7233F, 6.0F, 0.0F, 2.0F, 0.0F, false);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setRotationPoint(0.0F, -3.0F, -2.0F);
        this.mid_root.addChild(this.tail1);
        this.tail1.setTextureOffset(38, 63).addBox(-5.0F, -3.0F, 0.0F, 10.0F, 6.0F, 20.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setRotationPoint(0.0F, 0.5F, 18.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(0, 0).addBox(-4.0F, -2.5F, 0.0F, 8.0F, 5.0F, 20.0F, 0.0F, false);
        this.tail3 = new AdvancedModelBox(this, "tail3");
        this.tail3.setRotationPoint(0.0F, 0.5F, 18.0F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(0, 26).addBox(-3.5F, -2.0F, 0.0F, 7.0F, 4.0F, 16.0F, 0.0F, false);
        this.tail4 = new AdvancedModelBox(this, "tail4");
        this.tail4.setRotationPoint(-0.5F, 1.0F, 15.0F);
        this.tail3.addChild(this.tail4);
        this.tail4.setTextureOffset(83, 79).addBox(-2.5F, -2.0F, 0.0F, 6.0F, 3.0F, 16.0F, 0.0F, false);
        this.tail5 = new AdvancedModelBox(this, "tail5");
        this.tail5.setRotationPoint(0.5F, 0.0F, 15.0F);
        this.tail4.addChild(this.tail5);
        this.tail5.setTextureOffset(75, 99).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 2.0F, 15.0F, 0.0F, false);
        this.tailend = new AdvancedModelBox(this, "tailend");
        this.tailend.setRotationPoint(0.0F, 0.0F, 15.0F);
        this.tail5.addChild(this.tailend);
        this.updateDefaultPose();
        this.tailOriginal = new AdvancedModelBox[]{this.tail1, this.tail2, this.tail3, this.tail4, this.tail5, this.tailend};
        this.tailDynamic = new AdvancedModelBox[this.tailOriginal.length];
    }

    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.everything.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if (this.tail != null) {
            this.tail.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha, this.tailDynamic);
        }

        for (AdvancedModelBox modelBox : this.tailOriginal) {
            modelBox.showModel = false;
        }

    }

    public void animate(WadjetServant entity, float f, float f1, float f2, float f3, float f4) {
        this.tail = entity.dc;
        this.resetToDefaultPose();
    }

    public void setupAnim(WadjetServant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float swimSpeed = 0.1F;
        float swimDegree = 0.5F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float attackProgress = entity.getAttackProgress(partialTick);
        float attackAmount = attackProgress * limbSwingAmount * 1.5F;
        this.animateHeadLookTarget(netHeadYaw, headPitch);
        this.animateWalk(Wadjet_Animation.WALK, limbSwing, limbSwingAmount, 1.0F, 1.0F);
        this.progressRotationPrev(this.upper_body1, attackAmount, (float)Math.toRadians(23.159099578857422), 0.0F, 0.0F, 10.0F);
        this.animate(entity.getAnimationState("idle"), Wadjet_Animation.IDLE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("sleep"), Wadjet_Animation.SLEEP, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("awake"), Wadjet_Animation.AWAKE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("charge"), Wadjet_Animation.SPEAR_CHARGE, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("magic"), Wadjet_Animation.MAGIC, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("death"), Wadjet_Animation.DEATH, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("doubleswing"), Wadjet_Animation.DOUBLE_SWING, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("stabnswing"), Wadjet_Animation.STAB_N_SWING, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("block"), Wadjet_Animation.BLOCK, ageInTicks, 1.0F);
        this.chainSwing(this.tailOriginal, swimSpeed * 4.0F, swimDegree * 1.0F, -3.0, limbSwing, limbSwingAmount);
        this.chainSwing(this.tailOriginal, swimSpeed * 0.6F, swimDegree * 0.15F, -3.0, ageInTicks, 1.0F);
        entity.dc.updateChain(Minecraft.getInstance().getFrameTime(), this.tailOriginal, this.tailDynamic, 0.4F, 1.5F, 1.8F, 0.87F, 20, true);
    }

    private void animateHeadLookTarget(float yRot, float xRot) {
        float yawAmount = yRot / 57.295776F;
        float pitchAmount = xRot / 57.295776F;
        this.neck2.rotateAngleX += pitchAmount * 0.5F;
        this.neck2.rotateAngleY += yawAmount * 0.5F;
        this.face.rotateAngleX += pitchAmount * 0.5F;
        this.face.rotateAngleY += yawAmount * 0.5F;
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.everything);
    }

    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.everything, this.upper_body1, this.mid_root, this.pelvis, this.upper_body2, this.body, this.neck1, this.neck2, this.face, this.head, this.cube_r1, this.cube_r2, new AdvancedModelBox[]{this.cube_r3, this.cube_r4, this.jaw, this.right_arm, this.right_fore_arm, this.right_finger3, this.right_finger2, this.right_finger1, this.right_finger4, this.wand, this.cube_r5, this.left_arm, this.left_fore_arm, this.left_finger3, this.left_finger2, this.left_finger1, this.left_finger4, this.tail1, this.tail2, this.tail3, this.tail4, this.tail5, this.tailend});
    }
}
