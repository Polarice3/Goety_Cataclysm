package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.client.render.model.SymbioctoServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.acropolis.SymbioctoServant;
import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class SymbioctoServantRenderer extends MobRenderer<SymbioctoServant, SymbioctoServantModel<SymbioctoServant>> {
    private static final ResourceLocation OPEN = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/symbiocto_open.png");
    private static final ResourceLocation CLOSE = new ResourceLocation(Cataclysm.MODID,"textures/entity/sea/symbiocto_close.png");

    public SymbioctoServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SymbioctoServantModel<>(renderManagerIn.bakeLayer(CMModelLayers.OCTOSITE_MODEL)), 0.25F);
        this.addLayer(new SymbioctoSecretLayer<>(this, renderManagerIn.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(SymbioctoServant entity) {
        if (entity.isCloseEye()) {
            return CLOSE;
        } else {
            return OPEN;
        }
    }


    public static class SymbioctoSecretLayer<T extends SymbioctoServant> extends RenderLayer<T, SymbioctoServantModel<T>> {
        private static final ResourceLocation TEXTURES = GoetyCataclysm.location("textures/entity/servants/symbiocto_secret.png");
        private final SymbioctoServantModel<T> layerModel;

        public SymbioctoSecretLayer(RenderLayerParent<T, SymbioctoServantModel<T>> p_i50919_1_, EntityModelSet p_174555_) {
            super(p_i50919_1_);
            this.layerModel = new SymbioctoServantModel<>(p_174555_.bakeLayer(CMModelLayers.OCTOSITE_MODEL));
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.interestTime > 0) {
                coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
