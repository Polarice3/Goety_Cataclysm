package com.Polarice3.goety_cataclysm.common.entities.util;

import com.Polarice3.Goety.common.entities.projectiles.SpellEntity;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.LootingExplosion;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.deepling.leviathan.LeviathanServant;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.effect.Cm_Falling_Block_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModParticle;
import com.github.L_Ender.cataclysm.init.ModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class DimensionalRift extends SpellEntity {
    protected static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(DimensionalRift.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> STAGE = SynchedEntityData.defineId(DimensionalRift.class, EntityDataSerializers.INT);
    private boolean madeOpenNoise;
    private boolean madeCloseNoise;
    private boolean madeParticle;
    public int ambientSoundTime;

    public DimensionalRift(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.madeOpenNoise = false;
        this.madeCloseNoise = false;
        this.madeParticle = false;
    }

    public DimensionalRift(Level worldIn, double x, double y, double z, LivingEntity casterIn) {
        this(GCEntityType.DIMENSIONAL_RIFT.get(), worldIn);
        this.setOwner(casterIn);
        this.setLifespan(300);
        this.setPos(x, y, z);
    }

    protected Component getTypeName() {
        return ModEntities.DIMENSIONAL_RIFT.get().getDescription();
    }

    public void tick() {
        super.tick();
        if(!madeOpenNoise){
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.playSound(CataclysmSounds.BLACK_HOLE_OPENING.get(), 0.7F, 1 + random.nextFloat() * 0.2F);
            madeOpenNoise = true;
        }

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(30), entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !MobUtil.areAllies(this.getOwner(), entity))) {
            if (entity != this.getOwner()) {
                boolean flag = true;
                if (MobUtil.getOwner(entity) != null) {
                    if (MobUtil.areAllies(MobUtil.getOwner(entity), this.getOwner())) {
                        flag = false;
                    }
                }
                if (flag) {
                    Vec3 diff = entity.position().subtract(this.position().add(0, 0, 0));
                    if (entity instanceof LivingEntity) {
                        diff = diff.normalize().scale(getStage() * 0.015);
                        entity.setDeltaMovement(entity.getDeltaMovement().subtract(diff));
                    } else if (!entity.getType().is(ModTag.DIMENSIONAL_LIFT_IMMUNE)) {
                        diff = diff.normalize().scale(getStage() * 0.045);
                        entity.setDeltaMovement(entity.getDeltaMovement().subtract(diff));
                    }
                }
            }
        }

        if (this.getOwner() instanceof LeviathanServant) {
            if (GCMobsConfig.LeviathanGriefing.get()) {
                this.berserkBlockBreaking(15, 15, 15);
            }
        }

        for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
            this.damage(livingentity);
        }

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(0.5))) {
            if (entity instanceof Cm_Falling_Block_Entity) {
                entity.remove(RemovalReason.DISCARDED);
            }
        }


        if (this.random.nextInt(3000) < this.ambientSoundTime++) {
            this.resetAmbientSoundTime();
            this.playSound(CataclysmSounds.BLACK_HOLE_LOOP.get(), 0.7F, 1 + random.nextFloat() * 0.2F);
        }



        this.setLifespan(this.getLifespan() - 1);
        if(this.getLifespan() <= 100){
            if(!madeCloseNoise){
                this.gameEvent(GameEvent.ENTITY_PLACE);
                this.playSound(CataclysmSounds.BLACK_HOLE_CLOSING.get(), 0.7F, 1 + random.nextFloat() * 0.2F);
                madeCloseNoise = true;
            }
            if(this.tickCount % 40 == 0){
                this.setStage(this.getStage() - 1);
            }

            if (this.getStage() <= 0) {
                if(!madeParticle){
                    if (this.level().isClientSide) {
                        this.level().addParticle(ModParticle.SHOCK_WAVE.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                    } else {
                        LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(this.getOwner()) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
                        ExplosionUtil.lootExplode(this.level(), this.getOwner(), this.getX(), this.getY(), this.getZ(), 4.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
                    }
                    madeParticle = true;
                }else{
                    this.discard();
                }
            }
        }
    }

    private void damage(LivingEntity Hitentity) {
        LivingEntity owner1 = this.getOwner();
        if (Hitentity.isAlive() && !Hitentity.isInvulnerable() && Hitentity != owner1 && !MobUtil.areAllies(owner1, Hitentity)) {
            if (this.tickCount % 5 == 0) {
                DamageSource damageSource = owner1 == null ? damageSources().magic() : damageSources().indirectMagic(this, owner1);
                Hitentity.hurt(damageSource, 10.0F);
            }
        }
    }


    private void berserkBlockBreaking(int x, int y, int z) {
        int MthX = Mth.floor(this.getX());
        int MthY = Mth.floor(this.getY());
        int MthZ = Mth.floor(this.getZ());
        if (!this.level().isClientSide) {
            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                for (int k2 = -x; k2 <= x; ++k2) {
                    for (int l2 = -z; l2 <= z; ++l2) {
                        for (int j = -y; j <= y; ++j) {
                            int i3 = MthX + k2;
                            int k = MthY + j;
                            int l = MthZ + l2;
                            BlockPos blockpos = new BlockPos(i3, k, l);

                            BlockPos blockonpos = new BlockPos(i3, k+1, l);

                            BlockState block = level().getBlockState(blockpos);
                            BlockState blockon = level().getBlockState(blockonpos);
                            BlockEntity tileEntity = level().getBlockEntity(blockpos);
                            if ((blockon == Blocks.AIR.defaultBlockState() || blockon == Blocks.WATER.defaultBlockState()) && block != Blocks.AIR.defaultBlockState() && !block.is(ModTag.LEVIATHAN_IMMUNE)) {
                                if (tileEntity == null && random.nextInt(2000) == 0) {
                                    this.level().removeBlock(blockpos, true);
                                    Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(level(), i3 + 0.5D, k + 0.5D, l + 0.5D, block, 5);
                                    level().setBlock(blockpos, block.getFluidState().createLegacyBlock(), 3);
                                    level().addFreshEntity(fallingBlockEntity);

                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public int getAmbientSoundInterval() {
        return 80;
    }

    private void resetAmbientSoundTime() {
        this.ambientSoundTime = -this.getAmbientSoundInterval();
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int i) {
        this.entityData.set(LIFESPAN, i);
    }

    public int getStage() {
        return this.entityData.get(STAGE);
    }

    public void setStage(int i) {
        this.entityData.set(STAGE, i);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LIFESPAN, 300);
        this.entityData.define(STAGE, 0);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setLifespan(compound.getInt("Lifespan"));
        this.setStage(compound.getInt("Stage"));
    }

    public boolean shouldRenderAtSqrDistance(double p_36837_) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return p_36837_ < d0 * d0;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Lifespan", getLifespan());
        compound.putInt("Stage", getStage());
    }
}
