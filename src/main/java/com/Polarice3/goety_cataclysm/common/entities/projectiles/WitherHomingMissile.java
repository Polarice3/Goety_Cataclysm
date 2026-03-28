package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.LootingExplosion;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.*;

public class WitherHomingMissile extends Projectile {
    public double accelerationPower;
    @Nullable
    private Entity finalTarget;
    @Nullable
    private UUID targetId;
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(WitherHomingMissile.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(WitherHomingMissile.class, EntityDataSerializers.INT);

    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;

    public WitherHomingMissile(EntityType<? extends WitherHomingMissile> type, Level level) {
        super(type, level);
        this.accelerationPower = 0.1;
    }

    public WitherHomingMissile(EntityType<? extends WitherHomingMissile> type, double getX, double gety, double getz, Vec3 vec3, Level level) {
        this(type, level);
        this.moveTo(getX, gety, getz, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        this.assignDirectionalMovement(vec3, this.accelerationPower);

    }

    public WitherHomingMissile(LivingEntity p_36827_, Vec3 vec3, Level p_36831_,float damage,LivingEntity target) {
        this(GCEntityType.WITHER_HOMING_MISSILE.get(), p_36827_.getX(), p_36827_.getY(), p_36827_.getZ(), vec3, p_36831_);
        this.setOwner(p_36827_);
        this.setDamage(damage);
        this.setRot(p_36827_.getYRot(), p_36827_.getXRot());
        this.setFuse(80);
        this.finalTarget = target;
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.WITHER_HOMING_MISSILE.get().getDescription();
    }

    protected void defineSynchedData() {
        this.entityData.define(DAMAGE, 0.0F);
        this.entityData.define(FUSE, 80);
    }

    public float getDamage() {
        return (Float)this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    public void setFuse(int life) {
        this.entityData.set(FUSE, life);
    }

    public int getFuse() {
        return (Integer)this.entityData.get(FUSE);
    }

    public boolean shouldRenderAtSqrDistance(double p_36837_) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return p_36837_ < d0 * d0;
    }

    public void addAdditionalSaveData(CompoundTag p_37357_) {
        super.addAdditionalSaveData(p_37357_);
        if (this.finalTarget != null) {
            p_37357_.putUUID("Target", this.finalTarget.getUUID());
        }
        p_37357_.putFloat("damage", this.getDamage());
        p_37357_.putDouble("acceleration_power", this.accelerationPower);
        p_37357_.putShort("fuse", (short)this.getFuse());
    }

    public void readAdditionalSaveData(CompoundTag p_37353_) {
        super.readAdditionalSaveData(p_37353_);
        this.setFuse(p_37353_.getShort("fuse"));
        if (p_37353_.hasUUID("Target")) {
            this.targetId = p_37353_.getUUID("Target");
        }

        if (p_37353_.contains("acceleration_power", 6)) {
            this.accelerationPower = p_37353_.getDouble("acceleration_power");
        }
        this.setDamage(p_37353_.getFloat("damage"));
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 1.0F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
                f = 0.8F;
            }
            this.level().addParticle(ParticleTypes.SMOKE, this.getX() - vec3.x, this.getY() - vec3.y + 0.15D, this.getZ() - vec3.z, 0.0D, 0.0D, 0.0D);
            this.setDeltaMovement(vec3.add(vec3.normalize().scale(this.accelerationPower)).scale((double)f));
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
        int f = this.getFuse() - 1;
        this.setFuse(f);
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.finalTarget == null && this.targetId != null) {
                this.finalTarget = serverLevel.getEntity(this.targetId);
                if (this.finalTarget == null) {
                    this.targetId = null;
                }
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
                    this.finalTarget = livingEntity;
                    this.targetId = livingEntity.getUUID();
                }
            }
            LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(this.getOwner()) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
            if (this.finalTarget == null || !this.finalTarget.isAlive() || (this.finalTarget instanceof Player && this.finalTarget.isSpectator())) {
                ExplosionUtil.lootExplode(this.level(), this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
                this.discard();
            } else {
                Vec3 target = new Vec3(finalTarget.getX(),finalTarget.getY(0.5),finalTarget.getZ());
                Vec3 currentVelocity = this.getDeltaMovement();
                Vec3 toTarget = target.subtract(this.position());
                double distance = toTarget.length();

                if (distance > 0.1) {
                    Vec3 desiredDirection = toTarget.normalize();

                    double homingStrength = Mth.clamp((distance - 1.0) / 10.0, 0.1, 1.0);

                    Vec3 newDirection = currentVelocity.normalize().scale(1.0 - homingStrength)
                            .add(desiredDirection.scale(homingStrength))
                            .normalize()
                            .scale(currentVelocity.length());

                    this.assignDirectionalMovement(newDirection, this.getInertia());
                }
            }

            if (f <= 0) {
                ExplosionUtil.lootExplode(this.level(), this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
                this.discard();
            }
        }

        Vec3 trailAt = this.position().add(0, this.getBbHeight() / 2F, 0);
        if (trailPointer == -1) {
            Arrays.fill(trailPositions, trailAt);
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;
    }


    protected void onHitEntity(EntityHitResult p_37626_) {
        super.onHitEntity(p_37626_);
        if (!this.level().isClientSide) {
            Entity entity = p_37626_.getEntity();
            Entity entity1 = this.getOwner();
            boolean flag;
            if (entity1 instanceof LivingEntity livingentity) {
                flag = entity.hurt(this.damageSources().mobProjectile(this, livingentity), this.getDamage());
                if (flag) {
                    if (entity.isAlive()) {
                        this.doEnchantDamageEffects(livingentity, entity);
                    } else {
                        livingentity.heal(5.0F);
                    }
                }
            } else {
                flag = entity.hurt(this.damageSources().magic(), this.getDamage());
            }

            if (flag && entity instanceof LivingEntity livingEntity) {
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }

                if (i > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * i, 1), this.getEffectSource());
                }
            }
            LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(this.getOwner()) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
            ExplosionUtil.lootExplode(this.level(), this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
            this.discard();

        }
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(this.getOwner()) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
            ExplosionUtil.lootExplode(this.level(), this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
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

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity) && !pEntity.noPhysics;
            } else {
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
                    if (MobUtil.ownerStack(owned0, owned1)){
                        return false;
                    }
                }
            }
        }
        return super.canHitEntity(pEntity) && !pEntity.noPhysics;
    }

    protected float getInertia() {
        return 0.6F;
    }

    public boolean isPickable() {
        return false;
    }

    public float getPickRadius() {
        return 1.0F;
    }

    public boolean hurt(DamageSource p_37616_, float p_37617_) {
        return false;
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }

        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale((double)partialTick));
    }

    public boolean hasTrail() {
        return this.trailPointer != -1;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();

        return new ClientboundAddEntityPacket(
                this.getId(),
                this.getUUID(),
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getXRot(),
                this.getYRot(),
                this.getType(),
                i,
                this.getDeltaMovement(),
                0.0D
        );
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Vec3 vec3 = new Vec3(packet.getXa(), packet.getYa(), packet.getZa());
        this.setDeltaMovement(vec3);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    private void assignDirectionalMovement(Vec3 movement, double accelerationPower) {
        this.setDeltaMovement(movement.normalize().scale(accelerationPower));
        this.hasImpulse = true;
    }
}
