package com.Polarice3.goety_cataclysm.common.events;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.magic.GolemType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.Fangs;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.NoKnockBackDamageSource;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.common.blocks.GoetyBlocks;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.NMPart;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.common.items.GoetyItems;
import com.Polarice3.goety_cataclysm.common.magic.construct.NetheriteMonstrosityMold;
import com.Polarice3.goety_cataclysm.init.GCGolemTypes;
import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;

@Mod.EventBusSubscriber(modid = GoetyCataclysm.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GCEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level world = event.getLevel();
        /*if (entity instanceof Lava_Bomb_Entity original && original.getOwner() instanceof Netherite_Monstrosity_Entity monstrosity) {
            MagmaBomb magmaBomb = new MagmaBomb(monstrosity, world);
            magmaBomb.moveTo(original.position());
            Vec3 vec3 = original.getDeltaMovement();
            magmaBomb.setDeltaMovement(original.getDeltaMovement());
            double d0 = vec3.horizontalDistance();
            magmaBomb.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            magmaBomb.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            magmaBomb.yRotO = magmaBomb.getYRot();
            magmaBomb.xRotO = magmaBomb.getXRot();
            if (world.addFreshEntity(magmaBomb)) {
                original.discard();
                event.setCanceled(true);
            }
        }*/
    }

    @SubscribeEvent
    public static void worldLoad(LevelEvent.Load event) {
        GCGolemTypes.addGolems();
    }

    @SubscribeEvent
    public static void worldUnload(LevelEvent.Unload event) {
        GolemType[] members = GolemType.values();
        for (GolemType member : members) {
            if (GCGolemTypes.NEW_GOLEM_TYPES.contains(member)) {
                ArrayUtils.remove(members, member.ordinal());
            }
        }
    }

    @SubscribeEvent
    public static void PlayerAttackEvent(AttackEntityEvent event){
        if (event.getTarget() instanceof NMPart nmPart){
            ItemStack itemStack = event.getEntity().getMainHandItem();
            if (nmPart.getParent().getTrueOwner() == event.getEntity() || (nmPart.getParent().getTrueOwner() instanceof IOwned owned && owned.getTrueOwner() == event.getEntity())) {
                if (MobsConfig.OwnerAttackCancel.get()) {
                    itemStack.getItem().onLeftClickEntity(itemStack, event.getEntity(), nmPart.getParent());
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void UseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().is(CataclysmItems.INFERNAL_FORGE.get())){
            BlockState blockState = event.getLevel().getBlockState(event.getPos());
            if (blockState.is(GoetyBlocks.REINFORCED_REDSTONE_BLOCK.get())){
                if (new NetheriteMonstrosityMold().spawnServant(event.getEntity(), event.getItemStack(), event.getLevel(), event.getPos())){
                    event.getItemStack().shrink(1);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingInteract(PlayerInteractEvent.EntityInteract event){
        if (!event.getLevel().isClientSide){
            if (event.getTarget() instanceof NMPart nmPart){
                if (nmPart.getParent().getTrueOwner() == event.getEntity() || (nmPart.getParent().getTrueOwner() instanceof IOwned owned && owned.getTrueOwner() == event.getEntity())) {
                    event.getItemStack().getItem().interactLivingEntity(event.getItemStack(), event.getEntity(), nmPart.getParent(), event.getHand());
                }
            }
        }
    }

    @SubscribeEvent
    public static void SpecialDeath(LivingDeathEvent event){
        LivingEntity killed = event.getEntity();
        Entity killer = event.getSource().getEntity();
        Level world = killed.getCommandSenderWorld();
        if (event.getSource() instanceof NoKnockBackDamageSource noKnockBackDamageSource){
            killer = noKnockBackDamageSource.getOwner();
        }
        if (killer instanceof Player player){
            if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)){
                Entity entity = event.getSource().getDirectEntity();
                if (entity instanceof Fangs){
                    if (CuriosFinder.findRing(player).getItem() == GoetyItems.RING_OF_WANT.get()) {
                        int enchantment = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                        if (enchantment >= 3) {
                            if (world.random.nextFloat() <= (enchantment / 9.0F)) {
                                if (killed.getType() == ModEntities.DRAUGR.get() || killed.getType() == ModEntities.ELITE_DRAUGR.get() || killed.getType() == ModEntities.ROYAL_DRAUGR.get()) {
                                    killed.spawnAtLocation(new ItemStack(CataclysmItems.DRAUGR_HEAD.get()));
                                }
                                if (killed.getType() == ModEntities.APTRGANGR.get()){
                                    killed.spawnAtLocation(new ItemStack(CataclysmItems.APTRGANGR_HEAD.get()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
