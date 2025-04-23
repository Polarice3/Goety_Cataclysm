package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.entity.projectile.Laser_Beam_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class LaserBeamProjectile extends Laser_Beam_Entity {
    public LaserBeamProjectile(EntityType<? extends Laser_Beam_Entity> type, Level level) {
        super(type, level);
    }

    public LaserBeamProjectile(EntityType<? extends Laser_Beam_Entity> type, double getX, double gety, double getz, double p_36821_, double p_36822_, double p_36823_, Level level) {
        super(type, getX, gety, getz, p_36821_, p_36822_, p_36823_, level);
    }

    public LaserBeamProjectile(LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_, float damage) {
        super(p_36827_, p_36828_, p_36829_, p_36830_, p_36831_, damage);
    }

    public LaserBeamProjectile(EntityType<? extends Laser_Beam_Entity> type, LivingEntity p_36827_, double getX, double gety, double getz, double p_36821_, double p_36822_, double p_36823_, float damage, Level level) {
        super(type, p_36827_, getX, gety, getz, p_36821_, p_36822_, p_36823_, damage, level);
    }

    protected Component getTypeName() {
        return ModEntities.LASER_BEAM.get().getDescription();
    }

    @Override
    public EntityType<?> getType() {
        return GCEntityType.LASER_BEAM.get();
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity);
            } else {
                if(MobUtil.areAllies(this.getOwner(), pEntity)){
                    return false;
                }
                if (this.getOwner() instanceof Enemy && pEntity instanceof Enemy){
                    return false;
                }
                if (pEntity instanceof Projectile projectile && projectile.getOwner() == this.getOwner()){
                    return false;
                }
                if (pEntity instanceof IOwned owned0 && this.getOwner() instanceof IOwned owned1){
                    return !MobUtil.ownerStack(owned0, owned1);
                }
            }
        }
        return super.canHitEntity(pEntity);
    }
}
