package com.Polarice3.goety_cataclysm.common.entities.ally.acropolis;

import com.Polarice3.Goety.common.entities.ally.undead.zombie.DrownedServant;
import com.Polarice3.Goety.common.entities.ally.undead.zombie.ZombieServant;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;

public class DrownedHostServant extends DrownedServant {
    public boolean initiated = false;

    public DrownedHostServant(EntityType<? extends ZombieServant> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.getAvailableGoals().removeIf(wrappedGoal -> wrappedGoal.getGoal() instanceof LookAtPlayerGoal);
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        //the octopus took their senses :O
        return DrownedServant.setCustomAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    protected void updateControlFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof Mob) || this.getControllingPassenger() instanceof SymbioctoServant;
        boolean flag1 = !(this.getVehicle() instanceof Boat);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, flag);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
        this.goalSelector.setControlFlag(Goal.Flag.TARGET, flag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Initiated")) {
            this.initiated = pCompound.getBoolean("Initiated");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Initiated", this.initiated);
    }

    public void positionRider(Entity entity, Entity.MoveFunction callback) {
        super.positionRider(entity, callback);
        if (entity instanceof SymbioctoServant goblin) {
            goblin.setYBodyRot(this.yBodyRot);
            goblin.setYHeadRot(this.getYHeadRot());
            goblin.setYRot(this.getYRot());
        }
    }

    public boolean isBaby() {
        return false;
    }

    @Override
    protected boolean isSunSensitive() {
        if (!this.initiated) {
            return false;
        }
        if (this.getFirstPassenger() instanceof SymbioctoServant) {
            return false;
        }
        return super.isSunSensitive();
    }

    @Override
    public void tick() {
        super.tick();
        //Did this way cause the stupid takoyaki won't share owner
        if (this.level() instanceof ServerLevel serverLevel) {
            if (!this.initiated) {
                if (this.getFirstPassenger() == null) {
                    SymbioctoServant upper = new SymbioctoServant(GCEntityType.SYMBIOCTO_SERVANT.get(), serverLevel);
                    upper.moveTo(this.getX(), this.getY() + 1.3125, this.getZ(), this.getYRot(), 0.0F);
                    if (this.getTrueOwner() != null) {
                        upper.setTrueOwner(this.getTrueOwner());
                    }
                    if (this.isLimitedLife()) {
                        upper.setLimitedLife(this.getLifespan());
                    }
                    upper.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), this.getSpawnType() != null ? this.getSpawnType() : MobSpawnType.NATURAL, null, null);
                    upper.setHostile(this.isHostile());
                    upper.setYBodyRot(this.yBodyRot);
                    upper.setYHeadRot(this.getYHeadRot());
                    upper.setYRot(this.getYRot());
                    upper.startRiding(this);
                    serverLevel.addFreshEntity(upper);
                }
                this.initiated = true;
            }
        }
    }
}
