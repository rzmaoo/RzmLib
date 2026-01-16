package com.rzmao.rzmlib.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.rzmao.rzmlib.RzmLib.MOD_ID;

public class RzmParticles {
    public static final ParticleType<RzmTrailEffect> TRAIL_EFFECT = FabricParticleTypes.complex(
            RzmTrailEffect.CODEC,
            RzmTrailEffect.PACKET_CODEC
    );

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "rzm_trail"), TRAIL_EFFECT);
    }
}