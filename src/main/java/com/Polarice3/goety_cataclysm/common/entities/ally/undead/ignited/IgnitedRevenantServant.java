package com.Polarice3.goety_cataclysm.common.entities.ally.undead.ignited;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.goety_cataclysm.common.entities.ally.LLibraryBossSummon;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.AttackSummonMoveGoal;
import com.Polarice3.goety_cataclysm.common.entities.ally.ai.SimpleSummonAnimationGoal;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.entity.etc.path.CMPathNavigateGround;
import com.github.L_Ender.cataclysm.entity.projectile.Ashen_Breath_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Blazing_Bone_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;

public class IgnitedRevenantServant extends LLibraryBossSummon {
    private static final EntityDataAccessor<Boolean> ANGER = SynchedEntityData.defineId(IgnitedRevenantServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHIELD_DURABILITY = SynchedEntityData.defineId(IgnitedRevenantServant.class, EntityDataSerializers.INT);
    public static final Animation ASH_BREATH_ATTACK = Animation.create(53);
    public static final Animation BONE_STORM_ATTACK = Animation.create(49);
    public static final int BREATH_COOLDOWN = 200;
    public static final int STORM_COOLDOWN = 200;
    private float allowedHeightOffset = 0.5F;
    private int nextHeightOffsetChangeTick;

    public float angerProgress;
    public float prevangerProgress;
    private int breath_cooldown = 0;
    private int storm_cooldown = 0;

    public IgnitedRevenantServant(EntityType<? extends LLibraryBossSummon> entity, Level world) {
        super(entity, world);
        this.setMaxUpStep(1.5F);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        setConfigAttribute(this, CMConfig.RevenantHealthMultiplier, CMConfig.RevenantDamageMultiplier);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{
                NO_ANIMATION,
                ASH_BREATH_ATTACK,
                BONE_STORM_ATTACK};
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BoneStormGoal(this, BONE_STORM_ATTACK));
        this.goalSelector.addGoal(0, new ShootGoal(this, ASH_BREATH_ATTACK));
        this.goalSelector.addGoal(1, new WanderGoal<>(this, 1.0F, 80));
        this.goalSelector.addGoal(2, new IgnitedRevenantGoal());
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    @Override
    public int xpReward() {
        return 25;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getDirectEntity();
        if (!this.level().isClientSide) {
            if(this.getIsAnger()) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (livingEntity.getMainHandItem().getItem() instanceof AxeItem axeItem) {
                        double itemDamage = axeItem.getAttackDamage()+ 1;
                        if (damage >= itemDamage + (itemDamage / 2)) {
                            if (this.getShieldDurability() < 4) {
                                this.playSound(SoundEvents.WITHER_BREAK_BLOCK, 1.0F, 1.5F);

                                this.setShieldDurability(this.getShieldDurability() + 1);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        if (damage > 0.0F && canBlockDamageSource(source)) {
            this.hurtCurrentlyUsedShield(damage);
            if (!source.is(DamageTypeTags.IS_PROJECTILE)) {
                if (entity instanceof LivingEntity) {
                    this.blockUsingShield((LivingEntity) entity);
                }
            }
            this.playSound(SoundEvents.ANVIL_PLACE, 0.3F, 0.5F);
            return false;
        }
        return super.hurt(source, damage);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        Entity entity = damageSourceIn.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow arrow) {
            if (arrow.getPierceLevel() > 0) {
                flag = true;
            }
        }
        if (!damageSourceIn.is(DamageTypeTags.BYPASSES_SHIELD)&& !flag && this.getIsAnger() && this.getShieldDurability() < 4) {
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGER, false);
        this.entityData.define(SHIELD_DURABILITY, 0);
    }

    public void setIsAnger(boolean isAnger) {
        this.entityData.set(ANGER, isAnger);
    }

    public boolean getIsAnger() {
        return this.entityData.get(ANGER);
    }

    public void setShieldDurability(int ShieldDurability) {
        this.entityData.set(SHIELD_DURABILITY, ShieldDurability);
    }

    public int getShieldDurability() {
        return this.entityData.get(SHIELD_DURABILITY);
    }

    public void tick() {
        super.tick();
        // setYRot(yBodyRot);
        LivingEntity target = this.getTarget();
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }
        prevangerProgress = angerProgress;
        if (this.getIsAnger() && angerProgress < 5F) {
            angerProgress++;
        }
        if (!this.getIsAnger() && angerProgress > 0F) {
            angerProgress--;
        }
        if (breath_cooldown > 0) {
            breath_cooldown--;
        }
        if (storm_cooldown > 0) {
            storm_cooldown--;
        }
        if (this.isAlive()) {
            if (target != null && target.isAlive()) {
                if (breath_cooldown <= 0 && !isNoAi() && this.getAnimation() == NO_ANIMATION && (this.random.nextInt(35) == 0 && this.distanceTo(target) < 4.5F) && this.getShieldDurability() < 4) {
                    breath_cooldown = BREATH_COOLDOWN;
                    this.setAnimation(ASH_BREATH_ATTACK);
                } else if (storm_cooldown <= 0 && this.distanceTo(target) < 6 && !isNoAi() && this.getAnimation() == NO_ANIMATION && this.random.nextInt(15) == 0) {
                    storm_cooldown = STORM_COOLDOWN;
                    this.setAnimation(BONE_STORM_ATTACK);
                }else if (!isNoAi() && this.getAnimation() == NO_ANIMATION && (this.random.nextInt(12) == 0 && this.distanceTo(target) < 4.5F) && this.getShieldDurability() > 3) {
                    this.setAnimation(ASH_BREATH_ATTACK);
                }
            }
            if(this.getAnimation() == NO_ANIMATION && this.getIsAnger() && this.getShieldDurability() < 4){
                if(this.tickCount % (6 + this.getShieldDurability() * 2) == 0){
                    for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.25D))) {
                        if (!MobUtil.areAllies(this, entity)) {
                            boolean flag = entity.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                            if (flag) {
                                double d0 = entity.getX() - this.getX();
                                double d1 = entity.getZ() - this.getZ();
                                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                                entity.push(d0 / d2 * 1.5D, 0.2D, d1 / d2 * 1.5D);
                            }
                        }
                    }
                }
            }
        }

    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.REVENANT_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.REVENANT_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.REVENANT_DEATH.get();
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new CMPathNavigateGround(this, worldIn);
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.level().isClientSide){
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            Item item = itemstack.getItem();
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
                if ((itemstack.is(Tags.Items.BONES) || itemstack.is(Tags.Items.RODS_BLAZE) || item == Items.BLAZE_POWDER) && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(ModSounds.REVENANT_IDLE.get(), 1.0F, 1.25F);
                    float healAmount = 1.0F;
                    if (itemstack.is(Tags.Items.BONES)){
                        healAmount = 2.0F;
                    }
                    if (itemstack.is(Tags.Items.RODS_BLAZE)){
                        healAmount = 4.0F;
                    }
                    this.heal(healAmount);
                    if (this.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                        }
                    }
                    pPlayer.swing(pHand);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public void tryKill(Player player) {
        if (this.killChance <= 0) {
            this.warnKill(player);
        } else {
            super.tryKill(player);
        }
    }

    class IgnitedRevenantGoal extends AttackSummonMoveGoal {

        public IgnitedRevenantGoal() {
            super(IgnitedRevenantServant.this, true, 1.1);
        }

        @Override
        public void start() {
            super.start();
            IgnitedRevenantServant.this.setIsAnger(true);

        }

        @Override
        public void stop() {
            super.stop();
            IgnitedRevenantServant.this.setIsAnger(false);
        }
    }

    class ShootGoal extends SimpleSummonAnimationGoal<IgnitedRevenantServant> {

        public ShootGoal(IgnitedRevenantServant entity, Animation animation) {
            super(entity, animation);
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public void start() {
            super.start();
            IgnitedRevenantServant.this.setIsAnger(true);

        }

        @Override
        public void stop() {
            super.stop();
            IgnitedRevenantServant.this.setIsAnger(false);
        }

        public void tick() {
            LivingEntity target = IgnitedRevenantServant.this.getTarget();

            if (target != null) {
                if (IgnitedRevenantServant.this.getAnimationTick() < 27) {
                    IgnitedRevenantServant.this.getLookControl().setLookAt(target, 30.0F, 30.0F);
                }else{
                    IgnitedRevenantServant.this.getLookControl().setLookAt(target, 3.0F, 30.0F);
                }
            }

            if (IgnitedRevenantServant.this.getAnimationTick() == 21) {
                IgnitedRevenantServant.this.playSound(ModSounds.REVENANT_BREATH.get(), 1.0f, 1.0f);

            }

            Vec3 mouthPos = new Vec3(0, 2.3, 0);
            mouthPos = mouthPos.yRot((float) Math.toRadians(-getYRot() - 90));
            mouthPos = mouthPos.add(position());
            mouthPos = mouthPos.add(new Vec3(0, 0, 0).xRot((float) Math.toRadians(-getXRot())).yRot((float) Math.toRadians(-yHeadRot)));
            Ashen_Breath_Entity breath = new Ashen_Breath_Entity(ModEntities.ASHEN_BREATH.get(), IgnitedRevenantServant.this.level(), (float) CMConfig.Ashenbreathdamage,IgnitedRevenantServant.this);
            if (IgnitedRevenantServant.this.getAnimationTick() == 27) {
                breath.absMoveTo(mouthPos.x, mouthPos.y, mouthPos.z, IgnitedRevenantServant.this.yHeadRot, IgnitedRevenantServant.this.getXRot());
                IgnitedRevenantServant.this.level().addFreshEntity(breath);
            }
        }
    }

    class BoneStormGoal extends SimpleSummonAnimationGoal<IgnitedRevenantServant> {

        public BoneStormGoal(IgnitedRevenantServant entity, Animation animation) {
            super(entity, animation);
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public void tick() {
            LivingEntity target = entity.getTarget();

            if (target != null) {
                entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            if (entity.getAnimationTick() == 5) {
                switch (random.nextInt(3)) {
                    case 0 -> launchbone1();
                    case 1 -> launchbone2();
                    case 2 -> launchbone3();
                    default -> {
                    }
                }

            }
            if(entity.getAnimationTick() == 10){
                switch (random.nextInt(3)) {
                    case 0 -> launchbone1();
                    case 1 -> launchbone2();
                    case 2 -> launchbone3();
                    default -> {
                    }
                }
            }
            if(entity.getAnimationTick() == 15){
                switch (random.nextInt(3)) {
                    case 0 -> launchbone1();
                    case 1 -> launchbone2();
                    case 2 -> launchbone3();
                    default -> {
                    }
                }
            }
            if(entity.getAnimationTick() == 20){
                switch (random.nextInt(3)) {
                    case 0 -> launchbone1();
                    case 1 -> launchbone2();
                    case 2 -> launchbone3();
                    default -> {
                    }
                }
            }
            --entity.nextHeightOffsetChangeTick;
            if (entity.nextHeightOffsetChangeTick <= 0) {
                entity.nextHeightOffsetChangeTick = 100;
                entity.allowedHeightOffset = (float)entity.random.triangle(0.5D, 6.891D);
            }

            if (target != null && target.getEyeY() > entity.getEyeY() + (double)entity.allowedHeightOffset && entity.canAttack(target)) {
                Vec3 vec3 = entity.getDeltaMovement();
                entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, ((double)0.3F - vec3.y) * (double)0.3F, 0.0D));
                entity.hasImpulse = true;
            }

        }
    }

    private void launchbone1() {
        this.playSound(SoundEvents.TRIDENT_THROW, 1F, 0.75f);
        for (int i = 0; i < 8; i++) {
            float yawRadians = (float) (Math.toRadians(90 + this.getYRot()));
            float throwAngle = yawRadians + i * Mth.PI / 4F;

            double sx = this.getX() + (Mth.cos(throwAngle) * 1);
            double sy = this.getY() + (this.getBbHeight() * 0.62D);
            double sz = this.getZ() + (Mth.sin(throwAngle) * 1);

            double vx = Mth.cos(throwAngle);
            double vy = 0;
            double vz = Mth.sin(throwAngle);

            Blazing_Bone_Entity projectile = new Blazing_Bone_Entity(this.level(), (float) CMConfig.BlazingBonedamage,this);

            projectile.moveTo(sx, sy, sz, i * 45F, this.getXRot());
            float speed = 0.5F;
            projectile.shoot(vx, vy, vz, speed, 1.0F);
            this.level().addFreshEntity(projectile);
        }

    }

    private void launchbone2() {
        this.playSound(SoundEvents.TRIDENT_THROW, 1F, 0.75f);
        for (int i = 0; i < 6; i++) {
            float yawRadians = (float) (Math.toRadians(90 + this.getYRot()));
            float throwAngle = yawRadians +  i * Mth.PI / 3F;

            double sx = this.getX() + (Mth.cos(throwAngle) * 1);
            double sy = this.getY() + (this.getBbHeight() * 0.62D);
            double sz = this.getZ() + (Mth.sin(throwAngle) * 1);

            double vx = Mth.cos(throwAngle);
            double vy = 0;
            double vz = Mth.sin(throwAngle);

            Blazing_Bone_Entity projectile = new Blazing_Bone_Entity(this.level(), (float) CMConfig.BlazingBonedamage,this);

            projectile.moveTo(sx, sy, sz, i * 60F, this.getXRot());
            float speed = 0.6F;
            projectile.shoot(vx, vy, vz, speed, 1.0F);
            this.level().addFreshEntity(projectile);
        }

    }

    private void launchbone3() {
        this.playSound(SoundEvents.TRIDENT_THROW, 1F, 0.75f);
        for (int i = 0; i < 10; i++) {
            float yawRadians = (float) (Math.toRadians(90 + this.getYRot()));
            float throwAngle = yawRadians + i * Mth.PI / 5F;

            double sx = this.getX() + (Mth.cos(throwAngle) * 1);
            double sy = this.getY() + (this.getBbHeight() * 0.62D);
            double sz = this.getZ() + (Mth.sin(throwAngle) * 1);

            double vx = Mth.cos(throwAngle);
            double vy = 0;
            double vz = Mth.sin(throwAngle);

            Blazing_Bone_Entity projectile = new Blazing_Bone_Entity(this.level(), (float) CMConfig.BlazingBonedamage,this);

            projectile.moveTo(sx, sy, sz, i * 36F, this.getXRot());
            float speed = 0.4F;
            projectile.shoot(vx, vy, vz, speed, 1.0F);
            this.level().addFreshEntity(projectile);
        }

    }
}
