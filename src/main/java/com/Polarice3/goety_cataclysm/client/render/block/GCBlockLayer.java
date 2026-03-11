package com.Polarice3.goety_cataclysm.client.render.block;

import com.Polarice3.goety_cataclysm.GoetyCataclysm;
import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.Set;

public class GCBlockLayer {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final ModelLayerLocation NM_HEAD = register("nm_head");

    private static ModelLayerLocation register(String p_171294_) {
        return register(p_171294_, "main");
    }

    private static ModelLayerLocation register(String p_171296_, String p_171297_) {
        ModelLayerLocation modellayerlocation = createLocation(p_171296_, p_171297_);
        if (!ALL_MODELS.add(modellayerlocation)) {
            throw new IllegalStateException("Duplicate registration for " + modellayerlocation);
        } else {
            return modellayerlocation;
        }
    }
    private static ModelLayerLocation createLocation(String p_171301_, String p_171302_) {
        return new ModelLayerLocation(GoetyCataclysm.location(p_171301_), p_171302_);
    }

}
