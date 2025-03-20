package com.Polarice3.goety_cataclysm.common.items.revive;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.neutral.BlazeServant;
import com.Polarice3.Goety.common.items.revive.ReviveServantItem;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited.IgnitedBerserkerServant;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class IgnitedSoul extends ReviveServantItem {
    public IgnitedSoul() {
        super(new Properties()
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .fireResistant()
                .stacksTo(1));
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.getCommandSenderWorld();
        if (getSummon(stack, level) != null) {
            Entity entity = getSummon(stack, level);
            if (entity instanceof IgnitedBerserkerServant servant) {
                boolean flag = target instanceof BlazeServant || target instanceof Blaze;
                if (flag && servant.getTrueOwner() == player && RitualRequirements.canSummon(level, player, GCEntityType.IGNITED_BERSERKER.get())) {
                    servant.setHealth(servant.getMaxHealth());
                    servant.setPos(target.getX(), target.getY(), target.getZ());
                    servant.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
                    if (level.addFreshEntity(servant)) {
                        servant.spawnAnim();
                        if (level instanceof ServerLevel serverLevel) {

                            for(int i = 0; i < 8; ++i) {
                                ServerParticleUtil.addParticlesAroundSelf(serverLevel, (ParticleOptions) ModParticleTypes.BIG_FIRE.get(), servant);
                                ServerParticleUtil.addParticlesAroundSelf(serverLevel, ParticleTypes.SMOKE, servant);
                            }
                        }

                        servant.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.5F);
                        servant.playSound(CataclysmSounds.REVENANT_IDLE.get(), 2.0F, 0.5F);
                        target.discard();
                        player.swing(hand);
                        player.getCooldowns().addCooldown(this, MathHelper.secondsToTicks(30));
                        stack.shrink(1);
                    }
                }
            }
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }
}
