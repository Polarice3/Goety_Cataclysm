package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.util.CustomExplosion.IgnisExplosion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IgnisFireball extends AbstractIgnisFireball{
    private static final EntityDataAccessor<Boolean> SOUL = SynchedEntityData.defineId(IgnisFireball.class, EntityDataSerializers.BOOLEAN);

    public IgnisFireball(EntityType<? extends IgnisFireball> type, Level level) {
        super(type, level);
    }

    public IgnisFireball(Level level, LivingEntity entity, double x, double y, double z) {
        super(GCEntityType.IGNIS_FIREBALL.get(), entity, x, y, z, level);
    }

    public IgnisFireball(Level worldIn, LivingEntity owner, @Nullable LivingEntity target) {
        this(GCEntityType.IGNIS_FIREBALL.get(), worldIn);
        this.setOwner(owner);
        this.setTarget(target);
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.IGNIS_FIREBALL.get().getDescription();
    }

    public void tick() {
        super.tick();
        if (this.timer == 0 || this.timer == -40) {
            if (this.getTarget() != null) {
                LivingEntity target = this.getTarget();

                float speed = this.isSoul() ? 0.25F : 0.2F;

                double dx = target.getX() - this.getX();
                double dy = target.getY() + target.getBbHeight() * 0.5F - this.getY();
                double dz = target.getZ() - this.getZ();

                double d = Math.sqrt(dx * dx + dy * dy + dz * dz);

                dx /= d;
                dy /= d;
                dz /= d;
                this.xPower = dx * speed;
                this.yPower = dy * speed;
                this.zPower = dz * speed;
            } else {
                List<LivingEntity> list = new ArrayList<>();
                for (Entity entity1 : this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(16.0F))) {
                    LivingEntity livingEntity = null;
                    if (entity1 instanceof PartEntity<?> partEntity && partEntity.getParent() instanceof LivingEntity living){
                        livingEntity = living;
                    } else if (entity1 instanceof LivingEntity living){
                        livingEntity = living;
                    }
                    if (livingEntity != null) {
                        if (MobUtil.ownedPredicate(this).test(livingEntity)){
                            list.add(livingEntity);
                        }
                    }
                }
                list.sort(Comparator.comparingDouble(this::distanceTo));
                if (list.stream().findFirst().isPresent()){
                    LivingEntity livingEntity = list.stream().findFirst().get();
                    this.setTarget(livingEntity);
                }
            }
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity shooter = this.getOwner();
        if (!this.level().isClientSide && getFired()) {
            Entity entity = result.getEntity();
            boolean flag;
            if (shooter instanceof LivingEntity owner) {
                float damage = this.isSoul() ? GCSpellConfig.ExtinctFlameSoulDamage.get().floatValue() : GCSpellConfig.ExtinctFlameDamage.get().floatValue();
                damage *= WandUtil.damageMultiply();
                damage += this.getExtraDamage();
                if (entity instanceof LivingEntity livingEntity) {
                    flag = entity.hurt(damageSources().mobProjectile(this, owner), damage + (livingEntity.getMaxHealth() * 0.07F));
                }else{
                    flag = entity.hurt(damageSources().mobProjectile(this, owner), damage);
                }

                if (flag) {
                    this.doEnchantDamageEffects(owner, entity);
                    if (entity instanceof LivingEntity){
                        owner.heal(5.0F);
                    }

                }
            } else {
                flag = entity.hurt(this.damageSources().magic(), 6.0F + this.getExtraDamage());
            }

            IgnisExplosion explosion = new IgnisExplosion(this.level(), this, null, null, this.getX(), this.getY(), this.getZ(), 1.0F, GCSpellConfig.ExtinctFlameGriefing.get(), Explosion.BlockInteraction.KEEP);
            explosion.explode();
            explosion.finalizeExplosion(this.isSoul() ? 2 : 1, 0.35);
            this.discard();

            if (flag && entity instanceof LivingEntity livingEntity) {
                MobEffectInstance effectinstance1 = livingEntity.getEffect(ModEffect.EFFECTBLAZING_BRAND.get());
                int i = 1;
                if (effectinstance1 != null) {
                    i += effectinstance1.getAmplifier();
                    livingEntity.removeEffectNoUpdate(ModEffect.EFFECTBLAZING_BRAND.get());
                } else {
                    --i;
                }

                i = Mth.clamp(i, 0, 4);
                MobEffectInstance effectinstance = new MobEffectInstance(ModEffect.EFFECTBLAZING_BRAND.get(), 200, i, false, false, true);
                livingEntity.addEffect(effectinstance);

            }

        }
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide && this.getFired()) {
            IgnisExplosion explosion = new IgnisExplosion(this.level(), this, (DamageSource)null, (ExplosionDamageCalculator)null, this.getX(), this.getY(), this.getZ(), 1.0F, GCSpellConfig.ExtinctFlameGriefing.get(), Explosion.BlockInteraction.KEEP);
            explosion.explode();
            explosion.finalizeExplosion(this.isSoul() ? 2 : 1, 0.35);
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult ray) {
        HitResult.Type raytraceresult$type = ray.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) ray);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) ray);
        }
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource p_37616_, float p_37617_) {
        return false;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SOUL, false);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("is_soul", this.isSoul());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSoul(compound.getBoolean("is_soul"));
    }

    public boolean isSoul() {
        return this.entityData.get(SOUL);
    }

    public void setSoul(boolean IsSoul) {
        this.entityData.set(SOUL, IsSoul);
    }

}
