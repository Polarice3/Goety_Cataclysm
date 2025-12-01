package com.Polarice3.goety_cataclysm.common.entities.neutral;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.client.particles.MagicSmokeParticle;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.AbstractSkeletonServant;
import com.Polarice3.Goety.common.entities.neutral.AbstractNecromancer;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.*;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.DraugrServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.EliteDraugrServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.draugr.RoyalDraugrServant;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.PhantomArrow;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.common.items.GoetyItems;
import com.Polarice3.goety_cataclysm.common.items.revive.DraugrSoulJar;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class AbstractDraugrNecromancer extends AbstractNecromancer {
    public int attackTick;

    public AbstractDraugrNecromancer(EntityType<? extends AbstractSkeletonServant> type, Level level) {
        super(type, level);
    }

    public void projectileGoal(int priority) {
        this.goalSelector.addGoal(priority, new NecromancerRangedGoal(this, 1.0, 30, 10.0F));
    }

    public void summonSpells(int priority) {
        this.goalSelector.addGoal(priority, new SummonServantSpell());
        this.goalSelector.addGoal(priority + 1, new SummonUndeadGoal(){
            @Override
            protected void populateDefaultEquipmentSlots(LivingEntity livingEntity, RandomSource p_217055_) {
            }
        });
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.DraugrNecromancerHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.DraugrNecromancerArmor.get())
                .add(Attributes.FOLLOW_RANGE, GCAttributesConfig.DraugrNecromancerFollowRange.get())
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.DraugrNecromancerDamage.get());
    }

    public void setConfigurableAttributes(){
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.DraugrNecromancerHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.DraugrNecromancerArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.FOLLOW_RANGE), GCAttributesConfig.DraugrNecromancerFollowRange.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.DraugrNecromancerDamage.get());
    }

    public void soulJar() {
        if (this.getTrueOwner() instanceof Player player && MobsConfig.NecromancerSoulJar.get()){
            Optional<ItemStack> optional = player.getInventory().items.stream().filter(itemStack1 -> itemStack1.is(GoetyItems.EMPTY_SOUL_JAR.get())).findFirst();
            if (optional.isPresent()){
                ItemStack original = optional.get();
                if (original.is(GoetyItems.EMPTY_SOUL_JAR.get())){
                    if (!player.isCreative()){
                        original.shrink(1);
                    }
                    ItemStack itemStack = new ItemStack(GCItems.SOUL_JAR_DRAUGR.get());
                    DraugrSoulJar.setOwnerName(this.getTrueOwner(), itemStack);
                    DraugrSoulJar.setSummon(this, itemStack);
                    SEHelper.addCooldown(player, GoetyItems.SOUL_JAR.get(), MathHelper.secondsToTicks(30));
                    SEHelper.addCooldown(player, itemStack.getItem(), MathHelper.secondsToTicks(30));
                    if (!player.getInventory().add(itemStack)) {
                        player.drop(itemStack, false, true);
                    }
                }
            }
        }
    }

    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        if (p_33317_ != null) {
            if (this.getNecroLevel() <= 0) {
                PhantomArrow arrow = new PhantomArrow(this.level(), this, p_33317_);
                double d0 = p_33317_.getX() - this.getX();
                double d1 = p_33317_.getY(0.3333333333333333) - arrow.getY();
                double d2 = p_33317_.getZ() - this.getZ();
                double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
                arrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.6F, 1.0F);
                if (this.level().addFreshEntity(arrow)) {
                    this.playSound(CataclysmSounds.STRONGSWING.get());
                    this.swing(InteractionHand.MAIN_HAND);
                }
            } else {
                for (int i = -this.getNecroLevel(); i <= this.getNecroLevel(); ++i) {
                    Vec3 vector3d = this.getViewVector(1.0F);
                    PhantomArrow arrow = new PhantomArrow(this.level(), this, p_33317_);
                    arrow.shoot(vector3d.x + (double) ((float) i / 10.0F), vector3d.y, vector3d.z + (double) ((float) i / 10.0F), 1.6F, 1.0F);
                    if (this.level().addFreshEntity(arrow)) {
                        this.playSound(CataclysmSounds.STRONGSWING.get());
                        this.swing(InteractionHand.MAIN_HAND);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getCurrentAnimation() == this.getAnimationState(ATTACK)) {
                ++this.attackTick;
            } else {
                if (this.attackTick > 0) {
                    this.attackTick = 0;
                }
            }
            if (this.attackTick >= 20) {
                this.setAnimationState(IDLE);
            }
        }
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.DRAUGR_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.DRAUGR_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {
        return CataclysmSounds.DRAUGR_IDLE.get();
    }

    public Summoned getDefaultSummon() {
        return new DraugrServant(GCEntityType.DRAUGR_SERVANT.get(), this.level());
    }

    public Summoned getSummon(){
        Summoned summoned = getDefaultSummon();
        if (this.getSummonList().contains(GCEntityType.DRAUGR_SERVANT.get())) {
            if (this.level().random.nextFloat() <= 0.75F) {
                summoned = new DraugrServant(GCEntityType.DRAUGR_SERVANT.get(), this.level());
            }
        }
        if (this.getSummonList().contains(GCEntityType.ELITE_DRAUGR_SERVANT.get())) {
            if (this.level().random.nextBoolean()) {
                summoned = new EliteDraugrServant(GCEntityType.ELITE_DRAUGR_SERVANT.get(), this.level());
            }
        }
        if (this.getSummonList().contains(GCEntityType.ROYAL_DRAUGR_SERVANT.get())){
            if (this.level().random.nextFloat() <= 0.15F) {
                summoned = new RoyalDraugrServant(GCEntityType.ROYAL_DRAUGR_SERVANT.get(), this.level());
            }
        }
        return summoned;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.level().isClientSide) {
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            Item item = itemstack.getItem();
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
                if (!this.spawnUndeadIdle() && pHand == InteractionHand.MAIN_HAND && itemstack.isEmpty()){
                    if (this.idleSpellCool <= 0 && this.getSpellCooldown() <= 0){
                        this.setUndeadIdle(true);
                    } else {
                        this.playSound(CataclysmSounds.DRAUGR_HURT.get());
                        this.level().broadcastEntityEvent(this, (byte) 9);
                    }
                    return InteractionResult.SUCCESS;
                } else if (item == Items.ROTTEN_FLESH && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.SKELETON_STEP, 1.0F, 1.25F);
                    this.heal(2.0F);
                    if (this.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else if (!this.getSummonList().contains(GCEntityType.DRAUGR_SERVANT.get()) && item == GCItems.CURSED_GRAVE_FOCUS.get()){
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.addSummon(GCEntityType.DRAUGR_SERVANT.get());
                    this.playSound(CataclysmSounds.DRAUGR_IDLE.get(), 1.0F, 1.5F);
                    return InteractionResult.SUCCESS;
                } else if (!this.getSummonList().contains(GCEntityType.ELITE_DRAUGR_SERVANT.get()) && item == GCItems.CURSED_CAIRN_FOCUS.get()){
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.addSummon(GCEntityType.ELITE_DRAUGR_SERVANT.get());
                    this.playSound(CataclysmSounds.DRAUGR_IDLE.get(), 1.0F, 1.5F);
                    return InteractionResult.SUCCESS;
                } else if (!this.getSummonList().contains(GCEntityType.ROYAL_DRAUGR_SERVANT.get()) && item == GCItems.CURSED_TOMB_FOCUS.get()){
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.addSummon(GCEntityType.ROYAL_DRAUGR_SERVANT.get());
                    this.playSound(CataclysmSounds.DRAUGR_IDLE.get(), 1.0F, 1.5F);
                    return InteractionResult.SUCCESS;
                } else if (itemstack.is(GCItems.SOUL_JAR_DRAUGR.get())){
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    if (this.getNecroLevel() < 2) {
                        this.setNecroLevel(this.getNecroLevel() + 1);
                    }
                    this.heal(GCAttributesConfig.DraugrNecromancerHealth.get().floatValue());
                    if (this.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                        }
                    }
                    this.playSound(CataclysmSounds.DRAUGR_IDLE.get(), 1.0F, 0.5F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public class SummonServantSpell extends SummoningSpellGoal {

        public boolean canUse() {
            Predicate<Entity> predicate = entity -> entity.isAlive() && entity instanceof IOwned owned && owned.getTrueOwner() == AbstractDraugrNecromancer.this;
            int i = AbstractDraugrNecromancer.this.level().getEntitiesOfClass(LivingEntity.class, AbstractDraugrNecromancer.this.getBoundingBox().inflate(64.0D, 16.0D, 64.0D)
                    , predicate).size();
            return super.canUse() && i < 6;
        }

        protected void castSpell(){
            if (AbstractDraugrNecromancer.this.level() instanceof ServerLevel serverLevel) {
                for (int i1 = 0; i1 < 2; ++i1) {
                    Summoned summonedentity = AbstractDraugrNecromancer.this.getSummon();
                    BlockPos blockPos = BlockFinder.SummonRadius(AbstractDraugrNecromancer.this.blockPosition(), summonedentity, serverLevel);
                    summonedentity.setTrueOwner(AbstractDraugrNecromancer.this);
                    summonedentity.moveTo(blockPos, 0.0F, 0.0F);
                    MobUtil.moveDownToGround(summonedentity);
                    if (MobsConfig.NecromancerSummonsLife.get()) {
                        summonedentity.setLimitedLife(MobUtil.getSummonLifespan(serverLevel));
                    }
                    summonedentity.setPersistenceRequired();
                    summonedentity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(AbstractDraugrNecromancer.this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                    if (serverLevel.addFreshEntity(summonedentity)){
                        SoundUtil.playNecromancerSummon(summonedentity);
                        ColorUtil colorUtil = new ColorUtil(0x2ac9cf);
                        ServerParticleUtil.windShockwaveParticle(serverLevel, colorUtil, 0.1F, 0.1F, 0.05F, -1, summonedentity.position());
                        for(int i2 = 0; i2 < serverLevel.getRandom().nextInt(10) + 10; ++i2) {
                            serverLevel.sendParticles(new MagicSmokeParticle.Option(1552608, 16777215, 10 + serverLevel.getRandom().nextInt(10), 0.2F), summonedentity.getRandomX(1.5), summonedentity.getRandomY(), summonedentity.getRandomZ(1.5), 0, 0.0, 0.0, 0.0, 1.0);
                        }
                    }
                }
            }
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.PREPARE_SUMMON.get();
        }

        @Override
        protected NecromancerSpellType getNecromancerSpellType() {
            return NecromancerSpellType.ZOMBIE;
        }

        protected void playLaughSound() {
            AbstractDraugrNecromancer.this.playSound(GoetySounds.NECROMANCER_LAUGH.get(), 2.0F, AbstractDraugrNecromancer.this.getVoicePitch() - 0.5F);
        }
    }
}
