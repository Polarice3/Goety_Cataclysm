package com.Polarice3.goety_cataclysm.client.render;

import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.Polarice3.goety_cataclysm.client.render.model.DraugrNecromancerModel;
import com.Polarice3.goety_cataclysm.common.entities.neutral.AbstractDraugrNecromancer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class DraugrNecromancerRenderer extends MobRenderer<AbstractDraugrNecromancer, DraugrNecromancerModel<AbstractDraugrNecromancer>> {
   private static final ResourceLocation SKELETON_LOCATION = GoetyCataclysm.location("textures/entity/necromancer/draugr_necromancer.png");
   private static final ResourceLocation SERVANT_LOCATION = GoetyCataclysm.location("textures/entity/necromancer/draugr_necromancer_servant.png");

   public DraugrNecromancerRenderer(EntityRendererProvider.Context p_174382_) {
      super(p_174382_, new DraugrNecromancerModel<>(p_174382_.bakeLayer(GCModelLayer.DRAUGR_NECROMANCER)), 0.5F);
      this.addLayer(new NecromancerEyesLayer<>(this));
   }

   protected void scale(AbstractDraugrNecromancer necromancer, PoseStack matrixStackIn, float partialTickTime) {
      float original = 1.25F;
      float f1 = (float)necromancer.getNecroLevel();
      float size = original + Math.max(f1 * 0.15F, 0);
      matrixStackIn.scale(size, size, size);
   }

   public ResourceLocation getTextureLocation(AbstractDraugrNecromancer p_115941_) {
      if (p_115941_.isHostile() || !MobsConfig.NecromancerServantTexture.get()){
         return SKELETON_LOCATION;
      } else {
         return SERVANT_LOCATION;
      }
   }

   public static class NecromancerEyesLayer<T extends AbstractDraugrNecromancer, M extends DraugrNecromancerModel<T>> extends EyesLayer<T, M> {
      private static final ResourceLocation GLOW = GoetyCataclysm.location("textures/entity/necromancer/draugr_necromancer_glow.png");

      public NecromancerEyesLayer(RenderLayerParent<T, M> p_i50919_1_) {
         super(p_i50919_1_);
      }

      @Override
      public RenderType renderType() {
         return RenderType.eyes(GLOW);
      }
   }

}