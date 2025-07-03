package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.util.CustomExplosion.IgnisExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IgnisAbyssFireball extends AbstractIgnisFireball{
    private static final EntityDataAccessor<Integer> BOUNCES = SynchedEntityData.defineId(IgnisAbyssFireball.class, EntityDataSerializers.INT);

    public IgnisAbyssFireball(EntityType<? extends IgnisAbyssFireball> type, Level level) {
        super(type, level);
    }

    public IgnisAbyssFireball(Level level, LivingEntity entity, double x, double y, double z) {
        super(GCEntityType.IGNIS_ABYSS_FIREBALL.get(), entity, x, y, z, level);
    }

    public IgnisAbyssFireball(Level worldIn, LivingEntity owner, @Nullable LivingEntity target) {
        this(GCEntityType.IGNIS_ABYSS_FIREBALL.get(), worldIn);
        this.setOwner(owner);
        this.setTarget(target);
    }

    @Override
    public void defineRadius() {
        this.entityData.define(RADIUS, 2.0F);
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.IGNIS_ABYSS_FIREBALL.get().getDescription();
    }

    public void tick() {
        super.tick();
        if (this.timer == 0 || this.timer == -40) {
            if(this.getTotalBounces() == 0) {
                if (this.getTarget() != null) {
                    LivingEntity target = this.getTarget();

                    float speed =  0.2F;

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
    }

    protected void onHitEntity(EntityHitResult p_37626_) {
        super.onHitEntity(p_37626_);
        Entity shooter = this.getOwner();
        if (!this.level().isClientSide && getFired()) {
            Entity entity = p_37626_.getEntity();
            boolean flag;
            if (shooter instanceof LivingEntity owner) {
                if (entity instanceof LivingEntity livingEntity) {
                    flag = entity.hurt(damageSources().mobProjectile(this, owner), (10.0F + this.getExtraDamage()) + (livingEntity.getMaxHealth() * 0.2F));
                }else{
                    flag = entity.hurt(damageSources().mobProjectile(this, owner), 10.0F + this.getExtraDamage());
                }
                if (flag) {
                    this.doEnchantDamageEffects(owner, entity);
                    if (entity instanceof LivingEntity) {
                        owner.heal(5.0F);
                    }
                }
            } else {
                flag = entity.hurt(this.damageSources().magic(), 5.0F + this.getExtraDamage());
            }
            IgnisExplosion explosion = new IgnisExplosion(this.level(), this, (DamageSource)null, (ExplosionDamageCalculator)null, this.getX(), this.getY(), this.getZ(), 2.0F, true, Explosion.BlockInteraction.KEEP);
            explosion.explode();
            explosion.finalizeExplosion(3, 0.75);
            this.discard();

            if (flag && entity instanceof LivingEntity livingEntity) {
                MobEffectInstance effectinstance1 = livingEntity.getEffect(ModEffect.EFFECTBLAZING_BRAND.get());
                int i = 2;
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
        BlockState blockstate = this.level().getBlockState(result.getBlockPos());
        if (!blockstate.getCollisionShape(this.level(), result.getBlockPos()).isEmpty() && getFired()) {
            Direction face = result.getDirection();
            blockstate.onProjectileHit(this.level(), blockstate, result, this);

            Vec3 motion = this.getDeltaMovement();

            double motionX = motion.x();
            double motionY = motion.y();
            double motionZ = motion.z();

            if (face == Direction.EAST)
                motionX = -motionX;
            else if (face == Direction.SOUTH)
                motionZ = -motionZ;
            else if (face == Direction.WEST)
                motionX = -motionX;
            else if (face == Direction.NORTH)
                motionZ = -motionZ;
            else if (face == Direction.UP)
                motionY = -motionY;
            else if (face == Direction.DOWN)
                motionY = -motionY;

            this.setDeltaMovement(motionX, motionY, motionZ);
            this.xPower = motionX * 0.05D;
            this.yPower = motionY * 0.05D;
            this.zPower = motionZ * 0.05D;

            if (this.tickCount <= 500 && this.getTotalBounces() <= 5) {
                this.setTotalBounces(this.getTotalBounces() + 1);
            } else if (!this.level().isClientSide) {
                IgnisExplosion explosion = new IgnisExplosion(this.level(), this, (DamageSource)null, (ExplosionDamageCalculator)null, this.getX(), this.getY(), this.getZ(), 2.0F, true, Explosion.BlockInteraction.KEEP);
                explosion.explode();
                explosion.finalizeExplosion(3, 0.5);
                this.discard();
            }
        }

    }

    @Override
    protected void onHit(HitResult ray) {
        HitResult.Type hitresult$type = ray.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)ray);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, ray.getLocation(), GameEvent.Context.of(this, (BlockState)null));
        } else if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)ray;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOUNCES, 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("totalBounces", this.getTotalBounces());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTotalBounces(compound.getInt("totalBounces"));
    }

    public int getTotalBounces() {
        return this.entityData.get(BOUNCES);
    }

    public void setTotalBounces(int bounces) {
        this.entityData.set(BOUNCES, bounces);
    }

    @Override
    public boolean hurt(DamageSource p_36839_, float p_36840_) {
        if (this.isInvulnerableTo(p_36839_)) {
            return false;
        } else {
            this.markHurt();
            Entity entity = p_36839_.getEntity();
            if (entity != null && this.getFired()) {
                if (!this.level().isClientSide) {
                    Vec3 vec3 = entity.getLookAngle();
                    this.setDeltaMovement(vec3);
                    this.xPower = vec3.x * 0.1D;
                    this.yPower = vec3.y * 0.1D;
                    this.zPower = vec3.z * 0.1D;
                    this.setOwner(entity);
                }

                return true;
            } else {
                return false;
            }
        }
    }
}
