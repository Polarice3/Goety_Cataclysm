package com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr;

import com.Polarice3.goety_cataclysm.common.entities.neutral.AbstractDraugrNecromancer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class DraugrNecromancerServant extends AbstractDraugrNecromancer {
    public DraugrNecromancerServant(EntityType<? extends AbstractDraugrNecromancer> type, Level level) {
        super(type, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new WanderGoal<>(this, 1.0D, 10));
    }
}
