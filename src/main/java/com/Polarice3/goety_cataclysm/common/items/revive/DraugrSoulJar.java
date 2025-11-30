package com.Polarice3.goety_cataclysm.common.items.revive;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.items.revive.SoulJar;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.DraugrNecromancerServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.DraugrServant;
import com.Polarice3.goety_cataclysm.common.items.GoetyItems;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.Draugar.Draugr_Entity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DraugrSoulJar extends SoulJar {
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.getCommandSenderWorld();

        Entity entity;
        if (getSummon(stack, level) != null){
            entity = getSummon(stack, level);
        } else {
            entity = new DraugrNecromancerServant(GCEntityType.DRAUGR_NECROMANCER_SERVANT.get(), level);
            IOwned owned = (IOwned) entity;
            owned.setTrueOwner(player);
        }
        if (entity instanceof DraugrNecromancerServant necromancer) {
            if (target instanceof Draugr_Entity
                    || target instanceof DraugrServant) {
                if (necromancer.getTrueOwner() == player) {
                    if (RitualRequirements.canSummon(level, player, GCEntityType.DRAUGR_NECROMANCER_SERVANT.get())) {
                        necromancer.setHealth(necromancer.getMaxHealth());
                        necromancer.setPos(target.getX(), target.getY(), target.getZ());
                        necromancer.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
                        if (level.addFreshEntity(necromancer)) {
                            necromancer.spawnAnim();
                            if (level instanceof ServerLevel serverLevel) {
                                for (int i = 0; i < 8; ++i) {
                                    ServerParticleUtil.addParticlesAroundSelf(serverLevel, ParticleTypes.SCULK_SOUL, necromancer);
                                    ServerParticleUtil.addParticlesAroundSelf(serverLevel, ParticleTypes.POOF, necromancer);
                                }
                            }
                            necromancer.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.5F);
                            necromancer.playSound(GoetySounds.NECROMANCER_LAUGH.get(), 2.0F, 0.5F);
                            target.discard();
                            player.swing(hand);
                            SEHelper.addCooldown(player, this, MathHelper.secondsToTicks(30));
                            SEHelper.addCooldown(player, GoetyItems.SOUL_JAR.get(), MathHelper.secondsToTicks(30));
                            stack.shrink(1);
                        }
                    }
                }
            }
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }
}
