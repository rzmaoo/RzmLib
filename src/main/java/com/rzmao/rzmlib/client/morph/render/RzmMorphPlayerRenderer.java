package com.rzmao.rzmlib.client.morph.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

@FunctionalInterface
public interface RzmMorphPlayerRenderer {
    void render(AbstractClientPlayerEntity player, float entityYaw, float tickDelta,
                MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
}