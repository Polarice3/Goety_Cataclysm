package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.util.AbyssBlastPortal;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedEntityModel;
import com.github.L_Ender.lionfishapi.client.model.tools.AdvancedModelBox;
import com.github.L_Ender.lionfishapi.client.model.tools.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class AbyssBlastPortalModel extends AdvancedEntityModel<AbyssBlastPortal> {
    private final AdvancedModelBox root;
    private final AdvancedModelBox portal;

    public AbyssBlastPortalModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.root.setTextureOffset(0, 49).addBox(-6.0F, -2.0F, 18.0F, 9.0F, 4.0F, 7.0F, 0.0F, false);
        this.root.setTextureOffset(1, 72).addBox(-3.0F, -1.0F, 16.0F, 7.0F, 2.0F, 6.0F, 0.0F, false);
        this.root.setTextureOffset(1, 72).addBox(-21.0F, -1.0F, 5.0F, 7.0F, 2.0F, 6.0F, 0.0F, false);
        this.root.setTextureOffset(1, 72).addBox(-20.0F, -1.0F, -9.0F, 7.0F, 2.0F, 6.0F, 0.0F, false);
        this.root.setTextureOffset(1, 72).addBox(5.0F, -1.0F, 17.0F, 7.0F, 2.0F, 6.0F, 0.0F, false);
        this.root.setTextureOffset(0, 49).addBox(17.0F, -2.0F, -9.0F, 9.0F, 4.0F, 7.0F, 0.0F, false);
        this.root.setTextureOffset(0, 49).addBox(-24.0F, -2.0F, -2.0F, 9.0F, 4.0F, 7.0F, 0.0F, false);
        this.root.setTextureOffset(1, 72).addBox(15.0F, -1.0F, -12.0F, 7.0F, 2.0F, 6.0F, 0.0F, false);
        this.root.setTextureOffset(46, 55).addBox(13.0F, -1.0F, 15.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        this.root.setTextureOffset(46, 55).addBox(16.0F, -1.0F, -17.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        this.root.setTextureOffset(46, 49).addBox(15.0F, -1.0F, 11.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        this.root.setTextureOffset(46, 49).addBox(-15.0F, -1.0F, 13.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        this.root.setTextureOffset(46, 49).addBox(-21.0F, -1.0F, -13.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        this.root.setTextureOffset(46, 49).addBox(-16.0F, -1.0F, -15.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        this.root.setTextureOffset(46, 49).addBox(20.0F, -1.0F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        this.portal = new AdvancedModelBox(this);
        this.portal.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.portal);
        this.portal.setTextureOffset(-48, 0).addBox(-24.0F, 0.0F, -24.0F, 48.0F, 0.0F, 48.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.portal);
    }

    public void setupAnim(AbyssBlastPortal entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.portal.rotateAngleY -= ageInTicks * 0.1F;
    }

    public void setRotationAngle(AdvancedModelBox modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
