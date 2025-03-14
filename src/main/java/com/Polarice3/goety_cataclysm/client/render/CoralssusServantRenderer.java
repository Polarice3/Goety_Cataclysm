package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.goety_cataclysm.client.render.model.CoralssusServantModel;
import com.Polarice3.goety_cataclysm.common.entities.ally.golem.CoralssusServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CoralssusServantRenderer extends MobRenderer<CoralssusServant, CoralssusServantModel> {
    private static final ResourceLocation FIRE_TEXTURE = new ResourceLocation("cataclysm", "textures/entity/deepling/coralssus_fire.png");
    private static final ResourceLocation HORN_TEXTURE = new ResourceLocation("cataclysm", "textures/entity/deepling/coralssus_horn.png");
    private static final ResourceLocation TUBE_TEXTURE = new ResourceLocation("cataclysm", "textures/entity/deepling/coralssus_tube.png");
    private static final ResourceLocation SPONGE_TEXTURE = new ResourceLocation("cataclysm", "textures/entity/deepling/coralssus_sponge_horn.png");

    public CoralssusServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CoralssusServantModel(), 1.7F);
    }

    public ResourceLocation getTextureLocation(CoralssusServant entity) {
        if (entity.isSponge()) {
            return SPONGE_TEXTURE;
        } else {
            ResourceLocation resourcelocation;
            switch (entity.getVariant()) {
                case FIRE:
                    resourcelocation = FIRE_TEXTURE;
                    break;
                case HORN:
                    resourcelocation = HORN_TEXTURE;
                    break;
                case TUBE:
                    resourcelocation = TUBE_TEXTURE;
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            return resourcelocation;
        }
    }

    protected float getFlipDegrees(CoralssusServant entity) {
        return 0.0F;
    }

    protected void scale(CoralssusServant entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.35F, 1.35F, 1.35F);
    }
}
