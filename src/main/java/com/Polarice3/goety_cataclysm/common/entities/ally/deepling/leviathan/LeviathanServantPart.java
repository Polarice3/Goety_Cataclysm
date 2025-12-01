package com.Polarice3.goety_cataclysm.common.entities.ally.deepling.leviathan;

import com.Polarice3.goety_cataclysm.common.entities.ally.OwnedCMPart;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.gameevent.GameEvent;

public class LeviathanServantPart extends OwnedCMPart<LeviathanServant> {

    private final EntityDimensions size;
    public float scale = 1;

    public LeviathanServantPart(LeviathanServant parent, float sizeX, float sizeY) {
        super(parent);
        this.size = EntityDimensions.scalable(sizeX, sizeY);
        this.refreshDimensions();
    }

    public LeviathanServantPart(LeviathanServant entityCachalotWhale, float sizeX, float sizeY, EntityDimensions size) {
        super(entityCachalotWhale);
        this.size = size;
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    protected void setSize(EntityDimensions size) {
        super.setSize(size);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = this.getParent() != null && this.getParent().attackEntityFromPart(this, source, amount);
        if (flag) {
            this.gameEvent(GameEvent.ENTITY_DAMAGE);
        }
        return flag;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.size;
    }

}
