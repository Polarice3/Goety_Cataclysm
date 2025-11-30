package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.client.render.model.ZombieAmethystCrabModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.undead.ZombieAmethystCrab;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZombieAmethystCrabRenderer extends MobRenderer<ZombieAmethystCrab, ZombieAmethystCrabModel> {
    private static final ResourceLocation TEXTURES = GoetyCataclysm.location("textures/entity/servants/zombie_amethyst_crab.png");
    private static final ResourceLocation KRABS_TEXTURES = GoetyCataclysm.location("textures/entity/servants/mr_zombie_amethyst_krabs.png");

    public ZombieAmethystCrabRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ZombieAmethystCrabModel(), 1.5F);

    }
    @Override
    public ResourceLocation getTextureLocation(ZombieAmethystCrab entity) {
        return entity.isKrusty() ? KRABS_TEXTURES : TEXTURES;
    }

    @Override
    protected void scale(ZombieAmethystCrab entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
    }

    @Override
    protected float getFlipDegrees(ZombieAmethystCrab entity) {
        return 0;
    }

}

