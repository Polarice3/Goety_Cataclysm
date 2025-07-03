package com.Polarice3.goety_cataclysm.common.magic.spells.abyss;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.utils.*;
import com.Polarice3.goety_cataclysm.common.entities.GCEntityType;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.DrownedHostServant;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.SymbioctoServant;
import com.Polarice3.goety_cataclysm.config.GCSpellConfig;
import com.Polarice3.goety_cataclysm.init.GoetySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OctoHostSpell extends SummonSpell {

    public int defaultSoulCost() {
        return GCSpellConfig.OctoHostCost.get();
    }

    public int defaultCastDuration() {
        return GCSpellConfig.OctoHostDuration.get();
    }

    public int SummonDownDuration() {
        return GCSpellConfig.OctoHostSummonDown.get();
    }

    public SoundEvent CastingSound() {
        return GoetySounds.ABYSS_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return GCSpellConfig.OctoHostCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ABYSS;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public Predicate<LivingEntity> summonPredicate() {
        return livingEntity -> livingEntity instanceof SymbioctoServant || livingEntity instanceof DrownedHostServant;
    }

    @Override
    public int summonLimit() {
        return GCSpellConfig.OctoHostLimit.get();
    }

    @Override
    public void commonResultHit(ServerLevel worldIn, LivingEntity caster) {
        for (int i = 0; i < caster.level().random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.POOF, caster.getX(), caster.getEyeY(), caster.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
        this.playSound(worldIn, caster, GoetySounds.DROWNED_NECROMANCER_SUMMON.get());
    }

    public void teleportServants(LivingEntity owner, Entity servant) {
        if (servant instanceof IOwned servant1) {
            if (servant instanceof LivingEntity servant2) {
                if (servant1.getTrueOwner() == owner && !SEHelper.isGrounded(owner, servant2)) {
                    if (servant2.isPassenger()
                            && servant2.getVehicle() instanceof Summoned servant3
                            && servant3.getTrueOwner() == owner) {
                        servant3.moveTo(owner.position());
                    }
                }
            }
        }
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        this.commonResult(worldIn, caster);
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }
        if (!isShifting(caster)) {
            int i = 1;
            if (rightStaff(staff)){
                i = 2 + caster.level().random.nextInt(4);
            }
            for (int i1 = 0; i1 < i; ++i1) {
                DrownedHostServant summonedentity = new DrownedHostServant(GCEntityType.DROWNED_HOST_SERVANT.get(), worldIn);
                BlockPos blockPos = BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn);
                if (caster.isUnderWater()) {
                    blockPos = BlockFinder.SummonWaterRadius(caster, worldIn);
                }
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(blockPos, 0.0F, 0.0F);
                if (!caster.isUnderWater()) {
                    MobUtil.moveDownToGround(summonedentity);
                }
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.setPersistenceRequired();
                summonedentity.finalizeSpawn(worldIn, caster.level().getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED,null,null);
                if (potency > 0){
                    int boost = Mth.clamp(potency - 1, 0, 10);
                    summonedentity.addEffect(new MobEffectInstance(GoetyEffects.BUFF.get(), EffectsUtil.infiniteEffect(), boost, false, false));
                }
                this.SummonSap(caster, summonedentity);
                this.setTarget(caster, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                this.summonAdvancement(caster, summonedentity);
            }
            this.SummonDown(caster);
            this.playSound(worldIn, caster, GoetySounds.DROWNED_NECROMANCER_SUMMON.get());
        }
    }
}
