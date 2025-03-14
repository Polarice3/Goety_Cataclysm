package com.Polarice3.goety_cataclysm.common.entities.projectiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.entities.projectiles.SpellThrowableProjectile;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.github.L_Ender.cataclysm.client.particle.LightTrailParticle;
import com.github.L_Ender.cataclysm.entity.projectile.Flame_Jet_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class FlareBomb extends SpellThrowableProjectile {
    public double prevDeltaMovementX, prevDeltaMovementY, prevDeltaMovementZ;

    public FlareBomb(EntityType<? extends FlareBomb> type, Level world) {
        super(type, world);
    }

    public FlareBomb(EntityType<? extends FlareBomb> type, Level world, LivingEntity thrower) {
        super(type, thrower, world);
    }

    @Override
    protected Component getTypeName() {
        return ModEntities.FLARE_BOMB.get().getDescription();
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity shooter = this.getOwner();
        Entity entity = result.getEntity();
        if (!this.level().isClientSide && !MobUtil.areAllies(shooter, entity)) {
            boolean flag;
            float damage = (GCSpellConfig.FlareBombDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get()) + this.getExtraDamage();
            if (shooter instanceof LivingEntity livingentity) {
                flag = entity.hurt(this.damageSources().mobProjectile(this, livingentity), damage);
                if (flag) {
                    if (entity.isAlive()) {
                        entity.setSecondsOnFire(5);
                        this.doEnchantDamageEffects(livingentity, entity);
                    }
                }
            } else {
                entity.hurt(this.damageSources().magic(), damage);
            }
        }
    }


    protected void onHit(HitResult p_37628_) {
        super.onHit(p_37628_);
        if (!this.level().isClientSide) {
            this.playSound(SoundEvents.GENERIC_BURN, 1.5f, 0.75f);
            this.level().broadcastEntityEvent(this, (byte)4);
            if(random.nextBoolean()){
                this.XStrikeJet(10,2);
            }else{
                this.PlusStrikeJet(10,2);
            }
            this.discard();
        }

    }

    protected void PlusStrikeJet(int rune, double time) {
        for (int i = 0; i < 4; i++) {

            float yawRadians = (float) (Math.toRadians(90 + this.getYRot()));
            float throwAngle = yawRadians + i * Mth.PI / 2;

            for (int k = 0; k < rune; ++k) {
                double d2 = 0.8D * (double) (k + 1);
                int d3 = (int) (time * (k + 1));
                this.spawnjet(this.getX() + (double) Mth.cos(throwAngle) * 1.25D * d2, this.getZ() + (double) Mth.sin(throwAngle) * 1.25D * d2, this.getY() -2, this.getY() + 2, throwAngle, d3);
            }

        }

    }

    protected void XStrikeJet(int rune, double time) {
        for (int i = 0; i < 4; i++) {

            float yawRadians = (float) (Math.toRadians(45 + this.getYRot()));
            float throwAngle = yawRadians + i * Mth.PI / 2;

            for (int k = 0; k < rune; ++k) {
                double d2 = 0.8D * (double) (k + 1);
                int d3 = (int) (time * (k + 1));
                this.spawnjet(this.getX() + (double) Mth.cos(throwAngle) * 1.25D * d2, this.getZ() + (double) Mth.sin(throwAngle) * 1.25D * d2, this.getY() - 2, this.getY() + 2, throwAngle, d3);
            }

        }

    }

    protected void spawnjet(double x, double z, double minY, double maxY, float rotation, int delay) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0D;

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

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            float damage = (GCSpellConfig.FlareBombDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get()) + this.getExtraDamage();
            if (this.getOwner() != null) {
                this.level().addFreshEntity(new Flame_Jet_Entity(this.level(), x, (double) blockpos.getY() + d0, z, rotation, delay, damage, this.getOwner()));
            } else {
                this.level().addFreshEntity(new Flame_Jet_Entity(this.level(), x, (double) blockpos.getY() + d0, z, rotation, delay, damage, null));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.prevDeltaMovementX = this.getDeltaMovement().x;
        this.prevDeltaMovementY = this.getDeltaMovement().y;
        this.prevDeltaMovementZ = this.getDeltaMovement().z;

        this.setYRot(-((float) Mth.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z)) * (180F / (float)Math.PI)) ;

        if (this.level() instanceof ServerLevel serverLevel){
            double dx = getX() + 1.5F * (random.nextFloat() - 0.5F);
            double dy = getY() + 1.5F * (random.nextFloat() - 0.5F);
            double dz = getZ() + 1.5F * (random.nextFloat() - 0.5F);
            float ran = 0.04f;
            float r = 195/255F + random.nextFloat() * ran * 1.5F;
            float g = 95/255F + random.nextFloat() * ran;
            float b = 3/255F + random.nextFloat() * ran;
            serverLevel.sendParticles((new LightTrailParticle.OrbData(r, g, b,0.1F,this.getBbHeight()/2,this.getId())),  dx, dy, dz, 1, 0, 0, 0, 0);
        }
    }

    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F, false);
            this.level().addParticle(ModParticle.FLARE_EXPLODE.get(), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, 0.005D, this.random.nextGaussian() * 0.05D);
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity);
            } else {
                if(MobUtil.areAllies(this.getOwner(), pEntity)){
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

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    protected float getGravity() {
        return 0.025F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
