package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.goety_cataclysm.common.entities.projectiles.FlareBomb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.phys.Vec3;

public class FlareBombModel extends HierarchicalModel<FlareBomb> {
    private final ModelPart root;
    private final ModelPart outer;
    private final ModelPart inner;

    public FlareBombModel(ModelPart root) {
        this.root = root.getChild("root");
        this.outer = this.root.getChild("outer");
        this.inner = this.root.getChild("inner");
    }

    public void setupAnim(FlareBomb entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float delta = ageInTicks - (float)entity.tickCount;
        Vec3 prevV = new Vec3(entity.prevDeltaMovementX, entity.prevDeltaMovementY, entity.prevDeltaMovementZ);
        Vec3 dv = prevV.add(entity.getDeltaMovement().subtract(prevV).scale((double)delta));
        double d = Math.sqrt(dv.x * dv.x + dv.y * dv.y + dv.z * dv.z);
        if (d != 0.0) {
            double a = dv.y / d;
            a = Math.max(-10.0, Math.min(1.0, a));
            float pitch = -((float)Math.asin(a));
            this.root.xRot = pitch + 1.5707964F;
        }

        this.inner.yRot = ageInTicks * 20.0F * 0.017453292F;
        this.inner.xRot = ageInTicks * 20.0F * 0.017453292F;
        this.inner.zRot = ageInTicks * 20.0F * 0.017453292F;
        this.outer.yRot = ageInTicks * -10.0F * 0.017453292F;
        this.outer.xRot = ageInTicks * -10.0F * 0.017453292F;
        this.outer.zRot = ageInTicks * -10.0F * 0.017453292F;
    }

    public ModelPart root() {
        return this.root;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
