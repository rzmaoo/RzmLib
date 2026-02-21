package com.rzmao.rzmlib.client.morph.render;

import net.minecraft.client.render.entity.EntityRendererFactory;

@FunctionalInterface
public interface RzmMorphPlayerRendererFactory {
    RzmMorphPlayerRenderer create(EntityRendererFactory.Context context);
}