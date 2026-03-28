package com.Polarice3.goety_cataclysm.common.entities.ally.ai;

import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class InternalSummonAttackGoal extends Goal {
    protected final InternalAnimationSummon entity;
    protected final int getattackstate;
    protected final int attackstate;
    protected final int attackendstate;
    protected final int attackMaxtick;
    protected final int attackseetick;
    protected final float attackrange;

    public InternalSummonAttackGoal(InternalAnimationSummon entity, int getattackstate, int attackstate, int attackendstate,int attackMaxtick,int attackseetick,float attackrange) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK,Flag.JUMP));
        this.getattackstate = getattackstate;
        this.attackstate = attackstate;
        this.attackendstate = attackendstate;
        this.attackMaxtick = attackMaxtick;
        this.attackseetick = attackseetick;
        this.attackrange = attackrange;
    }

    public InternalSummonAttackGoal(InternalAnimationSummon entity, int getattackstate, int attackstate, int attackendstate,int attackMaxtick,int attackseetick,float attackrange, EnumSet<Flag> interruptFlagTypes) {
        this.entity = entity;
        setFlags(interruptFlagTypes);
        this.getattackstate = getattackstate;
        this.attackstate = attackstate;
        this.attackendstate = attackendstate;
        this.attackMaxtick = attackMaxtick;
        this.attackseetick = attackseetick;
        this.attackrange = attackrange;
    }


    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        return target != null && target.isAlive() && this.entity.distanceTo(target) < attackrange && this.entity.getAttackState() == getattackstate;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        this.entity.setAttackState(attackstate);
        this.entity.getNavigation().stop();
    }

    @Override
    public void stop() {
        EndState();
        LivingEntity target = entity.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.entity.setTarget((LivingEntity)null);
        }
        this.entity.getNavigation().stop();
        if (this.entity.getTarget() == null) {
            this.entity.setAggressive(false);
        }
    }

    protected void EndState() {
        this.entity.setAttackState(attackendstate);
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getAttackState() == attackstate && this.entity.attackTicks <= attackMaxtick;
    }

    public void tick() {
        LivingEntity target = entity.getTarget();

        if(target !=null){
            boolean flag = entity.attackTicks < attackseetick;
            if(flag){
                entity.getLookControl().setLookAt(target,  30.0F, 30.0F);
                entity.lookAt(target, 30.0F, 30.0F);
            }else{
                entity.getLookControl().setLookAt(target,0F, 30.0F);
                entity.setYRot(entity.yRotO);
            }

        }else{
            entity.setYRot(entity.yRotO);
        }

        this.entity.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
