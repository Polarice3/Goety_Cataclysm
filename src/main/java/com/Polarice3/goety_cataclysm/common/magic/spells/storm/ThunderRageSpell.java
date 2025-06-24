package com.Polarice3.goety_cataclysm.common.magic.spells.storm;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.items.GCItems;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import com.github.L_Ender.cataclysm.client.particle.CircleLightningParticle;
import com.github.L_Ender.cataclysm.client.particle.StormParticle;
import com.github.L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Lightning_Spear_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Water_Spear_Entity;
import com.github.L_Ender.cataclysm.init.ModParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ThunderRageSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.ThunderRageCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return 150;
    }

    public int castDuration(LivingEntity caster) {
        return 150;
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.ThunderRageCoolDown.get();
    }

    @Nullable
    @Override
    public SoundEvent CastingSound(LivingEntity caster) {
        return GoetySounds.ZAP.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.VELOCITY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void useSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int castTime, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        float velocity = spellStat.getVelocity();
        double radius = spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 10.0F;
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        float f = Mth.cos(caster.yBodyRot * ((float) Math.PI / 180F));
        float f1 = Mth.sin(caster.yBodyRot * ((float) Math.PI / 180F));
        double theta = (caster.yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        BlockPos.MutableBlockPos moved = new BlockPos.MutableBlockPos(caster.getX(), caster.getY(), caster.getZ());

        while(moved.getY() < caster.getY() + 9.0D && !worldIn.getBlockState(moved).blocksMotion()) {
            moved.move(Direction.UP);
        }
        double height = moved.getY() - caster.getY();
        if (castTime < 115 && castTime > 55) {
            CircleLighning(worldIn, caster, 0.2F, 0.2F, (caster.getRandom().nextFloat() - 0.5F) * 12, height, 3, 1);
            Stormknockback(caster, 0.7F, 5.5D);
            float r = 143 / 255F;
            float g = 241 / 255F;
            float b = 215 / 255F;
            worldIn.sendParticles((new StormParticle.OrbData(99 / 255F, 194 / 255F, 224 / 255F, 6.0F + caster.getRandom().nextFloat() * 0.25f, 1.5F + caster.getRandom().nextFloat() * 0.45f, caster.getId())), caster.getX(), caster.getY(), caster.getZ(), 1, 0, 0, 0, 0);
            worldIn.sendParticles((new StormParticle.OrbData(r, g, b, 4.0F + caster.getRandom().nextFloat() * 1.2F, 1.0F + caster.getRandom().nextFloat() * 0.45f, caster.getId())), caster.getX(), caster.getY(), caster.getZ(), 1, 0, 0, 0, 0);
            worldIn.sendParticles((new StormParticle.OrbData(r, g, b, 2.0F + caster.getRandom().nextFloat() * 0.7F, 0.35F + caster.getRandom().nextFloat() * 0.45f, caster.getId())), caster.getX(), caster.getY(), caster.getZ(), 1, 0, 0, 0, 0);
        }
        if (castTime < 140 && castTime > 55) {
            Nimbo(worldIn, caster, 0.2F, 0.2F, 5, height + 0.5D ,5,2);
        }
        if (castTime == 55) {
            ScreenShake_Entity.ScreenShake(worldIn, caster.position(), 15, 0.02f, 20, 10);
        }
        if (castTime == 75) {
            ScreenShake_Entity.ScreenShake(worldIn, caster.position(), 20, 0.03f, 20, 10);
        }
        if (castTime == 95) {
            ScreenShake_Entity.ScreenShake(worldIn, caster.position(), 25, 0.04f, 20, 10);
        }
        if (castTime == 114) {
            this.playSound(worldIn, caster, CataclysmSounds.SUPER_LIGHTNING.get(), 0.4f, 1.0F + caster.getRandom().nextFloat() * 0.1F);
            double d0 = caster.getX() + vecX * 0.2F + f * 0.2F;
            double d1 = caster.getY() + height;
            double d2 = caster.getZ() + vecZ * 0.2F + f1 * 0.2F;
            worldIn.sendParticles(ModParticle.LIGHTNING_EXPLODE.get(), d0,d1,d2, 1, 0, 0, 0, 0);
        }
        int i = 4;
        if (this.rightStaff(staff)) {
            i -= 2;
        }
        for (int l = 115; l <= 145; l = l + i) {
            if (castTime == l) {
                float f2 = caster.getRandom().nextFloat() * ((float) Math.PI * 2F);
                float f3 = Mth.sqrt(caster.getRandom().nextFloat()) * 5;
                float math = 0.2F;
                double d0 = caster.getX() + f * math + (double) (Mth.cos(f2) * f3);
                double d1 = caster.getY() + (0.9333D * height);
                double d2 = caster.getZ() + f1 * math + (double) (Mth.sin(f2) * f3);
                HitResult rayTrace = this.rayTrace(worldIn, caster, 16, 3.0D);
                Vec3 location = rayTrace.getLocation();
                LivingEntity target = this.getTarget(caster);
                if (target != null) {
                    location = target.position().add(0.0D, target.getBbHeight() * 0.35D, 0.0D);
                }
                double d3 = (location.x - ((caster.getRandom().nextDouble() - 0.5F) * 6)) - d0;
                double d4 = (location.y - caster.getRandom().nextDouble()) - d1;
                double d5 = (location.z - ((caster.getRandom().nextDouble() - 0.5F) * 6)) - d2;
                Vec3 vec3 = new Vec3(d3, d4, d5).normalize();
                float yRot = (float) (Mth.atan2(vec3.z, vec3.x) * (180F / Math.PI)) + 90F;

                float xRot = (float) -(Mth.atan2(vec3.y, Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z)) * (180F / Math.PI));

                Water_Spear_Entity water = new Water_Spear_Entity(caster, vec3, caster.level(), GCSpellConfig.WaterSpearDamage.get().floatValue() + potency);
                water.accelerationPower += velocity;
                water.setYRot(yRot);
                water.setXRot(xRot);
                water.setPosRaw(d0, d1, d2);
                water.setTotalBounces(3 + potency);

                worldIn.addFreshEntity(water);

                Lightning_Spear_Entity lightning = new Lightning_Spear_Entity(caster, vec3, caster.level(), GCSpellConfig.LightningSpearDamage.get().floatValue() + potency);
                lightning.accelerationPower += velocity;
                lightning.setYRot(yRot);
                lightning.setXRot(xRot);
                lightning.setPosRaw(d0, d1, d2);
                lightning.setAreaDamage(GCSpellConfig.LightningSpearAreaDamage.get().floatValue() + potency);
                if (rightStaff(staff)) {
                    lightning.setHpDamage(GCSpellConfig.LightningSpearHPDamage.get().floatValue() + potency);
                }
                lightning.setAreaRadius((float) (radius));

                worldIn.addFreshEntity(lightning);
            }
        }
    }

    private void CircleLighning(ServerLevel worldIn, LivingEntity caster, float vec, float math, float radius, double EndHeight, int amount, int randamount) {
        float f = Mth.cos(caster.yBodyRot * ((float)Math.PI / 180F)) ;
        float f1 = Mth.sin(caster.yBodyRot * ((float)Math.PI / 180F)) ;
        double theta = (caster.yBodyRot) * (Math.PI / 180);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        double d0 = caster.getX() + vecX * vec + f * math;
        double d1 = caster.getY() + EndHeight;
        double d2 = caster.getZ() + vecZ * vec + f1 * math;

        for (int i = 0; i < amount + caster.getRandom().nextInt(randamount +1); i++) {
            double theta2 = caster.getRandom().nextDouble() * 2 * Math.PI;
            double phi = caster.getRandom().nextDouble() * Math.PI;

            double posX = radius * Math.sin(phi) * Math.cos(theta2);
            double posY = radius * Math.cos(phi);
            double posZ = radius * Math.sin(phi) * Math.sin(theta2);

            worldIn.sendParticles(new CircleLightningParticle.CircleData(143, 241, 215), d0 + posX, d1 + posY, d2 + posZ, 0, d0, d1, d2, 1.0F);
        }
    }

    private void Stormknockback(LivingEntity caster, float scale, double distance) {
        List<Entity> hit = this.getEntitiesNearby(caster, Entity.class, distance, distance, distance, distance);
        for (Entity target : hit) {
            double d0 = target.getX() - caster.getX();
            double d1 = target.getZ() - caster.getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            double power = target.isShiftKeyDown() ? scale / 3: scale;
            target.push(d0 / d2 * (double)power, 0.0, d1 / d2 * (double)power);
        }
    }

    public List<LivingEntity> getEntityLivingBaseNearby(LivingEntity caster, double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(caster, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity caster, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return caster.level().getEntitiesOfClass(entityClass, caster.getBoundingBox().inflate(dX, dY, dZ), (e) -> {
            return e != caster && (double)caster.distanceTo(e) <= r + (double)(e.getBbWidth() / 2.0F) && e.getY() <= caster.getY() + dY;
        });
    }

    private void Nimbo(ServerLevel world, LivingEntity caster, float vec, float math,float radius,double EndHeight,int amount, int randamount) {
        for(int j = 0; j < amount + caster.getRandom().nextInt(randamount); ++j) {
            float f2 = caster.getRandom().nextFloat() * ((float)Math.PI * 2F);
            float f3 = Mth.sqrt(caster.getRandom().nextFloat()) * radius;

            float f = Mth.cos(caster.yBodyRot * ((float)Math.PI / 180F)) ;
            float f1 = Mth.sin(caster.yBodyRot * ((float)Math.PI / 180F)) ;
            double theta = (caster.yBodyRot) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            double d0 = caster.getX() + vecX * vec + f * math  + (double)(Mth.cos(f2) * f3);
            double d2 = caster.getY() + EndHeight;
            double d4 = caster.getZ() + vecZ * vec + f1 * math + (double)(Mth.sin(f2) * f3);
            world.sendParticles(ModParticle.RAIN_CLOUD.get(), d0, d2, d4, 1, caster.getRandom().nextGaussian() * 0.03D, caster.getRandom().nextGaussian() * 0.01D, caster.getRandom().nextGaussian() * 0.03D, 0.0F);
        }
    }

    @Override
    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int useTimeRemaining) {
        int useTime = this.castDuration(caster) - useTimeRemaining;
        if (useTime >= 115){
            if (caster instanceof Player player) {
                SEHelper.addCooldown(player, GCItems.THUNDER_RAGE_FOCUS.get(), this.spellCooldown());
                SEHelper.sendSEUpdatePacket(player);
            }
        }
    }
}
