package com.Polarice3.goety_cataclysm.common.entities.util;

import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class AbyssMark extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> CREATOR_ID = SynchedEntityData.defineId(AbyssMark.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(AbyssMark.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AbyssMark.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HP_DAMAGE = SynchedEntityData.defineId(AbyssMark.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> STAFF = SynchedEntityData.defineId(AbyssMark.class, EntityDataSerializers.BOOLEAN);
    private int potency;
    @Nullable
    private Entity finalTarget;
    @Nullable
    private UUID targetId;

    public AbyssMark(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public AbyssMark(Level worldIn, double x, double y, double z, int lifespan, float damage, float hpdamage, UUID casterIn, @Nullable LivingEntity finalTarget) {
        this(GCEntityType.ABYSS_MARK.get(), worldIn);
        this.setCreatorEntityUUID(casterIn);
        this.setLifespan(lifespan);
        this.setDamage(damage);
        this.setHpDamage(hpdamage);
        this.finalTarget = finalTarget;
        this.setPos(x, y, z);
    }

    public AbyssMark(Level worldIn, Vec3 vec3, int lifespan, float damage, float hpdamage, UUID casterIn, @Nullable LivingEntity finalTarget, boolean staff, int potency) {
        this(GCEntityType.ABYSS_MARK.get(), worldIn);
        this.setCreatorEntityUUID(casterIn);
        this.setLifespan(lifespan);
        this.setDamage(damage);
        this.setHpDamage(hpdamage);
        this.finalTarget = finalTarget;
        this.setPos(vec3.x, vec3.y, vec3.z);
        this.setStaff(staff);
        this.potency = potency;
    }

    protected Component getTypeName() {
        return ModEntities.ABYSS_MARK.get().getDescription();
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setLifespan(compound.getInt("Lifespan"));
        if (compound.hasUUID("Owner")) {
            this.setCreatorEntityUUID(compound.getUUID("Owner"));
        }
        if (compound.hasUUID("Target")) {
            this.targetId = compound.getUUID("Target");
        }
        if (compound.contains("Potency")) {
            this.potency = compound.getInt("Potency");
        }
        if (compound.contains("Damage")) {
            this.setDamage(compound.getFloat("Damage"));
        }
        if (compound.contains("HpDamage")) {
            this.setHpDamage(compound.getFloat("HpDamage"));
        }
        if (compound.contains("Staff")) {
            this.setStaff(compound.getBoolean("Staff"));
        }
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Lifespan", this.getLifespan());
        if (this.getCreatorEntityUUID() != null) {
            compound.putUUID("Owner", this.getCreatorEntityUUID());
        }
        if (this.finalTarget != null) {
            compound.putUUID("Target", this.finalTarget.getUUID());
        }
        compound.putInt("Potency", this.potency);
        compound.putFloat("Damage", this.getDamage());
        compound.putFloat("HpDamage", this.getHpDamage());
        compound.putBoolean("Staff", this.isStaff());
    }

    protected void defineSynchedData() {
        this.entityData.define(CREATOR_ID, Optional.empty());
        this.entityData.define(LIFESPAN, 300);
        this.entityData.define(DAMAGE, 0.0F);
        this.entityData.define(HP_DAMAGE, 0.0F);
        this.entityData.define(STAFF, false);
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    public float getHpDamage() {
        return this.entityData.get(HP_DAMAGE);
    }

    public void setHpDamage(float damage) {
        this.entityData.set(HP_DAMAGE, damage);
    }

    public boolean isStaff() {
        return this.entityData.get(STAFF);
    }

    public void setStaff(boolean staff) {
        this.entityData.set(STAFF, staff);
    }

    public void tick() {
        super.tick();
        this.updateMotion();
        Entity owner = this.getCreatorEntity();
        if (owner != null && !owner.isAlive()) {
            this.discard();
        }
        if (owner instanceof Player player) {
            if (!MobUtil.isSpellCasting(player)) {
                this.discard();
            }
        }

        this.setLifespan(this.getLifespan() - 1);
        if (!this.level().isClientSide) {
            if (this.finalTarget == null || !this.finalTarget.isAlive()) {
                if (this.targetId != null) {
                    this.finalTarget = ((ServerLevel) this.level()).getEntity(this.targetId);
                    if (this.finalTarget == null) {
                        this.targetId = null;
                    }
                } else {
                    for (Entity entity1 : this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(16.0F))) {
                        LivingEntity livingEntity = null;
                        if (entity1 instanceof PartEntity<?> partEntity && partEntity.getParent() instanceof LivingEntity living){
                            livingEntity = living;
                        } else if (entity1 instanceof LivingEntity living){
                            livingEntity = living;
                        }
                        if (livingEntity != null) {
                            if (MobUtil.ownedPredicate(this).test(livingEntity)){
                                this.finalTarget = livingEntity;
                                this.targetId = livingEntity.getUUID();
                            }
                        }
                    }
                }
            }
        }

        if (this.getLifespan() <= 0) {
            if (owner instanceof LivingEntity livingEntity) {
                this.level().addFreshEntity(new AbyssBlastPortal(this.level(), this.getX(), this.getY(), this.getZ(), this.getYRot(), 0, this.getDamage(), this.getHpDamage(), livingEntity));
                if (this.isStaff()){
                    Vec3 rayTrace = this.getViewVector(1.0F);
                    double d0 = Math.min(rayTrace.y, this.getY()) - 50.0;
                    double d1 = Math.max(rayTrace.y, this.getY()) + 3.0;
                    float f = (float)Mth.atan2(rayTrace.z - this.getZ(), rayTrace.x - this.getX());
                    if (this.finalTarget != null){
                        d0 = Math.min(this.finalTarget.getY(), this.getY()) - 50.0;
                        d1 = Math.max(this.finalTarget.getY(), this.getY()) + 3.0;
                        f = (float)Mth.atan2(this.finalTarget.getZ() - this.getZ(), this.finalTarget.getX() - this.getX());
                    }
                    for(int l = 0; l < 9; ++l) {
                        int j = (int)(5.0F * (float)l);
                        double randomNearbyX = rayTrace.x + this.random.nextGaussian() * 12.0;
                        double randomNearbyZ = rayTrace.z + this.random.nextGaussian() * 12.0;
                        if (this.finalTarget != null){
                            randomNearbyX = this.finalTarget.getX() + this.random.nextGaussian() * 12.0;
                            randomNearbyZ = this.finalTarget.getZ() + this.random.nextGaussian() * 12.0;
                        }
                        this.spawnUnderPortal(livingEntity, randomNearbyX, randomNearbyZ, d0, d1, f, j, this.potency);
                    }
                }
            }

            this.discard();
        }

    }

    private void spawnUnderPortal(LivingEntity caster, double x, double z, double minY, double maxY, float rotation, int delay, int potency) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = caster.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(caster.level(), blockpos1, Direction.UP)) {
                if (!caster.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = caster.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(caster.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            caster.level().addFreshEntity(new AbyssBlastPortal(caster.level(), x, (double)blockpos.getY() + d0, z, rotation, delay, (GCSpellConfig.AbyssalBeamDamage.get().floatValue() * WandUtil.damageMultiply()) + potency, GCSpellConfig.AbyssalBeamHPDamage.get().floatValue(), caster));
        }

    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int i) {
        this.entityData.set(LIFESPAN, i);
    }

    public UUID getCreatorEntityUUID() {
        return this.entityData.get(CREATOR_ID).orElse(null);
    }

    public void setCreatorEntityUUID(UUID id) {
        this.entityData.set(CREATOR_ID, Optional.ofNullable(id));
    }

    public Entity getCreatorEntity() {
        UUID uuid = this.getCreatorEntityUUID();
        return uuid != null && !this.level().isClientSide ? ((ServerLevel)this.level()).getEntity(uuid) : null;
    }

    private void updateMotion() {
        Vec3 vec3 = this.getDeltaMovement();
        double h0 = this.getX() + vec3.x;
        double h1 = this.getY() + vec3.y;
        double h2 = this.getZ() + vec3.z;
        if (this.finalTarget != null && this.finalTarget.isAlive() || this.finalTarget instanceof Player && !this.finalTarget.isSpectator()) {
            double dx = this.finalTarget.getX() - this.getX();
            double dz = this.finalTarget.getZ() - this.getZ();
            double p0 = Math.min(this.finalTarget.getY(), this.getY() - 50.0);
            double p1 = Math.max(this.finalTarget.getY(), this.getY());
            BlockPos blockpos = BlockPos.containing(this.finalTarget.getX(), p1, this.finalTarget.getZ());
            double d0 = 0.0;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = this.level().getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(this.level(), blockpos1, Direction.UP)) {
                    if (!this.level().isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = this.level().getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(this.level(), blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= Mth.floor(p0) - 1);

            this.setPos(h0, (double)blockpos.getY() + d0, h2);
            this.setDeltaMovement(vec3.add(dx, 0.0, dz).scale(0.05));
        }

    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

}
