package com.rzmao.rzmlib.client;

import com.rzmao.rzmlib.client.input.RzmInputHandler;
import com.rzmao.rzmlib.client.morph.RzmClientMorphManager;
import com.rzmao.rzmlib.client.particle.RzmClientParticles;
import com.rzmao.rzmlib.client.render.RzmEntityRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class RzmLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RzmEntityRenderers.registerRenderers();
        ClientTickEvents.END_CLIENT_TICK.register(RzmInputHandler::onClientTick);
        RzmClientParticles.registerFactories();
        RzmClientMorphManager.registerReceivers();
    }
}