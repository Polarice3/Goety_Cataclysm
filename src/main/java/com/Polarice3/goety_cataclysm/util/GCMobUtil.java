package com.Polarice3.goety_cataclysm.util;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.config.MainConfig;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.GameRules;

import java.util.function.Predicate;

public class GCMobUtil {
    public static Predicate<LivingEntity> projectilePredicate(Entity entity){
        return target -> isProjectileTargetable(entity, target);
    }

    public static boolean isProjectileTargetable(Entity attacker, LivingEntity target){
        LivingEntity owner = null;
        if (attacker instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity livingEntity){
            owner = livingEntity;
        }
        if (owner instanceof Enemy
                || (owner instanceof IOwned owned && owned.isHostile())
                || attacker instanceof Enemy
                || (attacker instanceof IOwned ownedAttacker && ownedAttacker.isHostile())){
            return target instanceof Player player && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player);
        } else {
            return ((target instanceof Enemy || (target instanceof IOwned ownedTarget && ownedTarget.isHostile()))
                    && !((target.getMobType() == MobType.UNDEAD || target.getType().is(ModTags.EntityTypes.LICH_NEUTRAL)) && LichdomHelper.isLich(owner) && MainConfig.LichUndeadFriends.get())
                    && !(owner != null && ((CuriosFinder.hasNecroSet(owner) && CuriosFinder.validNecroUndead(target)) || (CuriosFinder.neutralNamelessSet(owner) && CuriosFinder.validNamelessUndead(target))) && !MobsConfig.NecroRobeUndead.get())
                    && !(MobUtil.isWitchType(target) && owner != null && CuriosFinder.isWitchFriendly(owner) && !MobsConfig.VariousRobeWitch.get())
                    && !(CuriosFinder.validFrostMob(target) && owner != null && CuriosFinder.neutralFrostSet(owner))
                    && !(CuriosFinder.validWildMob(target) && owner != null && CuriosFinder.neutralWildSet(owner))
                    && !(CuriosFinder.validNetherMob(target) && owner != null && CuriosFinder.neutralNetherSet(owner))
                    && !(target.getMobType() == MobType.ARTHROPOD && owner != null && CuriosFinder.hasWarlockRobe(owner))
                    && !(target instanceof Creeper && target.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && MobsConfig.ServantsAttackCreepers.get())
                    && !(target instanceof NeutralMob neutralMob && ((owner != null && neutralMob.getTarget() != owner) || neutralMob.getTarget() != attacker))
                    && !(target instanceof AbstractPiglin piglin && ((owner != null && piglin.getTarget() != owner) || piglin.getTarget() != attacker))
                    && !(target instanceof IOwned ownedTarget && (owner != null && ownedTarget.getTrueOwner() == owner))
                    || (owner instanceof Player player
                    && ((!SEHelper.getGrudgeEntities(player).isEmpty() && SEHelper.getGrudgeEntities(player).contains(target))
                    || (!SEHelper.getGrudgeEntityTypes(player).isEmpty() && SEHelper.getGrudgeEntityTypes(player).contains(target.getType())))));
        }
    }
}
