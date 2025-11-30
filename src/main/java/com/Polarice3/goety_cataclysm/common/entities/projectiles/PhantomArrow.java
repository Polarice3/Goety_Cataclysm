package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.entity.projectile.Phantom_Arrow_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class PhantomArrow extends Phantom_Arrow_Entity {

    public PhantomArrow(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public PhantomArrow(EntityType type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    public PhantomArrow(Level worldIn, LivingEntity shooter, LivingEntity finalTarget) {
        super(worldIn, shooter, finalTarget);
    }

    public PhantomArrow(Level worldIn, LivingEntity shooter) {
        super(worldIn, shooter);
    }

    @Override
    public EntityType<?> getType() {
        return GCEntityType.PHANTOM_ARROW.get();
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.PHANTOM_ARROW.get().getDescription();
    }

    @Override
    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity);
            } else {
                if (this.getOwner() instanceof Mob mob) {
                    if (mob.getVehicle() != null && pEntity == mob.getVehicle()) {
                        if (mob.getTarget() != pEntity) {
                            return false;
                        }
                    }
                }
                if (MobUtil.areAllies(this.getOwner(), pEntity)){
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
