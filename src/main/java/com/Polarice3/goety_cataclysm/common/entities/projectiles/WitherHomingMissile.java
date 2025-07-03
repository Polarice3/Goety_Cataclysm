package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.LootingExplosion;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class WitherHomingMissile extends Projectile {
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(WitherHomingMissile.class, EntityDataSerializers.FLOAT);
    public double xPower;
    public double yPower;
    public double zPower;
    @Nullable
    private Entity finalTarget;
    @Nullable
    private UUID targetId;

    public WitherHomingMissile(EntityType<? extends WitherHomingMissile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public WitherHomingMissile(Level worldIn, LivingEntity entity) {
        this(GCEntityType.WITHER_HOMING_MISSILE.get(), worldIn);
        this.setOwner(entity);
    }

    public WitherHomingMissile(EntityType<? extends WitherHomingMissile> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        this(p_36817_, p_36824_);
        this.moveTo(p_36818_, p_36819_, p_36820_, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        double d0 = Math.sqrt(p_36821_ * p_36821_ + p_36822_ * p_36822_ + p_36823_ * p_36823_);
        if (d0 != 0.0D) {
            this.xPower = p_36821_ / d0 * 0.1D;
            this.yPower = p_36822_ / d0 * 0.1D;
            this.zPower = p_36823_ / d0 * 0.1D;
        }
    }

    public WitherHomingMissile(LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_, LivingEntity finalTarget) {
        this(GCEntityType.WITHER_HOMING_MISSILE.get(), p_36827_.getX(), p_36827_.getY(), p_36827_.getZ(), p_36828_, p_36829_, p_36830_, p_36831_);
        this.setOwner(p_36827_);
        this.finalTarget = finalTarget;
        this.setRot(p_36827_.getYRot(), p_36827_.getXRot());
    }

    public WitherHomingMissile(Level worldIn, LivingEntity entity, LivingEntity finalTarget) {
        this(GCEntityType.WITHER_HOMING_MISSILE.get(), worldIn);
        this.setOwner(entity);
        this.finalTarget = finalTarget;
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.WITHER_HOMING_MISSILE.get().getDescription();
    }

    protected void defineSynchedData() {
        this.entityData.define(DAMAGE, (float) CMConfig.WitherHomingMissiledamage);
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
        p_37357_.put("power", this.newDoubleList(new double[]{this.xPower, this.yPower, this.zPower}));
    }

    public void readAdditionalSaveData(CompoundTag p_37353_) {
        super.readAdditionalSaveData(p_37353_);
        if (p_37353_.hasUUID("Target")) {
            this.targetId = p_37353_.getUUID("Target");
        }
        if (p_37353_.contains("power", 9)) {
            ListTag listtag = p_37353_.getList("power", 6);
            if (listtag.size() == 3) {
                this.xPower = listtag.getDouble(0);
                this.yPower = listtag.getDouble(1);
                this.zPower = listtag.getDouble(2);
            }
        }
        this.setDamage(p_37353_.getFloat("damage"));

    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
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
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
                f = 0.8F;
            }
            this.level().addParticle(ParticleTypes.SMOKE, this.getX() - vec3.x, this.getY() - vec3.y + 0.15D, this.getZ() - vec3.z, 0.0D, 0.0D, 0.0D);
            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
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

            if (this.finalTarget == null || !this.finalTarget.isAlive() || (this.finalTarget instanceof Player && this.finalTarget.isSpectator())) {
                this.yPower = -0.175;
            } else {
                double d = this.distanceToSqr(this.finalTarget);
                double dx = this.finalTarget.getX() - this.getX();
                double dy = this.finalTarget.getY() + this.finalTarget.getBbHeight() * 1.2F - this.getY();
                double dz = this.finalTarget.getZ() - this.getZ();
                double d13 = 3;
                dx /= d;
                dy /= d;
                dz /= d;
                this.xPower += dx * d13;
                this.yPower += dy * d13;
                this.zPower += dz * d13;
                this.xPower = (double) Mth.clamp((float) this.xPower, -0.175, 0.175);
                this.yPower = (double) Mth.clamp((float) this.yPower, -0.175, 0.175);
                this.zPower = (double) Mth.clamp((float) this.zPower, -0.175, 0.175);
            }
        }

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
                int i = 5;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }

                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 5 * i, 0), this.getEffectSource());
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

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();
        return new ClientboundAddEntityPacket(this.getId(), this.getUUID(), this.getX(), this.getY(), this.getZ(), this.getXRot(), this.getYRot(), this.getType(), i, new Vec3(this.xPower, this.yPower, this.zPower), 0.0D);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_150128_) {
        super.recreateFromPacket(p_150128_);
        double d0 = p_150128_.getXa();
        double d1 = p_150128_.getYa();
        double d2 = p_150128_.getZa();
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        if (d3 != 0.0D) {
            this.xPower = d0 / d3 * 0.1D;
            this.yPower = d1 / d3 * 0.1D;
            this.zPower = d2 / d3 * 0.1D;
        }

    }
    protected boolean shouldBurn() {
        return false;
    }
}
