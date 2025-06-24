package com.Polarice3.goety_cataclysm.common.entities.ally.undead.desert;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.projectiles.FlyingItem;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.InternalAnimationSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonAttackGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.InternalSummonStateGoal;
import com.Polarice3.goety_cataclysm.common.items.CataclysmItems;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.common.items.revive.WarriorSpirit;
import com.Polarice3.goety_cataclysm.config.GCAttributesConfig;
import com.Polarice3.goety_cataclysm.config.GCMobsConfig;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.client.particle.RingParticle;
import com.github.L_Ender.cataclysm.entity.effect.Cm_Falling_Block_Entity;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.projectile.Poison_Dart_Entity;
import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModParticle;
import com.github.L_Ender.cataclysm.init.ModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class KobolediatorServant extends InternalAnimationSummon {
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState sleepAnimationState = new AnimationState();
    public AnimationState awakeAnimationState = new AnimationState();
    public AnimationState sword1AnimationState = new AnimationState();
    public AnimationState sword2AnimationState = new AnimationState();
    public AnimationState chargeprepareAnimationState = new AnimationState();
    public AnimationState chargeAnimationState = new AnimationState();
    public AnimationState chargeendAnimationState = new AnimationState();
    public AnimationState blockAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();
    public static final int SLEEP = 1;
    public static final int AWAKE = 2;
    public static final int SWORD_1 = 3;
    public static final int SWORD_2 = 4;
    public static final int CHARGE_PREPARE = 5;
    public static final int CHARGE = 6;
    public static final int CHARGE_END = 7;
    public static final int DEATH = 8;
    public static final int BLOCK = 9;
    private int earthquake_cooldown = 0;
    public static final int EARTHQUAKE_COOLDOWN = 80;

    private int charge_cooldown = 0;
    public static final int CHARGE_COOLDOWN = 160;

    public KobolediatorServant(EntityType<? extends Summoned> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.25F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new WanderGoal<>(this, 1.0F, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new InternalSummonMoveGoal(this,false,1.0D));

        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, SWORD_1, 0, 50, 15, 12){
            @Override
            public boolean canUse() {
                return super.canUse() && KobolediatorServant.this.getRandom().nextFloat() * 100.0F < 16f && KobolediatorServant.this.earthquake_cooldown <= 0;
            }
            @Override
            public void stop() {
                super.stop();
                KobolediatorServant.this.earthquake_cooldown = EARTHQUAKE_COOLDOWN;
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, SWORD_2, 0, 100, 64, 8));

        //chargePrepare
        this.goalSelector.addGoal(1, new InternalSummonAttackGoal(this, 0, CHARGE_PREPARE, CHARGE, 40, 30, 15) {
            @Override
            public boolean canUse() {
                return super.canUse() && KobolediatorServant.this.getRandom().nextFloat() * 100.0F < 9f && KobolediatorServant.this.charge_cooldown <= 0;
            }
        });

        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, CHARGE, CHARGE, CHARGE_END, 30, 0){
            @Override
            public void tick() {
                if(this.entity.onGround()){
                    Vec3 vector3d = this.entity.getDeltaMovement();
                    float f = this.entity.getYRot() * ((float)Math.PI / 180F);
                    Vec3 vector3d1 = new Vec3(-Mth.sin(f), this.entity.getDeltaMovement().y, Mth.cos(f)).scale(0.7D).add(vector3d.scale(0.5D));
                    this.entity.setDeltaMovement(vector3d1.x, this.entity.getDeltaMovement().y, vector3d1.z);
                }
            }
        });
        this.goalSelector.addGoal(0, new InternalSummonAttackGoal(this, CHARGE, CHARGE_END, 0, 40, 40, 5) {

            @Override
            public void stop() {
                super.stop();
                KobolediatorServant.this.charge_cooldown = CHARGE_COOLDOWN;
            }
        });
        this.goalSelector.addGoal(0, new InternalSummonStateGoal(this, CHARGE_END, CHARGE_END, 0, 40, 40) {
            @Override
            public void stop() {
                super.stop();
                KobolediatorServant.this.charge_cooldown = CHARGE_COOLDOWN;
            }
        });
        this.goalSelector.addGoal(1, new InternalSummonStateGoal(this, SLEEP, SLEEP, 0, 0, 0){
            @Override
            public void tick() {
                this.entity.setDeltaMovement(0, this.entity.getDeltaMovement().y, 0);
            }
        });

        this.goalSelector.addGoal(0, new InternalSummonAttackGoal(this, SLEEP, AWAKE, 0, 70, 0, 18));
        this.goalSelector.addGoal(0, new InternalSummonStateGoal(this, BLOCK, BLOCK, 0, 18, 0, false));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, GCAttributesConfig.KobolediatorDamage.get())
                .add(Attributes.MAX_HEALTH, GCAttributesConfig.KobolediatorHealth.get())
                .add(Attributes.ARMOR, GCAttributesConfig.KobolediatorArmor.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), GCAttributesConfig.KobolediatorHealth.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), GCAttributesConfig.KobolediatorArmor.get());
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), GCAttributesConfig.KobolediatorDamage.get());
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof KobolediatorServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return GCSpellConfig.KobolediatorLimit.get();
    }

    public ItemEntity spawnAtLocation(ItemStack stack) {
        ItemEntity itementity = this.spawnAtLocation(stack, 0.0F);
        if (itementity != null) {
            itementity.setGlowingTag(true);
            itementity.setExtendedLifetime();
        }

        return itementity;
    }

    @Override
    public int xpReward() {
        return 35;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getDirectEntity();
        if (entity instanceof Poison_Dart_Entity) {
            return false;
        }
        if (this.canBlockDamageSource(source)) {
            if (entity instanceof AbstractArrow) {
                float f = 170.0F + this.random.nextFloat() * 80.0F;
                entity.setDeltaMovement(entity.getDeltaMovement().scale(1.5));
                entity.setYRot(entity.getYRot() + f);
                entity.hurtMarked = true;
            }
            if (this.getAttackState() == 0) {
                this.setAttackState(BLOCK);
                this.playSound(SoundEvents.ANVIL_LAND, 1.0F, 2);
            }
            return false;
        }
        if (this.isSleep() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        return super.hurt(source, damage);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        boolean flag = false;
        if (!this.isNoAi() && damageSourceIn.is(DamageTypeTags.IS_PROJECTILE) && !flag && (this.getAttackState() == 0 || this.getAttackState() == 9)) {
            Vec3 vector3d2 = damageSourceIn.getSourcePosition();
            if (vector3d2 != null) {
                Vec3 vector3d = this.getViewVector(1.0F);
                Vec3 vector3d1 = vector3d2.vectorTo(this.position()).normalize();
                vector3d1 = new Vec3(vector3d1.x, 0.0D, vector3d1.z);
                return vector3d1.dot(vector3d) < 0.0D;
            }
        }
        return false;
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }


    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    public AnimationState getAnimationState(String input) {
        if (Objects.equals(input, "sleep")) {
            return this.sleepAnimationState;
        } else if (Objects.equals(input, "awake")) {
            return this.awakeAnimationState;
        } else if (Objects.equals(input, "sword1")) {
            return this.sword1AnimationState;
        } else if (Objects.equals(input, "sword2")) {
            return this.sword2AnimationState;
        } else if (Objects.equals(input, "idle")) {
            return this.idleAnimationState;
        } else if (Objects.equals(input, "charge_prepare")) {
            return this.chargeprepareAnimationState;
        } else if (Objects.equals(input, "charge")) {
            return this.chargeAnimationState;
        } else if (Objects.equals(input, "charge_end")) {
            return this.chargeendAnimationState;
        } else if (Objects.equals(input, "death")) {
            return this.deathAnimationState;
        } else if (Objects.equals(input, "block")) {
            return this.blockAnimationState;
        }else {
            return new AnimationState();
        }
    }

    public boolean isSleep() {
        return this.getAttackState() == SLEEP || this.getAttackState() == AWAKE;
    }

    public void setSleep(boolean sleep) {
        this.setAttackState(sleep ? SLEEP : 0);
    }

    public boolean canBeSeenAsEnemy() {
        return !this.isSleep() && super.canBeSeenAsEnemy();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            switch (this.getAttackState()) {
                case 0 -> this.stopAllAnimationStates();
                case 1 -> {
                    this.stopAllAnimationStates();
                    this.sleepAnimationState.startIfStopped(this.tickCount);
                }
                case 2 -> {
                    this.stopAllAnimationStates();
                    this.awakeAnimationState.startIfStopped(this.tickCount);
                }
                case 3 -> {
                    this.stopAllAnimationStates();
                    this.sword1AnimationState.startIfStopped(this.tickCount);
                }
                case 4 -> {
                    this.stopAllAnimationStates();
                    this.sword2AnimationState.startIfStopped(this.tickCount);
                }
                case 5 -> {
                    this.stopAllAnimationStates();
                    this.chargeprepareAnimationState.startIfStopped(this.tickCount);
                }
                case 6 -> {
                    this.stopAllAnimationStates();
                    this.chargeAnimationState.startIfStopped(this.tickCount);
                }
                case 7 -> {
                    this.stopAllAnimationStates();
                    this.chargeendAnimationState.startIfStopped(this.tickCount);
                }
                case 8 -> {
                    this.stopAllAnimationStates();
                    this.deathAnimationState.startIfStopped(this.tickCount);
                }
                case 9 -> {
                    this.stopAllAnimationStates();
                    this.blockAnimationState.startIfStopped(this.tickCount);
                }
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.sleepAnimationState.stop();
        this.awakeAnimationState.stop();
        this.sword1AnimationState.stop();
        this.sword2AnimationState.stop();
        this.chargeprepareAnimationState.stop();
        this.chargeAnimationState.stop();
        this.blockAnimationState.stop();
        this.chargeendAnimationState.stop();
        this.deathAnimationState.stop();
    }

    public void die(DamageSource p_21014_) {
        if (this.getTrueOwner() != null && GCMobsConfig.KobolediatorSpirit.get()) {
            ItemStack itemStack = new ItemStack(GCItems.WARRIOR_SPIRIT.get());
            WarriorSpirit.setOwnerName(this.getTrueOwner(), itemStack);
            WarriorSpirit.setSummon(this, itemStack);
            FlyingItem flyingItem = new FlyingItem(ModEntityType.FLYING_ITEM.get(), this.level(), this.getX(), this.getY(), this.getZ());
            flyingItem.setOwner(this.getTrueOwner());
            flyingItem.setItem(itemStack);
            flyingItem.setParticle(ModParticle.SANDSTORM.get());
            flyingItem.setSecondsCool(30);
            this.level().addFreshEntity(flyingItem);
        }
        super.die(p_21014_);
        this.setAttackState(DEATH);
    }

    public int deathTimer(){
        return 60;
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("is_Sleep", this.isSleep());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSleep(compound.getBoolean("is_Sleep"));
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(!this.walkAnimation.isMoving() && this.getAttackState() == 0, this.tickCount);
        }
        if (earthquake_cooldown > 0) {
            earthquake_cooldown--;
        }
        if (charge_cooldown > 0) {
            charge_cooldown--;
        }

    }

    public void aiStep() {
        super.aiStep();
        if (this.getAttackState() == AWAKE) {
            if (this.attackTicks == 12) {
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.06f, 0, 20);
            }
        }

        if (this.getAttackState() == SWORD_1) {
            if (this.attackTicks == 20) {
                this.AreaAttack(10.0f,6.0F,60,1,120);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.1f, 0, 20);
                this.MakeParticle(0.5f, 9.0f, 1.2f);
            }
            for (int l = 19; l <= 28; l = l + 2) {
                if (this.attackTicks == l) {
                    int d = l - 17;
                    int d2 = l - 16;
                    float ds = (float) (d + d2) / 2;
                    this.StompDamage(0.25f, d, 5,1.05F, 2.0f, -0.2f, 0, 1.0f);
                    this.StompDamage(0.25f, d2, 5,1.05F, 2.0f, -0.2f, 0, 1.0f);
                    this.StompSound(ds,-0.2F);
                }
            }
        }

        if (this.getAttackState() == SWORD_2) {
            if (this.attackTicks == 18) {
                this.AreaAttack(9.0f,6.0F,270,1,0);
                this.playSound(CataclysmSounds.STRONGSWING.get(), 1F, 1.0f);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.05f, 0, 10);
            }
            if (this.attackTicks == 36) {
                this.AreaAttack(9.0f,6.0F,270,1,0);
                this.playSound(CataclysmSounds.STRONGSWING.get(), 1F, 1.0f);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.05f, 0, 10);
            }
            if (this.attackTicks == 65) {
                this.AreaAttack(10.0f,6.0F,45,1.25F,120);
                this.playSound(CataclysmSounds.REMNANT_STOMP.get(), 1F, 1.0f);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.1f, 0, 20);
                this.MakeParticle(0.5f, 9.0f, 1.8f);
            }
        }
        if (this.getAttackState() == CHARGE) {
            /*if (!this.level().isClientSide) {
                if(CMConfig.KobolediatorBlockBreaking) {
                    ChargeBlockBreaking();
                }else{
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                        ChargeBlockBreaking();
                    }
                }
            }*/
            if (this.tickCount % 4 == 0) {
                for (LivingEntity Lentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5D))) {
                    if (!MobUtil.areAllies(this, Lentity)) {
                        boolean flag = Lentity.hurt(this.getMobAttack(), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.4F);
                        if (flag) {
                            if (Lentity.onGround()) {
                                double d0 = Lentity.getX() - this.getX();
                                double d1 = Lentity.getZ() - this.getZ();
                                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                                float f = 1.5F;
                                Lentity.push(d0 / d2 * f, 0.5F, d1 / d2 * f);
                            }
                        }
                    }
                }
            }
        }
        if (this.getAttackState() == CHARGE_END) {
            if (this.attackTicks == 5) {
                this.AreaAttack(9.0f,6.0F,200,1.25F,120);
                this.playSound(CataclysmSounds.STRONGSWING.get(), 1F, 1.0f);
                ScreenShake_Entity.ScreenShake(level(), this.position(), 15, 0.05f, 0, 10);
            }
        }

    }

    private void MakeParticle(float size, float vec, float math) {
        if (this.level().isClientSide) {
            float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
            double theta = (this.yBodyRot) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            for (int i1 = 0; i1 < 80 + this.random.nextInt(12); i1++) {
                double DeltaMovementX = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementY = getRandom().nextGaussian() * 0.07D;
                double DeltaMovementZ = getRandom().nextGaussian() * 0.07D;
                float angle = (0.01745329251F * this.yBodyRot) + i1;
                double extraX = size * Mth.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = size * Mth.cos(angle);
                int hitX = Mth.floor(getX() + vec * vecX + extraX);
                int hitY = Mth.floor(getY());
                int hitZ = Mth.floor(getZ() + vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = this.level().getBlockState(hit.below());
                if (block.getRenderShape() != RenderShape.INVISIBLE) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), getX() + vec * vecX + extraX + f * math, this.getY() + extraY, getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);
                }
            }
            this.level().addParticle(new RingParticle.RingData(0f, (float)Math.PI/2f, 30, 1.0f, 1.0F,  1.0F, 1.0f, 20f, false, RingParticle.EnumRingBehavior.GROW_THEN_SHRINK), getX() + vec * vecX + f * math, getY() + 0.2f, getZ() + vec * vecZ + f1 * math, 0, 0, 0);

        }
    }

    private void ChargeBlockBreaking(){
        boolean flag = false;
        AABB aabb = this.getBoundingBox().inflate(0.5D, 0.2D, 0.5D);
        for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(this.getY()), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (!blockstate.isAir() && blockstate.canEntityDestroy(this.level(), blockpos, this) && !blockstate.is(ModTag.REMNANT_IMMUNE) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                if (random.nextInt(6) == 0 && !blockstate.hasBlockEntity()) {
                    Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(level(), blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, blockstate, 20);
                    flag = this.level().destroyBlock(blockpos, false, this) || flag;
                    fallingBlockEntity.setDeltaMovement(fallingBlockEntity.getDeltaMovement().add(this.position().subtract(fallingBlockEntity.position()).multiply((-1.2D + random.nextDouble()) / 3, 0.2D + getRandom().nextGaussian() * 0.15D, (-1.2D + random.nextDouble()) / 3)));
                    this.level().addFreshEntity(fallingBlockEntity);
                } else {
                    flag = this.level().destroyBlock(blockpos, false, this) || flag;
                }
            }
        }
    }


    private void StompSound(float distance, float math) {
        double theta = (this.yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
        float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
        this.level().playSound((Player)null, this.getX() + distance * vecX + f * math, this.getY(), this.getZ() + distance * vecZ + f1 * math, CataclysmSounds.REMNANT_STOMP.get(), this.getSoundSource(), 0.6f, 1.0f);
    }

    private void AreaAttack(float range, float height, float arc, float damage, int shieldbreakticks) {
        List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(range, height, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX()));
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                if (!MobUtil.areAllies(this, entityHit)) {
                    entityHit.hurt(this.getMobAttack(), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                    if (entityHit.isDamageSourceBlocked(this.getMobAttack()) && shieldbreakticks > 0) {
                        this.disableShield(entityHit, shieldbreakticks);
                    }

                }
            }
        }
    }

    private void StompDamage(float spreadarc, int distance, int height, float mxy, float vec,float math, int shieldbreakticks, float damage) {
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        double spread = Math.PI * spreadarc;
        int arcLen = Mth.ceil(distance * spread);
        float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F)) ;
        float f1 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F)) ;
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = this.getX() + vx * distance + vec * Math.cos((yBodyRot + 90) * Math.PI / 180) + f * math;
            double pz = this.getZ() + vz * distance + vec * Math.sin((yBodyRot + 90) * Math.PI / 180  + f1 * math);
            float factor = 1 - distance / (float) 12;
            int hitX = Mth.floor(px);
            int hitZ = Mth.floor(pz);
            BlockPos pos = new BlockPos(hitX, hitY + height, hitZ);
            BlockState block = this.level().getBlockState(pos);

            int maxDepth = 30;
            for (int depthCount = 0; depthCount < maxDepth; depthCount++) {
                if (block.getRenderShape() == RenderShape.MODEL) {
                    break;
                }
                pos = pos.below();
                block = this.level().getBlockState(pos);
            }

            if (block.getRenderShape() != RenderShape.MODEL) {
                block = Blocks.AIR.defaultBlockState();
            }
            this.spawnBlocks(hitX,hitY + height ,hitZ, (int) (this.getY() - height),block, px, pz, mxy, vx, vz, factor, shieldbreakticks, damage);

        }
    }


    private void spawnBlocks(int hitX, int hitY, int hitZ, int lowestYCheck,BlockState blockState,double px,double pz,float mxy,double vx,double vz,float factor, int shieldbreakticks,float damage) {
        BlockPos blockpos = new BlockPos(hitX, hitY, hitZ);
        BlockState block = this.level().getBlockState(blockpos);
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level(), blockpos1, Direction.UP)) {
                if (!this.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(lowestYCheck) - 1);


        Cm_Falling_Block_Entity fallingBlockEntity = new Cm_Falling_Block_Entity(this.level(), hitX + 0.5D, (double)blockpos.getY() + d0 + 0.5D, hitZ + 0.5D, blockState, 10);
        fallingBlockEntity.push(0, 0.2D + this.getRandom().nextGaussian() * 0.04D, 0);
        this.level().addFreshEntity(fallingBlockEntity);

        AABB selection = new AABB(px - 0.5, (double)blockpos.getY() + d0 -1, pz - 0.5, px + 0.5, (double)blockpos.getY() + d0 + mxy, pz + 0.5);
        List<LivingEntity> hit = this.level().getEntitiesOfClass(LivingEntity.class, selection);
        for (LivingEntity entity : hit) {
            if (!MobUtil.areAllies(this, entity)) {
                boolean flag = entity.hurt(this.getMobAttack(), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage));
                if (entity.isDamageSourceBlocked(this.getMobAttack()) && shieldbreakticks > 0) {
                    disableShield(entity, shieldbreakticks);
                }

                if (flag) {
                    double magnitude = -4;
                    double x = vx * (1 - factor) * magnitude;
                    double y = 0;
                    if (entity.onGround()) {
                        y += 0.15;
                    }
                    double z = vz * (1 - factor) * magnitude;
                    entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
                }
            }
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CataclysmSounds.KOBOLEDIATOR_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CataclysmSounds.KOBOLEDIATOR_DEATH.get();
    }

    protected SoundEvent getAmbientSound() {
        return this.isSleep() ? super.getAmbientSound() : CataclysmSounds.KOBOLEDIATOR_AMBIENT.get();
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return p_34192_.getEffect() != ModEffect.EFFECTSTUN.get() && p_34192_.getEffect() != ModEffect.EFFECTABYSSAL_CURSE.get() && super.canBeAffected(p_34192_);
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand p_230254_2_) {
        ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if ((itemstack.is(Tags.Items.BONES) || itemstack.is(CataclysmItems.KOBOLETON_BONE.get())) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
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
            }
        }
        return InteractionResult.PASS;
    }
}
