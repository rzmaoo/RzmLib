package com.rzmao.rzmlib.mixin.client;

import com.rzmao.rzmlib.client.morph.RzmClientMorphState;
import com.rzmao.rzmlib.client.morph.render.RzmMorphPlayerRenderer;
import com.rzmao.rzmlib.client.morph.render.RzmMorphRenderRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 接管原版玩家渲染，按客户端同步的 morph 状态切换自定义 renderer
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Unique
    private EntityRendererFactory.Context rzm$renderContext;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void rzm$captureRenderContext(EntityRendererFactory.Context context, boolean slim, CallbackInfo ci) {
        this.rzm$renderContext = context;
    }

    @Inject(
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void rzm$renderMorph(AbstractClientPlayerEntity player, float entityYaw, float tickDelta,
                                 MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (this.rzm$renderContext == null) {
            return;
        }

        Identifier morphId = RzmClientMorphState.getMorph(player.getUuid());
        if (morphId == null) {
            return;
        }

        RzmMorphPlayerRenderer renderer = RzmMorphRenderRegistry.getRenderer(morphId, this.rzm$renderContext);
        if (renderer == null) {
            return;
        }

        renderer.render(player, entityYaw, tickDelta, matrices, vertexConsumers, light);
        ci.cancel();
    }
}
