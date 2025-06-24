package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.projectiles.StormSerpent;
import com.github.L_Ender.cataclysm.client.animation.Storm_Serpent_Animation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class StormSerpentModel extends HierarchicalModel<StormSerpent> {
    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart seg6;
    private final ModelPart seg5;
    private final ModelPart seg4;
    private final ModelPart seg3;
    private final ModelPart seg2;
    private final ModelPart seg1;
    private final ModelPart head;
    private final ModelPart upper;
    private final ModelPart teeth;
    private final ModelPart lower;
    private final ModelPart teeth2;

    public StormSerpentModel(ModelPart root) {
        this.root = root;
        this.everything = this.root.getChild("everything");
        this.seg6 = this.everything.getChild("seg6");
        this.seg5 = this.seg6.getChild("seg5");
        this.seg4 = this.seg5.getChild("seg4");
        this.seg3 = this.seg4.getChild("seg3");
        this.seg2 = this.seg3.getChild("seg2");
        this.seg1 = this.seg2.getChild("seg1");
        this.head = this.seg1.getChild("head");
        this.upper = this.head.getChild("upper");
        this.teeth = this.upper.getChild("teeth");
        this.lower = this.head.getChild("lower");
        this.teeth2 = this.lower.getChild("teeth2");
    }

    @Override
    public void setupAnim(StormSerpent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.root.visible = entity.getState() != 0;
        this.animate(entity.getAnimationState("spawn"), Storm_Serpent_Animation.STORM_SERPENT, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("spawn2"), Storm_Serpent_Animation.STORM_SERPENT2, ageInTicks, 1.0F);
        this.root.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.root.xRot = headPitch * ((float) Math.PI / 180F);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
