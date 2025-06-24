package com.Polarice3.goety_cataclysm.common.entities.ally.ai;

import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
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

    public InternalSummonAttackGoal(InternalAnimationSummon entity, int getattackstate, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        this.getattackstate = getattackstate;
        this.attackstate = attackstate;
        this.attackendstate = attackendstate;
        this.attackMaxtick = attackMaxtick;
        this.attackseetick = attackseetick;
        this.attackrange = attackrange;
    }

    public InternalSummonAttackGoal(InternalAnimationSummon entity, int getattackstate, int attackstate, int attackendstate, int attackMaxtick, int attackseetick, float attackrange, EnumSet<Goal.Flag> interruptFlagTypes) {
        this.entity = entity;
        this.setFlags(interruptFlagTypes);
        this.getattackstate = getattackstate;
        this.attackstate = attackstate;
        this.attackendstate = attackendstate;
        this.attackMaxtick = attackMaxtick;
        this.attackseetick = attackseetick;
        this.attackrange = attackrange;
    }

    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive() && this.entity.distanceTo(target) < this.attackrange && this.entity.getAttackState() == this.getattackstate;
    }

    public void start() {
        this.entity.setAttackState(this.attackstate);
    }

    public void stop() {
        this.entity.setAttackState(this.attackendstate);
    }

    public boolean canContinueToUse() {
        return this.entity.getAttackState() == this.attackstate && this.entity.attackTicks <= this.attackMaxtick;
    }

    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (this.entity.attackTicks < this.attackseetick && target != null) {
            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.entity.lookAt(target, 30.0F, 30.0F);
        } else {
            this.entity.setYRot(this.entity.yRotO);
        }

    }

    public boolean requiresUpdateEveryTick() {
        return false;
    }
}
