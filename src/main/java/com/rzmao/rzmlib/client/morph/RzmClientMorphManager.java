package com.rzmao.rzmlib.client.morph;

import com.rzmao.rzmlib.client.morph.render.RzmMorphRenderRegistry;
import com.rzmao.rzmlib.network.s2c.PlayerMorphSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 客户端变形同步管理
 */
public final class RzmClientMorphManager {
    private static final AtomicBoolean RECEIVERS_REGISTERED = new AtomicBoolean(false);

    private RzmClientMorphManager() {
    }

    public static void registerReceivers() {
        if (!RECEIVERS_REGISTERED.compareAndSet(false, true)) {
            return;
        }

        ClientPlayNetworking.registerGlobalReceiver(PlayerMorphSyncPayload.ID, (payload, context) ->
                context.client().execute(() -> RzmClientMorphState.applySync(payload))
        );

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            RzmClientMorphState.clear();
            RzmMorphRenderRegistry.clearRendererCache();
        });
    }
}
