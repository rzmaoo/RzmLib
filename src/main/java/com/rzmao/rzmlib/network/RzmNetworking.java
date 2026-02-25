package com.rzmao.rzmlib.network;

import com.rzmao.rzmlib.event.RzmInputEvents;
import com.rzmao.rzmlib.network.c2s.KeyInputPayload;
import com.rzmao.rzmlib.network.s2c.PlayerMorphSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class RzmNetworking {

    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(KeyInputPayload.ID, KeyInputPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerMorphSyncPayload.ID, PlayerMorphSyncPayload.CODEC);
    }

    public static void registerS2CPackets() {
    }

    public static void registerC2SReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(KeyInputPayload.ID, (payload, context) -> {
            int keyCode = payload.keyCode();
            String action = payload.action();
            boolean pressed = payload.pressed();
            context.server().execute(() -> {
                RzmInputEvents.C2S_KEY_INPUT.invoker().handle(context.player(), keyCode, action, pressed);
            });
        });
    }
}