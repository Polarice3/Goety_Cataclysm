package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.entity.projectile.Octo_Ink_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class OctoInk extends Octo_Ink_Entity {
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(OctoInk.class, EntityDataSerializers.FLOAT);

    public OctoInk(EntityType<? extends Octo_Ink_Entity> entityType, Level level) {
        super(entityType, level);
    }

    public OctoInk(Level level, LivingEntity spitter) {
        super(level, spitter);
    }

    @Override
    public EntityType<?> getType() {
        return GCEntityType.OCTO_INK.get();
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.OCTO_INK.get().getDescription();
    }

    protected void defineSynchedData() {
        this.entityData.define(DAMAGE, 3.0F);
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    protected void onHitEntity(EntityHitResult p_37241_) {
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity livingentity) {
            p_37241_.getEntity().hurt(this.damageSources().mobProjectile(this, livingentity), this.getDamage());
        }
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
