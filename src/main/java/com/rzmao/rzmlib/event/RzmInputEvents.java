package com.rzmao.rzmlib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 专门管理与网络相关的自定义事件
 */
public class RzmInputEvents {

    public static final Event<KeyReceiveCallback> C2S_KEY_INPUT = EventFactory.createArrayBacked(
            KeyReceiveCallback.class,
            (listeners) -> (player, keyCode, action, pressed) -> {
                for (KeyReceiveCallback listener : listeners) {
                    listener.handle(player, keyCode, action, pressed);
                }
            }
    );

    @FunctionalInterface
    public interface KeyReceiveCallback {
        void handle(ServerPlayerEntity player, int keyCode, String action, boolean pressed);
    }
}