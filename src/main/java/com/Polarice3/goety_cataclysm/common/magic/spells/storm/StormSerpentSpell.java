package com.Polarice3.goety_cataclysm.common.magic.spells.storm;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import com.Polarice3.goety_cataclysm.common.entities.projectiles.StormSerpent;
import com.Polarice3.goety_cataclysm.common.network.GCNetwork;
import com.Polarice3.goety_cataclysm.common.network.server.SRingParticlePacket;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.CataclysmSounds;
import com.github.L_Ender.cataclysm.client.particle.Gathering_Water_Particle;
import com.github.L_Ender.cataclysm.client.particle.RoarParticle;
import com.github.L_Ender.cataclysm.client.particle.StormParticle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class StormSerpentSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return GCSpellConfig.StormSerpentCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return 75;
    }

    @Override
    public int castDuration(LivingEntity caster, ItemStack staff) {
        return 75;
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.StormSerpentCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        return list;
    }

    @Override
    public void useSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int castTime, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
        }
        LivingEntity target = this.getTarget(caster);
        float multiply = WandUtil.damageMultiply();
        float damage = (float) (GCSpellConfig.StormSerpentDamage.get() * multiply);
        damage += potency;
        if (castTime < 20 && castTime > 1) {
            for (int i = 0; i < 2; i++) {
                float r = 94 / 255F;
                float g = 150 / 255F;
                float b = 226 / 255F;
                double p0 = caster.getX();
                double p1 = caster.getY() + 0.1;
                double p2 = caster.getZ();
                worldIn.sendParticles(new Gathering_Water_Particle.GatheringData(r, g, b), p0 + (caster.getRandom().nextFloat() - 0.5F) * 12, p1 + (caster.getRandom().nextFloat() - 0.5F) * 2, p2 + (caster.getRandom().nextFloat() - 0.5F) * 12, 0, p0, p1, p2, 0.5F);
            }
        }
        if (castTime == 30) {
            GCNetwork.sentToTrackingEntityAndPlayer(caster, new SRingParticlePacket(0.0F, (float) Math.PI / 2.0F, 30, 94, 150, 226, 1.0F, 65, false, 1, caster.getX(), caster.getY() + 0.02f, caster.getZ()));
        }

        if (castTime < 40 && castTime > 30) {
            float r = 143 / 255F;
            float g = 241 / 255F;
            float b = 215 / 255F;

            worldIn.sendParticles((new StormParticle.OrbData(99/255F, 194/255F, 224/255F, 6f + caster.getRandom().nextFloat() * 0.25f, 1.5F + caster.getRandom().nextFloat() * 0.45f, caster.getId())), caster.getX(), caster.getY(), caster.getZ(), 1, 0, 0, 0, 0);
            worldIn.sendParticles((new StormParticle.OrbData(r, g, b, 4f + caster.getRandom().nextFloat() * 1.2f, 1.0F + caster.getRandom().nextFloat() * 0.45f, caster.getId())), caster.getX(), caster.getY(), caster.getZ(), 1, 0, 0, 0, 0);
            worldIn.sendParticles((new StormParticle.OrbData(r, g, b, 2f + caster.getRandom().nextFloat() * 0.7f, 0.35F + caster.getRandom().nextFloat() * 0.45f, caster.getId())), caster.getX(), caster.getY(), caster.getZ(), 1, 0, 0, 0, 0);
        }
        if (castTime == 30) {
            this.playSound(worldIn, caster, CataclysmSounds.SCYLLA_ROAR.get(), 0.6F, 1.0f);
            RoarParticle(caster, -0.4f, 0, 2.4F, 20, 99, 194, 224, 0.4F, 0.4f, 0.5F, 2.5F);
        }

        if (castTime == 33 || castTime == 36 || castTime == 39) {
            RoarParticle(caster, -0.4f, 0, 2.4F, 20, 99, 194, 224, 0.4F, 0.4f, 0.5F, 2.5F);
        }
        if (castTime == 28) {
            for (int i = 0; i < 2; ++i) {
                float f = Mth.cos(caster.yBodyRot * ((float) Math.PI / 180F));
                float f1 = Mth.sin(caster.yBodyRot * ((float) Math.PI / 180F));
                double dis = 8;
                double firstAngleOffset = (2 - 1) / 2.0 * dis;
                double math = 0 - firstAngleOffset + (i * dis);
                double d0 = caster.getX() + f * math;
                double d1 = caster.getY() + caster.getBbHeight() * 0.7F;
                double d2 = caster.getZ() + f1 * math;
                HitResult rayTrace = this.rayTrace(worldIn, caster, 16, 3.0D);
                Vec3 location = rayTrace.getLocation();
                if (target != null) {
                    location = target.position().add(0.0D, target.getBbHeight() * 0.35D, 0.0D);
                }
                double d3 = location.x - d0;
                double d4 = location.y - d1;
                double d5 = location.z - d2;
                Vec3 vec3 = new Vec3(d3, d4, d5).normalize();
                worldIn.addFreshEntity(new StormSerpent(worldIn, d0, caster.getY(), d2, (float) (Mth.atan2(vec3.z, vec3.x)), i * 8, caster, damage, target, i == 0));
            }
        }

        if (this.rightStaff(staff)) {
            if (castTime == 40) {
                for (int i = 0; i < 2; ++i) {
                    float f = Mth.cos(caster.yBodyRot * ((float) Math.PI / 180F));
                    float f1 = Mth.sin(caster.yBodyRot * ((float) Math.PI / 180F));
                    double dis = 12;
                    double firstAngleOffset = (2 - 1) / 2.0 * dis;
                    double math = 0 - firstAngleOffset + (i * dis);
                    double d0 = caster.getX() + f * math;
                    double d1 = caster.getY() + caster.getBbHeight() * 0.7F;
                    double d2 = caster.getZ() + f1 * math;
                    HitResult rayTrace = this.rayTrace(worldIn, caster, 16, 3.0D);
                    Vec3 location = rayTrace.getLocation();
                    if (target != null) {
                        location = target.position().add(0.0D, target.getBbHeight() * 0.35D, 0.0D);
                    }
                    double d3 = location.x - d0;
                    double d4 = location.y - d1;
                    double d5 = location.z - d2;
                    Vec3 vec3 = new Vec3(d3, d4, d5).normalize();
                    worldIn.addFreshEntity(new StormSerpent(worldIn, d0, caster.getY(), d2, (float) (Mth.atan2(vec3.z, vec3.x)), i * 8, caster, damage, target, i == 0));
                }
            }
        }
    }

    private void RoarParticle(LivingEntity caster, float vec, float math, float y, int duration, int r, int g, int b, float a, float start, float inc, float end) {
        if (caster.level() instanceof ServerLevel serverLevel) {
            float f = Mth.cos(caster.yBodyRot * ((float)Math.PI / 180F)) ;
            float f1 = Mth.sin(caster.yBodyRot * ((float)Math.PI / 180F)) ;
            double theta = (caster.yBodyRot) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);

            serverLevel.sendParticles(new RoarParticle.RoarData(duration, r, g, b, a, start,inc,end), caster.getX() + vec * vecX + f * math, caster.getY() + y, caster.getZ() + vec * vecZ + f1 * math, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
        if (castTime >= 28){
            if (caster instanceof Player player) {
                SEHelper.addCooldown(player, focus.getItem(), this.spellCooldown());
                SEHelper.sendSEUpdatePacket(player);
            }
        }
    }
}
