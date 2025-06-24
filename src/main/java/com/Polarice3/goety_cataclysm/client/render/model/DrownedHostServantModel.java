package com.Polarice3.goety_cataclysm.client.render.model;

import com.Polarice3.Goety.client.render.model.DrownedServantModel;
import com.Polarice3.Goety.common.entities.ally.undead.zombie.ZombieServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.SymbioctoServant;
import net.minecraft.client.model.geom.ModelPart;

public class DrownedHostServantModel<T extends ZombieServant> extends DrownedServantModel<T> {

    public DrownedHostServantModel(ModelPart p_170534_) {
        super(p_170534_);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (entity.isVehicle() && entity.getFirstPassenger() instanceof SymbioctoServant) {
            this.head.xRot -= (float)Math.toRadians(22.5F);
        }
    }
}
