package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.AbstractZombieServantRenderer;
import com.Polarice3.Goety.client.render.model.DrownedServantModel;
import com.Polarice3.Goety.common.entities.ally.undead.zombie.DrownedServant;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.goety_cataclysm.client.render.model.DrownedHostServantModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DrownedHostServantRenderer extends AbstractZombieServantRenderer<DrownedServant, DrownedServantModel<DrownedServant>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/zombie/drowned_servant.png");
    private static final ResourceLocation DROWNED_LOCATION = new ResourceLocation("textures/entity/zombie/drowned.png");

    public DrownedHostServantRenderer(EntityRendererProvider.Context p_173964_) {
        super(p_173964_, new DrownedHostServantModel<>(p_173964_.bakeLayer(ModelLayers.DROWNED)), new DrownedHostServantModel<>(p_173964_.bakeLayer(ModelLayers.DROWNED_INNER_ARMOR)), new DrownedHostServantModel<>(p_173964_.bakeLayer(ModelLayers.DROWNED_OUTER_ARMOR)));
        this.addLayer(new DrownedHostServantOuterLayer<>(this, p_173964_.getModelSet()));
    }

    public ResourceLocation getTextureLocation(DrownedServant p_114115_) {
        return !p_114115_.isHostile() && MobsConfig.DrownedServantTexture.get() ? TEXTURE : DROWNED_LOCATION;
    }

    protected void setupRotations(DrownedServant p_114109_, PoseStack p_114110_, float p_114111_, float p_114112_, float p_114113_) {
        super.setupRotations(p_114109_, p_114110_, p_114111_, p_114112_, p_114113_);
        float f = p_114109_.getSwimAmount(p_114113_);
        if (f > 0.0F) {
            p_114110_.mulPose(Axis.XP.rotationDegrees(Mth.lerp(f, p_114109_.getXRot(), -10.0F - p_114109_.getXRot())));
        }

    }

    public static class DrownedHostServantOuterLayer<T extends DrownedServant> extends RenderLayer<T, DrownedServantModel<T>> {
        private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation("textures/entity/zombie/drowned_outer_layer.png");
        private static final ResourceLocation TEXTURES = Goety.location("textures/entity/servants/zombie/drowned_servant_outer_layer.png");
        private final DrownedHostServantModel<T> model;

        public DrownedHostServantOuterLayer(RenderLayerParent<T, DrownedServantModel<T>> p_174490_, EntityModelSet p_174491_) {
            super(p_174490_);
            this.model = new DrownedHostServantModel<>(p_174491_.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
        }

        public void render(PoseStack p_116924_, MultiBufferSource p_116925_, int p_116926_, T p_116927_, float p_116928_, float p_116929_, float p_116930_, float p_116931_, float p_116932_, float p_116933_) {
            ResourceLocation resourceLocation = TEXTURES;
            if (p_116927_.isHostile() || !MobsConfig.DrownedServantTexture.get()) {
                resourceLocation = DROWNED_OUTER_LAYER_LOCATION;
            }

            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, resourceLocation, p_116924_, p_116925_, p_116926_, p_116927_, p_116928_, p_116929_, p_116931_, p_116932_, p_116933_, p_116930_, 1.0F, 1.0F, 1.0F);
        }
    }
}
