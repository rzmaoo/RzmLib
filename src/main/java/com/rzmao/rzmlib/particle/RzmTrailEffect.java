package com.rzmao.rzmlib.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Vector3f;

public record RzmTrailEffect(int age, Vector3f color, float scale) implements ParticleEffect {

    public static final MapCodec<RzmTrailEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("age", 10).forGetter(RzmTrailEffect::age),
            Codecs.VECTOR_3F.optionalFieldOf("color", new Vector3f(1.0f, 1.0f, 1.0f)).forGetter(RzmTrailEffect::color),
            Codec.FLOAT.optionalFieldOf("scale", 1.0f).forGetter(RzmTrailEffect::scale)
    ).apply(instance, RzmTrailEffect::new));

    public static final PacketCodec<RegistryByteBuf, RzmTrailEffect> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, RzmTrailEffect::age,
            PacketCodecs.VECTOR3F, RzmTrailEffect::color,
            PacketCodecs.FLOAT, RzmTrailEffect::scale,
            RzmTrailEffect::new
    );

    @Override
    public ParticleType<?> getType() {
        return RzmParticles.TRAIL_EFFECT;
    }
}