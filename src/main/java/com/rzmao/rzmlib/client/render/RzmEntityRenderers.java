package com.rzmao.rzmlib.client.render;

import com.rzmao.rzmlib.entity.RzmEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class RzmEntityRenderers {

    public static void registerRenderers() {
        EntityRendererRegistry.register(RzmEntityTypes.DISPLAY_ITEM, RzmDisplayItemEntityRenderer::new);
        EntityRendererRegistry.register(RzmEntityTypes.ORBIT, RzmOrbitEntityRenderer::new);
    }
}