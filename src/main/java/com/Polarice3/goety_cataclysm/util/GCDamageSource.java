package com.Polarice3.goety_cataclysm.util;

import com.Polarice3.Goety.utils.ModDamageSource;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GCDamageSource extends ModDamageSource {

    public GCDamageSource(Holder<DamageType> p_270906_, @Nullable Entity p_270796_, @Nullable Entity p_270459_, @Nullable Vec3 p_270623_) {
        super(p_270906_, p_270796_, p_270459_, p_270623_);
    }

    public static DamageSource deathLaser(Entity pSource, @Nullable Entity pIndirectEntity) {
        return noKnockbackDamageSource(pSource.level(), CMDamageTypes.DEATHLASER, pSource, pIndirectEntity);
    }
}
