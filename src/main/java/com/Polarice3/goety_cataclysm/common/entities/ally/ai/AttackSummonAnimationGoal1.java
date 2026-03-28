package com.Polarice3.goety_cataclysm.common.entities.ally.ai;

import com.Polarice3.goety_cataclysm.common.entities.ally.LLibrarySummon;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class AttackSummonAnimationGoal1<T extends LLibrarySummon & IAnimatedEntity> extends SimpleSummonAnimationGoal<T> {
    private final int look1;
    private final boolean see;

    public AttackSummonAnimationGoal1(T entity, Animation animation, int look1, boolean see) {
        super(entity, animation);
        this.look1 = look1;
        this.see = see;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (this.see) {
            if(target !=null){
                boolean flag = entity.getAnimationTick() < look1;
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
        } else {
            if(target !=null){
                boolean flag = entity.getAnimationTick() > look1;
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
        }

        this.entity.setDeltaMovement(0.0, this.entity.getDeltaMovement().y, 0.0);
    }
}
