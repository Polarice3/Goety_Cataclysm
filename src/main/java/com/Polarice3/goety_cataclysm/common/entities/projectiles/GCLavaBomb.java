package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.LootingExplosion;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;

public class GCLavaBomb extends ThrowableProjectile {
    private static final EntityDataAccessor<Boolean> ON_GROUND = SynchedEntityData.defineId(GCLavaBomb.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LAVA_TIME = SynchedEntityData.defineId(GCLavaBomb.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_LAVA_TIME = SynchedEntityData.defineId(GCLavaBomb.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> LAVA_POS = SynchedEntityData.defineId(GCLavaBomb.class, EntityDataSerializers.BLOCK_POS);

    public GCLavaBomb(EntityType<GCLavaBomb> type, Level world) {
        super(type, world);
    }

    public GCLavaBomb(EntityType<GCLavaBomb> type, Level world, LivingEntity thrower) {
        super(type, thrower, world);
    }

    protected void defineSynchedData() {
        this.entityData.define(ON_GROUND, false);
        this.entityData.define(LAVA_TIME, 0);
        this.entityData.define(MAX_LAVA_TIME, 200);
        this.entityData.define(LAVA_POS, BlockPos.ZERO);
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.LAVA_BOMB.get().getDescription();
    }

    protected void onHit(HitResult ray) {
        HitResult.Type raytraceresult$type = ray.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)ray);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult)ray);
        }

    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity shooter = this.getOwner();
        if (!this.getGround() && !this.level().isClientSide && !MobUtil.areAllies(result.getEntity(), shooter != null ? shooter : this)) {
            this.playSound(SoundEvents.GENERIC_BURN, 1.5F, 0.75F);
            LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(shooter) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
            ExplosionUtil.lootExplode(this.level(), shooter, this.getX(), this.getY(), this.getZ(), 2.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
            if (GCMobsConfig.NetheriteMonstrositySpawnLava.get()) {
                this.doTerrainEffects();
                this.setGround(true);
            } else {
                this.discard();
            }
        }

    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Entity shooter = this.getOwner();
        if (!this.level().isClientSide() && !this.getGround()) {
            this.playSound(SoundEvents.GENERIC_BURN, 1.5F, 0.75F);
            LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(shooter) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
            ExplosionUtil.lootExplode(this.level(), shooter, this.getX(), this.getY(), this.getZ(), 2.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
            if (GCMobsConfig.NetheriteMonstrositySpawnLava.get()) {
                this.doTerrainEffects();
                this.setGround(true);
            } else {
                this.discard();
            }
        }

    }

    protected void doTerrainEffects() {
        BlockPos landed;
        for(landed = this.blockPosition(); landed.getY() < this.level().getMaxBuildHeight() && (!this.level().getBlockState(landed).isAir() || !this.level().getBlockState(landed).getFluidState().isEmpty() && this.level().getBlockState(landed).getFluidState().getFluidType() != ForgeMod.LAVA_TYPE.get()); landed = landed.above()) {
        }

        this.setLavaPos(landed);
        if (this.level().getBlockState(this.getLavaPos()).isAir()) {
            BlockState fluid = Blocks.LAVA.defaultBlockState();
            this.level().setBlockAndUpdate(this.getLavaPos(), fluid);
        }

    }

    public void tick() {
        super.tick();
        if (this.getGround()) {
            this.setLavaTime(this.getLavaTime() + 1);
            this.setDeltaMovement(Vec3.ZERO);
            if (!this.level().isClientSide && this.getLavaTime() >= this.getMaxLavaTime() && this.getLavaPos() != BlockPos.ZERO) {
                this.discard();
            }
        } else {
            this.makeTrail();
        }

    }

    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
        if (!this.level().isClientSide && this.getLavaPos() != BlockPos.ZERO && this.level().getFluidState(this.getLavaPos()).getFluidType() == ForgeMod.LAVA_TYPE.get()) {
            this.level().setBlockAndUpdate(this.getLavaPos(), Blocks.AIR.defaultBlockState());
        }

    }

    protected void makeTrail() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 5; ++i) {
                double dx = this.getX() + (double)(1.5F * (this.random.nextFloat() - 0.5F));
                double dy = this.getY() + (double)(1.5F * (this.random.nextFloat() - 0.5F));
                double dz = this.getZ() + (double)(1.5F * (this.random.nextFloat() - 0.5F));
                this.level().addParticle(ParticleTypes.FLAME, dx, dy, dz, -this.getDeltaMovement().x(), -this.getDeltaMovement().y(), -this.getDeltaMovement().z());
            }
        }

    }

    public void setLavaPos(BlockPos p_31960_) {
        this.entityData.set(LAVA_POS, p_31960_);
    }

    public BlockPos getLavaPos() {
        return (BlockPos)this.entityData.get(LAVA_POS);
    }

    public boolean getGround() {
        return (Boolean)this.entityData.get(ON_GROUND);
    }

    public void setGround(boolean weapon) {
        this.entityData.set(ON_GROUND, weapon);
    }

    public int getLavaTime() {
        return (Integer)this.entityData.get(LAVA_TIME);
    }

    public void setLavaTime(int time) {
        this.entityData.set(LAVA_TIME, time);
    }

    public int getMaxLavaTime() {
        return (Integer)this.entityData.get(MAX_LAVA_TIME);
    }

    public void setMaxLavaTime(int time) {
        this.entityData.set(MAX_LAVA_TIME, time);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setGround(compound.getBoolean("bomb_ground"));
        this.setLavaTime(compound.getInt("lava_time"));
        this.setMaxLavaTime(compound.getInt("max_lava_time"));
        int i = compound.getInt("LavaPosX");
        int j = compound.getInt("LavaPosY");
        int k = compound.getInt("LavaPosZ");
        this.setLavaPos(new BlockPos(i, j, k));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("LavaPosX", this.getLavaPos().getX());
        compound.putInt("LavaPosY", this.getLavaPos().getY());
        compound.putInt("LavaPosZ", this.getLavaPos().getZ());
        compound.putInt("lava_time", this.getLavaTime());
        compound.putInt("max_lava_time", this.getMaxLavaTime());
        compound.putBoolean("bomb_ground", this.getGround());
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    protected float getGravity() {
        return this.onGround() ? 0.0F : 0.025F;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
