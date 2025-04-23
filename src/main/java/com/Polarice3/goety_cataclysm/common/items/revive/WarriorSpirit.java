package com.Polarice3.goety_cataclysm.common.items.revive;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.items.revive.ReviveServantItem;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.KobolediatorServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert.KoboletonServant;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.Koboleton_Entity;
import com.github.L_Ender.cataclysm.init.ModParticle;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class WarriorSpirit extends ReviveServantItem {
    public WarriorSpirit() {
        super(new Properties()
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1));
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.getCommandSenderWorld();
        Entity entity;
        if (getSummon(stack, level) != null){
            entity = getSummon(stack, level);
        } else {
            entity = new KobolediatorServant(GCEntityType.KOBOLEDIATOR.get(), level);
            IOwned owned = (IOwned) entity;
            owned.setTrueOwner(player);
        }
        if (entity instanceof KobolediatorServant servant) {
            boolean flag = target instanceof KoboletonServant || target instanceof Koboleton_Entity;
            if (flag && servant.getTrueOwner() == player && RitualRequirements.canSummon(level, player, GCEntityType.KOBOLEDIATOR.get())) {
                servant.setHealth(servant.getMaxHealth());
                servant.setPos(target.getX(), target.getY(), target.getZ());
                servant.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
                if (level.addFreshEntity(servant)) {
                    servant.spawnAnim();
                    if (level instanceof ServerLevel serverLevel) {
                        for(int i = 0; i < 8; ++i) {
                            ServerParticleUtil.addParticlesAroundSelf(serverLevel, ModParticle.SANDSTORM.get(), servant);
                        }
                    }

                    servant.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.5F);
                    servant.playSound(CataclysmSounds.KOBOLEDIATOR_AMBIENT.get(), 2.0F, 0.5F);
                    target.discard();
                    player.swing(hand);
                    player.getCooldowns().addCooldown(this, MathHelper.secondsToTicks(30));
                    stack.shrink(1);
                }
            }
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }
}
