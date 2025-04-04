package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.LootingExplosion;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.github.L_Ender.cataclysm.client.particle.LightTrailParticle;
import com.github.L_Ender.cataclysm.client.particle.LightningParticle;
import com.github.L_Ender.cataclysm.init.ModEffect;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
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

public class AbyssOrb extends Projectile {
    private static final EntityDataAccessor<Boolean> TRACKING = SynchedEntityData.defineId(AbyssOrb.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AbyssOrb.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AbyssOrb.class, EntityDataSerializers.FLOAT);
    public double xPower;
    public double yPower;
    public double zPower;
    @Nullable
    private Entity finalTarget;
    @Nullable
    private UUID targetId;
    private int timer;
    private int lifetick;

    public AbyssOrb(EntityType<? extends AbyssOrb> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public AbyssOrb(Level worldIn, LivingEntity entity) {
        this(GCEntityType.ABYSS_ORB.get(), worldIn);
        this.setOwner(entity);
    }

    public AbyssOrb(EntityType<? extends AbyssOrb> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
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

    public AbyssOrb(LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_,float damage, LivingEntity finalTarget) {
        this(GCEntityType.ABYSS_ORB.get(), p_36827_.getX(), p_36827_.getY(), p_36827_.getZ(), p_36828_, p_36829_, p_36830_, p_36831_);
        this.setOwner(p_36827_);
        this.setDamage(damage);
        this.finalTarget = finalTarget;
        this.setRot(p_36827_.getYRot(), p_36827_.getXRot());
    }

    public AbyssOrb(Level worldIn, LivingEntity entity, LivingEntity finalTarget) {
        this(GCEntityType.ABYSS_ORB.get(), worldIn);
        this.setOwner(entity);
        this.finalTarget = finalTarget;
    }

    protected Component getTypeName() {
        return ModEntities.ABYSS_ORB.get().getDescription();
    }

    protected void defineSynchedData() {
        this.entityData.define(TRACKING, false);
        this.entityData.define(DAMAGE, 0.0F);
        this.entityData.define(RADIUS, 1.0F);
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(float damage) {
        this.entityData.set(RADIUS, damage);
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
        p_37357_.put("power", this.newDoubleList(this.xPower, this.yPower, this.zPower));
        p_37357_.putInt("timer", this.timer);
        p_37357_.putFloat("damage", this.getDamage());
        p_37357_.putFloat("radius", this.getRadius());
        p_37357_.putBoolean("tracking", getTracking());
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
        this.timer = p_37353_.getInt("timer");
        this.setTracking(p_37353_.getBoolean("fired"));
        this.setDamage(p_37353_.getFloat("damage"));
        this.setRadius(p_37353_.getFloat("radius"));
    }

    public void setTracking(boolean tracking) {
        this.entityData.set(TRACKING, tracking);
    }

    public boolean getTracking() {
        return this.entityData.get(TRACKING);
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
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * f1, d1 - vec3.y * f1, d2 - vec3.z * f1, vec3.x, vec3.y, vec3.z);
                }
                f = 0.8F;
            }
            this.level().addParticle(ParticleTypes.REVERSE_PORTAL, this.getX() - vec3.x, this.getY() - vec3.y + 0.15D, this.getZ() - vec3.z, 0.0D, 0.0D, 0.0D);
            this.level().addParticle((new LightningParticle.OrbData(102, 26, 204)), d0 - vec3.x * 0.5D + this.random.nextDouble() * 0.6D - 0.3D, d1 - vec3.y * 0.5D, d2 - vec3.z * 0.5D + this.random.nextDouble() * 0.6D - 0.3D, vec3.x, vec3.y, vec3.z);
            double dx = getX() + 1.5F * (random.nextFloat() - 0.5F);
            double dy = getY() + 1.5F * (random.nextFloat() - 0.5F);
            double dz = getZ() + 1.5F * (random.nextFloat() - 0.5F);
            float r = 102/255F;
            float g = 26/255F;
            float b = 204/255F;

            this.level().addParticle((new LightTrailParticle.OrbData(r, g, b,0.1F,this.getBbHeight()/2,this.getId())),  dx, dy, dz, 0, 0, 0);

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
        if (!this.level().isClientSide) {
            this.timer--;
            this.lifetick++;
            if (this.timer <= 0) {
                if (!this.getTracking()) {
                    this.setTracking(true);
                }
            }
            if (this.lifetick >= 400) {
                if (this.getTracking()) {
                    this.setTracking(false);
                }
            }
            if (this.lifetick >= 500) {
                LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(this.getOwner()) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
                ExplosionUtil.lootExplode(this.level(), entity, this.getX(), this.getY(), this.getZ(), this.getRadius(), false, Explosion.BlockInteraction.KEEP, lootMode);
                this.discard();
            }
            if (this.finalTarget == null || !this.finalTarget.isAlive()) {
                if (this.targetId != null) {
                    this.finalTarget = ((ServerLevel) this.level()).getEntity(this.targetId);
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
            }
            if (this.getTracking()) {
                if (this.finalTarget == null || !this.finalTarget.isAlive() || (this.finalTarget instanceof Player && this.finalTarget.isSpectator())) {
                    this.yPower = -0.05;
                } else {
                    double d = this.distanceToSqr(finalTarget);
                    double dx = finalTarget.getX() - this.getX();
                    double dy = finalTarget.getY() + finalTarget.getBbHeight() * 1.2F - this.getY();
                    double dz = finalTarget.getZ() - this.getZ();
                    double d13 = 2;
                    dx /= d;
                    dy /= d;
                    dz /= d;
                    this.xPower += dx * d13;
                    this.yPower += dy * d13;
                    this.zPower += dz * d13;
                    this.xPower = Mth.clamp(this.xPower, -0.05D, 0.05D);
                    this.yPower = Mth.clamp(this.yPower, -0.05D, 0.05D);
                    this.zPower = Mth.clamp(this.zPower, -0.05D, 0.05D);
                }
            }
        }

    }

    public void setUp(int delay) {
        this.setTracking(false);
        this.timer = delay;
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity1 = this.getOwner();
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            boolean flag;
            if (entity1 instanceof LivingEntity livingentity) {
                flag = entity.hurt(damageSources().mobProjectile(this, livingentity), this.getDamage());
            } else {
                flag = entity.hurt(damageSources().magic(), this.getDamage());
            }

            if (flag && entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(ModEffect.EFFECTABYSSAL_FEAR.get(), 100, 0), this.getEffectSource());
            }
            LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(entity1) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
            ExplosionUtil.lootExplode(this.level(), entity1, this.getX(), this.getY(), this.getZ(), this.getRadius(), false, Explosion.BlockInteraction.KEEP, lootMode);
            this.discard();

        }
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Entity entity1 = this.getOwner();
        LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(entity1) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
        if (this.getTracking()) {
            if (this.lifetick >= 200) {
                if (!this.level().isClientSide) {
                    ExplosionUtil.lootExplode(this.level(), entity1, this.getX(), this.getY(), this.getZ(), this.getRadius(), false, Explosion.BlockInteraction.KEEP, lootMode);
                    this.discard();
                }
            }
        } else {
            if (this.lifetick >= 400) {
                if (!this.level().isClientSide) {
                    ExplosionUtil.lootExplode(this.level(), entity1, this.getX(), this.getY(), this.getZ(), this.getRadius(), false, Explosion.BlockInteraction.KEEP, lootMode);
                    this.discard();
                }
            }
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
                return super.canHitEntity(pEntity);
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
                    return !MobUtil.ownerStack(owned0, owned1);
                }
            }
        }
        return super.canHitEntity(pEntity);
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
}
