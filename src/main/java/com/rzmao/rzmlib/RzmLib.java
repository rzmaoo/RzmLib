package com.rzmao.rzmlib;

import com.rzmao.rzmlib.entity.RzmEntityTypes;
import com.rzmao.rzmlib.morph.RzmPlayerMorphManager;
import com.rzmao.rzmlib.network.RzmNetworking;
import com.rzmao.rzmlib.particle.RzmParticles;
import com.rzmao.rzmlib.util.RzmLogger;
import net.fabricmc.api.ModInitializer;

public class RzmLib implements ModInitializer {
    public static final String MOD_ID = "rzmlib";
    public static final RzmLogger LOGGER = RzmLogger.get(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        RzmEntityTypes.register();
        RzmNetworking.registerPayloads();
        RzmNetworking.registerC2SReceivers();
        RzmPlayerMorphManager.registerLifecycleCallbacks();
        RzmParticles.register();
    }
}