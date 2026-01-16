package com.rzmao.rzmlib.client.particle;

import com.rzmao.rzmlib.particle.RzmParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class RzmClientParticles {
    public static void registerFactories() {
        ParticleFactoryRegistry.getInstance().register(RzmParticles.TRAIL_EFFECT, RzmTrailParticle.Factory::new);
    }
}
